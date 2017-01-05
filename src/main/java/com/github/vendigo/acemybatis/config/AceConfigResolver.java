package com.github.vendigo.acemybatis.config;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Creates {@link AceConfig}. Overrides values in the given AceConfig
 * by explicitly specified other parameters (If they greater then 0).
 */
class AceConfigResolver {

    private AceConfigResolver() {
    }

    static AceConfig resolveConfig(AceConfig config, int selectChunkSize, int updateChunkSize, int threadCount,
                                   String listName, String elementName) {
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
            if (!isBlank(listName)) {
                config.setListName(listName);
            }
            if (!isBlank(elementName)) {
                config.setElementName(elementName);
            }
        }
        return config;
    }
}
