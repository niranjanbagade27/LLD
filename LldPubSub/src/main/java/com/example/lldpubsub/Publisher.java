package com.example.lldpubsub;

public interface Publisher {
    public void register(Subscriber subscriber);
    public void publish(String message);

    public void replay(int from);
}
