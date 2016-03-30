package com.bhsc.net;

/**
 * Created by lynn on 3/10/16.
 */
public class ResponseObject<T> {
    private int result;
    private long ts;
    private int resultMessage;
    private T returnValue;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public int getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(int resultMessage) {
        this.resultMessage = resultMessage;
    }

    public T getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(T returnValue) {
        this.returnValue = returnValue;
    }
}
