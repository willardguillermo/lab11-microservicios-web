# Evidencia de pruebas

Resultados reales de las peticiones hechas con `curl` durante el desarrollo (14/11/2026), con los servicios corriendo en local. Las mismas peticiones están en `postman/Lab11-Microservicios.postman_collection.json`.

## categoria-service (:8082)

| Petición | Código | Respuesta |
|---|---|---|
| `POST /api/categorias` `{"nombre":"Laptops","estado":"ACTIVO"}` | 201 | `{"id":1,"nombre":"Laptops","estado":"ACTIVO"}` |
| `GET /api/categorias` | 200 | lista con Laptops, Accesorios, Impresoras |
| `PUT /api/categorias/3` `{"nombre":"Impresoras Laser",...}` | 200 | `{"id":3,"nombre":"Impresoras Laser","estado":"ACTIVO"}` |
| `POST` con `nombre` y `estado` vacíos | 400 | `{"estado":"El estado es obligatorio","nombre":"El nombre es obligatorio"}` |
| `POST` con JSON mal formado | 400 | `{"codigo":"400","mensaje":"El cuerpo de la petición no es un JSON válido"}` |
| `GET /api/categorias/999` | 404 | `{"codigo":"404","mensaje":"Categoria no encontrada con id: 999"}` |
| `DELETE /api/categorias/999` | 404 | `{"codigo":"404","mensaje":"Categoria no encontrada con id: 999"}` |
| `GET /api/categorias/ambiente` (perfil dev) | 200 | `{"ambiente":"desarrollo"}`; el log muestra `profile is active: "dev"` |

## cliente-service (:8081)

| Petición | Código | Respuesta |
|---|---|---|
| `POST /api/clientes` (Juan, DNI 12345678) | 201 | `{"id":1,"nombre":"Juan",...}` |
| `POST /api/clientes` (María, DNI 87654321) | 201 | `{"id":2,"nombre":"María",...}` |
| `POST` con `nroDni":"123"` | 400 | `{"nroDni":"El DNI debe tener 8 dígitos"}` |
| `POST` con DNI repetido | 400 | `{"codigo":"400","mensaje":"Ya existe un registro con ese dato único (DNI)"}` |
| `GET /api/clientes/50` | 404 | `{"codigo":"404","mensaje":"Cliente no encontrado con id: 50"}` |

## producto-service (:8083): llama a categoria-service

| Petición | Código | Respuesta |
|---|---|---|
| `POST /api/productos` (Laptop Lenovo, categoriaId 1) | 201 | `{"id":1,"nombre":"Laptop Lenovo ThinkPad","precio":3500.0,"stock":15,"categoriaId":1}` |
| `GET /api/productos/1/detalle` | 200 | `{"id":1,...,"categoria":{"id":1,"nombre":"Laptops","estado":"ACTIVO"}}` |
| `POST` con `categoriaId: 99` | 400 | `{"codigo":"400","mensaje":"La categoría con id 99 no existe en categoria-service"}` |
| `POST` con nombre vacío, precio y stock negativos | 400 | `{"precio":"El precio debe ser mayor a 0","stock":"El stock no puede ser negativo","nombre":"El nombre es obligatorio","categoriaId":"La categoría es obligatoria"}` |
| `GET /api/productos/77` | 404 | `{"codigo":"404","mensaje":"Producto no encontrado con id: 77"}` |
| `GET /api/productos/1/detalle` **con categoria-service apagado** | 500 | `{"codigo":"500","mensaje":"No se pudo comunicar con categoria-service"}` |

## pedido-service (:8084): llama a cliente-service y producto-service

| Petición | Código | Respuesta |
|---|---|---|
| `POST /api/pedidos` (cliente 1, 1 laptop + 2 mouse) | 201 | `{"idPedido":1,"total":3679.8,"detalles":[...]}` |
| `POST /api/pedidos` (cliente 2, 2 mouse + 2 teclados) | 201 | `{"idPedido":2,"total":579.78,...}` |
| `PUT /api/pedidos/2` (1 teclado) | 200 | `{"idPedido":2,"fecha":"2026-06-11","total":199.99,"detalles":[{"idProducto":3,...}]}` |
| `POST` con `idCliente: 99` | 400 | `{"codigo":"400","mensaje":"El cliente con id 99 no existe en cliente-service"}` |
| `POST` con `idProducto: 42` | 400 | `{"codigo":"400","mensaje":"El producto con id 42 no existe en producto-service"}` |
| `POST` sin fecha y con detalles vacíos | 400 | `{"fecha":"La fecha es obligatoria","detalles":"El pedido debe tener al menos un detalle"}` |
| `GET /api/pedidos/999` | 404 | `{"codigo":"404","mensaje":"Pedido no encontrado con id: 999"}` |

## paciente-service (:8085)

| Petición | Código | Respuesta |
|---|---|---|
| `POST /api/pacientes` (Ana Lucía Quispe) | 201 | `{"idPaciente":1,"dni":"70123456","estado":"A",...}` |
| `PUT /api/pacientes/2` (nombres "Carlos Alberto") | 200 | `{"idPaciente":2,"nombres":"Carlos Alberto",...}` |
| `POST` con DNI de 1 dígito, sexo "Z", correo inválido y fecha futura | 400 | `{"fechaNacimiento":"La fecha de nacimiento debe ser una fecha pasada","correo":"El correo no es válido","sexo":"El sexo debe ser M o F","dni":"El DNI debe tener 8 dígitos","nombres":"Los nombres son obligatorios"}` |
| `POST` con DNI repetido | 400 | `{"codigo":"400","mensaje":"Ya existe un registro con ese dato único"}` |
| `GET /api/pacientes/99` | 404 | `{"codigo":"404","mensaje":"Paciente no encontrado con id: 99"}` |

