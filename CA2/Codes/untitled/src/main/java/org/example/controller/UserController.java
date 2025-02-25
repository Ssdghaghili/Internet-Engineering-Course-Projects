//package org.example.controller;
//
//import org.example.model.User;
//import jakarta.validation.Valid;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
//        return userService.addUser(user);
//    }
//}
