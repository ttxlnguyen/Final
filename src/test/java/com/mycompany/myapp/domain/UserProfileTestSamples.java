package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile().id(1L).username("username1").email("email1").password("password1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile().id(2L).username("username2").email("email2").password("password2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString());
    }
}
