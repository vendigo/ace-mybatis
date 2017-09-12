package com.github.vendigo.acemybatis.config;

import static com.github.vendigo.acemybatis.utils.StringUtils.isBlank;

/**
 * Creates {@link AceConfig}. Overrides values in the given AceConfig
 * by explicitly specified other parameters (If they greater then 0).
 */
class AceConfigResolver {

    private AceConfigResolver() {
    }

    static AceConfig resolveConfig(AceConfig config, int updateChunkSize, int threadCount,
                                   String listName, String elementName) {
        if (config == null) {
            config = new AceConfig();
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
