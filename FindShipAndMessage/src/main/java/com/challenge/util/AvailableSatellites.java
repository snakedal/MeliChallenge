package com.challenge.util;

import java.util.Arrays;

public enum AvailableSatellites {
    KENOBI("kenobi", -500, -200),
    SKYWALKER("skywalker", 100, -100),
    SATO("sato", 500, 100);

    public final String name;
    public final Integer positionX;
    public final Integer positionY;

    private AvailableSatellites(String name, int positionX, int positionY) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}
