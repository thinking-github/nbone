package org.nbone.util;

import java.util.UUID;

public class UUIDGenerator {
	
    protected UUIDGenerator() {
    }

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
