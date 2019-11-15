package org.nbone.util;

import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/**
 * 内存、磁盘大小单位美化 （ Memory）date format
 *
 * @author thinking
 * @Version: 1.0
 * @since 13-3-22 下午12:18
 */
public class PrettyUtils {

    private static final int UNIT = 1024;

    /**
     * @param byteSize 字节
     * @return
     */
    public static String prettyByteSize(long byteSize) {

        double size = 1.0 * byteSize;

        String type = "B";
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1KB
            type = "B";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1MB
            type = "KB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1GB
            type = "MB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1TB
            type = "GB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1PB
            type = "TB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) {
            type = "PB";
            return format(size, type);
        }
        return ">PB";
    }

    private static String format(double size, String type) {
        int precision = 0;

        if (size * 1000 % 10 > 0) {
            precision = 3;
        } else if (size * 100 % 10 > 0) {
            precision = 2;
        } else if (size * 10 % 10 > 0) {
            precision = 1;
        } else {
            precision = 0;
        }

        String formatStr = "%." + precision + "f";

        if ("KB".equals(type)) {
            return String.format(formatStr, (size)) + "KB";
        } else if ("MB".equals(type)) {
            return String.format(formatStr, (size)) + "MB";
        } else if ("GB".equals(type)) {
            return String.format(formatStr, (size)) + "GB";
        } else if ("TB".equals(type)) {
            return String.format(formatStr, (size)) + "TB";
        } else if ("PB".equals(type)) {
            return String.format(formatStr, (size)) + "PB";
        }
        return String.format(formatStr, (size)) + "B";
    }


    /**
     * 显示秒值为**年**月**天 **时**分**秒  如1年2个月3天 10小时
     *
     * @return
     */
    public static final String prettySeconds(int totalSeconds) {
        StringBuilder s = new StringBuilder();
        int second = totalSeconds % 60;
        if (totalSeconds > 0) {
            s.append("秒");
            s.append(StringUtils.reverse(String.valueOf(second)));
        }

        totalSeconds = totalSeconds / 60;
        int minute = totalSeconds % 60;
        if (totalSeconds > 0) {
            s.append("分");
            s.append(StringUtils.reverse(String.valueOf(minute)));
        }

        totalSeconds = totalSeconds / 60;
        int hour = totalSeconds % 24;
        if (totalSeconds > 0) {
            s.append(StringUtils.reverse("小时"));
            s.append(StringUtils.reverse(String.valueOf(hour)));
        }

        totalSeconds = totalSeconds / 24;
        int day = totalSeconds % 31;
        if (totalSeconds > 0) {
            s.append("天");
            s.append(StringUtils.reverse(String.valueOf(day)));
        }

        totalSeconds = totalSeconds / 31;
        int month = totalSeconds % 12;
        if (totalSeconds > 0) {
            s.append("月");
            s.append(StringUtils.reverse(String.valueOf(month)));
        }

        totalSeconds = totalSeconds / 12;
        int year = totalSeconds;
        if (totalSeconds > 0) {
            s.append("年");
            s.append(StringUtils.reverse(String.valueOf(year)));
        }
        return s.reverse().toString();
    }

    /**
     * 美化时间 如显示为 1小时前 2分钟前
     *
     * @return
     */
    public static final String prettyTime(Date date) {
        PrettyTime p = new PrettyTime();
        return p.format(date);

    }

    public static final String prettyTime(long millisecond) {
        PrettyTime p = new PrettyTime();
        return p.format(new Date(millisecond));
    }

    public static void main(String[] args) {
        System.out.println(PrettyUtils.prettyByteSize(102600000));
        System.out.println(PrettyUtils.prettyByteSize(1L * UNIT));
        System.out.println(PrettyUtils.prettyByteSize(1L * UNIT * UNIT));
        System.out.println(PrettyUtils.prettyByteSize(1L * UNIT * 1023));
        System.out.println(PrettyUtils.prettyByteSize(1L * 1023 * 1023 * 1023));
        System.out.println(PrettyUtils.prettyByteSize(1L * UNIT * UNIT * UNIT));
        System.out.println(PrettyUtils.prettyByteSize(1L * UNIT * UNIT * UNIT * UNIT));
        System.out.println(PrettyUtils.prettyByteSize(1L * UNIT * UNIT * UNIT * UNIT * UNIT));
        System.out.println(PrettyUtils.prettyByteSize(1L * UNIT * UNIT * UNIT * UNIT * UNIT * UNIT));


        System.out.println("-----------");
        System.out.println(PrettyUtils.prettyTime(new Date()));
        System.out.println(PrettyUtils.prettyTime(123));
        System.out.println(PrettyUtils.prettySeconds(10));
        System.out.println(PrettyUtils.prettySeconds(61));
        System.out.println(PrettyUtils.prettySeconds(3661));
        System.out.println(PrettyUtils.prettySeconds(36611));
        System.out.println(PrettyUtils.prettySeconds(366111));
        System.out.println(PrettyUtils.prettySeconds(3661111));
        System.out.println(PrettyUtils.prettySeconds(36611111));
    }

}
