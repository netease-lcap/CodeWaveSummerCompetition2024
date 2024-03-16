package com.yu.api;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/3/16 9:59
 **/
public class IPUtil {
    private static final Logger log = LoggerFactory.getLogger(IPUtil.class);
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
            log.error("UnknownHostException  !!!");
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
        try {
            List<String> ipv4Addr = new ArrayList<>();
            InetAddress localHost = InetAddress.getLocalHost();
            InetAddress[] allAddresses = InetAddress.getAllByName(localHost.getHostName());
            for (InetAddress address : allAddresses) {
                if (address.getHostAddress().matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                    ipv4Addr.add(address.getHostAddress());
                }
            }
            return ipv4Addr;
        } catch (UnknownHostException e) {
            log.error("UnknownHostException  !!!");
        }
        return null;
    }

    /**
     * 是否是有效的IPv4
     *
     * @param ip
     * @return
     */
    @NaslLogic(enhance = false)
    public static Boolean isValidIPv4(String ip) {
        return IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * 是否是有效的IPv6
     *
     * @param ip
     * @return
     */
    @NaslLogic(enhance = false)
    public static Boolean isValidIPv6(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address instanceof Inet6Address;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 获取ip类型
     *
     * @param ip
     * @return
     */
    @NaslLogic(enhance = false)
    public static String getIPType(String ip) {
        if (isValidIPv4(ip)) {
            return "IPv4";
        } else if (isValidIPv6(ip)) {
            return "IPv6";
        } else {
            return "Invalid";
        }
    }

    /**
     * 将ipv4转成ipv6
     *
     * @param ipv4
     * @return
     */
    @NaslLogic(enhance = false)
    public static String convertIPv4ToIPv6(String ipv4) {
        if (!isValidIPv4(ipv4)) {
            throw new IllegalArgumentException("Invalid IPv4 address");
        }
        return "::ffff:" + ipv4;
    }

    /**
     * 将ipv6转成ipv4
     *
     * @param ipv6
     * @return
     */
    @NaslLogic(enhance = false)
    public static String convertIPv6ToIPv4(String ipv6) {
        if (!isValidIPv6(ipv6)) {
            throw new IllegalArgumentException("Invalid IPv6 address");
        }
        return ipv6.substring(7);
    }

    /**
     * 解析ip地址，将ip地址分成四个部分（ipv4)
     *
     * @param ipv4
     * @return
     */
    @NaslLogic(enhance = false)
    public static List<Integer> parseIPv4(String ipv4) {
        if (!isValidIPv4(ipv4)) {
            throw new IllegalArgumentException("Invalid IPv4 address");
        }
        String[] parts = ipv4.split("\\.");
        List<Integer> result = new ArrayList<>(10);
        for (int i = 0; i < 4; i++) {
            result.add(Integer.parseInt(parts[i]));
        }
        return result;
    }

    /**
     * 解析ip地址，将ip地址分解成八个部分（ipv6)
     *
     * @param ipv6
     * @return
     */
    @NaslLogic(enhance = false)
    public static List<Integer> parseIPv6(String ipv6) {
        if (!isValidIPv6(ipv6)) {
            throw new IllegalArgumentException("Invalid IPv6 address");
        }
        String[] parts = ipv6.split(":");
        List<Integer> result = new ArrayList<>(10);
        for (int i = 0; i < 8; i++) {
            result.add(Integer.parseInt(parts[i], 16));
        }
        return result;
    }


    public static void main(String[] args) {
        // 获取本地主机地址
        System.out.println("本地主机地址: " + getHostAddr());
        // 获取本机的内网 IPv4 地址
        for (String s : Objects.requireNonNull(getIpv4Addr())) {
            System.out.println("本机的内网 IPv4 地址: " + s);
        }
    }
}
