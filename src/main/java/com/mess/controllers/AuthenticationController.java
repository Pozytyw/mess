package com.mess.controllers;

import com.mess.DTO.database.UserDTO;
import com.mess.DTO.database.UserForm;
import com.mess.DTO.database.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
public class AuthenticationController {
    @Autowired
    private UserService userService;

    //return login side
    @RequestMapping(value= {"/login"}, method= RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();

        model.setViewName("authentication/login");
        return model;
    }

    //return signUp side
    @RequestMapping(value= {"/signUp"}, method=RequestMethod.GET)
    public ModelAndView signUp() {
        ModelAndView model = new ModelAndView();
        UserForm userForm = new UserForm();
        model.addObject("userForm", userForm);
        model.setViewName("authentication/signUp");

        return model;
    }

    //processing post request from singUp side
    //register new user
    @RequestMapping(value= {"/signUp"}, method=RequestMethod.POST)
    public ModelAndView createUser(@Valid UserForm userForm, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        UserDTO userExists = userService.findUserByEmail(userForm.getEmail());

        //new user email equals to exists user
        if (userExists != null) {
            bindingResult.rejectValue("email", "error.user", "This email already exists!");
        }
        //passwords aren't confirm
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())){
            bindingResult.rejectValue("confirmPassword", "error.user", "Password must be confirm");
        }

        if (!bindingResult.hasErrors()) {
            //save new user if it's valid
            userService.saveUser(userForm);
            model.addObject("msg", "User has been registered successfully!");
            //return login form
            model.setViewName("authentication/login");
        }else{
            //remove password
            userForm.setPassword("");
            model.addObject("userForm", userForm);
            //if error occur return again signUP form
            model.setViewName("authentication/signUp");
        }

        return model;
    }
}
