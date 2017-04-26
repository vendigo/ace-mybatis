package com.github.vendigo.acemybatis.config;


import static com.github.vendigo.acemybatis.utils.Validator.isPositive;

/**
 * Config for AceMappers.
 * <ul>
 *     <li>selectChunkSize - will be used for batch select.</li>
 *     <li>updateChunkSize - chunk size for batch insert/update/delete (Methods which returns CompletableFuture).</li>
 *     <li>threadCount - count of threads for batch insert/update/delete. If 0 - computed bases on available cores.</li>
 * </ul>
 */
public class AceConfig {
    private static final int DEFAULT_SELECT_CHUNK_SIZE = 10_000;
    private static final int DEFAULT_CHANGE_CHUNK_SIZE = 2_000;
    private static final String DEFAULT_LIST_NAME = "entities";
    private static final String DEFAULT_ELEMENT_NAME = "entity";

    private int selectChunkSize = DEFAULT_SELECT_CHUNK_SIZE;
    private int updateChunkSize = DEFAULT_CHANGE_CHUNK_SIZE;
    private String listName = DEFAULT_LIST_NAME;
    private String elementName = DEFAULT_ELEMENT_NAME;
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

    public void setSelectChunkSize(int selectChunkSize) {
        this.selectChunkSize = selectChunkSize;
    }

    public int getUpdateChunkSize() {
        return updateChunkSize;
    }

    public void setUpdateChunkSize(int updateChunkSize) {
        this.updateChunkSize = updateChunkSize;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
}
