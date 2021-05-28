package com.groceryapp.upcdata.DB.Friend;

import com.groceryapp.upcdata.DB.User.User;

import java.io.Serializable;

public class Comment implements Serializable {
    private String CommentText;
    private User user;

    public Comment(){}
    public Comment(String commentText, User user) {
        CommentText = commentText;
        this.user = user;
    }

    public void setCommentText(String commentText) {
        this.CommentText = commentText;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentText() {
        return CommentText;
    }

    public User getUser() {
        return user;
    }



}
