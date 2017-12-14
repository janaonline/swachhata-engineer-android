package com.ichangemycity.model;


public class NotificationHeaderData {
    
    private String headerTitle,dateValue;
    private int TYPE_ITEM ;
    private int notificationId, contentId;
    String feedCreatedOn, contentCreatedOn, feedType, redirectTo, textMsg;
    boolean isRead;
    int imageIcon, bgColor;

    /**
     * @return the bgColor
     */
    public int getBgColor() {
        return bgColor;
    }

    /**
     * @param bgColor the bgColor to set
     */
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * @return the imageIcon
     */
    public int getImageIcon() {
        return imageIcon;
    }

    /**
     * @param imageIcon the imageIcon to set
     */
    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }

    /**
     * @return the isRead
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * @param isRead
     *            the isRead to set
     */
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * @return the notificationId
     */
    public int getNotificationId() {
        return notificationId;
    }

    /**
     * @param notificationId
     *            the notificationId to set
     */
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * @return the contentId
     */
    public int getContentId() {
        return contentId;
    }

    /**
     * @param contentId
     *            the contentId to set
     */
    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    /**
     * @return the feedCreatedOn
     */
    public String getFeedCreatedOn() {
        return feedCreatedOn;
    }

    /**
     * @param feedCreatedOn
     *            the feedCreatedOn to set
     */
    public void setFeedCreatedOn(String feedCreatedOn) {
        this.feedCreatedOn = feedCreatedOn;
    }

    /**
     * @return the contentCreatedOn
     */
    public String getContentCreatedOn() {
        return contentCreatedOn;
    }

    /**
     * @param contentCreatedOn
     *            the contentCreatedOn to set
     */
    public void setContentCreatedOn(String contentCreatedOn) {
        this.contentCreatedOn = contentCreatedOn;
    }

    /**
     * @return the feedType
     */
    public String getFeedType() {
        return feedType;
    }

    /**
     * @param feedType
     *            the feedType to set
     */
    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    /**
     * @return the redirectTo
     */
    public String getRedirectTo() {
        return redirectTo;
    }

    /**
     * @param redirectTo
     *            the redirectTo to set
     */
    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    /**
     * @return the textMsg
     */
    public String getTextMsg() {
        return textMsg;
    }

    /**
     * @param textMsg
     *            the textMsg to set
     */
    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    
    
    /**
     * @return the tYPE_ITEM
     */
    public int getTYPE_ITEM() {
        return TYPE_ITEM;
    }
    /**
     * @param tYPE_ITEM the tYPE_ITEM to set
     */
    public void setTYPE_ITEM(int tYPE_ITEM) {
        TYPE_ITEM = tYPE_ITEM;
    }
    /**
     * @return the headerTitle
     */
    public String getHeaderTitle() {
        return headerTitle;
    }
    /**
     * @param headerTitle the headerTitle to set
     */
    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }
    /**
     * @return the dateValue
     */
    public String getDateValue() {
        return dateValue;
    }
    /**
     * @param dateValue the dateValue to set
     */
    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }
    /**
     * @return the notificationData
     */
   

}
