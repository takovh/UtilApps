package com.takovh.testProject.mp3InfoEditor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;


/**
 * 获得MP3文件的信息
 *
 */
public class MP3Info {

    private String charset = "utf-8";//解析MP3信息时用的字符编码

    private byte[] buf;//MP3的标签信息的byte数组

    private MP3InfoBean mp3InfoBean;


    /**
     * 实例化一个获得MP3文件的信息的类
     * @param mp3 MP3文件
     * @throws IOException 读取MP3出错或则MP3文件不存在
     */
    public MP3Info(File mp3, String charset) throws IOException{

        buf = new byte[128];//初始化标签信息的byte数组

        RandomAccessFile raf = new RandomAccessFile(mp3, "r");//随机读写方式打开MP3文件
        raf.seek(raf.length() - 128);//移动到文件MP3末尾
        raf.read(buf);//读取标签信息

        raf.close();//关闭文件

        if(buf.length != 128){//数据是否合法
            throw new IOException("MP3标签信息数据长度不合法!");
        }

        this.charset = charset;

        initial();
    }

    private void initial(){
        setMp3InfoBean(new MP3InfoBean(getSongName(), getArtist(), getAlbum(),getYear(), getComment()));
    }

    /**
     * 获得目前解析时用的字符编码
     * @return 目前解析时用的字符编码
     */
    private String getCharset() {
        return charset;
    }

    /**
     * 设置解析时用的字符编码
     * @param charset 解析时用的字符编码
     */
    private void setCharset(String charset) {
        this.charset = charset;
    }

//    public MP3InfoBean getMP3InfoBean(){
//        return new MP3InfoBean(getSongName(), getArtist(), getAlbum(),getYear(), getComment());
//    }


    public MP3InfoBean getMp3InfoBean() {
        return mp3InfoBean;
    }

    private void setMp3InfoBean(MP3InfoBean mp3InfoBean) {
        this.mp3InfoBean = mp3InfoBean;
    }

    private String getSongName(){
        try {
            return new String(buf,3,30,charset).trim();
        } catch (UnsupportedEncodingException e) {
            return new String(buf,3,30).trim();
        }
    }

    private String getArtist(){
        try {
            return new String(buf,33,30,charset).trim();
        } catch (UnsupportedEncodingException e) {
            return new String(buf,33,30).trim();
        }
    }

    private String getAlbum(){
        try {
            return new String(buf,63,30,charset).trim();
        } catch (UnsupportedEncodingException e) {
            return new String(buf,63,30).trim();
        }
    }

    private String getYear(){
        try {
            return new String(buf,93,4,charset).trim();
        } catch (UnsupportedEncodingException e) {
            return new String(buf,93,4).trim();
        }
    }

    private String getComment(){
        try {
            return new String(buf,97,28,charset).trim();
        } catch (UnsupportedEncodingException e) {
            return new String(buf,97,28).trim();
        }
    }


}