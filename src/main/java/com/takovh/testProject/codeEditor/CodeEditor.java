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
    public static void main(String[] args) {
        String basePath = "C:\\Users\\tako_\\Desktop\\provider";
        String outputPath = "C:\\Users\\tako_\\Desktop\\output.txt";
        TextEditor textEditor = new TextEditor();
        textEditor.collectCode2File(basePath, outputPath);
        textEditor.deleteSingleLineComments(outputPath);
        textEditor.deleteMultilineComments(outputPath);
        textEditor.deleteBlankLines(outputPath);
    }
}
