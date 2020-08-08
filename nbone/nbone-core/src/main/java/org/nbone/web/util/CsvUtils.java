package org.nbone.web.util;

import org.nbone.constants.CharsetConstant;
import org.nbone.constants.ContentType;
import org.nbone.util.WebIOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/18
 */
public class CsvUtils {

    public final static  String  ENCODING = "encoding";

    public final static  Class<?>[] PARAMETER_TYPES = {Writer.class,CsvPreference.class};
    /**
     * 标签,国家,语言,产品
     * 卿博士,cn,zh,cat
     * 卿博士熟食,cn,zh,cat
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param csvWriterClass  csvWriterClass
     * @param csvFileName 文件名称
     * @param dataList    数据列表
     * @param header      文件标题
     * @param nameMapping 映射数组用于取值
     * @throws IOException
     */
    public static void exportCsv(HttpServletRequest request,
                           HttpServletResponse response,
                           Class<? extends AbstractCsvWriter> csvWriterClass,
                           String csvFileName,
                           List<?> dataList,
                           String[] header,
                           String... nameMapping) throws IOException {
        String encode = request.getParameter("encoding");
        String dataEncoding  = response.getHeader(ENCODING);

        if (encode == null) {
            encode = CharsetConstant.CHARSET_UTF8;
        }

        //Writer writer = WebIOUtils.getWriter(response, ContentType.CSV_UTF8_VALUE,csvFileName);
        OutputStream os = WebIOUtils.getOutputStream(response, ContentType.CSV_UTF8_VALUE, csvFileName);
        //office excel 乱码问题！
        if(dataEncoding != null && CharsetConstant.CHARSET_UTF8_BOM.equals(dataEncoding.toUpperCase())){
            os.write(CharsetConstant.UTF_8_BOM_HEAD);
        }
        OutputStreamWriter writer = new OutputStreamWriter(os, Charset.forName(encode));

        ICsvBeanWriter csvWriter;
        if(csvWriterClass == null){
            csvWriter  = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
        }else {
            Constructor<?> csvWriterConstructor = ClassUtils.getConstructorIfAvailable(csvWriterClass, PARAMETER_TYPES);
            csvWriter = (ICsvBeanWriter) BeanUtils.instantiateClass(csvWriterConstructor,new Object[]{writer,CsvPreference.STANDARD_PREFERENCE});
        }

        csvWriter.writeHeader(header);
        if (nameMapping == null || nameMapping.length == 0) {
            nameMapping = header;
        }
        for (Object item : dataList) {
            csvWriter.write(item, nameMapping);
        }
        csvWriter.close();
    }

    public static void exportCsv(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String csvFileName,
                                 List<?> dataList,
                                 String[] header,
                                 String... nameMapping) throws IOException {

        exportCsv(request,response,null,csvFileName,dataList,header,nameMapping);
    }

}
