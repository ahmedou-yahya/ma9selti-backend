package backend.ma9selti.controller;

import backend.ma9selti.exception.ResourceNotFoundException;
import backend.ma9selti.model.Customer;
import backend.ma9selti.model.Order;
import backend.ma9selti.repository.CustomerRepository;
import backend.ma9selti.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8087")
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<List<Order>> getAllOrdersByCustomerId(@PathVariable(value = "customerId") Long customerId){
        if (!customerRepository.existsById(customerId))
            throw new ResourceNotFoundException("Not found customer with id = " + customerId);
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        if (orders.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/customers/{customerId}/order_id/{orderId}")
    public ResponseEntity<Order> getOrderByCustomerId(@PathVariable(value = "customerId") Long customerId, @PathVariable(value = "orderId") Long orderId){
        if (!customerRepository.existsById(customerId))
            throw new ResourceNotFoundException("Not found customer with id = " + customerId);
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        for (Order order: orders) {
            if(order.getId().equals(orderId)){
                return new ResponseEntity<>(order, HttpStatus.OK);
            }
        }
        throw  new ResourceNotFoundException("Not found order with id = " + orderId);
    }

    @PostMapping("/customers/{customerId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable(value = "customerId") Long customerId, @RequestBody Order orderRequest){
        Order order = customerRepository.findById(customerId).map(customer -> {
            orderRequest.setCustomer((customer));
            return orderRepository.save(orderRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found customer with id = " + customerId));

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/customers/{customerId}/order_phone/{orderId}")
    public ResponseEntity<HttpStatus> deleteOrderByCustomerId(@PathVariable(value = "customerId") Long customerId, @PathVariable(value = "orderId") Long orderId){
        if (!customerRepository.existsById(customerId))
            throw new ResourceNotFoundException("Not found customer with id = " + customerId);
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        for (Order order: orders) {
            if(order.getId().equals(orderId)){
                orderRepository.delete(order);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        throw  new ResourceNotFoundException("Not found order with id = " + orderId);
    }
    @PutMapping("/customers/{customerId}/orders/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable(value = "customerId") Long customerId, @PathVariable(value = "orderId") Long orderId, @RequestBody Order orderRequest ){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found customer with id = " + customerId));
        Order order = getOrderByCustomerId(customerId, orderId);

    }

    @GetMapping("/customers/{customerId}/order_phone/{phone_number}")
    public ResponseEntity<List<Order>> searchByPhoneNumber(@PathVariable(value = "customerId") Long customerId, @PathVariable(value = "phone_number") String phoneNumber){
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        if (orders.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<Order> answer = orders.stream().filter((Order order) -> {
            return order.getPhoneNumber().equals(phoneNumber);
        }).collect(Collectors.toList());

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
}

