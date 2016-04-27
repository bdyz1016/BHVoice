package com.bhsc.mobile.userpages.dialog;

/**
 * Created by lynn on 10/15/15.
 */
public class DialogControler {

    private String message = "";
    private boolean positiveButton = false;
    private boolean negativeButton = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPositiveButton() {
        return positiveButton;
    }

    public void setPositiveButton(boolean positiveButton) {
        this.positiveButton = positiveButton;
    }

    public boolean isNegativeButton() {
        return negativeButton;
    }

    public void setNegativeButton(boolean negativeButton) {
        this.negativeButton = negativeButton;
    }
}
