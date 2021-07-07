package com.lyf.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 *   Redis Lua脚本文件
 */


public class RedisScriptUtil {


    //解析lua
    public static String getScript(String path) {
        StringBuilder stringBuilder = new StringBuilder();

        //类路径读取文件
        InputStream inputStream = RedisScriptUtil.class.getClassLoader().getResourceAsStream(path);

        //()中最后会自动关闭资源
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        System.out.println(getScript("redisLimit.lua"));
    }
}
