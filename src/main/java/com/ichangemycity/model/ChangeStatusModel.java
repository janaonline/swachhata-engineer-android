package com.ichangemycity.model;

public class ChangeStatusModel {
    String statusName;
    int statusID;
    int color;
    int currentStatusColor;

    /**
     * @return the currentStatusColor
     */
    public int getCurrentStatusColor() {
        return currentStatusColor;
    }

    /**
     * @param currentStatusColor
     *            the currentStatusColor to set
     */
    public void setCurrentStatusColor(int currentStatusColor) {
        this.currentStatusColor = currentStatusColor;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName
     *            the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the statusID
     */
    public int getStatusID() {
        return statusID;
    }

    /**
     * @param statusID
     *            the statusID to set
     */
    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    /**
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(int color) {
        this.color = color;
    }
}
