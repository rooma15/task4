package com.epam.esm.web.contoller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.registration.RegistrationClient;
import com.epam.esm.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class AuthController {
  private final RegistrationClient registrationClient;
  private final UserService userService;

  @Autowired
  public AuthController(RegistrationClient registrationClient, UserService userService) {
    this.registrationClient = registrationClient;
    this.userService = userService;
  }

  @RequestMapping(value = "/register", method = POST)
  public UserDto register(@RequestBody UserDto user) {
    UserDto newUser = userService.save(user);
    registrationClient.registerUser(user);
    return newUser;
  }
}
