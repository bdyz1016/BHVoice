package com.bhsc.mobile.dataclass;

/**
 * Created by lynn on 10/20/15.
 */
public class Data_DB_NewsType extends BaseClass {
    private String TypeName;
    private int TypeId;
    public Data_DB_NewsType(){
        super();
        this.TypeName = "";
        this.TypeId = 0;
    }

    private String id = "";

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
