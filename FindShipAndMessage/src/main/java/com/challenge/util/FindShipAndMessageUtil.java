package com.challenge.util;

import com.challenge.dto.Coordinates;
import com.challenge.dto.ResponsePositionAndMessage;
import com.challenge.model.SatelliteMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class FindShipAndMessageUtil {

    private final static Double ERROR_MARGIN = 0.1;

    public static Coordinates getIntersectionPoint(Float distanceKenobi, Float distanceSkywalker, Float distanceSato) {

        //Getting satelites Positions
        int kenobiX = AvailableSatellites.KENOBI.positionX;
        int kenobiY = AvailableSatellites.KENOBI.positionY;
        int skywalkerX = AvailableSatellites.SKYWALKER.positionX;
        int skywalkerY = AvailableSatellites.SKYWALKER.positionY;
        int satoX = AvailableSatellites.SATO.positionX;
        int satoY = AvailableSatellites.SATO.positionY;
        Coordinates intersectionCoordinates = new Coordinates();

        Double distanceBetweenTwoCircles = Math.sqrt(Math.pow((skywalkerX - kenobiX), 2) + Math.pow((skywalkerY - kenobiY), 2));

        if (distanceBetweenTwoCircles > distanceKenobi + distanceSkywalker) {
            return null;
        }
        //One circle within other
        if (distanceBetweenTwoCircles < Math.abs(distanceKenobi - distanceSkywalker)) {
            return null;
        }
        //coincident circles
        if (distanceBetweenTwoCircles == 0 && distanceKenobi == distanceSkywalker) {
            return null;
        }

        Double variation = 0.25 * (Math.sqrt((distanceBetweenTwoCircles + distanceKenobi + distanceSkywalker) *
                (distanceBetweenTwoCircles + distanceKenobi - distanceSkywalker) *
                (distanceBetweenTwoCircles - distanceKenobi + distanceSkywalker) *
                (-distanceBetweenTwoCircles + distanceKenobi + distanceSkywalker)
        )
        );

        Double commonPartEquation = Math.pow(distanceKenobi, 2) - Math.pow(distanceSkywalker, 2);

        Double equationXPart1 = (kenobiX + skywalkerX) / 2d;
        double equationXPart2 = ((skywalkerX - kenobiX) * commonPartEquation) / (2 * Math.pow(distanceBetweenTwoCircles, 2));
        double equationXPart3 = (2 * (kenobiY - skywalkerY) / Math.pow(distanceBetweenTwoCircles, 2)) * variation;

        Double intersectionPointX1 = (equationXPart1 +
                equationXPart2 +
                equationXPart3
        );

        Double intersectionPointX2 = (equationXPart1 +
                equationXPart2 -
                equationXPart3
        );

        Double equationYPart1 = (kenobiY + skywalkerY) / 2d;
        double equationYPart2 = ((skywalkerY - kenobiY) * commonPartEquation) / (2 * Math.pow(distanceBetweenTwoCircles, 2));
        double equationYPart3 = (2 * (kenobiX - skywalkerX) / (Math.pow(distanceBetweenTwoCircles, 2))) * variation;

        Double intersectionPointY1 = (equationYPart1 +
                equationYPart2 +
                equationYPart3
        );

        Double intersectionPointY2 = (equationYPart1 +
                equationYPart2 -
                equationYPart3
        );

        /* Lets determine if circle 3 intersects at either of the above intersection points. */
        Double dx = intersectionPointX1 - satoX;
        Double dy = intersectionPointY1 - satoY;
        Double d1 = Math.sqrt((dy * dy) + (dx * dx));

        dx = intersectionPointX2 - satoX;
        dy = intersectionPointY2 - satoY;
        Double d2 = Math.sqrt((dy * dy) + (dx * dx));

        if (Math.abs(d1 - distanceSato) < ERROR_MARGIN) {
            intersectionCoordinates.setX(intersectionPointX1);
            intersectionCoordinates.setY(intersectionPointY1);
        } else if (Math.abs(d2 - distanceSato) < ERROR_MARGIN) {
            intersectionCoordinates.setX(intersectionPointX2);
            intersectionCoordinates.setY(intersectionPointY2);
        } else {
            return null;
        }

        return intersectionCoordinates;
    }

    public static String getMessage(String[]... messages) {
        Set messageDecoded = new LinkedHashSet();
        if (messages != null && messages.length > 0) {
            int position = 0;
            boolean decodeFinished = false;
            //move vertically
            while (!decodeFinished) {
                int countMessageFinished = 0;
                //move horizontally
                for (String[] message : messages) {
                    try {
                        if (position <= message.length && message[position].trim().length() != 0) {
                            messageDecoded.add(message[position]);
                        } else {
                            countMessageFinished++;
                            //validate that I have finished all the words in all messages
                            if (countMessageFinished == messages.length) {
                                decodeFinished = true;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.getStackTrace();
                    }
                }
                position++;
            }
        }
        return messageDecoded.toString();
    }

    public static ResponsePositionAndMessage getResponseTopSecret(ArrayList<LinkedHashMap> satellites) {
        ResponsePositionAndMessage responsePositionAndMessage = new ResponsePositionAndMessage();

        Coordinates intersectionPoint = null;
        String message = null;

        if (satellites.size() == 3) {

            Float distanceKenobi = 0f;
            String[] messageKenobi = new String[]{};

            Float distanceSkywalker = 0f;
            String[] messageSkywalker = new String[]{};

            Float distanceSato = 0f;
            String[] messageSato = new String[]{};

            for (LinkedHashMap satellite : satellites) {
                if (satellite.get("name").toString().equalsIgnoreCase(AvailableSatellites.KENOBI.name)) {
                    distanceKenobi = ((Double) satellite.get("distance")).floatValue();
                    messageKenobi = formatMessageToProcess((ArrayList<String>) satellite.get("message"));

                } else if (satellite.get("name").toString().equalsIgnoreCase(AvailableSatellites.SKYWALKER.name)) {
                    distanceSkywalker = ((Double) satellite.get("distance")).floatValue();
                    messageSkywalker = formatMessageToProcess((ArrayList<String>) satellite.get("message"));

                } else if (satellite.get("name").toString().equalsIgnoreCase(AvailableSatellites.SATO.name)) {
                    distanceSato = ((Double) satellite.get("distance")).floatValue();
                    messageSato = formatMessageToProcess((ArrayList<String>) satellite.get("message"));

                }
            }

            intersectionPoint = getIntersectionPoint(distanceKenobi, distanceSkywalker, distanceSato);
            message = getMessage(messageKenobi, messageSkywalker, messageSato);
        }


        responsePositionAndMessage.setPosition(intersectionPoint);
        responsePositionAndMessage.setMessage(message);

        return responsePositionAndMessage;

    }

    public static String[] formatMessageToProcess(ArrayList<String> message) {
        String[] messageToProcess = new String[message.size()];
        return message.toArray(messageToProcess);
    }

    public static ResponsePositionAndMessage getResponseTopSecretSplit(ArrayList<SatelliteMessages> satelliteMessages) {
        ResponsePositionAndMessage responsePositionAndMessage = new ResponsePositionAndMessage();

        Coordinates intersectionPoint = null;
        String message = null;

        if (satelliteMessages.size() == 3) {

            Float distanceKenobi = 0f;
            String[] messageKenobi = new String[]{};

            Float distanceSkywalker = 0f;
            String[] messageSkywalker = new String[]{};

            Float distanceSato = 0f;
            String[] messageSato = new String[]{};

            for (SatelliteMessages satellite : satelliteMessages) {
                if (satellite.getName().equalsIgnoreCase(AvailableSatellites.KENOBI.name)) {
                    distanceKenobi = satellite.getDistance();
                    messageKenobi = satellite.getMessage();

                } else if (satellite.getName().equalsIgnoreCase(AvailableSatellites.SKYWALKER.name)) {
                    distanceSkywalker = satellite.getDistance();
                    messageSkywalker = satellite.getMessage();

                } else if (satellite.getName().equalsIgnoreCase(AvailableSatellites.SATO.name)) {
                    distanceSato = satellite.getDistance();
                    messageSato = satellite.getMessage();

                }
            }

            intersectionPoint = getIntersectionPoint(distanceKenobi, distanceSkywalker, distanceSato);
            message = getMessage(messageKenobi, messageSkywalker, messageSato);
        }
        responsePositionAndMessage.setPosition(intersectionPoint);
        responsePositionAndMessage.setMessage(message);

        return responsePositionAndMessage;
    }

    public static boolean validateSatelliteName(String satelliteName) throws Exception {
        String[] validSatelliteNames = AvailableSatellites.getNames(AvailableSatellites.class);
        if (Arrays.stream(validSatelliteNames).noneMatch(validName -> validName.equalsIgnoreCase(satelliteName))) {
            throw new Exception("Invalid Satellite Name");
        } else {
            return true;
        }
    }

}
