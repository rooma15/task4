package com.epam.esm.validator.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.DuplicateResourceException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.localization.LocaleTranslator;
import com.epam.esm.validator.Validator;
import com.epam.esm.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userValidator")
public class UserValidator implements Validator<UserDto> {

  private UserService userService;

  private final int USERNAME_MIN_LENGTH = 3;
  private final int PASSWORD_MIN_LENGTH = 8;

  @Autowired
  public UserValidator(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean validate(UserDto user) {
    String translatedException;
    if (userService.isUserExists(user.getUsername())) {
      translatedException = LocaleTranslator.translate("user.alreadyExists");
      throw new DuplicateResourceException(translatedException, 40904);
    }

    if (user.getFirstName() == null) {
      translatedException = LocaleTranslator.translate("user.firstnameMustBeFilled");
      throw new ValidationException(translatedException, 40304);
    }

    if (user.getLastName() == null) {
      translatedException = LocaleTranslator.translate("user.lastnameMustBeFilled");
      throw new ValidationException(translatedException, 40304);
    }

    if (user.getUsername() == null) {
      translatedException = LocaleTranslator.translate("user.usernameMustBeFilled");
      throw new ValidationException(translatedException, 40304);
    }

    if (user.getPassword() == null) {
      translatedException = LocaleTranslator.translate("user.passwordMustBeFilled");
      throw new ValidationException(translatedException, 40304);
    }

    if (user.getUsername().length() <= USERNAME_MIN_LENGTH) {
      translatedException =
          String.format(
              LocaleTranslator.translate("user.usernameMustBeGreater"), USERNAME_MIN_LENGTH);
      throw new ValidationException(translatedException, 40304);
    }

    if (user.getPassword().length() < PASSWORD_MIN_LENGTH) {
      translatedException =
          String.format(
              LocaleTranslator.translate("user.passwordMustBeGreater"), PASSWORD_MIN_LENGTH);
      throw new ValidationException(translatedException, 40304);
    }

    return true;
  }
}
