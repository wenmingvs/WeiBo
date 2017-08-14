/**
 * Copyright (C) 2008-2015 MEITU Technologies.
 * All rights reserved.
 * Created on 2015-3-27 下午12:55:14
 *
 * @author Liwentao
 * @func: App 的配置相关类 渠道信息等
 */
package com.wenming.weiswift.app.config;

import android.text.TextUtils;
import android.util.Xml;

import com.wenming.weiswift.utils.SDCardUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AppConfig {
    private static final String CONFIG_FILE_FOR_TEST = "TestConfig_WeiSwift.xml";
    private static boolean sIsDebug;
    private static boolean sOpenLeakCanary;

    static {
        initTestConfig();
    }

    /**
     * 初始化SDCard下test config文件
     */
    private static void initTestConfig() {
        final String configFilePath = SDCardUtils.getSdcardPath() + CONFIG_FILE_FOR_TEST;
        final File testConfigFile = new File(configFilePath);
        if (!testConfigFile.exists()) {
            return;
        }
        FileInputStream fileInput = null;
        BufferedInputStream in = null;
        try {
            fileInput = new FileInputStream(testConfigFile);
            in = new BufferedInputStream(fileInput, 80960);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "UTF-8");
            setConfigValues(parser);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void setConfigValues(XmlPullParser parser) {
        try {
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        int attributeCount = parser.getAttributeCount();
                        if (attributeCount > 0) {
                            String name = parser.getAttributeValue(0);
                            if ("debug".equals(name)) {
                                String str = parser.nextText();
                                if (!TextUtils.isEmpty(str)) {
                                    sIsDebug = Boolean.parseBoolean(str);
                                }
                            } else if ("open_leak_canary".equals(name)) {
                                String openLeakCanary = parser.nextText();
                                if (!TextUtils.isEmpty(openLeakCanary)) {
                                    sOpenLeakCanary = Boolean.parseBoolean(openLeakCanary);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是debug版本
     */
    public static boolean isDebug() {
        return sIsDebug;
    }

    /**
     * 是否打开内存泄漏检测工具
     */
    public static boolean openLeakCanary() {
        return sOpenLeakCanary;
    }
}