## medico-service (:8086)

| Petición | Código | Respuesta |
|---|---|---|
| `POST /api/especialidades` (Cardiología) | 201 | `{"idEspecialidad":1,"nombre":"Cardiología",...}` |
| `POST /api/medicos` (Dra. Rosa Salazar, especialidad 1) | 201 | `{"idMedico":1,"cmp":"CMP-045123","especialidad":{"nombre":"Cardiología",...}}` |
| `POST /api/medicos/1/horarios` (LUNES 08:00–13:00) | 201 | `{"idHorario":1,"diaSemana":"LUNES","horaInicio":"08:00:00","horaFin":"13:00:00"}` |
| `PUT /api/medicos/2` | 200 | `{"idMedico":2,"nombres":"Jorge Luis",...}` |
| `POST` con `idEspecialidad: 9` | 400 | `{"codigo":"400","mensaje":"La especialidad con id 9 no existe"}` |
| `POST` con CMP repetido | 400 | `{"codigo":"400","mensaje":"Ya existe un registro con ese dato único (CMP)"}` |
| Horario 13:00–08:00 | 400 | `{"codigo":"400","mensaje":"La hora de fin debe ser posterior a la hora de inicio"}` |
| Horario con día "FERIADO" | 400 | `{"diaSemana":"El día debe ser LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO o DOMINGO"}` |
| `DELETE` de un médico con horarios | 200 | `{"mensaje":"Médico eliminado correctamente"}` (sus horarios también se borran) |
| `GET /api/medicos/50` | 404 | `{"codigo":"404","mensaje":"Médico no encontrado con id: 50"}` |

## cita-service (:8087): llama a paciente-service y medico-service

| Petición | Código | Respuesta |
|---|---|---|
| `POST /api/citas` (16/11 09:00, paciente 1, médico 1) | 201 | `{"idCita":1,"estado":"PROGRAMADA",...}` |
| `GET /api/citas/1/detalle` | 200 | `{"cita":{...},"paciente":{"nombres":"Ana Lucía",...},"medico":{"nombres":"Rosa","especialidad":{"nombre":"Cardiología"},...}}` |
| `PATCH /api/citas/1/estado` `{"estado":"CONFIRMADA"}` | 200 | `{"idCita":1,"estado":"CONFIRMADA",...}` |
| `POST /api/citas/1/atencion` (Angina estable) | 201 | `{"idAtencion":1,"idCita":1,"diagnostico":"Angina estable","fechaRegistro":"2026-11-14T14:36:55",...}`; la cita pasa a ATENDIDA |
| `PATCH /api/citas/3/cancelar` | 200 | `{"idCita":3,"estado":"CANCELADA",...}` |
| Misma hora y mismo médico que la cita 1 | 400 | `{"codigo":"400","mensaje":"El médico ya tiene una cita el 2026-11-16 a las 09:00"}` |
| Lunes 18:00 (fuera del horario) | 400 | `{"codigo":"400","mensaje":"El médico no atiende el LUNES a las 18:00"}` |
| `idPaciente: 99` | 400 | `{"codigo":"400","mensaje":"El paciente con id 99 no existe en paciente-service"}` |
| `idMedico: 99` | 400 | `{"codigo":"400","mensaje":"El médico con id 99 no existe en medico-service"}` |
| Fecha pasada y campos faltantes | 400 | `{"idPaciente":"El paciente es obligatorio","fecha":"La fecha no puede ser pasada","hora":"La hora es obligatoria","idMedico":"El médico es obligatorio"}` |
| `PATCH estado` `{"estado":"ATENDIDA"}` | 400 | `{"mensaje":"Para marcar la cita como ATENDIDA registre la atención en POST /api/citas/1/atencion"}` |
| `PATCH estado` `{"estado":"PENDIENTE"}` | 400 | `{"estado":"El estado debe ser PROGRAMADA, CONFIRMADA, ATENDIDA o CANCELADA"}` |
| Cancelar la cita 1 (ya ATENDIDA) | 400 | `{"codigo":"400","mensaje":"No se puede pasar una cita de ATENDIDA a CANCELADA"}` |
| Registrar atención en la cita 3 (CANCELADA) | 400 | `{"codigo":"400","mensaje":"No se puede pasar una cita de CANCELADA a ATENDIDA"}` |
| `GET /api/citas/2/atencion` (sin atención) | 404 | `{"codigo":"404","mensaje":"La cita 2 aún no tiene atención registrada"}` |
| `GET /api/citas/99` | 404 | `{"codigo":"404","mensaje":"Cita no encontrada con id: 99"}` |
| `GET /api/citas/2/detalle` **con medico-service apagado** | 500 | `{"codigo":"500","mensaje":"No se pudo comunicar con medico-service"}` |

## Pruebas automáticas

```
./mvnw test  →  OK en los 7 servicios (carga del contexto de Spring con conexión a MySQL)
```
