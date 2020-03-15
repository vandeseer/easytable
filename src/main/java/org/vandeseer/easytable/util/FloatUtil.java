package org.vandeseer.easytable.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FloatUtil {

    /**
     * The delta that is still acceptable in float comparisons.
     */
    public static final double EPSILON = 0.0001;


    /**
     * Floats can't hold any value (see https://www.geeksforgeeks.org/rounding-off-errors-java/).
     * So this method is a workaround to compare to floats - all values where the difference is lower than {@value EPSILON} are equal.
     *
     * @param x left value
     * @param y right value
     * @return true, if difference between x and y lower than {@value EPSILON}.
     */
    public static boolean isEqualInEpsilon(float x, float y) {
        float difference = Math.abs(y - x);
        return difference < EPSILON;
    }

}
