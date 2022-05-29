package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.entity.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;

  @Override
  public OrderDto createOrder(OrderDto orderDetails) {
    orderDetails.setOrderId(UUID.randomUUID().toString());
    orderDetails.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    OrderEntity orderEntity = mapper.map(orderDetails, OrderEntity.class);

    orderRepository.save(orderEntity);
    return mapper.map(orderEntity, OrderDto.class);
  }

  @Override
  public OrderDto getOrderByOrderId(String orderId) {
    OrderEntity order = orderRepository.findByOrderId(orderId);
    return new ModelMapper().map(order, OrderDto.class);
  }

  @Override
  public Iterable<OrderEntity> getOrdersByUserId(String userId) {
    return orderRepository.findByUserId(userId);
  }
}
