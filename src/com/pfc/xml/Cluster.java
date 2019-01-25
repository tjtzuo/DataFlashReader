/*
 * Copyright (c) 2018 PowerFlash, Inc.
 * All rights reserved.
 */
package com.pfc.xml;

import java.util.List;

/**
 *
 * @author joseph
 */
public class Cluster {
    private String name;
    private List<DataFlash> dfList;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<DataFlash> getDFList() {
        return dfList;
    }
    public void setDFList(List<DataFlash> dfList) {
        this.dfList = dfList;
    }

    @Override
    public String toString() {
        return "Cluster:: Name=" + this.name;
    }

}
