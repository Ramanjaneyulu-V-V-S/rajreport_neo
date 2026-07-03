package com.sedin.rajreport.web.model;

/**
 * Section 2 of the department report: "Status of letters received in English & replied
 * to in Hindi", scoped to A & B regions only, mirroring rajreport.java's 5-arg
 * Aregion()/Bregion().
 */
public record EnglishReplyStatus(String region, int receivedEnglish, int repliedHindi, int repliedEnglish) {

    public int notRequiredToReply() {
        return receivedEnglish - repliedHindi - repliedEnglish;
    }
}
