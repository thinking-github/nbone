package org.nbone.demo.jodd;

import jodd.io.StreamUtil;
import jodd.io.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by chenyicheng on 2019/3/20.
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2019/3/20
 */
public class ZipUtilDemo {

    public static void main(String[] args) {



    }

    /**
     *
     * @param os
     * @param zipDir
     */
    public static  void  download(OutputStream os,String zipDir) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        ZipUtil.addToZip(zos, new File(zipDir), null, null, true);
        StreamUtil.close(zos);
    }


}
