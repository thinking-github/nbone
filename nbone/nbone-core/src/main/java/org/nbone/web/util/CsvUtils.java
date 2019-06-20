package org.nbone.web.util;

import org.nbone.constants.CharsetConstant;
import org.nbone.constants.ContentType;
import org.nbone.util.WebIOUtils;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/18
 */
public class CsvUtils {
    /**
     * 标签,国家,语言,产品
     * 卿博士,cn,zh,cat
     * 卿博士熟食,cn,zh,cat
     *
     * @param request
     * @param response
     * @param csvFileName 文件名称
     * @param dataList    数据列表
     * @param header      文件标题
     * @param nameMapping 映射数组用于取值
     * @throws IOException
     */
    public static void exportCsv(HttpServletRequest request,
                           HttpServletResponse response,
                           String csvFileName,
                           List<?> dataList,
                           String[] header,
                           String... nameMapping) throws IOException {
        String encode = request.getParameter("encoding");
        if (encode == null) {
            encode = CharsetConstant.CHARSET_UTF8;
        }
        //Writer writer = WebIOUtils.getWriter(response, ContentType.CSV_UTF8_VALUE,csvFileName);
        OutputStream os = WebIOUtils.getOutputStream(response, ContentType.CSV_UTF8_VALUE, csvFileName);
        OutputStreamWriter writer = new OutputStreamWriter(os, Charset.forName(encode));

        ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
        csvWriter.writeHeader(header);
        if (nameMapping == null || nameMapping.length == 0) {
            nameMapping = header;
        }
        for (Object item : dataList) {
            csvWriter.write(item, nameMapping);
        }
        csvWriter.close();
    }

}
