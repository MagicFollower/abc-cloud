package com.abc.common.core.utils;

import com.abc.common.core.constant.Constants;
import com.abc.common.core.text.StrFormatter;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 字符串工具类（基于commons-lang3#StringUtils）<br>
 * -> org.apache.commons.lang3.StringUtils是一个独立的功能强大的字符串工具（比spring-core中的StringUtils提供了更多通用的API）<br>
 * <br>
 * ⬇️依赖项⬇️<br>
 * 1.需要引入spring-core：<br>
 * -> org.springframework.util.AntPathMatcher用于模式匹配<br>
 * -> spring-core引入后，可以使用PatternMatchUtils简易版字符串模式匹配工具<br>
 * 2.依赖com.abc.common.core.text.*<br>
 * 3.依赖com.abc.common.core.constant.Constants<br>
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";

    private static final char SEPARATOR = '_';

    /**
     * 是否为http(s)://开头<br>
     * 示例：<br>
     * 1. StringUtils.isHttp(link)<br>
     *
     * @param link 链接
     * @return 结果
     */
    public static boolean isHttp(String link) {
        return containsAny(link, Constants.HTTP, Constants.HTTPS);
    }

    /**
     * 判断给定的集合collection中是否包含数组array （存在）<br>
     * 示例：<br>
     * 1. StringUtils.containsAny(role.getPermissions(), Convert.toStrArray(permission))<br>
     *
     * @param collection 给定的集合
     * @param array      给定的数组
     * @return boolean 结果
     */
    public static boolean containsAny(Collection<String> collection, String... array) {
        if (!isEmpty(collection) && !isEmpty(array)) {
            for (String str : array) {
                if (collection.contains(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is {} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params) {
        if (isEmpty(params) || isNotBlank(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /* ################################################################################ */
    /* ### 去空格(统一使用trim、trimLeading、trimTrailing)                                 */
    /* ### commons-lang3中的strip、stripStart、stripEnd对于null返回的是null                */
    /* ### 1.9开始新增了3个strip相关方法, 这里使用commons-lang3中StringUtils#strip*替代       */
    /* ################################################################################ */

    public static String trim(String str) {
        return (str == null ? "" : strip(str));
    }

    public static String trimLeading(String str) {
        return (str == null ? "" : stripStart(str, null));
    }

    public static String trimTrailing(String str) {
        return (str == null ? "" : stripEnd(str, null));
    }


    /* ######################################################################### */
    /* ### 驼峰下划线互转                                                          */
    /*  StringUtils.toCamelCase("dict_type");  -> dictType                      */
    /*  StringUtils.toCamelCase("Dict_type");  -> dictType                      */
    /*  StringUtils.toCamelCase("Dict_tyPe");  -> dictType                      */
    /*  StringUtils.toUnderScoreCase("dictType");  -> dict_type                 */
    /*  StringUtils.toUnderScoreCase("DictTyPe");  -> dict_type                 */
    /*  StringUtils.toUnderScoreCase("DictType");  -> dict_ty_pe                */
    /* ######################################################################### */

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一个字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
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

    /* ####################################################################################### */
    /* ### 字符串模式匹配                                                                        */
    /* ### 查找指定字符串是否匹配指定字符串列表中的任意一个字符串                                        */
    /* ### API名称兼容PatternMatchUtils#simpleMatch(S,S)、PatternMatchUtils#simpleMatch(S[],S)   */
    /* ####################################################################################### */

    public static boolean simpleMatch(List<String> strList, String str) {
        if (isEmpty(str) || isEmpty(strList)) {
            return false;
        }
        for (String pattern : strList) {
            if (simpleMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean simpleMatch(String[] strArray, String str) {
        if (isEmpty(str) || isEmpty(strArray)) {
            return false;
        }
        for (String pattern : strArray) {
            if (simpleMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置:<br>
     * ? 表示单个字符;<br>
     * * 表示一层路径内的任意字符串，不可跨层级;<br>
     * ** 表示任意层路径;<br>
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     * @return 是否匹配
     */
    public static boolean simpleMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    /* ######################################################################### */
    /* ### 其他: 集合、数组是否为空                                                  */
    /* ######################################################################### */

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }
    public static boolean isNotNull(Object object) {
        return object != null;
    }

    /**
     * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }
}