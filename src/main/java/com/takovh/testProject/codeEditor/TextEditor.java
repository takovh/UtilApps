package com.takovh.testProject.codeEditor;

import com.takovh.testProject.utils.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextEditor {
    /**
     * 获取到的文件路径集合
     */
    private List<String> filePaths = new ArrayList<String>();

    /**
     * 根据basePath获取所有filePaths
     * @param src basePath
     */
    public void getFileList(File src){
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
     * @param filePath 需要初始化的文件
     */
    private File initOutputFile(String filePath) {
//        System.out.println("文件" + file.getName() + "是否存在：" + file.exists());
        File file = new File(filePath);
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
        return file;
    }

    /**
     * 将filePaths中的代码整合到outputFilePath中
     */
    public void collectCode2File(String basePath, String outputPath){
        if(null == basePath){
            System.out.println("程序未执行，请先设置basePath！");
            return;
        }
        getFileList(new File(basePath));
        File outputFile = initOutputFile(outputPath);
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
     * @param file 要处理的文件
     * @param regexs 正则表达式
     */
    public void deleteDesignatedLines(File file, String...regexs){
        int i = 0;
        String tempPath = file.getAbsolutePath() + ".temp";
        File tempFile = initOutputFile(tempPath);
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new BufferedWriter(new FileWriter(tempFile));
            reader = new BufferedReader(new FileReader(file));
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
        if(file.delete()) tempFile.renameTo(file);
    }

    /**
     * 删除空行
     * @param filePath 要处理的文件
     */
    public void deleteBlankLines(String filePath) {
        System.out.println("删除空行...");
        // ^\s*$ 匹配空行
        String regex = "^\\s*$";
        File file = new File(filePath);
        deleteDesignatedLines(file, regex);
    }

    /**
     * 删除单行注释
     * @param filePath 要处理的文件
     */
    public void deleteSingleLineComments(String filePath) {
        System.out.println("删除单行注释...");
        // ^\s*//.*?$ 匹配单行注释
        String regex = "^\\s*//.*?$";
        File file = new File(filePath);
        deleteDesignatedLines(file, regex);
    }

    /**
     * 删除多行注释
     * @param filePath 要处理的文件
     */
    public void deleteMultilineComments(String filePath) {
        System.out.println("删除多行注释...");
        // ^\s*/\*.*$ 注释开始
        String regex1 = "^\\s*/\\*.*$";
        // ^.*\*/\s*$ 注释结束
        String regex2 = "^.*\\*/\\s*$";
        File file = new File(filePath);
        // 判断是否发现注释
        boolean flag = false;
        int i = 0;
        String tempPath = file.getAbsolutePath() + ".temp";
        File tempFile = initOutputFile(tempPath);
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new BufferedWriter(new FileWriter(tempFile));
            reader = new BufferedReader(new FileReader(file));
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
        if(file.delete()) tempFile.renameTo(file);
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public TextEditor(){}


}
