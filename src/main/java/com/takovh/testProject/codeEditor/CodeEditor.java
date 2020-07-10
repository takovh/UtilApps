package com.takovh.testProject.codeEditor;

import com.takovh.testProject.utils.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 将代码整合到同一个文件内
 * 删除空行与注释
 * 整合后的输出文件与代码文件夹在同一目录下，命名为output.txt
 */
public class CodeEditor {

    /**
     * 要处理的代码的文件夹路径
     */
    private String basePath = null;

    /**
     * 获取到的文件路径集合
     */
    private List<String> filePaths = new ArrayList<String>();

    /**
     * 最终输出文件
     */
    private File outputFile = null;

    public void run(){
        File parent = new File(basePath);
        getFileList(parent);
        initOutputFile();
        collectCode2File();
        deleteBlankLines();
        deleteComments();
    }


    /**
     * 根据basePath获取所有filePaths
     * @param src
     */
    private void getFileList(File src){
        if(null==src||!src.exists()) return;
        if(!src.isDirectory()) filePaths.add(src.getAbsolutePath());
        if(src.isDirectory()){
            for(File sub:src.listFiles()){
                getFileList(sub);
            }
        }
    }

    /**
     * 初始化输出文件，若已存在则删除文件并重新创建
     */
    private void initOutputFile() {
        System.out.println("文件是否存在：" + outputFile.exists());
        try {
            if(outputFile.exists()){
                System.out.println(outputFile.delete()?"删除成功！":"删除失败！");
            }
            System.out.println(outputFile.createNewFile()?"创建成功！":"创建失败！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将filePaths中的代码整合到outputFilePath中
     */
    private void collectCode2File(){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            if(filePaths!=null){
                System.out.println("以行为单位读取文件内容，一次读一整行：");

                for (String path : filePaths){
                    System.out.println(path);
                    File file = new File(path);
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(file));
                        String tempString = null;
                        while ((tempString = reader.readLine()) != null){
    //                        System.out.println("line " + ": " + tempString);
                            writer.write(tempString);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 删除空行
     */
    private void deleteBlankLines() {
    }

    /**
     * 删除注释
     */
    private void deleteComments() {
    }

    public String getBasePath() {
        return basePath;
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public CodeEditor(String dirPath) {
        this.basePath = dirPath;
        this.outputFile = new File(new File(this.basePath).getParent(), "output.txt");
        this.run();
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\tako_\\Desktop\\java";
        CodeEditor codeEditor = new CodeEditor(path);
        System.out.println(codeEditor.getBasePath());
        System.out.println(codeEditor.getOutputFile());
    }

}
