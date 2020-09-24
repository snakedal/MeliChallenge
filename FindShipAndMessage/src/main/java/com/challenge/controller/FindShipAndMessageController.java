package com.challenge.controller;

import com.challenge.dto.ResponsePositionAndMessage;
import com.challenge.model.SatelliteMessages;
import com.challenge.service.FindShipAndMessageImpl;
import com.challenge.util.FindShipAndMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class FindShipAndMessageController {

    public static final String MESSAGE_RECEIVED_SUCCESSFULLY_USE_GET_SERVICE_TO_DETERMINE_MESSAGE_AND_POSITION = "Message received successfully, use Get Service to determine message and position";
    @Autowired
    FindShipAndMessageImpl findShipAndMessage;

    @RequestMapping(value = "/topsecret", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    public ResponsePositionAndMessage topSecret(@RequestBody Map body, HttpServletResponse httpServletResponse) throws Exception {
        ResponsePositionAndMessage responsePositionAndMessage = new ResponsePositionAndMessage();
        try {
            ArrayList<LinkedHashMap> satellites = (ArrayList<LinkedHashMap>) body.get("satellites");
            responsePositionAndMessage = findShipAndMessage.topSecret(satellites);
            if (responsePositionAndMessage.getMessage() == null || responsePositionAndMessage.getPosition() == null) {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "OPS! we were unable to decode the message or find the position");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return responsePositionAndMessage;
    }

    @RequestMapping(value = "/topsecret_split/{satellite_name}", produces = "application/json", consumes = "application/json", method = {RequestMethod.POST})
    public String postTopSecretSplit(@PathVariable String satellite_name, @RequestBody Map body) throws Exception {
        try {
            Float distance = ((Double) body.get("distance")).floatValue();
            String[] message = FindShipAndMessageUtil.formatMessageToProcess((ArrayList<String>) body.get("message"));
            findShipAndMessage.postTopSecretSplit(new SatelliteMessages(satellite_name, distance, message));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return MESSAGE_RECEIVED_SUCCESSFULLY_USE_GET_SERVICE_TO_DETERMINE_MESSAGE_AND_POSITION;
    }

    @RequestMapping(value = "/topsecret_split/{satellite_name}", produces = "application/json", consumes = "application/json", method = RequestMethod.GET)
    public ResponsePositionAndMessage getTopSecretSplit(@PathVariable String satellite_name, @RequestBody Map body, HttpServletResponse httpServletResponse) throws Exception {
        ResponsePositionAndMessage responsePositionAndMessage = new ResponsePositionAndMessage();
        try {
            SatelliteMessages satelliteMessage = new SatelliteMessages();
            satelliteMessage.setName(satellite_name);
            satelliteMessage.setDistance(((Double) body.get("distance")).floatValue());
            satelliteMessage.setMessage(FindShipAndMessageUtil.formatMessageToProcess((ArrayList<String>) body.get("message")));
            responsePositionAndMessage = findShipAndMessage.getTopSecretSplit(satelliteMessage);
            if (responsePositionAndMessage.getMessage() == null || responsePositionAndMessage.getPosition() == null) {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Not enough information received to get position and message");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return responsePositionAndMessage;
    }
}
