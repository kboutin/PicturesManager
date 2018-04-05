package com.kboutin.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Speed Tests")
public class FileUtilsTest {

    @Test
    @DisplayName("MD5 Speed")
    public final void testSpeedMD5Hash() {

    	File f = new File(getClass().getClassLoader().getResource("FeuilleDeFiguier.jpg").getFile());
    	assertTrue(f.exists());
    	assertAll("TestFile",
                () -> assertEquals(5000153, f.length()),
                () -> assertTrue(f.getName().endsWith(".jpg"))
        );

        long start = System.currentTimeMillis();
        String md5Hash3 = FileUtils.getMD5Hash(f);
        long duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash3 + " - Duration : " + duration);

        start = System.currentTimeMillis();
        String md5Hash1 = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash1 + " - Duration : " + duration);

        assertEquals(md5Hash1, md5Hash3);

        start = System.currentTimeMillis();
        md5Hash3 = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash3 + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash1 = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash1 + " - Duration : " + duration);
    }
}
