package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

  private final UserService userService;
  private final Environment environment;
  private final Greeting greeting;

  public UserController(Environment environment,
                        Greeting greeting,
                        UserService userService) {
    this.environment = environment;
    this.greeting = greeting;
    this.userService = userService;
  }

  @GetMapping("/health-check")
  public String status() {
    return String.format("It's Working In User Service on Port %s",
        environment.getProperty("local.server.port"));
  }

  @GetMapping("/welcome")
  public String welcome() {
//    return environment.getProperty("greeting.message");
    return greeting.getMessage();
  }

  @PostMapping("/users")
  public ResponseEntity<ResponseUser> createUser(@RequestBody @Valid RequestUser requestUser) {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    UserDto userDto = mapper.map(requestUser, UserDto.class);
    userService.createUser(userDto);

    ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseUser);
  }

  @GetMapping("/users")
  public ResponseEntity<List<ResponseUser>> getUserByAll() {
    Iterable<UserEntity> users = userService.getUserByAll();
    List<ResponseUser> result = new ArrayList<>();

    users.forEach(user ->
      result.add(new ModelMapper().map(user, ResponseUser.class))
    );
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(result);
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
    UserDto userDto = userService.getUserByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(new ModelMapper()
            .map(userDto, ResponseUser.class)
        );
  }
}
