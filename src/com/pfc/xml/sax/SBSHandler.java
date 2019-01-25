/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.xml.sax;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.pfc.xml.SBS;

/**
 *
 * @author joseph
 */
public class SBSHandler extends DefaultHandler {

    //List to hold SBS object
    private List<SBS> sbsList = null;
    private SBS sbs = null;

    //getter method for sbs list
    public List<SBS> getSBSList() {
        return sbsList;
    }

    boolean bCmd = false;
    boolean bName = false;
    boolean bSize = false;
    boolean bUnit = false;
    boolean bFormat = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        switch (qName) {
            case "element":
                //create a new SBS and put it in Map
//            String id = attributes.getValue("id");
                //initialize SBS object and set id attribute
                sbs = new SBS();
                //            sbs.setId(Integer.parseInt(id));
                //initialize list
                if (sbsList == null)
                    sbsList = new ArrayList<>();
                break;
            case "cmd":
                //set boolean values for fields, will be used in setting SBS variables
                bCmd = true;
                break;
            case "name":
                bName = true;
                break;
            case "size":
                bSize = true;
                break;
            case "unit":
                bUnit = true;
                break;
            case "Format":
                bFormat = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("element")) {
            //add SBS object to list
            sbsList.add(sbs);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (bCmd) {
            String s = new String(ch, start, length);
            if (s.startsWith("0x")) {
//                sbs.setCmd(Integer.parseInt(s.substring(2), 0x10));
                sbs.setCmd(Byte.decode(s));
            }
            bCmd = false;
        } else if (bSize) {
            sbs.setSize(Integer.parseInt(new String(ch, start, length)));
            bSize = false;
        } else if (bName) {
            sbs.setName(new String(ch, start, length));
            bName = false;
        } else if (bFormat) {
            sbs.setFormat(new String(ch, start, length));
            bFormat = false;
        } else if (bUnit) {
            sbs.setUnit(new String(ch, start, length));
            bUnit = false;
        }
    }

}
