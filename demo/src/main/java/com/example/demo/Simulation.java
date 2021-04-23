package com.example.demo;

import com.example.demo.services.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Simulation implements CommandLineRunner {

    private final Dispatcher dispatcher;

    @Autowired
    public Simulation(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


    public static void main(String[] args) {
        SpringApplication.run(Simulation.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dispatcher.init();
        dispatcher.start();
    }
}
