package com.github.vendigo.acemybatis.config;

public class AceConfigResolver {

    private AceConfigResolver() {}

    public static AceConfig resolveConfig(AceConfig config, int selectChunkSize, int updateChunkSize, int threadCount) {
        if (config == null) {
            config = new AceConfig();
            if (selectChunkSize > 0) {
                config.setSelectChunkSize(selectChunkSize);
            }
            if (updateChunkSize > 0) {
                config.setUpdateChunkSize(updateChunkSize);
            }
            if (threadCount > 0) {
                config.setThreadCount(threadCount);
            }
        }
        return config;
    }
}
