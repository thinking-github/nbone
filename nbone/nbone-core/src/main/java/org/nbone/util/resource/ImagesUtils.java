package org.nbone.util.resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author thinking
 * @Version: 1.0
 * @since 13-3-22 下午12:18
 */
public class ImagesUtils {

    private static final String[] IMAGES_SUFFIXES = {
            "bmp", "jpg", "jpeg", "gif", "png", "tiff"
    };

    /**
     * 是否是图片附件
     *
     * @param filename
     * @return
     */
    public static boolean isImage(String filename) {
        if (filename == null || filename.trim().length() == 0) return false;
        return ArrayUtils.contains(IMAGES_SUFFIXES, FilenameUtils.getExtension(filename).toLowerCase());
    }

}
