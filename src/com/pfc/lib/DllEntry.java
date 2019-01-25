/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
            System.out.println(ule);
        }
    }
}
