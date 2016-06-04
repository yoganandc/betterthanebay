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

    public List<Message> getMessagesByUser(String author) {
        List<Message> ret = new ArrayList<>();
        for(Message m : messages.values()) {
            if(m.getAuthor().equals(author)) {
                ret.add(m);
            }
        }
        return ret;
    }

    public List<Message> getMessagesByPage(int start, int size) {
        int numMessages = messages.size();
        List<Message> ret = new ArrayList<>();
        if(start >= numMessages) {
            return ret;
        }
        else if(start + size > numMessages) {
            ret.addAll(new ArrayList<Message>(messages.values()).subList(start, numMessages));
            return ret;
        }
        else {
            ret.addAll(new ArrayList<Message>(messages.values()).subList(start, start + size));
            return ret;
        }
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
        if (!messages.containsKey(message.getId())) {
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
