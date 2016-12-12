package com.github.vendigo.acemybatis.config;

public class AceConfig {
    private int selectChunkSize = 5000;
    private int updateChunkSize = 2000;
    private int threadCount = 0;

    public AceConfig() {
    }

    public AceConfig(int selectChunkSize, int updateChunkSize, int threadCount) {
        this.selectChunkSize = selectChunkSize;
        this.updateChunkSize = updateChunkSize;
        this.threadCount = threadCount;
    }

    public int getSelectChunkSize() {
        return selectChunkSize;
    }

    public int getUpdateChunkSize() {
        return updateChunkSize;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
