package com.fdddf.iputil;

import com.fdddf.iputil.api.IPUtilApi;

public class Main {
    public static void main(String[] args) {
        System.out.println(IPUtilApi.getHostName());
        System.out.println(IPUtilApi.getInetIPv4Addresses());
        System.out.println(IPUtilApi.getExternalIP());
    }
}
