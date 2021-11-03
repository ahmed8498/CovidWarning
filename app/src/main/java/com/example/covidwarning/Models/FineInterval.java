package com.example.covidwarning.Models;

public class FineInterval {

    private String startOfInterval;
    private String endOfInterval;

    public FineInterval(String startOfInterval, String endOfInterval) {
        this.startOfInterval = startOfInterval;
        this.endOfInterval = endOfInterval;
    }

    public String getStartOfInterval() {
        return startOfInterval;
    }

    public void setStartOfInterval(String startOfInterval) {
        this.startOfInterval = startOfInterval;
    }

    public String getEndOfInterval() {
        return endOfInterval;
    }

    public void setEndOfInterval(String endOfInterval) {
        this.endOfInterval = endOfInterval;
    }
}
