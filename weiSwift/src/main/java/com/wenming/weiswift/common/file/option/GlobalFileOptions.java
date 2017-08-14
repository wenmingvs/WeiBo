package com.wenming.weiswift.common.file.option;

/**
 * func: 文件配置类
 * Created by Li WenTao on 2016/9/24 11:26
 */
public final class GlobalFileOptions {

    /**
     * 公司名称
     */
    private final String companyName;

    /**
     * 工程名称
     */
    private final String projectName;

    /**
     * 抛出的阈值，剩余空间小于该值则抛出异常。
     */
    private final long notEnoughSpaceLeftLine;

    private GlobalFileOptions(Builder builder) {
        this.companyName = builder.companyName;
        this.projectName = builder.projectName;
        this.notEnoughSpaceLeftLine = builder.notEnoughSpaceLeftLine;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getProjectName() {
        return projectName;
    }

    public long getNotEnoughSpaceLeftLine() {
        return notEnoughSpaceLeftLine;
    }

    public static class Builder {
        private String companyName;
        private String projectName;
        private long notEnoughSpaceLeftLine;

        public Builder() {
        }

        public Builder setCompanyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder setProjectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder setNotEnoughSpaceLeftLine(long notEnoughSpaceLeftLine) {
            this.notEnoughSpaceLeftLine = notEnoughSpaceLeftLine;
            return this;
        }

        public GlobalFileOptions build() {
            return new GlobalFileOptions(this);
        }
    }
}
