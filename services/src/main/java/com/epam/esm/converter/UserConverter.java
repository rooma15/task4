package com.epam.esm.converter;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {
  public static User convertDtoToModel(UserDto userDto) {
    List<Order> orders;
    if(userDto.getOrders() != null){
      orders =
              userDto.getOrders().stream()
                      .map(OrderConverter::convertDtoToModel)
                      .collect(Collectors.toList());
    }else {
      orders = new ArrayList<>();
    }
    return new User(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(), orders);
  }

  public static UserDto convertModelToDto(User user) {
    List<OrderDto> orders;
    if(user.getOrders() != null){
      orders =
              user.getOrders().stream()
                      .map(OrderConverter::convertModelToDto)
                      .collect(Collectors.toList());
    }else {
      orders = new ArrayList<>();
    }
    return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), orders);
  }
}
