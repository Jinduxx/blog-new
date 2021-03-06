package com.blog.controller;

import com.blog.Service.serviceImpl.UserServiceImpl;
import com.blog.model.User;
import com.blog.pojo.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    private final UserServiceImpl userService;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public String addUser(@RequestBody User user, HttpSession session) {

        if (user.getUserName().length() < 2) {
            session.setAttribute("message", " firstname cannot be less than 2 character long");
            System.out.println("1");
            return "redirect:/";
        }
        if (user.getFirstName().length() < 2) {
            session.setAttribute("message", " firstname cannot be less than 2 character long");
            System.out.println("2");
            return "redirect:/";
        }

        if (user.getLastName().length() < 2) {
            session.setAttribute("message", "lastname cannot be less than 2 character long");
            System.out.println("3");
            return "redirect:/";
        }


        if (user.getEmail() == null) {
            session.setAttribute("message", "email cannot be null");
            System.out.println("4");
            return "redirect:/";
        } else if(!isValidEmail(user.getEmail())){
            session.setAttribute("message", "email is not valid");
            System.out.println("5");
            return "redirect:/";
        }

        if (user.getPassword().length() < 7) {
            session.setAttribute("message", "password cannot be less than 6 character long");
            System.out.println("6");
            return "redirect:/";
        }

        if(userService.createUser(user)){
            session.setAttribute("message", "Successfully registered!!!");
            return "Successfully registered!!!";
        }else{
            session.setAttribute("message", "Failed to register or email already exist");
        }
        System.out.println("7");
        return "redirect:/";
    }


    @GetMapping( "/login")
    public String loginProcess(@RequestBody LoginDto loginDto, HttpSession session) {
        System.out.println("login 1");
        User user = userService.getUser(loginDto.getEmail(), loginDto.getPassword());
//        HttpSession httpSession = request.getSession();
        if (user != null) {
            session.setAttribute("user", user);
            return "successfully logged in " +user;
//            return "redirect:/home";
        } else {
            session.setAttribute("message", "Email or Password is wrong!!!");
            return "Email or Password is wrong!!!";
//            return "redirect:/";
        }
    }

    @GetMapping( "/Logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.invalidate();

        return "successfully logged out";
//        return "redirect:/";
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

//    @GetMapping("/users")
//    public List<User> getUsers() {
//        return userRepository.allUsers();
//    }


    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) throws InterruptedException {
        System.out.println("You have 1 minute to make up your mind");
        TimeUnit.SECONDS.sleep(60);
        System.out.println("Do you still want to delete");
        TimeUnit.SECONDS.sleep(60);
        System.out.println("deleted");
        return userService.deleteUser(id);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }
//    @Scheduled(fixedDelay = 2000)
//    public String deleteUser(){
//        return userService.deleteUser(id);
//    }

//    @DeleteMapping("/user")
//    public String deleteUser(@RequestBody User user){
//        return userService.deleteUser(user.getEmail());
//    }

    //for time delay

//
//    @Scheduled(fixedDelay = 2000)
//    public void scheduleTaskWithFixedDelay() {
//        logger.info("Fixed Delay Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException ex) {
//            logger.error("Ran into an error {}", ex);
//            throw new IllegalStateException(ex);
//        }
//    }
}
