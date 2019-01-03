package com.digi.circuit.resiliancedemo.service;

import io.vavr.control.Try;

public interface BusinessService {

    String failure();

    String success();

    String ignore();

    Try<String> methodWithRecovery();

}
