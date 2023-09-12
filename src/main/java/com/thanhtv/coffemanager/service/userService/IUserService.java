package com.thanhtv.coffemanager.service.userService;

import com.thanhtv.coffemanager.model.User;
import com.thanhtv.coffemanager.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {
    Optional<User> findByEmail(String email);
}
