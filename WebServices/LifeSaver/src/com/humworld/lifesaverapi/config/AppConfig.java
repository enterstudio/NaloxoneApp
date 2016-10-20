/*
 * Copyright (c) 2016 Humworld INC,
 */

package com.humworld.lifesaverapi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring configuration class.
 * 
 * @author Humworld
 * @version 1.0
 */
@Configuration
@EnableWebMvc
@ComponentScan( basePackages = "com.humworld.lifesaverapi" )
public class AppConfig {

}
