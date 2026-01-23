package com.Prakhar.Ecommerce.Contoller;


import com.Prakhar.Ecommerce.Entity.User;
import com.Prakhar.Ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user)
    {
        return userService.registerUser(user);
    }

     @PostMapping("/login")
    public User loginUser(@RequestBody User user)
    {
        return userService.LginUser(user.getEmail(), user.getPassword());
    }


    @GetMapping
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    }

}
