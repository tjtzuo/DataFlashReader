/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.xml;

/**
 *
 * @author joseph
 */
public class SBS {
    private String name;
    private byte cmd;
    private int size;
    private String unit;
    private String format;
    
    public byte getCmd() {
        return cmd;
    }
    public void setCmd(byte id) {
        this.cmd = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    
    @Override
    public String toString() {
        return "SBS:: Cmd=" + Integer.toHexString(this.cmd) + " Name=" + this.name + " Size=" + this.size +
                " Unit=" + this.unit + " Format=" + this.format;
    }
    
}
