package org.xiaowu.behappy.screw.util;

import lombok.experimental.UtilityClass;

/**
 * @author xiaowu
 */
@UtilityClass
public class ScrewStrUtils {

    /**
     * eg:
     * 截取第3个/和第一个?之间的字符串
     * "jdbc:mysql://gz-cdbrg-hmaqfkgz.sql.tencentcdb.com:61148/information_schema?useUnicode=true"
     * String originDataBase = ScrewStrUtils.subStr(dataSourceProperty.getUrl(), 3, "/", 1, "?");
     *
     * 截取正数第behindNum个`behindStr`后面,并且在正数第frontNum个`frontStr`之前的内容
     * @param str
     * @param behindNum
     * @param behindStr
     * @return
     */
    public String subStr(String str, Integer behindNum, String behindStr, Integer frontNum, String frontStr) {
        for (int i = 0; i < behindNum; i++) {
            str = str.substring(str.indexOf(behindStr) + 1);
        }
        str = str.substring(0, str.indexOf(frontStr, str.indexOf(frontStr) + frontNum - 1));
        return str;
    }
}
