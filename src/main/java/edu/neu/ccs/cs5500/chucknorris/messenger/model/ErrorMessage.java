package edu.neu.ccs.cs5500.chucknorris.messenger.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by yoganandc on 6/3/16.
 */

@XmlRootElement
public class ErrorMessage {

    private String message;
    private Integer code;
    private String link;

    public ErrorMessage() {

    }

    public ErrorMessage(String message, Integer code, String link) {
        this.message = message;
        this.code = code;
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
