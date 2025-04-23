package auth_service.services;

import auth_service.dtos.CreateOrderItemRequest;
import auth_service.dtos.CreateOrderRequest;
import auth_service.entities.Order;
import auth_service.entities.OrderItem;
import auth_service.entities.User;
import auth_service.enums.StatusPedido;
import auth_service.repositories.OrderRepository;
import auth_service.repositories.ProductRepository;
import auth_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<String> generateOrder(CreateOrderRequest request, String user) {
        var order = new Order();

        order.setStatus(StatusPedido.PENDING);
        order.setCustumer(userRepository.findByUsername(user).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado")));

        List<OrderItem> items = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for(CreateOrderItemRequest itemReq : request.items()) {
            var product = productRepository.getById(itemReq.productId());

            OrderItem item = new OrderItem();

            item.setOrder(order);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemReq.quantity());

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())));

            items.add(item);

        }

        order.setItems(items);
        order.setTotalAmount(total);
        orderRepository.save(order);


        return ResponseEntity.ok("Pedido realizado com sucesso!");
    }

    public List<Order> getAllOrders(){
        var orders = orderRepository.findAll();
        return orders;
    }



}

