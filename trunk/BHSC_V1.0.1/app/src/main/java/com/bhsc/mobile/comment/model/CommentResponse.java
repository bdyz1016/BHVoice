package com.bhsc.mobile.comment.model;

import com.bhsc.mobile.net.Response;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhanglei on 16/5/2.
 */
public class CommentResponse extends Response{
    private List<Data_DB_Comment> list;
    public CommentResponse(){
        list = new LinkedList<>();
    }

    public List<Data_DB_Comment> getList() {
        return list;
    }

    public void setList(List<Data_DB_Comment> list) {
        this.list = list;
    }
}
