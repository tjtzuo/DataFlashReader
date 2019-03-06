/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.xml.sax;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import com.pfc.xml.*;

/**
 *
 * @author joseph
 */
public class XMLParserSAX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SBSHandler sbsHandler = new SBSHandler();
            saxParser.parse(new File("sbs.ini"), sbsHandler);
            //Get SBS list
            List<SBS> sbsList = sbsHandler.getSBSList();
            //print sbs information
            for (SBS sbs : sbsList) {
                System.out.println(sbs);
            }
            DataFlashHandler dfHandler = new DataFlashHandler();
            saxParser.parse(new File("DataFlash.ini"), dfHandler);
            //Get DataFlashCluster list
            List<Cluster> clusterList = dfHandler.getDFClusterList();
            //print cluster information
            for (Cluster cluster : clusterList) {
                System.out.println(cluster);
                //Get DataFlash list
                List<DataFlash> dfList = cluster.getDFList();
                //print dataflash information
                for (DataFlash df : dfList) {
                    System.out.println(df);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println(e);
        }
    }
    
}
