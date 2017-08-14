package com.wenming.weiswift.app.utils;

import com.wenming.weiswift.utils.SDCardUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * 文本保存与读写工具类
 * <p>
 * Created by wenmingvs on 2017/8/14.
 */

public class TextSaveUtils {

    public static void write(String fileDir, String fileName, String content) {
        File filedir = new File(fileDir);
        File jsonfile = new File(filedir, fileName);
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
        try {
            jsonfile.createNewFile();
            if (SDCardUtils.isSdAvailable()) {
                FileOutputStream outputstream = new FileOutputStream(jsonfile);
                byte[] buffer = content.getBytes();
                outputstream.write(buffer);
                outputstream.flush();
                outputstream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String read(String fileDir, String fileName) {
        StringBuffer sb;
        try {
            File file = new File(fileDir, fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readline;
            sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                sb.append(readline);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
