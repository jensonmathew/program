package com.tmobile.edp.hello.domain;

/**
 * Created by ssajjad1 on 3/1/2017.
 */
public class Greeting {

    private long id;
    private String greetingMessage;

    public Greeting(long id, String greetingMessage) {
        this.id = id;
        this.greetingMessage = greetingMessage;
    }

    public long getId() {
        return id;
    }

    public String getGreeting() {
        return greetingMessage;
    }
}
