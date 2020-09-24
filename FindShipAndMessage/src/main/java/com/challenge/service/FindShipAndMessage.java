package com.challenge.service;

import com.challenge.dto.ResponsePositionAndMessage;
import com.challenge.model.SatelliteMessages;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface FindShipAndMessage {

    public abstract ResponsePositionAndMessage topSecret(ArrayList<LinkedHashMap> satellites);

    public abstract void postTopSecretSplit(SatelliteMessages satelliteMessage) throws Exception;

    public abstract ResponsePositionAndMessage getTopSecretSplit(SatelliteMessages satelliteMessageInput) throws Exception;
}
