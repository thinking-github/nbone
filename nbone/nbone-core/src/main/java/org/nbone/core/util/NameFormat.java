package org.nbone.core.util;

import com.google.common.base.CaseFormat;
import org.nbone.constants.CaseName;

import static com.google.common.base.CaseFormat.*;

/**
 * 字段名称格式转化
 *
 * @author thinking
 * @version 1.0
 * @since 2020-08-06
 */
public class NameFormat {

    /**
     * UPPER_CAMEL  --> LOWER_UNDERSCORE / UPPER_UNDERSCORE / LOWER_CAMEL
     *
     * @param name     驼峰名称
     * @param caseName 转化目标枚举标识
     * @return
     */
    public static String caseFormat(String name, CaseName caseName) {
        String usedName;
        switch (caseName) {
            case LOWER_UNDERSCORE:
                usedName = CaseFormat.UPPER_CAMEL.to(LOWER_UNDERSCORE, name);

                break;

            case UPPER_UNDERSCORE:
                usedName = CaseFormat.UPPER_CAMEL.to(UPPER_UNDERSCORE, name);

                break;

            case LOWER_CAMEL:
                usedName = CaseFormat.UPPER_CAMEL.to(LOWER_CAMEL, name);

                break;

            case LOWER_HYPHEN:
                usedName = CaseFormat.UPPER_CAMEL.to(LOWER_HYPHEN, name);

                break;

            default:
                usedName = name;
                break;
        }

        return usedName;

    }

    /**
     * 驼峰字段转化成其他类型字段名称
     *
     * @param name
     * @param caseFormat 转化目标枚举标识
     * @return
     */
    public static String camelTo(String name, CaseFormat caseFormat) {
        if (caseFormat == null) {
            return name;
        }
        return CaseFormat.UPPER_CAMEL.to(caseFormat, name);

    }


    /**
     * 蛇形字段转化成其他类型字段名称
     *
     * @param name
     * @param caseFormat 转化目标枚举标识
     * @return
     */
    public static String underscoreTo(String name, CaseFormat caseFormat) {
        if (caseFormat == null) {
            return name;
        }
        return CaseFormat.LOWER_UNDERSCORE.to(caseFormat, name);

    }

}
