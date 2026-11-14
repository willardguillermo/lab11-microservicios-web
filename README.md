# Lab 11: Creación de Microservicios con Spring Boot

Laboratorio de **Desarrollo de Aplicaciones Web** (Tecsup). Arquitectura de microservicios con Spring Boot 4, Spring Data JPA y MySQL. Cada servicio es un proyecto Maven independiente, con su propio puerto y su propia base de datos.

## Arquitectura

```
                         ┌──────────────────────┐
                         │  categoria-service   │ :8082  bd_categoria(_dev)
                         └──────────▲───────────┘
                                    │ HTTP (RestClient)
┌──────────────────┐     ┌──────────┴───────────┐
│ cliente-service  │◄────┤   producto-service   │ :8083  bd_producto
│ :8081 bd_cliente │     └──────────▲───────────┘
└────────▲─────────┘                │
         │ HTTP          ┌──────────┴───────────┐
         └───────────────┤    pedido-service    │ :8084  bd_pedido
                         └──────────────────────┘

Actividad: Clínica "Salud Integral"
┌──────────────────┐     ┌──────────────────────┐     ┌──────────────────┐
│ paciente-service │◄────┤     cita-service     ├────►│  medico-service  │
│ :8085 bd_paciente│ HTTP│ :8087 bd_cita        │HTTP │ :8086 bd_medico  │
└──────────────────┘     └──────────────────────┘     └──────────────────┘
```

| Servicio | Puerto | Base de datos | Tablas |
|---|---|---|---|
| cliente-service | 8081 | bd_cliente | cliente |
| categoria-service | 8082 (dev) / 9092 (prod) | bd_categoria_dev / bd_categoria | categoria |
| producto-service | 8083 | bd_producto | producto |
| pedido-service | 8084 | bd_pedido | pedido, detalle_pedido |
| paciente-service | 8085 | bd_paciente | paciente |
| medico-service | 8086 | bd_medico | especialidad, medico, horario_medico |
| cita-service | 8087 | bd_cita | cita, atencion |

Cada servicio está organizado por capas: `controller` → `services` → `repository` → `model`, con `dto` para validar la entrada (`@Valid`), `exception` para el manejo global de errores y `client` para las llamadas HTTP a otros servicios.

## Requisitos

- JDK 21 o superior (la guía pide Java 25; el proyecto compila con `java.version` 21 y funciona igual en 25).
- MySQL en `localhost:3306`, usuario `root` sin contraseña.
- Maven (cada proyecto trae `./mvnw`).

## Cómo ejecutar

```bash
mysql -uroot < crear-bases-datos.sql
mysql -uroot < actividad-citas/crear-bases-datos.sql
```

Luego levanta cada servicio en su carpeta. Arranca primero los que no dependen de otros (categoria, cliente, paciente, medico):

```bash
cd categoria-service && ./mvnw spring-boot:run
```

Las tablas las crea JPA (`ddl-auto: update`). Para probar, importa en Postman `postman/Lab11-Microservicios.postman_collection.json` (75 peticiones, con casos de éxito y de error).

## Endpoints

### Laboratorio

| Servicio | Endpoints |
|---|---|
| categoria | `GET/POST /api/categorias`, `GET/PUT/DELETE /api/categorias/{id}`, `GET /api/categorias/ambiente` |
| cliente | `GET/POST /api/clientes`, `GET/PUT/DELETE /api/clientes/{id}` |
| producto | `GET/POST /api/productos`, `GET/PUT/DELETE /api/productos/{id}`, `GET /api/productos/{id}/detalle` |
| pedido | `GET/POST /api/pedidos`, `GET/PUT/DELETE /api/pedidos/{id}` |

### Actividad: gestión de citas

