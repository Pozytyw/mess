package com.mess.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {
    @RequestMapping(value={"/","/home"}, method = RequestMethod.GET)
    public ModelAndView getIndex() {
        ModelAndView model = new ModelAndView();
        model.setViewName("home/home");
        return model;
    }
}
