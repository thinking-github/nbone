package org.nbone.util.resource;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * 图像信息操作类
 * 
 */
public final class ImageInfo {
    /**
     * JPEG格式
     */
    public static final int FORMAT_JPEG = 0;

    /**
     * GIF格式
     */
    public static final int FORMAT_GIF = 1;

    /**
     * PNG格式
     */
    public static final int FORMAT_PNG = 2;

    /**
     * BMP格式
     */
    public static final int FORMAT_BMP = 3;

    /**
     * PCX格式
     */
    public static final int FORMAT_PCX = 4;

    /**
     * IFF格式
     */
    public static final int FORMAT_IFF = 5;

    /**
     * RAS格式
     */
    public static final int FORMAT_RAS = 6;

    /**
     * PBM格式
     */
    public static final int FORMAT_PBM = 7;

    /**
     * PGM格式
     */
    public static final int FORMAT_PGM = 8;

    /**
     * PPM格式
     */
    public static final int FORMAT_PPM = 9;

    /**
     * PSD格式
     */
    public static final int FORMAT_PSD = 10;

    /**
     * SWF格式
     */
    public static final int FORMAT_SWF = 11;

    /**
     * 格式名称数组
     */
    private static final String[] FORMAT_NAMES = { "JPEG", "GIF", "PNG", "BMP",
            "PCX", "IFF", "RAS", "PBM", "PGM", "PPM", "PSD", "SWF" };
    /**
     * MIME类型数组
     */
    private static final String[] MIME_TYPE_STRINGS = { "image/jpeg",
            "image/gif", "image/png", "image/bmp", "image/pcx", "image/iff",
            "image/ras", "image/x-portable-bitmap", "image/x-portable-graymap",
            "image/x-portable-pixmap", "image/psd",
            "application/x-shockwave-flash" };
    /**
     * 宽度
     */
    private int width;

    /**
     * 高度
     */
    private int height;

    /**
     * 单位像素比特数
     */
    private int bitsPerPixel;

    /**
     * 格式
     */
    private int format;

    /**
     * 输入流
     */
    private InputStream in;

    /**
     * 数据输入
     */
    private DataInput din;

    /**
     * 支持集合注释标识
     */
    private boolean collectComments;

    /**
     * 主持集合
     */
    private Vector comments;

    /**
     * 支持图片编号标识
     */
    private boolean determineNumberOfImages;

    /**
     * 图片编号
     */
    private int numberOfImages;

    /**
     * 物理高度
     */
    private int physicalHeightDpi;

    /**
     * 物理宽度
     */
    private int physicalWidthDpi;

    /**
     * 比特缓冲数
     */
    private int bitBuf;

    /**
     * 比特位
     */
    private int bitPos;
    /**
     * 构造函数
     */
    public ImageInfo() {
        collectComments = true;
    }

    /**
     * 增加注释
     * 
     * @param s
     *                注释内容
     */
    private void addComment(String s) {
        if (comments == null) {
            comments = new Vector();
        }
        comments.addElement(s);
    }

    /**
     * 检查是否为图像文件
     * 
     * @return true=是图像文件 false=非图像文件
     */
    public boolean check() {
        format = -1;
        width = -1;
        height = -1;
        bitsPerPixel = -1;
        numberOfImages = 1;
        physicalHeightDpi = -1;
        physicalWidthDpi = -1;
        comments = null;
        try {
            int b1 = read() & 0xff;
            int b2 = read() & 0xff;
            if (b1 == 71 && b2 == 73) {
                return checkGif();
            }
            if (b1 == 137 && b2 == 80) {
                return checkPng();
            }
            if (b1 == 255 && b2 == 216) {
                return checkJpeg();
            }
            if (b1 == 66 && b2 == 77) {
                return checkBmp();
            }
            if (b1 == 10 && b2 < 6) {
                return checkPcx();
            }
            if (b1 == 70 && b2 == 79) {
                return checkIff();
            }
            if (b1 == 89 && b2 == 166) {
                return checkRas();
            }
            if (b1 == 80 && b2 >= 49 && b2 <= 54) {
                return checkPnm(b2 - 48);
            }
            if (b1 == 56 && b2 == 66) {
                return checkPsd();
            }
            if ((b1 == 70 || b1 == 67) && b2 == 87) {
                return checkSwf();
            } else {
                return false;
            }
        } catch (IOException ioexception) {
            return false;
        }
    }

