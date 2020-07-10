package com.takovh.testProject.utils;

public class Util {
    public static String getResourceFilePath(String path){
        String p = null;
        try {
            p = Util.class.getResource(path).getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
    public static String getResourceFilePath(){
        return getResourceFilePath("/");
    }
}
