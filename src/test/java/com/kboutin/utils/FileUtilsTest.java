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
    @DisplayName("getFileMD5 Speed")
    public final void testSpeedGetFileMD5() {

    	File f = new File(getClass().getClassLoader().getResource("FeuilleDeFiguier.jpg").getFile());
    	assertTrue(f.exists());
    	assertAll("TestFile",
                () -> assertEquals(5000153, f.length()),
                () -> assertTrue(f.getName().endsWith(".jpg"))
        );

        long start = System.currentTimeMillis();
        String md5Hash = FileUtils.getFileMD5(f);
        long duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getFileMD5(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileMD5 - Hash : " + md5Hash + " - Duration : " + duration);
    }

    /*@Test
    @DisplayName("getMD5Hash Speed")
    public final void testSpeedGetMD5Hash() {

        File f = new File(getClass().getClassLoader().getResource("FeuilleDeFiguier.jpg").getFile());
        assertTrue(f.exists());
        assertAll("TestFile",
                () -> assertEquals(5000153, f.length()),
                () -> assertTrue(f.getName().endsWith(".jpg"))
        );

        long start = System.currentTimeMillis();
        String md5Hash = FileUtils.getMD5Hash(f);
        long duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        md5Hash = FileUtils.getMD5Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getMD5Hash - Hash : " + md5Hash + " - Duration : " + duration);
    }*/

    @Test
    @DisplayName("getFileSHA1 Speed")
    public final void testSpeedGetFileSHA1() {

        File f = new File(getClass().getClassLoader().getResource("FeuilleDeFiguier.jpg").getFile());
        assertTrue(f.exists());
        assertAll("TestFile",
                () -> assertEquals(5000153, f.length()),
                () -> assertTrue(f.getName().endsWith(".jpg"))
        );

        long start = System.currentTimeMillis();
        String sha1Hash = FileUtils.getFileSHA1(f);
        long duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getFileSHA1(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getFileSHA1(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getFileSHA1(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getFileSHA1(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getFileSHA1(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getFileSHA1(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getFileSHA1(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getFileSHA1(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getFileSHA1(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getFileSHA1 - Hash : " + sha1Hash + " - Duration : " + duration);
    }

    /*@Test
    @DisplayName("getSHA1Hash Speed")
    public final void testSpeedGetSHA1Hash() {

        File f = new File(getClass().getClassLoader().getResource("FeuilleDeFiguier.jpg").getFile());
        assertTrue(f.exists());
        assertAll("TestFile",
                () -> assertEquals(5000153, f.length()),
                () -> assertTrue(f.getName().endsWith(".jpg"))
        );

        long start = System.currentTimeMillis();
        String sha1Hash = FileUtils.getSHA1Hash(f);
        long duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getSHA1Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getSHA1Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getSHA1Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getSHA1Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getSHA1Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getSHA1Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getSHA1Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getSHA1Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);

        start = System.currentTimeMillis();
        sha1Hash = FileUtils.getSHA1Hash(f);
        duration = System.currentTimeMillis() - start;
        System.out.println("getSHA1Hash - Hash : " + sha1Hash + " - Duration : " + duration);
    }*/
}
