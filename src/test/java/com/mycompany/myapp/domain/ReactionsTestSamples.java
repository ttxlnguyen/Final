package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReactionsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reactions getReactionsSample1() {
        return new Reactions().id(1L).reaction("reaction1");
    }

    public static Reactions getReactionsSample2() {
        return new Reactions().id(2L).reaction("reaction2");
    }

    public static Reactions getReactionsRandomSampleGenerator() {
        return new Reactions().id(longCount.incrementAndGet()).reaction(UUID.randomUUID().toString());
    }
}
