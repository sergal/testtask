package ru.sergal.testtask.restapi.restful.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageOut {

    private String message;

    public MessageOut() {}

    private MessageOut(String message) {
        this.message = message;
    }

    public static MessageOut withMessage(String message) {
        return new MessageOut(message);
    }
}
