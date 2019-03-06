/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.lib;

/**
 *
 * @author joseph
 */
public class DllEntry {

    // dec_dll
    public static native boolean cod128(String fileName);
    public static native int dec128(String fileName, byte[] outBuf);
    public static native int dec64(String fileName, byte[] outBuf);
    
    static {
        try {
            System.loadLibrary("dec_dll");
            System.out.println("Load DEC DLL");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println(ule);
        }
    }
}
