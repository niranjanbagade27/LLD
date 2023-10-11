package com.example.lldpubsub;

import lombok.ToString;

@ToString
public class EmailSubscriberImpl implements Subscriber {

    public String subscriberName;

    public EmailSubscriberImpl(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("Email is " + message);
    }
}
