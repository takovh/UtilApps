package com.takovh.testProject.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 工具类关闭流
 * 可变参数： ... 只能放在形参的最后一个位置,处理方式与数组一致
 * @author tako_
 *
 */
public class FileUtil {
	public static void close(Closeable ... io) {
		for(Closeable temp:io) {
			if(null!=temp) {
				try {
					temp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
