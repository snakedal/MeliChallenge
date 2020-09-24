package com.challenge.dto;

public class ResponsePositionAndMessage {

    private Coordinates position;
    private String message;

    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
