/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.xml.sax;

import com.pfc.xml.BitField;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Joseph
 */
public class BitsHandler extends DefaultHandler {

    //List to hold BitField object
    private List<BitField> bfList = null;
    private BitField bf = null;
    
    //getter method for sbs list
    public List<BitField> getBitFieldList() {
    return bfList;
    }

    boolean bCmd = false;
    boolean bName = false;
    boolean bSize = false;
    boolean bBit = false;
//    boolean bFormat = false;
    int bitIndex = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        switch (qName) {
            case "element":
                //create a new BitField and put it in Map
                //initialize BitField object
                bf = new BitField();
                //initialize list
                if (bfList == null)
                    bfList = new ArrayList<>();
                break;
            case "cmd":
                bCmd = true;
                break;
            case "name":
                bName = true;
                break;
            case "size":
                bSize = true;
                break;
            case "bit":
                bBit = true;
                break;
            case "Format":
//                bFormat = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("element")) {
            //add BitField object to list
            bfList.add(bf);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (bCmd) {
            String s = new String(ch, start, length);
            if (s.startsWith("0x")) {
                bf.setCmd(Byte.decode(s));
            }
            bCmd = false;
        } else if (bName) {
            bf.setName(new String(ch, start, length));
            bName = false;
        } else if (bSize) {
            bf.setSize(Integer.parseInt(new String(ch, start, length)));
            bf.setBits(new String[bf.getSize()*8]);
            bitIndex = 0;
            bSize = false;
        } else if (bBit) {
            bf.setBit(bitIndex++, new String(ch, start, length));
            bBit = false;
//        } else if (bFormat) {
//            sbs.setFormat(new String(ch, start, length));
//            bFormat = false;
        }
    }

}