| Servicio | Endpoints |
|---|---|
| paciente | `GET/POST /api/pacientes`, `GET/PUT/DELETE /api/pacientes/{id}` |
| medico | `GET/POST /api/medicos`, `GET/PUT/DELETE /api/medicos/{id}`, `GET/POST /api/medicos/{id}/horarios`, `GET/POST /api/especialidades`, `GET /api/especialidades/{id}` |
| cita | `GET/POST /api/citas`, `GET /api/citas/{id}`, `GET /api/citas/{id}/detalle`, `PATCH /api/citas/{id}/estado`, `PATCH /api/citas/{id}/cancelar`, `GET/POST /api/citas/{id}/atencion` |

Estados de una cita:

```
PROGRAMADA ──► CONFIRMADA ──► ATENDIDA   (al registrar la atención)
     │               │
     └──► CANCELADA ◄┘
```

## Comunicación entre microservicios

- **producto → categoria**: antes de guardar un producto verifica que la categoría exista; `/detalle` devuelve el producto con su categoría.
- **pedido → cliente y producto**: valida el cliente y cada producto del detalle. El total lo calcula el servidor.
- **cita → paciente y medico**: paciente y médico deben existir y estar activos, la hora debe caer dentro de un horario del médico y el médico no puede tener otra cita a la misma hora.

Una base de datos no puede tener una FK hacia la tabla de otra. Por eso cada servicio guarda solo el id (`categoriaId`, `idCliente`, `id_paciente`...) y la integridad se valida por HTTP.

## Manejo de respuestas HTTP

| Código | Cuándo |
|---|---|
| 200 | Consulta, actualización o eliminación correcta |
| 201 | Registro creado |
| 400 | DTO inválido, JSON mal formado, referencia a otro servicio inexistente, dato único duplicado o cambio de estado no permitido |
| 404 | El recurso no existe |
| 500 | Error no controlado o el servicio del que se depende no responde |

## Resultados de las pruebas

Todos los casos se ejecutaron con los 7 servicios levantados. El detalle está en [docs/PRUEBAS.md](docs/PRUEBAS.md).

- `./mvnw test` pasa en los 7 servicios.
- Escenarios exitosos (200 y 201) y de error (400, 404 y 500) probados en cada microservicio.
- Si se apaga categoria-service, `GET /api/productos/1/detalle` responde **500** con `"No se pudo comunicar con categoria-service"`. Lo mismo pasa con cita → medico.

## Decisiones y diferencias con la guía

- **Spring Boot 4.1.2** en lugar de 4.1.0, porque Initializr ya no ofrece 4.1.0.
- **Java 21**, que es el JDK instalado. El código no usa nada propio de Java 25.
- En el `CategoriaController` de la guía se quitaron los `if (c == null)`: el service lanza `ResourceNotFoundException` y nunca devuelve `null`.
- En `DetallePedido` se agregó `@JsonIgnore` a `pedido`. Sin él, Jackson entra en recursión infinita (Pedido → detalles → pedido...).
- `PedidoService` usa `ResourceNotFoundException` (404) en lugar de `RuntimeException`, que respondía 500.
- En Spring Boot 4, `RestClient.Builder` necesita `spring-boot-starter-restclient`.
- `RestClientConfig` fuerza HTTP/1.1 y pone timeouts. Con el upgrade a HTTP/2 (h2c) del cliente del JDK, la llamada de cita a paciente se quedaba colgada sin responder.

## Conclusiones

1. Separar el negocio en servicios con su propia base de datos permite desplegar, escalar y fallar cada parte por separado: si se cae medico-service, pacientes sigue funcionando.
2. Sin FKs entre bases de datos, la integridad pasa a ser responsabilidad del código: cada servicio valida por HTTP lo que antes garantizaba la BD.
3. Los DTO con anotaciones de validación y un `@RestControllerAdvice` hacen que todos los servicios respondan errores con el mismo formato y el código HTTP correcto.
4. La comunicación entre servicios necesita timeouts y manejo de fallas. Sin ellos, un servicio lento o una incompatibilidad de protocolo deja colgada toda la cadena.
5. YAML y los perfiles (`dev`/`prod`) permiten cambiar puerto, base de datos y propiedades por ambiente sin tocar el código.
