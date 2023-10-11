package com.example.lldpubsub;

import java.util.ArrayList;
import java.util.List;

public class PublisherImpl implements Publisher{
    private String publisherName;
    private List<Subscriber> subscriberList;

    public PublisherImpl(String publisherName){
        this.publisherName = publisherName;
        this.subscriberList = new ArrayList<>();
    }

    @Override
    public void register(Subscriber subscriber) {
        System.out.println("Registered new subscriber "+subscriber+" in publisher "+this.publisherName);
        subscriberList.add(subscriber);
    }

    @Override
    public void publish(String message) {
        for(Subscriber subscriber : subscriberList){
            System.out.println("sending notification to "+subscriber);
            subscriber.sendMessage(message);
        }
    }

}
