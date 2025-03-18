package com.mengnnakk.controller;


import com.mengnnakk.base.SystemCode;
import okhttp3.MediaType;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;


@RestController
public class ErroController {
    private static final String PATH  = "/error";

    public ErroController(){
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }


    @RequestMapping(produces = {PATH})
    @ResponseBody
    public ResponseEntity<Map<String,Object>> error(HttpServletRequest request){
        Map<String,Object> error = new HashMap<>(2);
        error.put("code", SystemCode.InnerError.getCode());
        error.put("massage",SystemCode.InnerError.getMessage());
        return new ResponseEntity<>(error,HttpStatus.OK);
    }
    @Override
    public String getErrorPATH(){
        return PATH;
    }
}
