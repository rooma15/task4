package com.epam.esm.web.impl;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.DuplicateResourceException;
import com.epam.esm.exception.PaginationException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.localization.LocaleTranslator;
import com.epam.esm.model.User;
import com.epam.esm.validator.PageValidator;
import com.epam.esm.validator.Validator;
import com.epam.esm.validator.impl.UserValidator;
import com.epam.esm.web.CertificateService;
import com.epam.esm.web.TagService;
import com.epam.esm.web.UserRepository;
import com.epam.esm.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final TagService tagService;
  private final PageValidator pageValidator;

  @Autowired
  public UserServiceImpl(
      UserRepository userRepository, TagService tagService, PageValidator pageValidator) {
    this.userRepository = userRepository;
    this.tagService = tagService;
    this.pageValidator = pageValidator;
  }

  @Override
  @Transactional
  public List<UserDto> getAll() {
    return userRepository.findAll().stream()
        .map(UserConverter::convertModelToDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UserDto getById(int id) {
    String formattedException = String.format(LocaleTranslator.translate("user.doesNotExist"), id);
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(formattedException, 40404));
    return UserConverter.convertModelToDto(user);
  }

  @Override
  @Transactional
  public TagDto getMostWidelyUsedTagOfRichestUser() {
    List<Object> params = new ArrayList<>();
    int tagId = userRepository.findMostUsedTag();
    return tagService.getById(tagId);
  }

  @Override
  @Transactional
  public List<UserDto> getPaginated(Integer page, Integer size) {
    pageValidator.validate(page, size);
    PageRequest pageRequest = PageRequest.of(page - 1, size);
    List<UserDto> users =
        userRepository.findAll(pageRequest).stream()
            .map(UserConverter::convertModelToDto)
            .collect(Collectors.toList());
    if (users.isEmpty()) {
      String formattedException = LocaleTranslator.translate("page.doesNotExist");
      throw new PaginationException(formattedException, 404);
    }
    return users;
  }

  public UserDto save(UserDto newUser) {
    Validator<UserDto> userValidator = new UserValidator(this);
    userValidator.validate(newUser);
    List<User> users = userRepository.findAll();
    for (User user : users) {
      if (user.getUsername().equals(newUser.getUsername())) {
        throw new DuplicateResourceException();
      }
    }
    User newDBUser = userRepository.saveAndFlush(UserConverter.convertDtoToModel(newUser));
    newUser.setId(newDBUser.getId());
    newUser.setOrders(
        newDBUser.getOrders().stream()
            .map(OrderConverter::convertModelToDto)
            .collect(Collectors.toList()));
    return newUser;
  }

  @Override
  public boolean isUserExists(String username) {
    User user = userRepository.findUserByUsername(username);
    return user != null;
  }
}
