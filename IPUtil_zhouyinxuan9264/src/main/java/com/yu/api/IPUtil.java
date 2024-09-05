package com.yu.api;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/3/16 9:59
 **/
public class IPUtil {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private static final String IPV4_REGEX =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);

    /**
     * 获取主机地址
     *
     * @return
     */
    @NaslLogic(enhance = false)
    public static String getHostAddr() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取主机地址异常 UnknownHostException：", e);
        }
        return null;
    }

    /**
     * 获取ipv4地址
     *
     * @return
     */
    @NaslLogic(enhance = false)
    public static List<String> getIpv4Addr() {
        List<String> ipv4Addr = new ArrayList<>();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            InetAddress[] allAddresses = InetAddress.getAllByName(localHost.getHostName());
            for (InetAddress address : allAddresses) {
                if (address.getHostAddress().matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                    ipv4Addr.add(address.getHostAddress());
                }
            }
        } catch (UnknownHostException e) {
            log.error("获取主机地址异常 UnknownHostException：", e);
        }
        String[] urls = new String[]{"https://api.ipify.org","http://checkip.amazonaws.com"};
        for (String s : urls) {
            try {
                URL url = new URL(s);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String body = br.lines().collect(Collectors.joining("\n"));
                String externalIp = body;
                ipv4Addr.add(externalIp);
                break;
            } catch (IOException e) {
                log.error("从" + s + "返回外部ip地址异常 ", e);
            }
        }
        return ipv4Addr;
    }


        public static void main (String[]args){
            // 获取本地主机地址
            System.out.println("本地主机地址: " + getHostAddr());
            // 获取本机的内网 IPv4 地址
            for (String s : Objects.requireNonNull(getIpv4Addr())) {
                System.out.println("本机的内网 IPv4 地址: " + s);
            }
        }
    }
