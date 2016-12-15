package com.github.vendigo.acemybatis.config;


import static com.github.vendigo.acemybatis.utils.Validator.isPositive;

/**
 * Config for AceMappers.
 * <ul>
 *     <li>selectChunkSize - chunk size for batch select (The one with count query).</li>
 *     <li>updateChunkSize - chunk size for batch insert/update/delete (Methods which returns CompletableFuture).</li>
 *     <li>threadCount - count of threads for batch select/insert/update/delete. If 0 - computed bases on available cores.</li>
 * </ul>
 */
public class AceConfig {
    private int selectChunkSize = 5000;
    private int updateChunkSize = 2000;
    private int threadCount = 0;

    AceConfig() {
    }

    public AceConfig(int selectChunkSize, int updateChunkSize, int threadCount) {
        this.selectChunkSize = isPositive(selectChunkSize);
        this.updateChunkSize = isPositive(updateChunkSize);
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

    void setSelectChunkSize(int selectChunkSize) {
        this.selectChunkSize = selectChunkSize;
    }

    void setUpdateChunkSize(int updateChunkSize) {
        this.updateChunkSize = updateChunkSize;
    }

    void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
}
