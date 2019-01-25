/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.xml;

/**
 *
 * @author Joseph
 */
public class BitField {
    private String name;
    private byte cmd;
    private int size;
    private String[] bits;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getBit(int i) {
        return bits[i];
    }

    public void setBit(int i, String bit) {
        this.bits[i] = bit;
    }

    public void setBits(String[] bits) {
        this.bits = bits;
    }

    @Override
    public String toString() {
        String str = "BitField:: Cmd=" + Integer.toHexString(this.cmd) + " Name=" + this.name + " Size=" + this.size + "\n";
        int i = 0;
        for (String bit : bits) {
            str += "bit["+(bits.length-1-i++)+"]=" + bit + "\n";
        }
        return str;
    }
    
}
