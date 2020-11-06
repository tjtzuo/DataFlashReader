/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.lib;

import java.util.Arrays;


/**
 *
 * @author joseph
 */
public class UsbSmb {
    private int devIndex = -1;
    private byte addr = 0x16;
    private boolean bPEC = false;
    private boolean bHDQ = false;

    public byte getAddr() {
        return addr;
    }

    public void setAddr(byte addr) {
        this.addr = addr;
    }

    public boolean isPEC() {
        return bPEC;
    }

    public void setPEC(boolean bPEC) {
        this.bPEC = bPEC;
    }

    public boolean isHDQ() {
        return bHDQ;
    }

    public void setHDQ(boolean bHDQ) {
        this.bHDQ = bHDQ;
    }
    
    static {
        try {
            System.loadLibrary("UsbSmb");
            System.out.println("Information about USB SMBus");
            System.out.println("===========================");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println(ule);
        }
    }
    public UsbSmb() {
        int count = usbInit();
        devIndex = count - 1;
        System.out.println("USB SMBus Count: " + count);
    }
    
    public short getVersion() {
        return usbGetVersion(devIndex);
    }
    public synchronized boolean readByte(int cmd, byte[] pValue) {
        if (bHDQ && cmd < 0x80)
            return usbHdqRead(devIndex, (byte)cmd, pValue);
        else
            return usbSmbRead(devIndex, addr, (byte)cmd, false, Byte.BYTES, pValue);
    }
    public synchronized boolean readBytes(int cmd, int nCount, byte[] pBuf) {
        if (bHDQ && cmd < 0x80) {
            for (int i = 0; i < nCount; i++) {
                byte[] pVal = new byte[1];
                if (!usbHdqRead(devIndex, (byte) (cmd + i), pVal))
                    return false;
                pBuf[i] = pVal[0];
            }
            return true;
        } else
            return usbSmbRead(devIndex, addr, (byte)cmd, false, nCount, pBuf);
    }
    public synchronized boolean readBlock(int cmd, int nCount, byte[] pBuf) {
        byte[] pBlock = new byte[nCount + 1];
        boolean result = usbSmbRead(devIndex, addr, (byte)cmd, bPEC, pBlock.length, pBlock);
        System.arraycopy(pBlock, 1, pBuf, 0, nCount);
        return result && (pBlock[0] <= nCount);
    }
    public synchronized boolean readWord(int cmd, short[] pwValue) {
        boolean result = false;
        byte[] pBuf = new byte[2];
        if (bHDQ && cmd < 0x80) {
            if (usbHdqRead(devIndex, (byte)(cmd + 1), pBuf)) {
                pBuf[1] = pBuf[0];
                if (usbHdqRead(devIndex, (byte)cmd, pBuf))
                    result = true;
            }
        } else {
            result = usbSmbRead(devIndex, addr, (byte)cmd, bPEC, Short.BYTES, pBuf);
        }
        pwValue[0] = (short) (Byte.toUnsignedInt(pBuf[0]) | (pBuf[1] << 8));
        return result;
    }
    public synchronized boolean sendByte(int cmd) {
        byte[] pBuf = new byte[1];
	return usbSmbWrite(devIndex, addr, (byte)cmd, false, 0, pBuf);
    }
    public synchronized boolean writeByte(int cmd, int val) {
        if (bHDQ && cmd < 0x80) {
            return usbHdqWrite(devIndex, (byte) cmd, (byte) val);
        } else {
            byte[] pBuf = new byte[1];
            pBuf[0] = (byte) val;
            return usbSmbWrite(devIndex, addr, (byte) cmd, false, Byte.BYTES, pBuf);
        }
    }
    public synchronized boolean writeBytes(int cmd, int nCount, byte[] pBuf) {
       if (bHDQ && cmd < 0x80) {
            for (int i = 0; i < nCount; i++)
                if (!usbHdqWrite(devIndex, (byte) (cmd + i), pBuf[i]))
                    return false;
            return true;
        } else
            return usbSmbWrite(devIndex, addr, (byte)cmd, false, nCount, pBuf);
    }
    public synchronized boolean writeBlock(int cmd, int nCount, byte[] pBuf) {
        byte[] pBlock = new byte[nCount + 1];
        System.arraycopy(pBuf, 0, pBlock, 1, nCount);
        pBlock[0] = (byte) nCount;
        return usbSmbWrite(devIndex, addr, (byte)cmd, bPEC, pBlock.length, pBlock);
    }
    public synchronized boolean writeWord(int cmd, int val) {
        if (bHDQ && cmd < 0x80) {
            if (usbHdqWrite(devIndex, (byte)cmd, (byte)-1))
                if (usbHdqWrite(devIndex, (byte)(cmd + 1), (byte)(val >> 8)))
                    if (usbHdqWrite(devIndex, (byte)cmd, (byte)val))
                        return true;
            return false;
        } else {
            byte[] pBuf = new byte[2];
            pBuf[0] = (byte) val;
            pBuf[1] = (byte) (val >> 8);
            return usbSmbWrite(devIndex, addr, (byte)cmd, bPEC, Short.BYTES, pBuf);
        }
    }
/*
    public boolean readByte(byte byCommand, byte[] pValue) {
        return usbSmbRead(devIndex, addr, byCommand, false, Byte.BYTES, pValue);
    }
    public boolean readBytes(byte byCommand, int nCount, byte[] pBuf) {
        return usbSmbRead(devIndex, addr, byCommand, false, nCount, pBuf);
    }
    public boolean readWord(byte byCommand, short[] pwValue) {
        byte[] pBuf = new byte[2];
        boolean result = usbSmbRead(devIndex, addr, byCommand, bPEC, Short.BYTES, pBuf);
        pwValue[0] = (short) (Byte.toUnsignedInt(pBuf[0]) | (pBuf[1] << 8));
        return result;
    }
    public boolean sendByte(byte byCommand) {
        byte[] pBuf = new byte[1];
	return usbSmbWrite(devIndex, addr, byCommand, false, 0, pBuf);
    }
    public boolean writeByte(byte byCommand, byte value) {
        byte[] pBuf = new byte[1];
        pBuf[0] = value;
        return usbSmbWrite(devIndex, addr, byCommand, false, Byte.BYTES, pBuf);
    }
    public boolean writeBytes(byte byCommand, int nCount, byte[] pBuf) {
        return usbSmbWrite(devIndex, addr, byCommand, false, nCount, pBuf);
    }
    public boolean writeWord(byte byCommand, short wValue) {
        byte[] pBuf = new byte[2];
        pBuf[0] = (byte) wValue;
        pBuf[1] = (byte) (wValue >> 8);
        return usbSmbWrite(devIndex, addr, byCommand, bPEC, Short.BYTES, pBuf);
    }
*/
    public boolean writeByteVerify(int cmd, int val) {
        if (writeByte(cmd, val)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
            byte[] pByte = new byte[1];
            if (readByte(cmd, pByte)) {
                if (pByte[0] == val)
                    return true;
            }
        }
        return false;
    }
    public boolean writeWordVerify(int cmd, int val) {
        if (writeWord(cmd, val)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
            short[] pWord = new short[1];
            if (readWord(cmd, pWord)) {
                if (pWord[0] == val)
                    return true;
            }
        }
        return false;
    }

