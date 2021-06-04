package com.msy.plus.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtils
 *
 * @author wzp
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";

    public static String removeLast(String str) {
        str = str.trim();
        return str.substring(0, str.length() - 1);
    }

    /**
     * 转换为字节数组
     *
     * @param str
     * @return
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 转换为字节数组
     *
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (isBlank(html)) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html
     * @return
     */
    public static String replaceMobileHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(trim(val.toString()));
        } catch (Exception e) {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 如果不为空，则设置值
     *
     * @param target
     * @param source
     */
    public static void setValueIfNotBlank(String target, String source) {
        if (isNotBlank(source)) {
            target = source;
        }
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     *
     * @param objectString 对象串 例如：row.user.no 返回：!row?'':!row.user?'':!row.user.no?'':row.user.no
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = split(objectString, ".");
        for (int i = 0; i < vals.length; i++) {
            val.append("." + vals[i]);
            result.append("!" + (val.substring(1)) + "?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }

    public static void camelCaseName(String name, StringBuilder result) {
        try {
            result.setLength(0);
            for (int i = 0; i < name.length(); ++i) {
                char c = name.charAt(i);
                if (c == '_') {
                    c = name.charAt(++i);
                    result.append(Character.toUpperCase(c));
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /***
     * 类对比
     *
     * @param str
     *
     * @return
     *
     * @function xxx
     * @date 2014-3-24 上午10:26:59
     */
    public static Boolean containsUpperAndLowerLetter(String str) {
        if (isEmpty(str)) {
            return false;
        }
        if (str.length() == 1) {
            return false;
        }
        Pattern pattern = Pattern.compile(".*?[a-z]+.*?");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) {
            return false;
        }
        pattern = Pattern.compile(".*?[A-Z]+.*?");
        matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static String encode(String str) throws Exception {
        if (!StringUtils.isBlank(str)) {
            try {
                return URLEncoder.encode(str, "utf-8");
            } catch (UnsupportedEncodingException e) {

                throw new Exception("编码转换中出现错误", e);
            }
        }
        return null;
    }

    public static String decode(String str) throws Exception {
        if (!StringUtils.isBlank(str)) {
            try {
                return URLDecoder.decode(str, "utf-8");
            } catch (UnsupportedEncodingException e) {

                throw new Exception("编码转换中出现错误", e);
            }
        }
        return null;
    }

    public static List<String> findEngLinks(String urlText) {
        List<String> result = Lists.newArrayList();
        String regexp = "http://\\w*\\.\\w*/\\w*";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(urlText);
        while (matcher.find()) {
            String url = matcher.group();
            result.add(url);
        }
        return result;
    }

    public static List<String> findLinks(String urlText) {
        List<String> result = Lists.newArrayList();
        // 匹配的条件选项为结束为空格(半角和全角)、换行符、字符串的结尾或者遇到其他格式的文本
        String regexp = "(((http|ftp|https|file)://)|((?<!((http|ftp|https|file)://))www\\.))" // 以http...或www开头
                + ".*?" // 中间为任意内容，惰性匹配
                + "(?=(&nbsp;|\\s|　|<br />|$|[<>]))"; // 结束条件
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(urlText);
        while (matcher.find()) {
            String url = "www".equalsIgnoreCase(matcher.group().substring(0, 3)) ? "http://" + matcher.group() : matcher.group();
            result.add(url);
        }
        return result;
    }

    public static String urlToLink(String urlText) {
        // 匹配的条件选项为结束为空格(半角和全角)、换行符、字符串的结尾或者遇到其他格式的文本
        String regexp = "(((http|ftp|https|file)://)|((?<!((http|ftp|https|file)://))www\\.))" // 以http...或www开头
                + ".*?" // 中间为任意内容，惰性匹配
                + "(?=(&nbsp;|\\s|　|<br />|$|[<>]))"; // 结束条件
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(urlText);
        StringBuffer stringbuffer = new StringBuffer();
        while (matcher.find()) {
            String url = "www".equalsIgnoreCase(matcher.group().substring(0, 3)) ? "http://" + matcher.group() : matcher.group();
            String tempString = "<a href=\"" + url + "\">" + matcher.group() + "</a>";
            // 这里对tempString中的"\"和"$"进行一次转义，因为下面对它替换的过程中appendReplacement将"\"和"$"作为特殊字符处理
            int tempLength = tempString.length();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < tempLength; ++i) {
                char c = tempString.charAt(i);
                if (c == '\\' || c == '$') {
                    buffer.append("\\").append(c);
                } else {
                    buffer.append(c);
                }
            }
            tempString = buffer.toString();
            matcher.appendReplacement(stringbuffer, tempString);
        }
        matcher.appendTail(stringbuffer);
        return stringbuffer.toString();
    }

    public static String textToLinks(String note) {
        // 转换的思想为把文本中不是链接("/a>"和"<a "之间)的内容逐个进行转换
        // 把字符串中的"\"和"$"加上转义符，避免appendReplacement替换字符串的时候将它们作为特殊字符处理
        int noteLength = note.length();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < noteLength; ++i) {
            char ch = note.charAt(i);
            if (ch == '\\' || ch == '$') {
                buffer.append("\\").append(ch);
            } else {
                buffer.append(ch);
            }
        }
        String linkNote = "/a>" + buffer.toString() + "<a ";
        String regexp = "(?<=/a>).*?(?=<a )";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(linkNote);
        StringBuffer stringbuffer = new StringBuffer();
        while (matcher.find()) {
            String tempString = urlToLink(matcher.group());
            matcher.appendReplacement(stringbuffer, tempString);
        }
        matcher.appendTail(stringbuffer);
        String result = stringbuffer.toString();
        // 返回的结果去掉加入的"/a>" 和"<a "
        return result.substring(3, result.length() - 3);
    }

    public static void underscoreName(String name, StringBuilder result) {
        result.setLength(0);
        // 第一个字母可能是大写
        result.append(Character.toLowerCase(name.charAt(0)));
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append("_");
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
    }

    public static boolean isCamelCase(String name) {
        if (Character.isUpperCase(name.charAt(0))) {
            return false;
        }
        int upperCaseCnt = 0;
        int lowCaseCnt = 0;
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                return false;
            }
            if (Character.isUpperCase(c)) {
                upperCaseCnt++;
            } else {
                lowCaseCnt++;
            }
        }
        return upperCaseCnt > 0 && lowCaseCnt > 0;
    }

    public static Map<String, String> toMap(String str, char primarychar, char subchar) {
        Map<String, String> params = Maps.newLinkedHashMap();
        List<String> list1 = toList(str, primarychar);
        List<String> buff = new ArrayList<String>(2);
        for (String e : list1) {
            toList(e, subchar, buff);
            if (buff.size() > 1) {
                params.put(buff.get(0), buff.get(1));
            }
        }
        return params;
    }

    public static List<String> toList(String str, char sep) {
        if (StringUtils.isEmpty(str)) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<String>();
        toList(str, sep, result);
        return result;
    }

    public static void toList(String str, char sep, List<String> list) {
        StringBuilder sb = new StringBuilder();
        list.clear();
        for (int idx = 0; idx < str.length(); ++idx) {
            char c = str.charAt(idx);
            if (c == sep) {
                list.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            list.add(sb.toString().trim());
            sb.setLength(0);
        }
    }

    public static String listToString(List<?> list, char sep) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (Object o : list) {
                if (o instanceof String) {
                    sb.append(sep).append(o);
                } else {
                    sb.append(sep).append(o.toString());
                }
            }
        }
        return sb.toString().length() > 0 ? sb.toString().substring(1) : "";
    }

    public static String arrayToString(Object[] array, char sep) {
        StringBuffer sb = new StringBuffer("");
        if (array != null && array.length > 0) {
            for (Object o : array) {
                if (o instanceof String) {
                    sb.append(sep).append(o);
                } else {
                    sb.append(sep).append(o.toString());
                }
            }
        }
        return sb.toString().length() > 0 ? sb.toString().substring(1) : "";
    }

    /**
     * @param displaysz
     * @return
     */
    public static long displayszToByteCount(String displaysz) {
        Pattern pattern = Pattern.compile("([\\d,]+)([kKmMgG]?).*");
        Matcher matcher = pattern.matcher(displaysz);
        if (!matcher.matches()) {
            return -1;
        } else {
            String digits = matcher.group(1);
            String suffix = matcher.group(2).toLowerCase();
            long number = Long.parseLong(digits);
            if ("k".equals(suffix)) {
                number = number * 1024;
            } else if ("m".equals(suffix)) {
                number = number * 1024 * 1024;
            } else if ("g".equals(suffix)) {
                number = number * 1024 * 1024 * 1024;
            }
            return number;
        }
    }

    /**
     * 将全角转换成半角 男，女
     *
     * @return
     */
    public static String toDBCase(String str) {
        StringBuilder outStr = new StringBuilder(str.length());
        String temp = "";
        byte[] bytes = null;
        boolean isSbc = false;
        for (int i = 0; i < str.length(); i++) {
            try {
                temp = str.substring(i, i + 1);
                bytes = temp.getBytes("unicode");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            isSbc = false;
            // utf8
            if (bytes[2] == -1) {
                bytes[3] = (byte) (bytes[3] + 32);
                bytes[2] = 0;
                isSbc = true;
            } else if (bytes[3] == -1) { // gb2312
                bytes[2] = (byte) (bytes[2] + 32);
                bytes[3] = 0;
                isSbc = true;
            }
            if (isSbc) {
                try {
                    outStr.append(new String(bytes, "unicode"));
                } catch (UnsupportedEncodingException e) {
                }
            } else {
                outStr.append(temp);
            }
        }
        return outStr.toString();
    }

    /**
     * 除去数字字符串的后缀.0, 如2.0变成2
     *
     * @param str
     * @return
     */
    public static final String ereaseBackZeros(String str) {
        if (NumberUtils.isDigits(str)) {
            return str.endsWith(".0") ? str.substring(0, str.length() - 2) : str;
        }
        return str;
    }

    static public String filterOffUtf8Mb4(String text) {
        if (StringUtils.isEmpty(text)) {
            return StringUtils.EMPTY;
        }
        try {
            byte[] bytes = text.getBytes("utf-8");
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            int i = 0;
            while (i < bytes.length) {
                short b = bytes[i];
                if (b > 0) {
                    buffer.put(bytes[i++]);
                    continue;
                }
                b += 256;
                if ((b ^ 0xC0) >> 4 == 0) {
                    buffer.put(bytes, i, 2);
                    i += 2;
                } else if ((b ^ 0xE0) >> 4 == 0) {
                    buffer.put(bytes, i, 3);
                    i += 3;
                } else if ((b ^ 0xF0) >> 4 == 0) {
                    i += 4;
                } else {
                    i += 2;
                }
            }
            buffer.flip();
            return new String(buffer.array(), "utf-8");
        } catch (UnsupportedEncodingException exception) {
            return StringUtils.EMPTY;
        }
    }

    public static String buildUrlString(String address, Map<String, Object> map) {
        StringBuilder sb = new StringBuilder(address);
        int idx = StringUtils.lastIndexOf(address, '?');
        if (idx == StringUtils.INDEX_NOT_FOUND) {
            sb.append("?");
        } else {
            if (idx != address.length() - 1) {
                sb.append("&");
            }
        }
        for (Map.Entry<String, Object> ent : map.entrySet()) {
            sb.append(ent.getKey()).append("=").append(ent.getValue());
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static Date parseDateForHttp(String dateFmtStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return sdf.parse(dateFmtStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int[] toIntArray(String list, char c) {
        List<String> ss = toList(list, c);
        int[] result = new int[ss.size()];
        int i = 0;
        for (String s : ss) {
            result[i++] = NumberUtils.toInt(s);
        }
        return result;
    }

    public static String getHrefText(String hrefString) {
        if (StringUtils.isEmpty(hrefString)) {
            return null;
        }
        Pattern pattern = Pattern.compile(">.*?</a>");
        Matcher matcher = pattern.matcher(hrefString);
        if (matcher.find()) {
            return matcher.group().replaceAll(">|</a>", "");
        }
        return null;
    }

    public static boolean isJsonStyle(String s) {
        return StringUtils.startsWith(s, "{") && StringUtils.endsWith(s, "}");
    }

    public static boolean isJsonJsonArrayStyle(String s) {
        return StringUtils.startsWith(s, "[") && StringUtils.endsWith(s, "]");
    }

    /**
     * @param inputString
     * @return
     * @Description: 文本去除html标签
     */
    public static String html2Text(String inputString) {
        if (inputString == null) {
            return "";
        }
        String htmlStr = inputString;
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        Pattern p_html1;
        Matcher m_html1;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String regEx_html1 = "<[^>]+";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            m_html1 = p_html1.matcher(htmlStr);
            htmlStr = m_html1.replaceAll(""); // 过滤html标签

            textStr = StringUtils.replace(htmlStr, "&nbsp;", StringUtils.EMPTY);
            textStr = StringUtils.deleteWhitespace(textStr);

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串
    }

    /**
     * 判断是否为手机号
     *
     * @return
     */
    public static boolean isMobileNO(String mobileNo) {
        if (null == mobileNo || "".equals(mobileNo)) {
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobileNo);
        return m.matches();
    }

    /**
     * 判断是否为Email
     *
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 对参数进行UTF-8编码，并替换特殊字符
     *
     * @param paramDecString 待编码的参数字符串
     * @return 完成编码转换的字符串
     */
    public static String urlEncode(String paramDecString) {
        try {
            return URLEncoder.encode(paramDecString, "UTF-8").replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~").replace("#", "%23");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将 %XX 换为原符号，并进行UTF-8反编码
     *
     * @param paramEncString 待反编码的参数字符串
     * @return 未进行UTF-8编码和字符替换的字符串
     */
    public static String urlDecode(String paramEncString) {
        return urlDecode(paramEncString, "UTF-8");
    }

    public static String urlDecode(String paramEncString, String code) {
        int nCount = 0;
        for (int i = 0; i < paramEncString.length(); i++) {
            if (paramEncString.charAt(i) == '%') {
                i += 2;
            }
            nCount++;
        }
        byte[] sb = new byte[nCount];
        for (int i = 0, index = 0; i < paramEncString.length(); i++) {
            if (paramEncString.charAt(i) != '%') {
                sb[index++] = (byte) paramEncString.charAt(i);
            } else {
                StringBuilder sChar = new StringBuilder();
                sChar.append(paramEncString.charAt(i + 1));
                sChar.append(paramEncString.charAt(i + 2));
                sb[index++] = Integer.valueOf(sChar.toString(), 16).byteValue();
                i += 2;
            }
        }
        String decode = "";
        try {
            decode = new String(sb, code);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    /**
     * 根据List&#60NameValuePair&#62格式存储的参数队列，生成queryString
     *
     * @return 不包括？的queryString
     */
    public static String getQueryString(Map<String, String> queryParamsList) {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> ent : queryParamsList.entrySet()) {
            queryString.append('&');
            queryString.append(ent.getKey());
            queryString.append('=');
            queryString.append(urlEncode(ent.getValue()));
        }
        // 去掉第一个&号
        return queryString.toString().substring(1);
    }

    /**
     * @return 不包括？的queryString
     */
    public static String hideStar(String no, int length, boolean isEnd) {
        if (StringUtils.isEmpty(no)) {
            return no;
        }
        int size = no.toString().length();
        if (length > size) {
            length = size;
        }
        String ret = "*****";
        if (length == size) {
            ret = "";
        }
        if (isEnd) {
            ret = ret + no.toString().substring(size - length);
        } else {
            ret = no.toString().substring(0, length) + ret;
        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(hideStar("1234567890", 3, false));
        System.out.println(hideStar("1234567890", 3, true));
        System.out.println(hideStar("1234567890", 10, true));
        System.out.println(hideStar("1234567890", 10, false));
        System.out.println(hideStar("1234567890", 11, true));
        System.out.println(hideStar("1234567890", 11, false));
    }
}
