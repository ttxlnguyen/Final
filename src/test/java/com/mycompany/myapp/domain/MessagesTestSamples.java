package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MessagesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Messages getMessagesSample1() {
        return new Messages().id(1L).content("content1");
    }

    public static Messages getMessagesSample2() {
        return new Messages().id(2L).content("content2");
    }

    public static Messages getMessagesRandomSampleGenerator() {
        return new Messages().id(longCount.incrementAndGet()).content(UUID.randomUUID().toString());
    }
}
