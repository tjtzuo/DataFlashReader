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
import com.pfc.xml.*;

/**
 *
 * @author joseph
 */
public class DataFlashHandler extends DefaultHandler {

    //List to hold DataFlash object
    private List<Cluster> clusterList = null;
    private Cluster cluster = null;
    private List<DataFlash> dfList = null;
    private DataFlash df = null;

    //getter method for cluster list
    public List<Cluster> getDFClusterList() {
        return clusterList;
    }

    boolean bClusterName = false;
    boolean bName = false;
    boolean bStrAdr = false;
    boolean bUnit = false;
    boolean bType = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        switch (qName) {
            case "cluster":
                //create a new Cluster and put it in Map
                cluster = new Cluster();
                //initialize list
                if (clusterList == null)
                    clusterList = new ArrayList<>();
                break;
            case "cluster_name":
                bClusterName = true;
                break;
            case "element":
                //create a new DataFlash and put it in Map
                df = new DataFlash();
                //initialize list
                if (dfList == null)
                    dfList = new ArrayList<>();
                break;
            case "stradr":
                //set boolean values for fields, will be used in setting DataFlash variables
                bStrAdr = true;
                break;
            case "name":
                bName = true;
                break;
            case "type":
                bType = true;
                break;
            case "unit":
                bUnit = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("element")) {
            //add DataFlash object to list
            dfList.add(df);
        }else if (qName.equals("cluster")) {
            //add Cluster object to list
            cluster.setDFList(dfList);
            clusterList.add(cluster);
            dfList = null;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (bStrAdr) {
            String s = new String(ch, start, length);
            if (s.startsWith("0x")) {
                df.setStartAdr(Integer.parseInt(s.substring(2), 16));
            }
            bStrAdr = false;
        } else if (bName) {
            df.setName(new String(ch, start, length));
            bName = false;
        } else if (bUnit) {
            df.setUnit(new String(ch, start, length));
            bUnit = false;
        } else if (bType) {
            df.setType(new String(ch, start, length));
            bType = false;
        } else if (bClusterName) {
            cluster.setName(new String(ch, start, length));
            bClusterName = false;
        }
    }

}
