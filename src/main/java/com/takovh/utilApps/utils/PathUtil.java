package com.takovh.utilApps.utils;

public class PathUtil {
    public static String getResourceFilePath(String path){
        String p = null;
        try {
            p = PathUtil.class.getResource(path).getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
    public static String getResourceFilePath(){
        return getResourceFilePath("/");
    }
}
