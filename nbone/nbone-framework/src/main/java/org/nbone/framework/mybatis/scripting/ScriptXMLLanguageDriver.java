package org.nbone.framework.mybatis.scripting;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;

/**
 * @author chenyicheng1
 * @version 1.0
 * @since 2023/2/16
 */
public class ScriptXMLLanguageDriver extends XMLLanguageDriver {

    private static Configuration configuration = new Configuration();

    private static ScriptXMLLanguageDriver languageDriver = new ScriptXMLLanguageDriver();


    @Override
    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
        XMLScriptBuilder builder = new ScriptXMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }


    /**
     * 使用默认配置信息
     *
     * @param script
     * @param parameterType
     * @return
     */
    public static ScriptDynamicSqlSource createSqlSource(String script, Class<?> parameterType) {
        String xmlScript = new StringBuilder()
                .append("<script>")
                .append(script)
                .append("</script>")
                .toString();
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, xmlScript, parameterType);
        if (sqlSource instanceof ScriptDynamicSqlSource) {
            return (ScriptDynamicSqlSource) sqlSource;
        }

        throw new IllegalStateException("请检查返回ScriptDynamicSqlSource实例!,SqlSource class:" + sqlSource.getClass());
    }


    public static void main(String[] args) {
        createSqlSource("select * from api", Object.class);
    }


}
