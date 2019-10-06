package org.vandeseer.easytable;

class DrawingGuard {

    private static final int FAILED_MAX = 2;
    private int numberOfDrawingFailsSinceSuccess = 0;

    public DrawingGuard() {
    }

    void noteRowFinishedSuccessfully() {
        numberOfDrawingFailsSinceSuccess = 0;
    }

    void noteRowNotFinishedSuccessfully() {
        numberOfDrawingFailsSinceSuccess++;
    }

    boolean isNoticingDrawingIssue() {
        return numberOfDrawingFailsSinceSuccess > FAILED_MAX;
    }

}
