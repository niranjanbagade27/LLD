package com.example.lldpubsub;

import java.util.ArrayList;
import java.util.List;

public class PublisherImpl implements Publisher {
    private String publisherName;
    private List<Subscriber> subscriberList;

    private List<String> queue;

    public PublisherImpl(String publisherName) {
        this.publisherName = publisherName;
        this.subscriberList = new ArrayList<>();
        this.queue = new ArrayList<>();
    }

    @Override
    public void register(Subscriber subscriber) {
        System.out.println("Registered new subscriber " + subscriber + " in publisher " + this.publisherName);
        subscriberList.add(subscriber);
    }

    @Override
    public void publish(String message) {
        queue.add(message);
        for (Subscriber subscriber : subscriberList) {
            System.out.println("sending notification to " + subscriber);
            subscriber.sendMessage(message);
        }
    }

    @Override
    public void replay(int from) {
        if (from < queue.size()) {
            for (int i = from; i < queue.size(); i++) {
                for (Subscriber subscriber : subscriberList) {
                    subscriber.sendMessage(queue.get(i));
                }
            }
        } else {
            System.out.println("ERROR! Insufficient data to replay. There are only " + queue.size() + " messages in the queue.");
        }
    }


}
