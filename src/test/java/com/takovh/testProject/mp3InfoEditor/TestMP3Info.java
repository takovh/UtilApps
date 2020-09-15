package com.takovh.testProject.mp3InfoEditor;

import com.takovh.testProject.utils.PathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestMP3Info {
    public static void main(String[] args) {
        //文件名不能有空格
        String pathname = PathUtil.getResourceFilePath(
                "/mp3InfoEditor/Breakerz-EverlastingLuv.mp3");
        File MP3FILE = new File(pathname);
        try {
            if(!MP3FILE.exists()) throw new FileNotFoundException("mp3不存在");
            MP3Info mp3Info = new MP3Info(MP3FILE,"GB2312");//ANSI、UTF-8、GB2312
            MP3InfoBean mp3InfoBean = mp3Info.getMp3InfoBean();
            System.out.println(mp3InfoBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
