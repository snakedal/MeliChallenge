package com.challenge.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SATELLITE_MESSAGES")
public class SatelliteMessages {

    @Id
    @Column(name = "SATELLITE_NAME")
    private String name;
    @Column(name = "DISTANCE")
    private Float distance;
    @Column(name = "MESSAGE")
    private String[] message;

    public SatelliteMessages() {
    }

    public SatelliteMessages(String name, Float distance, String[] message) {
        this.name = name;
        this.distance = distance;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }

}
