package com.digi.circuit.resiliancedemo.service;

import com.digi.circuit.resiliancedemo.connector.Connector;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.operator.CircuitBreakerOperator;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


@Service(value = "businessBService")
public class BusinessBService implements BusinessService {

    private final Connector backendBConnector;
    private final CircuitBreaker circuitBreaker;

    public BusinessBService(@Qualifier("backendBConnector") Connector backendBConnector,
                            CircuitBreakerRegistry circuitBreakerRegistry, CircuitBreakerProperties circuitBreakerProperties){
        this.backendBConnector = backendBConnector;
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("backendB", () -> circuitBreakerProperties.createCircuitBreakerConfig("backendB"));
    }


    @Override
    public String failure() {
        return CircuitBreaker.decorateSupplier(circuitBreaker,backendBConnector::failure).get();
    }


    @Override
    public String success() {
        return CircuitBreaker.decorateSupplier(circuitBreaker,backendBConnector::success).get();
    }

    @Override
    public String ignore() {
        return CircuitBreaker.decorateSupplier(circuitBreaker, backendBConnector::ignoreException).get();
    }

    @Override
    public Try<String> methodWithRecovery() {

        Supplier<String> backendFunction = CircuitBreaker.decorateSupplier(circuitBreaker, () -> backendBConnector.failure());

        return Try.ofSupplier(backendFunction).recover((throwable) -> recovery(throwable));
    }


    public Observable<String> methodwhichReturnsAStream(){

        return backendBConnector.methodWhichReturnsAStream().timeout(
                1, TimeUnit.SECONDS
        ).lift(CircuitBreakerOperator.of(circuitBreaker));
    }


    private String recovery(Throwable throwable) {
        return "Hello world from recovery";
    }
}