    /**
     * 检查是否为Bmp格式
     * 
     * @return true=是bmp图片 false=不是bmp图片
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkBmp() throws IOException {
        byte[] a = new byte[44];
        if (read(a) != a.length) {
            return false;
        }
        width = getIntLittleEndian(a, 16);
        height = getIntLittleEndian(a, 20);
        if (width < 1 || height < 1) {
            return false;
        }
        bitsPerPixel = getShortLittleEndian(a, 26);
        if (bitsPerPixel != 1 && bitsPerPixel != 4 && bitsPerPixel != 8
                && bitsPerPixel != 16 && bitsPerPixel != 24
                && bitsPerPixel != 32) {
            return false;
        }
        int x = getIntLittleEndian(a, 36);
        if (x > 0) {
            setPhysicalWidthDpi(x);
        }
        int y = getIntLittleEndian(a, 40);
        if (y > 0) {
            setPhysicalHeightDpi(y);
        }
        format = 3;
        return true;
    }

    /**
     * 检查是否为Gif格式
     * 
     * @return true=是gif图片 false=不是gif图片
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkGif() throws IOException {
        byte[] gifMagic87a = { 70, 56, 55, 97 };
        byte[] gifMagic89a = { 70, 56, 57, 97 };
        byte[] a = new byte[11];
        if (read(a) != 11) {
            return false;
        }
        if (!equals(a, 0, gifMagic89a, 0, 4)
                && !equals(a, 0, gifMagic87a, 0, 4)) {
            return false;
        }
        format = 1;
        width = getShortLittleEndian(a, 4);
        height = getShortLittleEndian(a, 6);
        int flags = a[8] & 0xff;
        bitsPerPixel = (flags >> 4 & 7) + 1;
        if (!determineNumberOfImages) {
            return true;
        }
        if ((flags & 0x80) != 0) {
            int tableSize = (1 << (flags & 7) + 1) * 3;
            skip(tableSize);
        }
        numberOfImages = 0;
        int blockType;
        do {
            blockType = read();
            switch (blockType) {
            case 44: {
                if (read(a, 0, 9) != 9) {
                    return false;
                }
                flags = a[8] & 0xff;
                int localBitsPerPixel = (flags & 7) + 1;
                if (localBitsPerPixel > bitsPerPixel) {
                    bitsPerPixel = localBitsPerPixel;
                }
                if ((flags & 0x80) != 0) {
                    skip((1 << localBitsPerPixel) * 3);
                }
                skip(1);
                int n;
                do {
                    n = read();
                    if (n > 0) {
                        skip(n);
                    } else if (n == -1) {
                        return false;
                    }
                } while (n > 0);
                numberOfImages++;
                break;
            }
            case 33: {
                int extensionType = read();
                int n;
                if (collectComments && extensionType == 254) {
                    StringBuffer sb = new StringBuffer();
                    // int n;
                    do {
                        n = read();
                        if (n == -1) {
                            return false;
                        }
                        if (n > 0) {
                            for (int i = 0; i < n; i++) {
                                int ch = read();
                                if (ch == -1) {
                                    return false;
                                }
                                sb.append((char) ch);
                            }
                        }
                    } while (n > 0);
                } else {
                    do {
                        n = read();
                        if (n > 0) {
                            skip(n);
                        } else if (n == -1) {
                            return false;
                        }
                    } while (n > 0);
                }
                break;
            }
                // '"'
            case 34:
                // '#'
            case 35:
                // '$'
            case 36:
                // '%'
            case 37:
                // '&'
            case 38:
                // '\''
            case 39:
                // '('
            case 40:
                // ')'
            case 41:
                // '*'
            case 42:
                // '+'
            case 43:
                // '-'
            case 45:
                // '.'
            case 46:
                // '/'
            case 47:
                // '0'
            case 48:
                // '1'
            case 49:
                // '2'
            case 50:
                // '3'
            case 51:
                // '4'
            case 52:
                // '5'
            case 53:
                // '6'
            case 54:
                // '7'
            case 55:
                // '8'
            case 56:
                // '9'
            case 57:
                // ':'
            case 58:
            default: {
                return false;
            }
                // ';'
            case 59:
                break;
            }
        } while (blockType != 59);
        return true;
    }

    /**
     * 检查是否为Iff格式
     * 
     * @return true=是Iff图片 false=不是Iff图片
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkIff() throws IOException {
        byte[] a = new byte[10];
        if (read(a, 0, 10) != 10) {
            return false;
        }
        byte[] iffRm = { 82, 77 };
        if (!equals(a, 0, iffRm, 0, 2)) {
            return false;
        }
        int type = getIntBigEndian(a, 6);
        if (type != 0x494c424d && type != 0x50424d20) {
            return false;
        }
        do {
            if (read(a, 0, 8) != 8) {
                return false;
            }
            int chunkId = getIntBigEndian(a, 0);
            int size = getIntBigEndian(a, 4);
            if ((size & 1) == 1) {
                size++;
            }
            if (chunkId == 0x424d4844) {
                if (read(a, 0, 9) != 9) {
                    return false;
                } else {
                    format = 5;
                    width = getShortBigEndian(a, 0);
                    height = getShortBigEndian(a, 2);
                    bitsPerPixel = a[8] & 0xff;
                    return width > 0 && height > 0 && bitsPerPixel > 0
                            && bitsPerPixel < 33;
                }
            }
            skip(size);
        } while (true);
    }

    /**
     * 检查是否为Jpeg格式
     * 
     * @return boolean
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkJpeg() throws IOException {
        byte[] data = new byte[12];
        do {
            if (read(data, 0, 4) != 4) {
                return false;
            }
            int marker = getShortBigEndian(data, 0);
            int size = getShortBigEndian(data, 2);
            if ((marker & 0xff00) != 65280) {
                return false;
            }
            if (marker == 65504) {
                if (size < 14) {
                    return false;
                }
                if (read(data, 0, 12) != 12) {
                    return false;
                }
                byte[] app0Id = { 74, 70, 73, 70, 0 };
                if (equals(app0Id, 0, data, 0, 5)) {
                    if (data[7] == 1) {
                        setPhysicalWidthDpi(getShortBigEndian(data, 8));
                        setPhysicalHeightDpi(getShortBigEndian(data, 10));
                    } else if (data[7] == 2) {
                        int x = getShortBigEndian(data, 8);
                        int y = getShortBigEndian(data, 10);
                        setPhysicalWidthDpi((int) (x * 2.54F));
                        setPhysicalHeightDpi((int) (y * 2.54F));
                    }
                }
                skip(size - 14);
            } else if (collectComments && size > 2 && marker == 65534) {
                byte[] chars = new byte[size -= 2];
                if (read(chars, 0, size) != size) {
                    return false;
                }
                String comment = new String(chars, "iso-8859-1");
                comment = comment.trim();
                addComment(comment);
            } else {
                if (marker >= 65472 && marker <= 65487 && marker != 65476
                        && marker != 65480) {
                    if (read(data, 0, 6) != 6) {
                        return false;
                    } else {
                        format = 0;
                        bitsPerPixel = (data[0] & 0xff) * (data[5] & 0xff);
                        width = getShortBigEndian(data, 3);
                        height = getShortBigEndian(data, 1);
                        return true;
                    }
                }
                skip(size - 2);
            }
        } while (true);
    }

    /**
     * 检查是否为Pcx格式
     * 
     * @return boolean
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkPcx() throws IOException {
        byte[] a = new byte[64];
        if (read(a) != a.length) {
            return false;
        }
        if (a[0] != 1) {
            return false;
        }
        int x1 = getShortLittleEndian(a, 2);
        int y1 = getShortLittleEndian(a, 4);
        int x2 = getShortLittleEndian(a, 6);
        int y2 = getShortLittleEndian(a, 8);
        if (x1 < 0 || x2 < x1 || y1 < 0 || y2 < y1) {
            return false;
        }
        width = (x2 - x1) + 1;
        height = (y2 - y1) + 1;
        int bits = a[1];
        int planes = a[63];
        if (planes == 1 && (bits == 1 || bits == 2 || bits == 4 || bits == 8)) {
            bitsPerPixel = bits;
        } else if (planes == 3 && bits == 8) {
            bitsPerPixel = 24;
        } else {
            return false;
        }
        setPhysicalWidthDpi(getShortLittleEndian(a, 10));
        setPhysicalHeightDpi(getShortLittleEndian(a, 10));
        format = 4;
        return true;
    }

    /**
     * 检查是否为Png格式
     * 
     * @return true=是png图片 false=不是png图片
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkPng() throws IOException {
        byte[] pngMagic = { 78, 71, 13, 10, 26, 10 };
        byte[] a = new byte[24];
        if (read(a) != 24) {
            return false;
        }
        if (!equals(a, 0, pngMagic, 0, 6)) {
            return false;
        }
        format = 2;
        width = getIntBigEndian(a, 14);
        height = getIntBigEndian(a, 18);
        bitsPerPixel = a[22] & 0xff;
        int colorType = a[23] & 0xff;
        if (colorType == 2 || colorType == 6) {
            bitsPerPixel *= 3;
        }
        return true;
    }

    /**
     * 检查是否为Pnm格式
     * 
     * @param id
     *                id编码
     * @return true=是pnm图片 false=不是pnm图片
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkPnm(int id) throws IOException {
        if (id < 1 || id > 6) {
            return false;
        }
        int[] pnmFormats = { 7, 8, 9 };
        format = pnmFormats[(id - 1) % 3];
        boolean hasPixelResolution = false;
        String s;
        do {
            do {
                s = readLine();
                if (s != null) {
                    s = s.trim();
                }
            } while (s == null || s.length() < 1);
            if (s.charAt(0) == '#') {
                if (collectComments && s.length() > 1) {
                    addComment(s.substring(1));
                }
                continue;
            }
            if (hasPixelResolution) {
                break;
            }
            int spaceIndex = s.indexOf(' ');
            if (spaceIndex == -1) {
                return false;
            }
            String widthString = s.substring(0, spaceIndex);
            spaceIndex = s.lastIndexOf(' ');
            if (spaceIndex == -1) {
                return false;
            }
            String heightString = s.substring(spaceIndex + 1);
            try {
                width = Integer.parseInt(widthString);
                height = Integer.parseInt(heightString);
            } catch (NumberFormatException numberformatexception1) {
                return false;
            }
            if (width < 1 || height < 1) {
                return false;
            }
            if (format == 7) {
                bitsPerPixel = 1;
                return true;
            }
            hasPixelResolution = true;
        } while (true);
        int maxSample;
        try {
            maxSample = Integer.parseInt(s);
        } catch (NumberFormatException numberformatexception) {
            return false;
        }
        if (maxSample < 0) {
            return false;
        }
        for (int i = 0; i < 25; i++) {
            if (maxSample < 1 << i + 1) {
                bitsPerPixel = i + 1;
                if (format == 9) {
                    bitsPerPixel *= 3;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否为Psd格式
     * 
     * @return true=是psd图片 false=不是psd图片
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkPsd() throws IOException {
        byte[] a = new byte[24];
        if (read(a) != a.length) {
            return false;
        }
        byte[] psdMagic = { 80, 83 };
        if (!equals(a, 0, psdMagic, 0, 2)) {
            return false;
        } else {
            format = 10;
            width = getIntBigEndian(a, 16);
            height = getIntBigEndian(a, 12);
            int channels = getShortBigEndian(a, 10);
            int depth = getShortBigEndian(a, 20);
            bitsPerPixel = channels * depth;
            return width > 0 && height > 0 && bitsPerPixel > 0
                    && bitsPerPixel <= 64;
        }
    }

    /**
     * 检查是否为Ras格式
     * 
     * @return true=是ras格式 false=不是ras格式
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkRas() throws IOException {
        byte[] a = new byte[14];
        if (read(a) != a.length) {
            return false;
        }
        byte[] rasMagic = { 106, -107 };
        if (!equals(a, 0, rasMagic, 0, 2)) {
            return false;
        } else {
            format = 6;
            width = getIntBigEndian(a, 2);
            height = getIntBigEndian(a, 6);
            bitsPerPixel = getIntBigEndian(a, 10);
            return width > 0 && height > 0 && bitsPerPixel > 0
                    && bitsPerPixel <= 24;
        }
    }

    /**
     * 检查是否为Swf格式
     * 
     * @return true=是swf格式 false=不是swf格式
     * @throws IOException
     *                 输入输出异常
     */
    private boolean checkSwf() throws IOException {
        byte[] a = new byte[6];
        if (read(a) != a.length) {
            return false;
        }
        format = 11;
        int bitSize = (int) readUBits(5);
        int minX = readSBits(bitSize);
        int maxX = readSBits(bitSize);
        int minY = readSBits(bitSize);
        int maxY = readSBits(bitSize);
        width = maxX / 20;
        height = maxY / 20;
        if (width < 0) {
            width = -width;
        }
        if (height < 0) {
            height = -height;
        }
        setPhysicalWidthDpi(72);
        setPhysicalHeightDpi(72);
        return width > 0 && height > 0;
    }

