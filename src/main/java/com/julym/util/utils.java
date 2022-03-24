package com.julym.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class utils {

    public static String getJavaRunPath() {

        /*
         * 该方法也有以下几种实现原理:
         * String result = Class.class.getClass().getResource("/").getPath();
         * String result = System.getProperty("user.dir");
         */
        // 利用 new File()相对路径原理
        return new File("").getAbsolutePath() + File.separator;
    }

    public static void generateConfig(String filePath){
        byte[] data;
        try {
            data = Objects.requireNonNull(utils.class.getClassLoader().getResourceAsStream("config.json")).readAllBytes();
            File _JsonConfig = new File(filePath);
            if(!_JsonConfig.createNewFile()) System.out.println("Create config.json file failed");
            FileOutputStream fos = new FileOutputStream(filePath);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(new String(data, StandardCharsets.UTF_8));
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
