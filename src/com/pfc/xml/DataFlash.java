/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.xml;

/**
 *
 * @author joseph
 */
public class DataFlash {
    private int stradr;
    private String name;
    private String type;
    private String unit;
    private String text;
    private int value;

    public int getStartAdr() {
        return stradr;
    }
    public void setStartAdr(int id) {
        this.stradr = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DataFlash:: Name=" + name + " StrAdr=" + Integer.toHexString(stradr) +
                " Type=" + type + " Unit=" + unit + " Value=" + value + " Text=" + text;
    }

}
