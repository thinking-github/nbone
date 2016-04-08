/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Description: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.util.resource;

import java.awt.Dimension;
import java.io.InputStream;

/**
 * 图像操作类
 * 
 */
public class ImageUtil {

    /**
     * 获得图像的尺寸
     * 
     * @param in
     *                图像输入流
     * @return 尺寸
     */
    public static Dimension getDimension(InputStream in) {
        try {
            ImageInfo ii = new ImageInfo();
            ii.setInput(in);
            if (ii.check()) {
                int width = ii.getWidth();
                int height = ii.getHeight();
                if (width > 0 && height > 0) {
                    int format = ii.getFormat();
                    if (format == ImageInfo.FORMAT_GIF
                            || format == ImageInfo.FORMAT_JPEG
                            || format == ImageInfo.FORMAT_PNG
                            || format == ImageInfo.FORMAT_SWF) {
                        return new Dimension(width, height);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 将指定名称的图像文件，转换为指定大小、指定名称的文件
     * 
     * @param oldName
     *                旧图像文件名
     * @param newSummName
     *                新图像文件名
     * @param width
     *                转换后的宽度
     * @param height
     *                转换后的高度
     * @return 转换是否成功 true=转换成功 false=转换失败
     */
   /* public static boolean convert(String oldName, String newSummName,
            int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, 1);
        Object uc = null;
        try {
            URL u = new URL("file", "localhost", oldName);
            uc = u.getContent();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        ImageProducer scaledImageProducer = null;
        int[] pixels = new int[width * height];
        try {
            scaledImageProducer = new FilteredImageSource((ImageProducer) uc,
                    new AreaAveragingScaleFilter(width, height));
            PixelGrabber pg = new PixelGrabber(scaledImageProducer, 0, 0,
                    width, height, pixels, 0, width);
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
        byte[] result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JPEGEncodeParam encParam = JPEGCodec
                .getDefaultJPEGEncodeParam(bufferedImage);
        encParam.setQuality(0.75F, false);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos, encParam);
        try {
            encoder.encode(bufferedImage, encParam);
            baos.flush();
            byte[] encodedImage = baos.toByteArray();
            baos.close();
            OutputStream os = new FileOutputStream(newSummName);
            os.write(encodedImage);
            os.flush();
            os.close();
            os = null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }*/

}