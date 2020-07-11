package org.vandeseer.easytable.settings;

public enum BorderStyle implements BorderStyleInterface {

    SOLID(new float[]{}, 0),
    DOTTED(new float[]{1}, 1),
    DASHED(new float[]{5,2}, 1);

    private final float[] pattern;
    private final int phase;

    BorderStyle(float[] pattern, int phase) {
        this.pattern = pattern;
        this.phase = phase;
    }

    public float[] getPattern() {
        return pattern;
    }

    public int getPhase() {
        return phase;
    }

}
