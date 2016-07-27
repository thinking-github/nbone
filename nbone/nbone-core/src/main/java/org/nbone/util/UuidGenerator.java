package org.nbone.util;

import java.util.UUID;

public class UuidGenerator {
	
    protected UuidGenerator() {
    }

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