    public boolean readDataFlashSector(boolean bNewSBS, byte[] sector_buf) {
        final int compare_end = 5;
        final int retry_end = 3;
        int retry_count;
        int index;
        byte[] sbuf = new byte[32];
        
        if (sector_buf.length < 256) {
            return false;
        }
        
        Arrays.fill(sector_buf, (byte)0xFF);
        
        for (index = 0; index < 8; index++) {
            if (bNewSBS) {
                for (retry_count = 0; retry_count < retry_end; retry_count++) {
                    if (writeByteVerify(0x3f, index)) {
                        break;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
                if (retry_count == retry_end) {
                    break;
                }
            }
            
            Arrays.fill(sbuf, (byte) 0xFF);
            
            int compare_count;
            for (compare_count = 0; compare_count < compare_end; compare_count++) {
                byte[] buf = new byte[32];
                boolean result;
                if (bNewSBS) {
                    result = readBytes(0x40, buf.length, buf);
                } else {
                    result = readBlock(0x78 + index, buf.length, buf);
                }
                if (result) {
                    if (Arrays.equals(buf, sbuf)) {
                        System.arraycopy(buf, 0, sector_buf, index * 32, buf.length);
                        break;
                    }
                    System.arraycopy(buf, 0, sbuf, 0, buf.length);
                }
            }
            if (compare_count == compare_end) {
                break;
            }
        }
            
        return (index == 8);
    }

    public boolean readDataFlashAll(boolean bNewSBS, byte[] buffer) {
        final int retry_end = 3;
        int retry_count;
        
        if (buffer.length < 2048) {
            return false;
        }

        if (bNewSBS) {
            for (retry_count = 0; retry_count < retry_end; retry_count++) {
                if (writeByte(0x61, 0))
                    break;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
            if (retry_count == retry_end)
                return false;
        }

        int index;
        for (index = 0; index < 8; index++) {
            for (retry_count = 0; retry_count < retry_end; retry_count++) {
                if (bNewSBS) {
                    if (writeByteVerify(0x3e, index))
                        break;
                } else {
                    if (writeWordVerify(0x77, index))
                        break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
            if (retry_count == retry_end)
                break;

            for (retry_count = 0; retry_count < retry_end; retry_count++) {
                byte[] sector_buf = new byte[256];
                if (readDataFlashSector(bNewSBS, sector_buf)) {
                    System.arraycopy(sector_buf, 0, buffer, index * 256, sector_buf.length);
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
            if (retry_count == retry_end)
                break;
        }

        return (index == 8);
    }

    byte chksum(int len, byte[] sbuf) {
        int sum = 0;
        for (int i = 0; i < len; i++) {
            sum += sbuf[i];
        }

	return (byte) ((~sum)+1);
    }

    public boolean writeDataFlashSector(boolean bNewSBS, final byte[] sector_buf) {
        final int retry_end = 3;
        int retry_count;
        
        if (sector_buf.length < 256) {
            return false;
        }
        
        if (bNewSBS) {
            int i, j;
            for (i = 0; i < 2; i++) {
                for (j = 0; j < 4; j++) {
                    for (retry_count = 0; retry_count < retry_end; retry_count++) {
                        if (writeByteVerify(0x3f, i * 4 + j)) {
                            break;
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                        }
                    }
                    if (retry_count == retry_end) {
                        break;
                    }
            
                    byte[] sbuf = new byte[32];
                    System.arraycopy(sector_buf, (i * 4 + j) * 32, sbuf, 0, sbuf.length);
                    if (!writeBytes(0x40, sbuf.length, sbuf)) {
                        break;
                    }
                    if (!writeByte(0x60, chksum(sbuf.length, sbuf)))
                        break;
                    
                    try {
                        if (j == 0) {
                            Thread.sleep(100);
                        } else {
                            Thread.sleep(10);
                        }
                    } catch (InterruptedException ex) {
                    }
                    
                    for (retry_count = 0; retry_count < retry_end; retry_count++) {
                        byte[] buf = new byte[32];
                        if (readBytes(0x40, buf.length, buf)) {
                            if (Arrays.equals(buf, sbuf)) {
                                break;
                            }
                        }
                    }
                    if (retry_count == retry_end) {
                        break;
                    }
                }
                if (j < 4)
                    break;
            }
            
            return (i == 2);
        } else {
            int i;
            for (i = 0; i < 8; i++) {
                byte[] sbuf = new byte[32];
                
                System.arraycopy(sector_buf, i * 32, sbuf, 0, sbuf.length);
                if (!writeBlock(0x78 + i, sbuf.length, sbuf)) {
                    break;
                }

                try {
                    if (i == 0) {
                        short[] pWord = new short[1];
                        if (readWord(0x77, pWord)) {
                            if (pWord[0] % 4 == 0) {
                                Thread.sleep(1700);
                            }
                        }
                    }
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }

                for (retry_count = 0; retry_count < retry_end; retry_count++) {
                    byte[] buf = new byte[32];
                    if (readBlock(0x78 + i, buf.length, buf)) {
//                        if (Arrays.equals(buf, sbuf))
                            break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                }
                if (retry_count == retry_end) {
                    break;
                }
            }
            
            return (i == 8);
        }
    }

    public boolean writeDataFlashAll(boolean bNewSBS, byte[] buffer) {
        final int retry_end = 3;
        int retry_count;
        
        if (buffer.length < 2048) {
            return false;
        }
        
        if (bNewSBS) {
            for (retry_count = 0; retry_count < retry_end; retry_count++) {
                if (writeByte(0x61, 0))
                    break;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
            if (retry_count == retry_end)
                return false;
        }

        int index;
        for (index = 0; index < 8; index++) {
            for (retry_count = 0; retry_count < retry_end; retry_count++) {
                if (bNewSBS) {
                    if (writeByteVerify(0x3e, index))
                        break;
                } else {
                    if (writeWordVerify(0x77, index))
                        break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
            if (retry_count == retry_end)
                break;

            for (retry_count = 0; retry_count < retry_end; retry_count++) {
                byte[] sector_buf = new byte[256];
                System.arraycopy(buffer, index * 256, sector_buf, 0, sector_buf.length);
                if (writeDataFlashSector(bNewSBS, sector_buf)) {
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
            if (retry_count == retry_end)
                break;
        }

        return (index == 8);
    }
    
    private native int usbInit();
    private native short usbGetVersion(int nDev);
    private native boolean usbSmbWrite(int nDev, byte addr, byte byCommand, boolean bPEC, int len, byte[] pBuf);
    private native boolean usbSmbRead(int nDev, byte addr, byte byCommand, boolean bPEC, int len, byte[] pBuf);
    private native boolean usbHdqWrite(int nDev, byte byCommand, byte byValue);
    private native boolean usbHdqRead(int nDev, byte byCommand, byte[] pValue);
    private native boolean usbEepromWrite(int nDev, int loc, byte[] pBuf);
    private native boolean usbEepromRead(int nDev, int loc, byte[] pBuf);
}
