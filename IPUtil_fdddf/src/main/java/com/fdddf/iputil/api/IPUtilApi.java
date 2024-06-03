package com.fdddf.iputil.api;


import com.alibaba.fastjson.JSONObject;
import com.netease.lowcode.core.annotation.NaslLogic;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IPUtilApi {

    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    /**
     * 获取当前主机的名称
     *
     * @return 返回当前主机的名称。如果无法确定主机名，则返回null
     */
    @NaslLogic
    public static String getHostName() {
        try {
            // 获取本地主机的IP地址对象
            InetAddress inetAddress = InetAddress.getLocalHost();
            // 从IP地址对象中获取主机名
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            logger.error("Get hostname failed: ", e);
            return null;
        }
    }

    /**
     * 获取当前系统中所有的IPv4地址列表。
     * 此方法会遍历所有的网络接口（网卡）和它们的地址，并筛选出非回环、非虚拟的IPv4地址。
     *
     * @return List<String> 包含所有非回环、非虚拟的IPv4地址的字符串列表。
     */
    @NaslLogic
    public static List<String> getInetIPv4Addresses() {
        List<String> ipList = new ArrayList<>();
        try {
            // 获取系统中所有的网络接口枚举
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();


            // 遍历每一个网络接口
            while (niList.hasMoreElements()) {
                NetworkInterface ni = niList.nextElement();
                // 跳过回环接口和虚拟接口
                if (ni.isLoopback() || ni.isVirtual()) {
                    continue;
                }

                // 获取当前网络接口的所有地址枚举
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();

                // 遍历每一个地址，筛选IPv4地址
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // 只关注IPv4地址
                    if (inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress();
                        ipList.add(ipAddress);
                    }
                }
            }
        } catch (SocketException e) {
            logger.error("call getInetIPv4Addresses failed: ", e);
            return null;
        }

        return ipList;
    }

    /**
     * 获取当前设备的外部IP地址。
     * 该方法通过访问https://myip.ipip.net/json来获取一个JSON格式的响应，然后解析出其中的IP地址。
     *
     * @return 返回一个字符串，表示当前设备的外部IP地址。
     */
    @NaslLogic
    public static String getExternalIP() {
        try {
            URL url = new URL("https://myip.ipip.net/json");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String jsonString = br.readLine();
            JSONObject json = JSON.parseObject(jsonString);
            String ipAddress = json.getJSONObject("data").getString("ip");
            br.close();
            return ipAddress;
        } catch (Exception e) {
            logger.error("call getExternalIP failed: ", e);
            return null;
        }
    }

}
