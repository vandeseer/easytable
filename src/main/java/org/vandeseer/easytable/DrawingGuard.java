package org.vandeseer.easytable;

class DrawingGuard {

    private static final int FAILED_MAX = 2;
    private int drawingFailedSinceSuccess = 0;

    void noteRowFinishedSuccessfully() {
        drawingFailedSinceSuccess = 0;
    }

    void noteRowNotFinishedSuccessfully() {
        drawingFailedSinceSuccess++;
    }

    boolean isNoticingDrawingIssue() {
        return drawingFailedSinceSuccess > FAILED_MAX;
    }

}
