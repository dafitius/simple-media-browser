package com.dafitius.simplemoviebrowser.Models;

import java.io.Serializable;

public class Rating implements Serializable {
    private String Source;
    private String Value;


    public String getSource() {
        return Source;
    }

    public String getValue() {
        return Value;
    }

    @Override
    public String toString() {
        return Value + '\n' + " " + Source + " ";
    }
}
