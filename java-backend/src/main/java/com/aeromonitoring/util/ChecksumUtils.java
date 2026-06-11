package com.aeromonitoring.util;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class ChecksumUtils {

    private ChecksumUtils() {
    }

    public static int calculate(String payload) {
        int checksum = 0;
        for (byte value : payload.getBytes(StandardCharsets.UTF_8)) {
            checksum = (checksum + (value & 0xFF)) & 0xFF;
        }
        return checksum;
    }

    public static String canonicalPayload(String timestamp,
                                          double temperature,
                                          double pressure,
                                          double vibration,
                                          double oxygenLevel,
                                          int heartRate,
                                          double batteryLevel) {
        return String.format(Locale.US, "%s|%.2f|%.2f|%.2f|%.2f|%d|%.2f",
            timestamp,
            temperature,
            pressure,
            vibration,
            oxygenLevel,
            heartRate,
            batteryLevel);
    }
}