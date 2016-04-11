//package com.bhsc.disclose.event;
//
//import com.bhsc.mobile.dataclass.Data_DB_Disclose;
//
//import java.util.List;
//
///**
// * Created by lynn on 10/23/15.
// */
//public class ActionEvent {
//    public static final int ACTION_ADD_DISCLOSE_FINISH = 0x1;
//    public static final int ACTION_LOAD_DISCLOSE = 0x2;
//    public static final int ACTION_DISCLOSE_REFRESH = 0x3;
//    public static final int ACTION_DISCLOSE_DELETE_SUCCESS = 0x4;
//    public static final int ACTION_DISCLOSE_DELETE_FAILED = 0x5;
//    public static final int ACTION_PRAISE_SUCCESS = 0x6;
//    public static final int ACTION_PRAISE_FAILED = 0x7;
//
//    private int Action;
//    private List<Data_DB_Disclose> DiscloseList;
//
//    private Object mExtra;
//
//    public ActionEvent(int action){
//        this.Action = action;
//    }
//
//    public int getAction() {
//        return Action;
//    }
//
//    public void setAction(int action) {
//        Action = action;
//    }
//
//    public List<Data_DB_Disclose> getDiscloseList() {
//        return DiscloseList;
//    }
//
//    public void setDiscloseList(List<Data_DB_Disclose> discloseList) {
//        DiscloseList = discloseList;
//    }
//
//    public void setExtra(Object obj){
//        this.mExtra = obj;
//    }
//
//    public Object getExtra(){
//        return mExtra;
//    }
//}
