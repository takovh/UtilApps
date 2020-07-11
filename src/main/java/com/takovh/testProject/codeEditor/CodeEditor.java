package com.takovh.testProject.codeEditor;

import com.takovh.testProject.utils.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 将代码整合到同一个文件内
 * 删除注释与空行
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

    private void run(){
        getFileList(new File(basePath));
        collectCode2File();
        try {
            deleteComments();
        } catch (Exception e) {
            e.printStackTrace();
        }
        deleteBlankLines();
    }


    /**
     * 根据basePath获取所有filePaths
     * @param src basePath
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
    private void initOutputFile(File outputFile) {
        System.out.println("文件" + outputFile.getName() + "是否存在：" + outputFile.exists());
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
        initOutputFile(outputFile);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            if(filePaths!=null){
                for (String path : filePaths){
                    File file = new File(path);
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(file));
                        String tempString = null;
                        // 以行为单位读取文件内容，一次读一整行
                        while ((tempString = reader.readLine()) != null){
                            writer.write(tempString);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        FileUtil.close(reader);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.close(writer);
        }
    }


    /**
     * 删除空行
     */
    private void deleteBlankLines() {
        String regex1 = "[\\s]*";
        String regex2 = "[\\s]*";
        int i = 0;
        String tempPath = outputFile.getAbsolutePath() + ".temp";
        File tempFile = new File(tempPath);
        initOutputFile(tempFile);
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new BufferedWriter(new FileWriter(tempFile));
            reader = new BufferedReader(new FileReader(outputFile));
            String tempString = null;
            // 以行为单位读取文件内容，一次读一整行
            while ((tempString = reader.readLine()) != null){
                boolean flag = tempString.matches(regex1) | tempString.matches(regex2);
                if(flag){
                    i++;
                    continue;
                }
                writer.write(tempString);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
            System.out.println("删除空行:" + i + "行！");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.close(reader);
            FileUtil.close(writer);
        }
    }

    /**
     * 删除注释
     */
    private void deleteComments() throws Exception {
        FileReader fr = new FileReader("src\\qi_mo\\aa.java");
        BufferedReader bufferedreader = new BufferedReader(fr);

        FileWriter fw = new FileWriter(new File("src\\qi_mo\\bb.java"));
        BufferedWriter bw = new BufferedWriter(fw);
        char ch;
        String str = "";
        int index;
        boolean hasElseSign = false;
        try{
            while ((str = bufferedreader.readLine().trim()) != null) {
                if (0 != str.length()) {
                    if(hasElseSign == false){//如果没有多行注释
                        for(index = 0;index < str.length();index++){
                            ch = str.charAt(index);
                            if((ch == '/')){
                                if(str.charAt(index+1) == '/'){//是否有单行注释
                                    str = str.substring(0,index);
                                    break;
                                }
                                if(str.charAt(index+1)=='*'){//是否有多行注释
                                    hasElseSign = true;
                                    break;
                                }
                            }
                        }
                        if(hasElseSign) continue;
                    }
                    else{//有多行注释时
                        for(index = 0;index < str.length();index++){
                            ch = str.charAt(index);
                            if((ch == '*')&&(index<str.length()-1)&&(str.charAt(index+1) == '/')){
                                hasElseSign = false;
                                break;
                            }
                        }
                        continue;
                    }
                    bw.write(str);//写入文件 把str存入缓冲
                    bw.newLine();//换行
                    bw.flush();//立即写入 （把缓冲里的所有内容写入）
                    System.out.println(str);//

                }
            }
        }
        catch (Exception ioe){
            ioe.printStackTrace();
        }
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
        System.out.println("BasePath:" + codeEditor.getBasePath());
        System.out.println("OutputPath:" + codeEditor.getOutputFile().getPath());
    }

}
