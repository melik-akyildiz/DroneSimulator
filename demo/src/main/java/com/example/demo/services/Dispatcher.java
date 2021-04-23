package com.example.demo.services;

import java.io.IOException;

public interface Dispatcher extends Destination {

    void init() throws IOException;

    void start();

}
