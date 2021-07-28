package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

public class UserDto extends RepresentationModel<UserDto> {
  private Integer id;
  private String firstName;
  private String lastName;
  private String username;
  private List<OrderDto> orders;

  public UserDto(Integer id, String firstName, String lastName, String username, List<OrderDto> orders) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.orders = orders;
  }

  public UserDto(String firstName, String lastName, String username, List<OrderDto> orders) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.orders = orders;
  }

  public UserDto() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<OrderDto> getOrders() {
    return orders;
  }

  public void setOrders(List<OrderDto> orders) {
    this.orders = orders;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    if(!super.equals(o)) return false;
    UserDto userDto = (UserDto) o;
    return Objects.equals(id, userDto.id) &&
            Objects.equals(firstName, userDto.firstName) &&
            Objects.equals(lastName, userDto.lastName) &&
            Objects.equals(username, userDto.username) &&
            Objects.equals(orders, userDto.orders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id, firstName, lastName, username, orders);
  }

  @Override
  public String toString() {
    return "UserDto{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", username='" + username + '\'' +
            ", orders=" + orders +
            '}';
  }
}
