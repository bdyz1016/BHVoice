package com.bhsc.mobile.comment.model;

import com.bhsc.mobile.net.Response;

/**
 * Created by zhanglei on 16/5/9.
 */
public class AddCommentResponse extends Response {
    private Data_DB_Comment comment;

    public Data_DB_Comment getComment() {
        return comment;
    }

    public void setComment(Data_DB_Comment comment) {
        this.comment = comment;
    }
}
