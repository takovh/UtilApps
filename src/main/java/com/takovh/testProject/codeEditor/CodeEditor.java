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
        deleteSingleLineComments();
        deleteMultilineComments();
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
     * @param file 需要初始化的文件
     */
    private void initOutputFile(File file) {
//        System.out.println("文件" + file.getName() + "是否存在：" + file.exists());
        Boolean flag = null;
        try {
            if(file.exists()){
                flag = file.delete();
//                System.out.println(flag?"删除成功！":"删除失败！");
            }
            flag = file.createNewFile();
//            System.out.println(flag?"创建成功！":"创建失败！");
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
     * 删除满足正则表达式regexs的行
     * @param regexs 正则表达式
     */
    private void deleteDesignatedLines(String...regexs){
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
                boolean flag = false;
                for (String regex : regexs){
                    flag = flag | tempString.matches(regex);
                }
                if(flag){
                    i++;
                    continue;
                }
                writer.write(tempString);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
            System.out.println("删除了" + i + "行！");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.close(reader);
            FileUtil.close(writer);
        }
        //删除原输出文件，将临时文件变为输出文件
        if(outputFile.delete()) tempFile.renameTo(outputFile);
    }

    /**
     * 删除空行
     */
    private void deleteBlankLines() {
        // ^\s*$ 匹配空行
        String regex = "^\\s*$";
        System.out.println("删除空行...");
        deleteDesignatedLines(regex);
    }

    /**
     * 删除单行注释
     */
    private void deleteSingleLineComments() {
        // ^\s*//.*?$ 匹配单行注释
        String regex = "^\\s*//.*?$";
        System.out.println("删除单行注释...");
        deleteDesignatedLines(regex);
    }

    private void deleteMultilineComments() {
        System.out.println("删除多行注释...");
        // ^\s*/\*.*$ 注释开始
        String regex1 = "^\\s*/\\*.*$";
        // ^.*\*/\s*$ 注释结束
        String regex2 = "^.*\\*/\\s*$";
        // 判断是否发现注释
        boolean flag = false;
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
                // 1.判断是否进入多行注释
                // 2.1若进入多行注释
                if(flag){
                    i++;
                    // 发现多行注释结束标志
                    if(tempString.matches(regex2)) flag = false;
                    continue;
                }
                // 2.2若未进入多行注释
                // 2.2.1发现多行注释开始标志
                if(tempString.matches(regex1)){
                    i++;
                    flag = true;
                    continue;
                }
                // 2.2.2未发现多行注释
                writer.write(tempString);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
            System.out.println("删除了" + i + "行！");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.close(reader);
            FileUtil.close(writer);
        }
        //删除原输出文件，将临时文件变为输出文件
        if(outputFile.delete()) tempFile.renameTo(outputFile);
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
        String path = "C:\\Users\\tako_\\Desktop\\provider";
        CodeEditor codeEditor = new CodeEditor(path);
        System.out.println("BasePath:" + codeEditor.getBasePath());
        System.out.println("OutputPath:" + codeEditor.getOutputFile().getPath());
    }

}
