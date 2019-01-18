package com.tmobile.edp.hello.domain;

/**
 * Created by ssajjad1 on 3/1/2017.
 */
public class Greeting {

    private long id;
    private String greeting;

    public Greeting(long id, String greeting) {
        this.id = id;
        this.greeting = greeting;
    }

    public long getId() {
        return id;
    }

    public String getGreeting() {
        return greeting;
    }
}
