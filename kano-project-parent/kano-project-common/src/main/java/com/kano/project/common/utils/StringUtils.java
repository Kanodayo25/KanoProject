package com.kano.project.common.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern p = Pattern.compile("\\d*");

    /**
     * 手机号脱敏
     *
     * @param s
     * @return
     */
    public static String phoneDesensitization(String s) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(s)) {
            return s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return s;
    }

    /**
     * 身份证脱敏
     *
     * @param s
     * @return
     */
    public static String idCardNumDesensitization(String s) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(s)) {
            return s.replaceAll("(?<=\\w{6})\\w(?=\\w{4})", "*");
        }
        return s;
    }

    /**
     * 将 String 转换为 long list
     *
     * @param s example -> 10015,10016
     * @return
     */
    public static List<Long> strToLongList(String s) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(s)) {
            String[] str = org.apache.commons.lang3.StringUtils.split(s, ",");
            if (str != null) {
                List<Long> longs = new ArrayList<>(str.length);
                for (int i = 0; i < str.length; i++) {
                    longs.add(Long.valueOf(str[i]));
                }
                return longs;
            }
            return null;
        }
        return null;
    }

    /**
     * 格式化数据
     *
     * @param ids
     * @return
     */
    public static String toSpilt(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return "";
        }
        if (ids.size() == 1) {
            return ids.get(0).toString();
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ids.size() - 1; i++) {
            builder.append(ids.get(i))
                    .append(",");
        }
        builder.append(ids.get(ids.size() - 1));
        return builder.toString();
    }

    /**
     * 判断是否是数字
     *
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        Matcher m = p.matcher(s);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 秒转
     *
     * @param second
     * @return
     */
    public static String secondToHumanText(Integer second) {
        if (second == null || second == 0) {
            return "0天0小时0分";
        }
        int minute = 0;
        int day = 0;
        int hour = 0;
        minute = second / 60;
        if (second % 60 != 0) {
            minute += 1;
        }
        if (minute >= 60) {
            hour = minute / 60;
            if (hour >= 24) {
                day = hour / 24;
                hour = hour - (day * 24);
            }
            minute = minute - (day * 24 * 60) - (hour * 60);
        }
        String desc = "";
        if (day != 0) {
            desc += day + "天";
        }
        if (hour != 0) {
            desc += hour + "小时";
        }
        if (minute != 0) {
            desc += minute + "分";
        }
        return desc;
    }

    public static void main(String[] args) {
//        System.out.println(isNumber("512346"));
//        System.out.println(secondToHumanText(2163601));
        System.out.println(idCardNumDesensitization("431122199305094519"));
    }
}
