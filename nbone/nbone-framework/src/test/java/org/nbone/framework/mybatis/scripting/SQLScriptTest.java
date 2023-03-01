package org.nbone.framework.mybatis.scripting;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.parsing.PropertyParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * @author chenyicheng1
 * @version 1.0
 * @since 2023/2/16
 */
public class SQLScriptTest {

    @Test
    public void sqlTest() {

        StringBuilder script = new StringBuilder(" select data_date,doctor_id,add_friend_num from  ads.ads_doctor_rhythm_d ");
        script.append(" <where>")
                .append("<if test=\"data_date!=null and data_date != ''\">")
                .append(" and data_date =${data_date}</if>")
                .append(" <if test=\"doctor_id!=null\"> ")
                .append(" and doctor_id in ")
                .append(" <foreach collection=\"doctor_id\" open=\"(\" close=\")\" separator=\",\" item=\"type\">")
                .append(" #{type} ")
                .append(" </foreach></if> ")
                .append(" </where> order by doctor_id desc");
        Map<String, Object> params = new HashMap<>();
        params.put("doctor_id", Arrays.asList("210000183470146", "210000003030151", "210000227920110"));
        params.put("data_date", "2021-07-21");

        Properties properties = new Properties();
        properties.putAll(params);

        String script1 = PropertyParser.parse(script.toString(), properties);

        System.out.println(script1);
    }

    @Test
    public void dynamicSqlTest() {

        StringBuilder script = new StringBuilder(" select data_date,doctor_id,add_friend_num from  ads.ads_doctor_rhythm_d ");
        script.append(" <where>")
                .append("<if test=\"data_date!=null and data_date != ''\">")
                .append(" and data_date =${data_date}</if>")
                .append(" <if test=\"doctor_id!=null\"> ")
                .append(" and doctor_id in ")
                .append(" <foreach collection=\"doctor_id\" open=\"(\" close=\")\" separator=\",\" item=\"type\">")
                .append(" #{type} ")
                .append(" </foreach></if> ")
                .append(" </where> order by doctor_id desc");
        Map<String, Object> params = new HashMap<>();
        params.put("doctor_id", Arrays.asList("210000183470146", "210000003030151", "210000227920110"));
        params.put("data_date", "2021-07-21");

        System.out.println(script.charAt(152));
        System.out.println(script.charAt(153));
        System.out.println(script.charAt(154));
        System.out.println(script.charAt(155));

        ScriptDynamicSqlSource sqlSource = ScriptXMLLanguageDriver.createSqlSource(script.toString(), Map.class);
        BoundSql namedSQL = sqlSource.getNamedSql(params);
        BoundSql mybatisSQL = sqlSource.getBoundSql(params);
        System.out.println(namedSQL);
        System.out.println(mybatisSQL);
    }
    @Test
    public void dynamicSqlTest1() {
        
        Map<String,Object> param = new HashMap<>();
        param.put("jd_pin",123);

        System.out.println("param :" + param);
        
        String sql ="select phone from odm.odm_com_pd_topic_auto_pdtymtime_query_log_i_d where jd_pin =:{jd_pin }";
        System.out.println(parse(sql,param));
        
    }

   static String parse(String sql, Map<String, Object> param) {
        String tempSql = sql;
        for (Map.Entry<String, Object> item : param.entrySet()) {
            tempSql = tempSql.replace("{", "").replace("}", "").replace(":" + item.getKey() + "",
                    Matcher.quoteReplacement(String.valueOf(item.getValue())));
        }
        return tempSql;
    }
}
