package com.getpoint.farminfomanager.entity.points;

/**
 * Created by Station on 2016/8/3.
 */
public enum PointItemType {

    FRAMEPOINT("") {

    },

    BYPASSPOINT("") {

    },

    CLIMBPOINT("") {

    },

    FORWAEDPOINT("") {

    };

    private String label;

    private PointItemType(String label) {
        this.label = label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}