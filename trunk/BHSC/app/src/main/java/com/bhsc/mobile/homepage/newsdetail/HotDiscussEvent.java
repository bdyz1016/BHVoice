package com.bhsc.mobile.homepage.newsdetail;

import com.bhsc.mobile.dataclass.Data_DB_Discuss;

import java.util.List;

/**
 * Created by lynn on 15-10-9.
 */
public class HotDiscussEvent {
    private List<Data_DB_Discuss> discusses;

    public List<Data_DB_Discuss> getDiscusses() {
        return discusses;
    }

    public void setDiscusses(List<Data_DB_Discuss> discusses) {
        this.discusses = discusses;
    }
}
