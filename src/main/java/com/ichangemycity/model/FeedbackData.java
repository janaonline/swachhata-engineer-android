package com.ichangemycity.model;

import java.util.ArrayList;

public class FeedbackData {
    private int id;
    private String title;
    private ArrayList<FeedbackData> options;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the options
     */
    public ArrayList<FeedbackData> getOptions() {
        return options;
    }

    /**
     * @param options
     *            the options to set
     */
    public void setOptions(ArrayList<FeedbackData> options) {
        this.options = options;
    }
}
