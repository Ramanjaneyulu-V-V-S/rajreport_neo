package com.sedin.rajreport.web.model;

/**
 * Outward-letter language breakdown for one region tier (A, B or C) of a department,
 * mirroring the console output of {@code Aregion}/{@code Bregion}/{@code Cregion} in rajreport.java.
 */
public record RegionTierResult(String tier, int hindiOrBilingual, int english) {

    public int total() {
        return hindiOrBilingual + english;
    }

    public double hindiPercentage() {
        return total() == 0 ? 0.0 : (double) hindiOrBilingual / total() * 100;
    }
}
