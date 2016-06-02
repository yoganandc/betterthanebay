package edu.neu.ccs.cs5500.chucknorris.messenger.service;

import edu.neu.ccs.cs5500.chucknorris.messenger.database.MessengerDB;
import edu.neu.ccs.cs5500.chucknorris.messenger.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yoganandc on 5/26/16.
 */
public class MessageService {

    private Map<Long, Message> messages = MessengerDB.getMessages();

    public List<Message> getAllMessages() {
        return new ArrayList<>(messages.values());
    }

    public Message getMessage(long id) {
        return messages.get(id);
    }

    public Message addMessage(Message message) {
        message.setId(MessengerDB.getNextMessageId());
        message.setUpdated(new Date());
        messages.put(message.getId(), message);
        return message;
    }

    public Message updateMessage(Message message) {
        if (message.getId() < 0) {
            return null;
        }
        message.setUpdated(new Date());
        messages.put(message.getId(), message);
        return message;
    }

    public Message removeMessage(long id) {
        return messages.remove(id);
    }
}
