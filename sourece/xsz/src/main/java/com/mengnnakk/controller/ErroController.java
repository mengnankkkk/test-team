package com.mengnnakk.controller;


import okhttp3.MediaType;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ErroController {
    private static final String PATH  = "/error";

    public ErroController(){
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }


    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String,Object>> error(HttpServletRequest request){

    }
}
