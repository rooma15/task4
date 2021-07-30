package com.epam.esm.web.contoller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.registration.RegistrationClient;
import com.epam.esm.web.OrderService;
import com.epam.esm.web.UserService;
import com.epam.esm.web.hateoas.HateoasHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/users")
public class UserController {

  private final UserService userService;
  private final OrderService orderService;
  private final HateoasHelper hateoasHelper;

  @Autowired
  public UserController(
      UserService userService, OrderService orderService, HateoasHelper hateoasHelper) {
    this.userService = userService;
    this.orderService = orderService;
    this.hateoasHelper = hateoasHelper;
  }

  @RequestMapping(method = GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public CollectionModel<UserDto> getUsers(
      @RequestHeader("accept-language") String language,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "5") int size) {
    Locale.setDefault(Locale.forLanguageTag(language));
    List<UserDto> users = userService.getPaginated(page, size);
    return hateoasHelper.makeUserLinks(users);
  }

  @RequestMapping(value = "/{id}", method = GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public UserDto getUser(@RequestHeader("accept-language") String language, @PathVariable int id) {
    Locale.setDefault(Locale.forLanguageTag(language));
    UserDto user = userService.getById(id);
    return hateoasHelper.makeUserLinks(user);
  }

  @RequestMapping(
      value = "/{userId}/orders",
      method = POST,
      produces = "application/json",
      consumes = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public OrderDto orderCertificate(
      @RequestHeader("accept-language") String language,
      @PathVariable int userId,
      @RequestBody OrderDto order) {
    Locale.setDefault(Locale.forLanguageTag(language));
    OrderDto newOrder = orderService.orderCertificate(userId, order.getCertificate().getId());
    return hateoasHelper.makeOrderLinks(newOrder, userId);
  }

  @RequestMapping(value = "/{userId}/orders", method = GET, produces = "application/json")
  public CollectionModel<OrderDto> getUserOrders(
      @RequestHeader("accept-language") String language,
      @PathVariable int userId,
      @RequestParam(required = false, defaultValue = "1") Integer page,
      @RequestParam(required = false, defaultValue = "5") Integer size) {
    Locale.setDefault(Locale.forLanguageTag(language));
    List<OrderDto> orders =
        orderService.getPaginated(orderService.getUserOrders(userId), page, size);
    return hateoasHelper.makeOrderLinks(orders, userId);
  }

  @RequestMapping(value = "/{userId}/orders/{orderId}", method = GET, produces = "application/json")
  public OrderDto getUserOrder(
      @RequestHeader("accept-language") String language,
      @PathVariable int userId,
      @PathVariable int orderId) {
    Locale.setDefault(Locale.forLanguageTag(language));
    OrderDto order = orderService.getUserOrder(userId, orderId);
    return hateoasHelper.makeOrderLinks(order, userId);
  }
}
