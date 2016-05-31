package edu.neu.ccs.cs5500.chucknorris.messenger.database;

import edu.neu.ccs.cs5500.chucknorris.messenger.model.Message;
import edu.neu.ccs.cs5500.chucknorris.messenger.model.Profile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yoganandc on 5/27/16.
 */
public class MessengerDB {

    private static Map<Long, Message> messages = new ConcurrentHashMap<>();
    private static Map<Long, Profile> profiles = new ConcurrentHashMap<>();
    private static AtomicLong messageCounter = new AtomicLong(2L);
    private static AtomicLong profileCounter = new AtomicLong();

    static {
        Message m1 = new Message(0L, "Hello World", "Yogi");
        messages.put(m1.getId(), m1);
        Message m2 = new Message(1L, "Bye World", "Anton");
        messages.put(m2.getId(), m2);
    }

    public static Map<Long, Message> getMessages() {
        return messages;
    }

    public static Map<Long, Profile> getProfiles() {
        return profiles;
    }

    public static long getNextMessageId() {
        return messageCounter.getAndIncrement();
    }

    public static long getNextProfileId() {
        return profileCounter.getAndIncrement();
    }

}