    /**
     * 确认冗余
     * 
     * @param args
     *                参数
     * @return true=冗余 false=非冗余
     */
    private static boolean determineVerbosity(String[] args) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if ("-c".equals(args[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断相等
     * 
     * @param a1
     *                源字节数组
     * @param offs1
     *                偏移量1
     * @param a2
     *                目标字节数组
     * @param offs2
     *                偏移量2
     * @param num
     *                个数
     * @return true=相等 false=不相等
     */
    private boolean equals(byte[] a1, int offs1, byte[] a2, int offs2, int num) {
        while (num-- > 0) {
            if (a1[offs1++] != a2[offs2++]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取单位像素的比特数
     * 
     * @return 比特数
     */
    public int getBitsPerPixel() {
        return bitsPerPixel;
    }

    /**
     * 返回注释
     * 
     * @param index
     *                注释索引
     * @return 注释
     */
    public String getComment(int index) {
        if (comments == null || index < 0 || index >= comments.size()) {
            throw new IllegalArgumentException("Not a valid comment index: "
                    + index);
        } else {
            return (String) comments.elementAt(index);
        }
    }

    /**
     * 返回格式
     * 
     * @return 格式类型
     */
    public int getFormat() {
        return format;
    }

    /**
     * 返回格式名称
     * 
     * @return 格式名称
     */
    public String getFormatName() {
        if (format >= 0 && format < FORMAT_NAMES.length) {
            return FORMAT_NAMES[format];
        } else {
            return "?";
        }
    }

    /**
     * 返回高度
     * 
     * @return 高度
     */
    public int getHeight() {
        return height;
    }

    /**
     * 获取整数大端
     * 
     * @param a
     *                图片字节数组
     * @param offs
     *                偏移量
     * @return 大端索引
     */
    private int getIntBigEndian(byte[] a, int offs) {
        return (a[offs] & 0xff) << 24 | (a[offs + 1] & 0xff) << 16
                | (a[offs + 2] & 0xff) << 8 | a[offs + 3] & 0xff;
    }

    /**
     * 获取整数小端
     * 
     * @param a
     *                图片字节数组
     * @param offs
     *                偏移量
     * @return 小端索引
     */
    private int getIntLittleEndian(byte[] a, int offs) {
        return (a[offs + 3] & 0xff) << 24 | (a[offs + 2] & 0xff) << 16
                | (a[offs + 1] & 0xff) << 8 | a[offs] & 0xff;
    }

    /**
     * 返回Mime类型
     * 
     * @return Mime类型
     */
    public String getMimeType() {
        if (format >= 0 && format < MIME_TYPE_STRINGS.length) {
            return MIME_TYPE_STRINGS[format];
        } else {
            return null;
        }
    }

    /**
     * 返回注释总数
     * 
     * @return 注释总数
     */
    public int getNumberOfComments() {
        if (comments == null) {
            return 0;
        } else {
            return comments.size();
        }
    }

    /**
     * 返回图片数
     * 
     * @return 图片数
     */
    public int getNumberOfImages() {
        return numberOfImages;
    }

    /**
     * 返回实际点每英寸高度
     * 
     * @return 实际点每英寸高度
     */
    public int getPhysicalHeightDpi() {
        return physicalHeightDpi;
    }

    /**
     * 返回实际英寸高度
     * 
     * @return 实际英寸高度
     */
    public float getPhysicalHeightInch() {
        int h = getHeight();
        int ph = getPhysicalHeightDpi();
        if (h > 0 && ph > 0) {
            return (float) h / (float) ph;
        } else {
            return -1F;
        }
    }

    /**
     * 返回实际点每英寸宽度
     * 
     * @return 实际点每英寸宽度
     */
    public int getPhysicalWidthDpi() {
        return physicalWidthDpi;
    }

    /**
     * 返回实际英寸宽度
     * 
     * @return 实际英寸宽度
     */
    public float getPhysicalWidthInch() {
        int w = getWidth();
        int pw = getPhysicalWidthDpi();
        if (w > 0 && pw > 0) {
            return (float) w / (float) pw;
        } else {
            return -1F;
        }
    }

    /**
     * 获取短整数大端
     * 
     * @param a
     *                图片字节数组
     * @param offs
     *                偏移量
     * @return 短整数大端
     */
    private int getShortBigEndian(byte[] a, int offs) {
        return (a[offs] & 0xff) << 8 | a[offs + 1] & 0xff;
    }

    /**
     * 获取短整数小端
     * 
     * @param a
     *                图片字节数组
     * @param offs
     *                偏移量
     * @return 短整数小端
     */
    private int getShortLittleEndian(byte[] a, int offs) {
        return a[offs] & 0xff | (a[offs + 1] & 0xff) << 8;
    }

    /**
     * 返回宽度
     * 
     * @return 宽度
     */
    public int getWidth() {
        return width;
    }

    /**
     * 打印方法
     * 
     * @param sourceName
     *                源名称
     * @param ii
     *                图片对象
     * @param verbose
     *                是否详细打印
     */
    private static void print(String sourceName, ImageInfo ii, boolean verbose) {
        if (verbose) {
            printVerbose(sourceName, ii);
        } else {
            printCompact(sourceName, ii);
        }
    }

    /**
     * 简洁方法
     * 
     * @param sourceName
     *                源名称
     * @param imageInfo
     *                图片对象
     */
    private static void printCompact(String sourceName, ImageInfo imageInfo) {
        System.out.println(imageInfo.getFormatName() + ";"
                + imageInfo.getMimeType() + ";" + imageInfo.getWidth() + ";"
                + imageInfo.getHeight() + ";" + imageInfo.getBitsPerPixel()
                + ";" + imageInfo.getNumberOfImages() + ";"
                + imageInfo.getPhysicalWidthDpi() + ";"
                + imageInfo.getPhysicalHeightDpi() + ";"
                + imageInfo.getPhysicalWidthInch() + ";"
                + imageInfo.getPhysicalHeightInch());
    }

    /**
     * 行打印
     * 
     * @param indentLevels
     *                缩进等级
     * @param text
     *                文本
     * @param value
     *                值
     * @param minValidValue
     *                最小有效值
     */
    private static void printLine(int indentLevels, String text, float value,
            float minValidValue) {
        if (value < minValidValue) {
            return;
        } else {
            printLine(indentLevels, text, Float.toString(value));
            return;
        }
    }

    /**
     * 行打印
     * 
     * @param indentLevels
     *                缩进等级
     * @param text
     *                文本
     * @param value
     *                值
     * @param minValidValue
     *                最小有效值
     */
    private static void printLine(int indentLevels, String text, int value,
            int minValidValue) {
        if (value >= minValidValue) {
            printLine(indentLevels, text, Integer.toString(value));
        }
    }

    /**
     * 行打印
     * 
     * @param indentLevels
     *                缩进等级
     * @param text
     *                文本
     * @param value
     *                值
     */
    private static void printLine(int indentLevels, String text, String value) {
        if (value == null || value.length() == 0) {
            return;
        }
        while (indentLevels-- > 0) {
            System.out.print("\t");
        }
        if (text != null && text.length() > 0) {
            System.out.print(text);
            System.out.print(" ");
        }
        System.out.println(value);
    }

    /**
     * 详细打印
     * 
     * @param sourceName
     *                源名称
     * @param ii
     *                图片对象
     */
    private static void printVerbose(String sourceName, ImageInfo ii) {
        printLine(0, null, sourceName);
        printLine(1, "File format: ", ii.getFormatName());
        printLine(1, "MIME type: ", ii.getMimeType());
        printLine(1, "Width (pixels): ", ii.getWidth(), 1);
        printLine(1, "Height (pixels): ", ii.getHeight(), 1);
        printLine(1, "Bits per pixel: ", ii.getBitsPerPixel(), 1);
        printLine(1, "Number of images: ", ii.getNumberOfImages(), 1);
        printLine(1, "Physical width (dpi): ", ii.getPhysicalWidthDpi(), 1);
        printLine(1, "Physical height (dpi): ", ii.getPhysicalHeightDpi(), 1);
        printLine(1, "Physical width (inches): ", ii.getPhysicalWidthInch(),
                1.0F);
        printLine(1, "Physical height (inches): ", ii.getPhysicalHeightInch(),
                1.0F);
        int numComments = ii.getNumberOfComments();
        printLine(1, "Number of textual comments: ", numComments, 1);
        if (numComments > 0) {
            for (int i = 0; i < numComments; i++) {
                printLine(2, null, ii.getComment(i));
            }
        }
    }

    /**
     * 读取UBits
     * 
     * @param numBits
     *                位数
     * @return 读取的UBits
     * @throws IOException
     *                 输入输出异常
     */
    public long readUBits(int numBits) throws IOException {
        if (numBits == 0) {
            return 0L;
        }
        int bitsLeft = numBits;
        long result = 0L;
        if (bitPos == 0) {
            if (in != null) {
                bitBuf = in.read();
            } else {
                bitBuf = din.readByte();
            }
            bitPos = 8;
        }
        do {
            int shift = bitsLeft - bitPos;
            if (shift > 0) {
                result |= bitBuf << shift;
                bitsLeft -= bitPos;
                if (in != null) {
                    bitBuf = in.read();
                } else {
                    bitBuf = din.readByte();
                }
                bitPos = 8;
            } else {
                result |= bitBuf >> -shift;
                bitPos -= bitsLeft;
                bitBuf &= 255 >> 8 - bitPos;
                return result;
            }
        } while (true);
    }

    /**
     * 读取SBits
     * 
     * @param numBits
     *                位数
     * @return 读取的SBits
     * @throws IOException
     *                 输入输出异常
     */
    private int readSBits(int numBits) throws IOException {
        long uBits = readUBits(numBits);
        if ((uBits & 1L << numBits - 1) != 0L) {
            uBits |= -1L << numBits;
        }
        return (int) uBits;
    }

    /**
     * 同步Bits方法
     * 
     */
    public void synchBits() {
        bitBuf = 0;
        bitPos = 0;
    }

    /**
     * 读取行
     * 
     * @param firstChar
     *                首字符
     * @return 读取行内容
     * @throws IOException
     *                 输入输出异常
     */
    private String readLine(int firstChar) throws IOException {
        StringBuffer result = new StringBuffer();
        result.append((char) firstChar);
        return readLine(result);
    }

    /**
     * 运行
     * 
     * @param sourceName
     *                源名称
     * @param in
     *                输入流
     * @param imageInfo
     *                图片对象
     * @param verbose
     *                是否运行详细信息
     */
    private static void run(String sourceName, InputStream in,
            ImageInfo imageInfo, boolean verbose) {
        imageInfo.setInput(in);
        imageInfo.setDetermineImageNumber(true);
        imageInfo.setCollectComments(verbose);
        if (imageInfo.check()) {
            print(sourceName, imageInfo, verbose);
        }
    }

    /**
     * 设置是否支持集合注释
     * 
     * @param newValue
     *                true=支持 false=不支持
     */
    public void setCollectComments(boolean newValue) {
        collectComments = newValue;
    }

    /**
     * 设置是否支持图片编号
     * 
     * @param newValue
     *                true=支持 false=不支持
     */
    public void setDetermineImageNumber(boolean newValue) {
        determineNumberOfImages = newValue;
    }

    /**
     * 设置数据输入源
     * 
     * @param dataInput
     *                数据输入源
     */
    public void setInput(DataInput dataInput) {
        din = dataInput;
        in = null;
    }

    /**
     * 设置输入流
     * 
     * @param inputStream
     *                输入流
     */
    public void setInput(InputStream inputStream) {
        in = inputStream;
        din = null;
    }

    /**
     * 设置物理高度
     * 
     * @param newValue
     *                物理高度
     */
    private void setPhysicalHeightDpi(int newValue) {
        physicalWidthDpi = newValue;
    }

    /**
     * 设置物理高度
     * 
     * @param newValue
     *                物理高度
     */
    private void setPhysicalWidthDpi(int newValue) {
        physicalHeightDpi = newValue;
    }

    /**
     * 跳过
     * 
     * @param num
     *                跳过数
     * @throws IOException
     *                 输入输出异常
     */
    private void skip(int num) throws IOException {
        if (in != null) {
            in.skip(num);
        } else {
            din.skipBytes(num);
        }
    }

    /**
     * 读取方法
     * 
     * @throws IOException
     *                 输入输出异常
     * @return int
     */
    private int read() throws IOException {
        if (in != null) {
            return in.read();
        } else {
            return din.readByte();
        }
    }

    /**
     * 读取方法
     * 
     * @param a
     *                字节数组
     * @return 读取字节长度
     * @throws IOException
     *                 输入输出异常
     */
    private int read(byte[] a) throws IOException {
        if (in != null) {
            return in.read(a);
        } else {
            din.readFully(a);
            return a.length;
        }
    }

    /**
     * 读取方法
     * 
     * @param a
     *                字节数组
     * @param offset
     *                偏移量
     * @param num
     *                数量
     * @return 读取字节长度
     * @throws IOException
     *                 输入输出异常
     */
    private int read(byte[] a, int offset, int num) throws IOException {
        if (in != null) {
            return in.read(a, offset, num);
        } else {
            din.readFully(a, offset, num);
            return num;
        }
    }

    /**
     * 读取行
     * 
     * @return 读取行
     * @throws IOException
     *                 输入输出异常
     */
    private String readLine() throws IOException {
        return readLine(new StringBuffer());
    }

    /**
     * 读取行
     * 
     * @param sb
     *                字符缓冲
     * @return 读取行
     * @throws IOException
     *                 输入输出异常
     */
    private String readLine(StringBuffer sb) throws IOException {
        boolean finished;
        do {
            int value = read();
            finished = value == -1 || value == 10;
            if (!finished) {
                sb.append((char) value);
            }
        } while (!finished);
        return sb.toString();
    }

}