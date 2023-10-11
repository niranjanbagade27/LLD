package com.example.lldpubsub;

public class PubSubMain {
    public static void main(String[] args) {
        Publisher publisher = new PublisherImpl("Advertisement Publisher");
        publisher.register(new EmailSubscriberImpl("Email Sub"));
        publisher.register(new SMSSubscriberImpl("SMS Sub"));
        publisher.publish("first message");
        System.out.println("-----------------");
        publisher.replay(0);
    }
}
