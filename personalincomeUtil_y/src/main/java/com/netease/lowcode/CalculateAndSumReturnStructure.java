package com.netease.lowcode;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.awt.*;
import java.util.List;

@NaslStructure
public class CalculateAndSumReturnStructure {
    public String sumInterest ;//总利息
    public List<CalculateReturnStructure> listCalculateReturnStructure;//每期还款实体

    public List<CalculateReturnStructure> getListCalculateReturnStructure() {
        return listCalculateReturnStructure;
    }

    public void setListCalculateReturnStructure(List<CalculateReturnStructure> listCalculateReturnStructure) {
        this.listCalculateReturnStructure = listCalculateReturnStructure;
    }

    public String getSumInterest() {
        return sumInterest;
    }

    public void setSumInterest(String sumInterest) {
        this.sumInterest = sumInterest;
    }

    @Override
    public String toString() {
        return "CalculateAndSumReturnStructure{" +
                "listCalculateReturnStructure=" + listCalculateReturnStructure +
                ", sumInterest='" + sumInterest + '\'' +
                '}';
    }
}
