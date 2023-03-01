package org.nbone.framework.mybatis.scripting;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;


/**
 * @author chenyicheng1
 * @version 1.0
 * @since 2023/2/16
 */
public class ScriptXMLScriptBuilder extends XMLScriptBuilder {

    private final XNode context;
    private final Class<?> parameterType;

    public ScriptXMLScriptBuilder(Configuration configuration, XNode context) {
        this(configuration, context, null);
    }

    public ScriptXMLScriptBuilder(Configuration configuration, XNode context,  Class<?> parameterType) {
        super(configuration, context, parameterType);
        this.context = context;
        this.parameterType = parameterType;
    }


    @Override
    public SqlSource parseScriptNode() {
        MixedSqlNode rootSqlNode = parseDynamicTags(context);
        SqlSource sqlSource = new ScriptDynamicSqlSource(configuration, rootSqlNode);
        return sqlSource;
    }

}
