package com.example.userservice.service;

import com.example.userservice.client.OrderClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.entity.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.example.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final OrderClient orderClient;
  private final CircuitBreakerFactory circuitBreakerFactory;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));

    return new User(
        userEntity.getEmail(), userEntity.getEncryptedPwd(),
        true, true, true, true,
        new ArrayList<>());
  }

  @Override
  public UserDto createUser(UserDto userDto) {
    userDto.setUserId(UUID.randomUUID().toString());

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = mapper.map(userDto, UserEntity.class);
    userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

    userRepository.save(userEntity);
    return mapper.map(userEntity, UserDto.class);
  }

  @Override
  public UserDto getUserByUserId(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//    var orders = orderClient.getOrders(userId);
    log.info("Before call orders 마이크로서비스");
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
    List<ResponseOrder> orders =  circuitBreaker.run(
        () -> orderClient.getOrders(userId),
        throwable -> Collections.emptyList());
    log.info("after called orders 마이크로서비스");

    userDto.setOrders(orders);

    return userDto;

  }

  @Override
  public Iterable<UserEntity> getUserByAll() {
    return userRepository.findAll();
  }

  @Override
  public UserDto getUserDetailByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));

    return new ModelMapper()
        .map(userEntity, UserDto.class);
  }

}
