/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.lib;

/**
 *
 * @author Joseph
 */
public class ReadWriteDF {
    
    public static native boolean readDataFlash(int nDev, int loc, byte[] pBuf, boolean bPEC);
    public static native boolean writeDataFlash(int nDev, int loc, byte[] pBuf, boolean bPEC);
    
    static {
        try {
            System.loadLibrary("ReadWriteDF");
            System.out.println("Load ReadWriteDF DLL");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println(ule);
        }
    }
}
