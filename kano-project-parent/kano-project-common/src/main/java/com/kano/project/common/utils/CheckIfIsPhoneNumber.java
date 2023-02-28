package com.kano.project.common.utils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1、手机号码
 * 组成
 *
 * 国家区域号-手机号码
 * 手机号码格式比较固定，无非是13x xxxx xxxx或者15x xxxx xxxx再或者18x xxxx xxxx的格式。座机就比较麻烦，比如长途区号变长（3位或者4位）电话号码变长（7位或者8位）有些还需要输入分机号。
 *
 * 通常可以看到解决这个复杂问题的解决方案是手机号和座机号分开。座机号拆分成三段，区号，电话号码+分机号。但是为了表单看起来清爽，设计的时候给了一个“万能”的输入框，给用户输入电话号码或者手机号码。
 *
 * 在这样的一个需求的大前提下，用复杂的正则表达式解决验证的问题是一种快速的解决方案。
 *
 * 首先搞定最容易的手机号码
 *
 * 因为目前开放的号段是130-139， 150-159， 185-189， 180
 *
 * 2、座机号码
 *
 * 组成：
 *
 * 国家区域号（+86等）-区号-固定电话号码-分机号
 * 三位 区号 的部分
 *
 * 010， 021-029，852（香港）
 *
 * 因为采用三位区号的地方都是8位电话号码，因此可以写成
 *
 * (010|021|022|023|024|025|026|027|028|029|852)\d{8}
 *
 * 当然不会这么简单，有些人习惯(010) xxxxxxxx的格式，我们也要支持一把，把以上表达式升级成
 *
 * 再看4位区号的城市
 *
 * 这里简单判断了不可能存在0111或者0222的区号，以及电话号码是7位或者8位。
 *
 * 最后是分机号（1-4位的数字）
 *
 * (?<分机号>\D?\d{1,4})?
 *
 * 以上拼装起来就是：
 *
 * "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
 * "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)"
 */
public class CheckIfIsPhoneNumber {

    /**
     * 获得电话号码的正则表达式：包括固定电话和移动电话
     * 符合规则的号码：
     *     1》、移动电话
     *         86+‘-’+11位电话号码
     *         86+11位正常的电话号码
     *         11位正常电话号码a
     *         (+86) + 11位电话号码
     *         (86) + 11位电话号码
     *     2》、固定电话
     *         区号 + ‘-’ + 固定电话  + ‘-’ + 分机号
     *         区号 + ‘-’ + 固定电话
     *         区号 + 固定电话
     * @return    电话号码的正则表达式
     */
    public static String isPhoneRegexp(){
        String regexp = "";
        //能满足最长匹配，但无法完成国家区域号和电话号码之间有空格的情况
        String mobilePhoneRegexp = "(?:(\\(\\+?86\\))((13[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})|" +
                "(?:86-?((13[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})|" +
                "(?:((13[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})";
        //固定电话正则表达式
        String landlinePhoneRegexp = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";

        regexp += "(?:" + mobilePhoneRegexp + "|" + landlinePhoneRegexp +")";
        return regexp;
    }


    /**
     * 从dataStr中获取出所有的电话号码（固话和移动电话），将其放入Set
     * @param dataStr    待查找的字符串
     * @param phoneSet    dataStr中的电话号码
     */
    public static void getPhoneNumFromStrIntoSet(String dataStr, Set<String> phoneSet){
        //获得固定电话和移动电话的正则表达式
        String regexp = isPhoneRegexp();

        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(dataStr);

        //找与该模式匹配的输入序列的下一个子序列
        while (matcher.find())
        {
            //获取到之前查找到的字符串，并将其添加入set中
            phoneSet.add(matcher.group());
        }
    }

    /**
     * 从dataStr中获取出所有的电话号码（固话或移动电话）
     * @param dataStr    待查找的字符串
     * @return  string    dataStr中的电话号码
     */
    public static String  getOnePhoneNumFromStr(String dataStr){
        //获得固定电话和移动电话的正则表达式
        String regexp = isPhoneRegexp();

        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(dataStr);

        //找与该模式匹配的输入序列的下一个子序列
        if (matcher.find()){
            //获取到之前查找到的字符串，并将其添加入set中
            return matcher.group();
        }
        return "";
    }
}
