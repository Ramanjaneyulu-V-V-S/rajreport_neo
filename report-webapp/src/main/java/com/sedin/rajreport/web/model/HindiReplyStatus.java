package com.sedin.rajreport.web.model;

/**
 * Section 1 of the department report: "Status of reply in Hindi to the letters
 * received in Hindi", mirroring rajreport.java's generateReport().
 */
public record HindiReplyStatus(int receivedHindi, int repliedHindi, int repliedEnglish) {

    public int notRequiredToReply() {
        return receivedHindi - repliedHindi - repliedEnglish;
    }
}
