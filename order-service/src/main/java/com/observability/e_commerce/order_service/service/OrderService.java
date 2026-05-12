package com.observability.e_commerce.order_service.service;

import com.observability.e_commerce.order_service.dto.ProductResponse;
import com.observability.e_commerce.order_service.dto.UserResponse;
import com.observability.e_commerce.order_service.entity.Order;
import com.observability.e_commerce.order_service.repository.OrderRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final MeterRegistry meterRegistry;

//    public Order create(Order order) {
//        Timer.Sample sample = Timer.start(meterRegistry);
//
//        try {
//            log.info("Creating order for userId={} productId={}",
//                    order.getUserId(),
//                    order.getProductId());
//
//            // Call user-service
//            UserResponse userResponse = restTemplate.getForObject("http://localhost:8081/users/" + order.getUserId(),
//                    UserResponse.class);
//
//            // Call product-service
//            ProductResponse productResponse = restTemplate.getForObject("http://localhost:8082/products/" + order.getProductId(),
//                    ProductResponse.class);
//
//            log.info("User found : {}", userResponse.getName());
//            log.info("Product found : {}", productResponse.getName());
//
//            order.setStatus("CREATED");
//
//            meterRegistry.counter("orders.created").increment();
//
//            return orderRepository.save(order);
//        } finally {
//            sample.stop(
//                    Timer.builder("orders.creation.time")
//                            .description("Time taken to create orders")
//                            .register(meterRegistry)
//            );
//        }
//    }

    public Order create(Order order) {

        Timer.Sample sample = Timer.start(meterRegistry);

        try {

            log.info("Creating order for userId={} productId={}",
                    order.getUserId(),
                    order.getProductId());

            // Simulate random failure
            if (Math.random() < 0.3) {

                meterRegistry.counter("orders.failed",
                                "reason", "random_failure")
                        .increment();

                log.error("Random failure occurred while creating order");

                throw new RuntimeException("Random order failure");
            }

            // Call user-service
            UserResponse userResponse;

            try {

                userResponse = restTemplate.getForObject(
                        "http://localhost:8081/users/" + order.getUserId(),
                        UserResponse.class
                );

            } catch (Exception e) {

                meterRegistry.counter("downstream.errors",
                                "service", "user-service")
                        .increment();

                log.error("user-service call failed : {}", e.getMessage());

                throw new RuntimeException("User service unavailable");
            }

            // Call product-service
            ProductResponse productResponse;

            try {

                productResponse = restTemplate.getForObject(
                        "http://localhost:8082/products/" + order.getProductId(),
                        ProductResponse.class
                );

            } catch (Exception e) {

                meterRegistry.counter("downstream.errors",
                                "service", "product-service")
                        .increment();

                log.error("product-service call failed : {}", e.getMessage());

                throw new RuntimeException("Product service unavailable");
            }

            log.info("User found : {}", userResponse.getName());
            log.info("Product found : {}", productResponse.getName());

            // Simulate stock validation failure
            if (productResponse.getStock() < order.getQuantity()) {

                meterRegistry.counter("orders.failed",
                                "reason", "insufficient_stock")
                        .increment();

                log.error("Insufficient stock for product={}",
                        productResponse.getName());

                throw new RuntimeException("Insufficient stock");
            }

            order.setStatus("CREATED");

            meterRegistry.counter("orders.created").increment();

            log.info("Order created successfully");

            return orderRepository.save(order);

        } catch (Exception e) {

            meterRegistry.counter("orders.errors.total")
                    .increment();

            log.error("Order creation failed : {}", e.getMessage());

            throw e;

        } finally {

            // Alert rules
            // Thread.sleep(3000);

            sample.stop(
                    Timer.builder("orders.creation.time")
                            .description("Time taken to create orders")
                            .register(meterRegistry)
            );
        }
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
