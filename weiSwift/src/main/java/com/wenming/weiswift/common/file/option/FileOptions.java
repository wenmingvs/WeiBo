package com.wenming.weiswift.common.file.option;

/**
 * func: 文件配置类
 * Created by Li WenTao on 2016/9/24 11:26
 */
public final class FileOptions {

    /**
     * 模块名称
     */
    private final String moduleName;

    /**
     *  是否隐藏目录
     */
    private final boolean isHideDir;

    private FileOptions(Builder builder) {
        this.moduleName = builder.moduleName;
        this.isHideDir = builder.isHideDir;
    }

    public boolean isHideDir() {
        return isHideDir;
    }

    public String getModuleName() {
        return moduleName;
    }

    public static FileOptions createOptions(String moduleName, boolean isHideDir) {
        FileOptions fileOptions = new FileOptions
                .Builder()
                .setModuleName(moduleName)
                .setHideDir(isHideDir)
                .build();
        return fileOptions;
    }

    public static class Builder {
        private String moduleName;
        private boolean isHideDir;

        public Builder() {
        }

        public Builder setHideDir(boolean hideDir) {
            isHideDir = hideDir;
            return this;
        }

        public FileOptions build() {
            return new FileOptions(this);
        }

        public Builder setModuleName(String moduleName) {
            this.moduleName = moduleName;
            return this;
        }
    }
}
