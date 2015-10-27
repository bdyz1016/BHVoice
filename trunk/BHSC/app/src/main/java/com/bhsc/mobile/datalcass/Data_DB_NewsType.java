package com.bhsc.mobile.datalcass;

/**
 * Created by lynn on 10/20/15.
 */
public class Data_DB_NewsType extends BaseClass {
    private String TypeName;
    private int TypeId;
    public Data_DB_NewsType(){
        this.TypeName = "";
        this.TypeId = 0;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }
}
