package com.challenge;

import com.challenge.controller.FindShipAndMessageController;
import com.challenge.dto.Coordinates;
import com.challenge.dto.ResponsePositionAndMessage;
import com.challenge.model.SatelliteMessages;
import com.challenge.service.FindShipAndMessageImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FindShipAndMessageControllerTest {

    private static final String MESSAGE_RECEIVED_SUCCESSFULLY_USE_GET_SERVICE_TO_DETERMINE_MESSAGE_AND_POSITION = "Message received successfully, use Get Service to determine message and position";
    private static final String satelliteName = "kenobi";
    @InjectMocks
    private FindShipAndMessageController findShipAndMessageController;
    @Mock
    private FindShipAndMessageImpl findShipAndMessageImpl;

    Map<String, ArrayList<LinkedHashMap>> messageFull = new HashMap();
    Map<String, ArrayList<LinkedHashMap>> message = new HashMap();
    ArrayList<LinkedHashMap> satellites = new ArrayList<>();
    ResponsePositionAndMessage responsePositionAndMessage = new ResponsePositionAndMessage();
    ResponsePositionAndMessage responsePositionAndMessageError = new ResponsePositionAndMessage();
    LinkedHashMap messageOneSatellite = new LinkedHashMap();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);


        //---------------------------
        messageFull.put("satellites", satellites);
        //---------------------------
        String[] message = {"este", "", "un", "", ""};
        ArrayList array = new ArrayList<String>();
        Collections.addAll(array, message);
        messageOneSatellite.put("distance", 899.4d);
        messageOneSatellite.put("message", array);

        //----------------------------
        Coordinates coordinates = new Coordinates();
        coordinates.setX(1d);
        coordinates.setY(2d);
        responsePositionAndMessage.setMessage("test");
        responsePositionAndMessage.setPosition(coordinates);

    }

    @Test
    public void topSecretTest() throws Exception {
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        when(findShipAndMessageImpl.topSecret(satellites)).thenReturn(responsePositionAndMessage);
        ResponsePositionAndMessage response = findShipAndMessageController.topSecret(messageFull, httpServletResponse);

        assertEquals(responsePositionAndMessage.getMessage(), response.getMessage());
        assertEquals(responsePositionAndMessage.getPosition(), response.getPosition());

    }

    @Test
    public void postTopSecretSplitTest() throws Exception {
        when(findShipAndMessageImpl.topSecret(satellites)).thenReturn(responsePositionAndMessage);
        String response = findShipAndMessageController.postTopSecretSplit(satelliteName, messageOneSatellite);

        assertEquals(MESSAGE_RECEIVED_SUCCESSFULLY_USE_GET_SERVICE_TO_DETERMINE_MESSAGE_AND_POSITION, response);

    }

    @Test
    public void getTopSecretSplitTest() throws Exception {

        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        when(findShipAndMessageImpl.getTopSecretSplit(any(SatelliteMessages.class))).thenReturn(responsePositionAndMessage);
        ResponsePositionAndMessage response = findShipAndMessageController.getTopSecretSplit(satelliteName, messageOneSatellite, httpServletResponse);

        assertEquals(responsePositionAndMessage.getMessage(), response.getMessage());
        assertEquals(responsePositionAndMessage.getPosition(), response.getPosition());

    }

    @Test(expected = Exception.class)
    public void getTopSecretSplitExceptionErrorTest() throws Exception {
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        ResponsePositionAndMessage response = findShipAndMessageController.getTopSecretSplit(satelliteName, messageOneSatellite, httpServletResponse);

        assertEquals(responsePositionAndMessage.getMessage(), response.getMessage());
        assertEquals(responsePositionAndMessage.getPosition(), response.getPosition());

    }

    @Test
    public void getTopSecretSplitNotFoundTest() throws Exception {
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        when(findShipAndMessageImpl.getTopSecretSplit(any(SatelliteMessages.class))).thenReturn(responsePositionAndMessageError);
        try {
            ResponsePositionAndMessage response = findShipAndMessageController.getTopSecretSplit(satelliteName, messageOneSatellite, httpServletResponse);
        } catch (Exception e) {
            assertEquals("Not enough information received to get position and message", e.getMessage());
        }
    }
}
