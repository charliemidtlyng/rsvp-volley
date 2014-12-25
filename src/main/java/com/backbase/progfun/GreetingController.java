package com.backbase.progfun;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

    @RequestMapping("/greeting")
    public @ResponseBody String greeting(@RequestParam(required = false, defaultValue = "World") String name) {
        return "Hello " + name;
    }

}
