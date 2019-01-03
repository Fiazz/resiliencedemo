package com.digi.circuit.resiliancedemo.connector;

import io.reactivex.Observable;

public interface Connector {

    String failure();

    String success();

    String ignoreException();

    Observable<String> methodWhichReturnsAStream();

}
