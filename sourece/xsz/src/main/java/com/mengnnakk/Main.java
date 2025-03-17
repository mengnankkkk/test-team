package com.mengnnakk;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.swing.*;
@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(value = {SystemConfig.class})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);

    }
}