package com.example.lldpubsub;

import lombok.ToString;

@ToString
public class SMSSubscriberImpl implements Subscriber {

    public String subscriberName;

    public SMSSubscriberImpl(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("SMS is " + message);
    }
}
