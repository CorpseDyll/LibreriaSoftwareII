package com.libreria.dto;

public class ReviewDto {
    private String reviewerName;
    private String reviewTitle;
    private String comment;
    private int score;

    public ReviewDto() {
    }

    public ReviewDto(String reviewerName, String reviewTitle, String comment, int score) {
        this.reviewerName = reviewerName;
        this.reviewTitle = reviewTitle;
        this.comment = comment;
        this.score = score;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
