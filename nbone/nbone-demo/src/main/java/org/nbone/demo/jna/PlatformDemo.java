package org.nbone.demo.jna;

import com.sun.jna.Platform;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/3/20
 * @see com.sun.jna.Platform
 */
public class PlatformDemo {

    public static void main(String[] args) {


        System.out.println(Platform.isWindows());
        System.out.println(Platform.isAIX());
        System.out.println(Platform.isLinux());
        System.out.println(Platform.isMac());
        System.out.println(Platform.isSolaris());
    }
}
