package nl.gremmee.mazegenerator;

import java.util.Random;

public class Utils {
    private static Random random = new Random();

    public static float getRandomFloat(float aMin, float aMax) {
        return (random.nextFloat() * (aMax - aMin)) + aMin;
    }

    public static final float map(float aMappedValue, float aInboundMin, float aInboundMax, float aOutboundMin,
            float aOutboundMax) {
        return aOutboundMin
                + ((aOutboundMax - aOutboundMin) * ((aMappedValue - aInboundMin) / (aInboundMax - aInboundMin)));
    }
}
