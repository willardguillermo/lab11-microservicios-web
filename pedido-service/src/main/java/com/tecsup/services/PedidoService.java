package com.tecsup.services;

import com.tecsup.client.ClienteClient;
import com.tecsup.client.ProductoClient;
import com.tecsup.dto.DetallePedidoDTO;
import com.tecsup.dto.PedidoDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.DetallePedido;
import com.tecsup.model.Pedido;
import com.tecsup.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private ProductoClient productoClient;

    public List<Pedido> listar() {
        return repository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    @Transactional
    public Pedido guardar(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        copiar(dto, pedido);
        return repository.save(pedido);
    }

    @Transactional
    public Pedido actualizar(Long id, PedidoDTO dto) {
        Pedido pedido = buscarPorId(id);
        copiar(dto, pedido);
        return repository.save(pedido);
    }

    public void eliminar(Long id) {
        repository.delete(buscarPorId(id));
    }

    // Valida contra los otros microservicios y arma el pedido con su total
    private void copiar(PedidoDTO dto, Pedido pedido) {
        clienteClient.verificarExiste(dto.getIdCliente());

        pedido.setFecha(dto.getFecha());
        pedido.setIdCliente(dto.getIdCliente());
        pedido.getDetalles().clear();

        double total = 0;
        for (DetallePedidoDTO d : dto.getDetalles()) {
            productoClient.verificarExiste(d.getIdProducto());

            DetallePedido detalle = new DetallePedido();
            detalle.setIdProducto(d.getIdProducto());
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecio(d.getPrecio());
            detalle.setPedido(pedido);
            pedido.getDetalles().add(detalle);

            total += d.getCantidad() * d.getPrecio();
        }
        pedido.setTotal(Math.round(total * 100) / 100.0);
    }
}
