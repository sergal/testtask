package ru.sergal.testtask.restapi.restful.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestGetResultOut {

    private String name;
    private Integer days;

    public RequestGetResultOut() {}

    public RequestGetResultOut(String name, Integer days) {
        this.name = name;
        this.days = days;
    }
}
