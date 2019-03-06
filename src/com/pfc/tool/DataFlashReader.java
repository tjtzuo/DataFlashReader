/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.tool;

import com.pfc.lib.UsbSmb;
import com.pfc.lib.DllEntry;
import com.pfc.xml.sax.DataFlashHandler;
import com.pfc.xml.*;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author joseph
 */
public class DataFlashReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UsbSmb usbSmb = new UsbSmb();
            System.out.println("USB SMBus Version: " + Integer.toHexString(usbSmb.getVersion()));
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DataFlashHandler dfHandler = new DataFlashHandler();
            saxParser.parse(new File("DataFlash.ini"), dfHandler);
//            boolean result = DllEntry.cod128("D:\\Temp\\Default.bin");
//            if (result);
//            byte[] outBuf = new byte[65536];
//            int len = DllEntry.dec64("D:\\Temp\\00_04_03_01_20.cod", outBuf);
            byte[] outBuf = new byte[2048];
            int len = DllEntry.dec128("\\Users\\Public\\Share\\Default.bin", outBuf);
            if (len == 2048) {
                int stradr = 0x7600;
                //Get DataFlashCluster list
                List<Cluster> clusterList = dfHandler.getDFClusterList();
                //print cluster information
                for (Cluster cluster : clusterList) {
                    System.out.println(cluster);
                    //Get DataFlash list
                    List<DataFlash> dfList = cluster.getDFList();
                    //print dataflash information
                    for (DataFlash df : dfList) {
                        int value = 0, addr = df.getStartAdr() - stradr;
                        switch (df.getType()) {
                            case "S1":
                                value = outBuf[addr];
                                break;
                            case "U1":
                                value = Byte.toUnsignedInt(outBuf[addr]);
                                break;
                            case "S2":
                                value = (outBuf[addr]<<8) |
                                        Byte.toUnsignedInt(outBuf[addr+1]);
                                break;
                            case "U2":
                                value = (Byte.toUnsignedInt(outBuf[addr])<<8) |
                                        Byte.toUnsignedInt(outBuf[addr+1]);
                                break;
                            case "S4":
                            case "U4":
                                value = (outBuf[addr]<<24) |
                                        (Byte.toUnsignedInt(outBuf[addr+1])<<16) |
                                        (Byte.toUnsignedInt(outBuf[addr+2])<<8) |
                                        Byte.toUnsignedInt(outBuf[addr+3]);
                                break;
                            case "string":
                                df.setText(new String(outBuf, addr+1, outBuf[addr]));
                                break;
                            case "-":
                                break;
                            default:
                                assert(false);
                        }
                        df.setValue(value);
                        if (df.getUnit().equals("hex")) {
                            df.setText("0x" + Integer.toHexString(value));
                        } else {
                            switch (df.getType()) {
                                case "S1":
                                case "U1":
                                case "S2":
                                case "U2":
                                case "S4":
                                    df.setText(Integer.toString(value));
                                    break;
                                case "U4":
                                    df.setText(Integer.toUnsignedString(value));
                                    break;
                                default:
                            }
                        }
                        System.out.println(df);
                    }
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.err.println(e);
        }
    }
}
