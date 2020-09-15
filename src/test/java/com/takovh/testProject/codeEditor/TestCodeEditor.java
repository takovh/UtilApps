package com.takovh.testProject.codeEditor;

import java.io.FileNotFoundException;

/**
 * 将代码整合到同一个文件内
 * 删除注释与空行
 * 整合后的输出文件与代码文件夹在同一目录下，命名为output.txt
 */
public class TestCodeEditor {
    public static void main(String[] args) {
        String basePath = "C:\\Users\\张煜\\Desktop\\javaa";
        String outputPath = "C:\\Users\\张煜\\Desktop\\output.txt";
        CodeEditor codeEditor = new CodeEditor();
        try {
            codeEditor.collectCode2File(basePath, outputPath);
            codeEditor.deleteSingleLineComments(outputPath);
            codeEditor.deleteMultilineComments(outputPath);
            codeEditor.deleteBlankLines(outputPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
