package com.navis.dashboard;

class Container {

    private String unitNbr;
    private String unitPosition;
    private long estMvTime;
    private int tier;

    private String kind;

    public Container(String _unitNbr, String _unitPosition, long _estMvTime, String _kind) {
        unitNbr = _unitNbr;
        unitPosition = _unitPosition;
        estMvTime = _estMvTime;
        tier = _unitPosition.charAt(_unitPosition.length() - 1) - 65;
        kind = _kind;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getUnitNbr() {
        return unitNbr;
    }

    public void setUnitNbr(String unitNbr) {
        this.unitNbr = unitNbr;
    }

    public String getUnitPosition() {
        return unitPosition;
    }

    public void setUnitPosition(String unitPosition) {
        this.unitPosition = unitPosition;
    }

    public long getEstMvTime() {
        return estMvTime;
    }

    public void setEstMvTime(long estMvTime) {
        this.estMvTime = estMvTime;
    }

    public String getUnitNumber() {
        return unitNbr;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNbr = unitNumber;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }
}
