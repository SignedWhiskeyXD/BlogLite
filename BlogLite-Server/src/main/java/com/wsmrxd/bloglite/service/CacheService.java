package com.wsmrxd.bloglite.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheService {
    <T> void setKeyValue(String key, T value);

    <T> boolean setKeyValueIfAbsent(String key, T value, Duration timeout);

    <T> T getValueByKey(String key);

    <T> void putKeyValToHash(String key, String hashKey, T value);

    <T> T getValueByHashKey(String key, String hashKey);

    <T> void rPushValToList(String key, T value);

    List getList(String key);

    List getListByRange(String key, long startIndex, long endIndex);

    long getListSize(String key);

    Map getHashEntriesByKey(String key);

    void increaseValueByHashKey(String key, String hashKey, long delta);

    <T> T getZSetValueByScore(String key, double score);

    <T> void addValueToSet(String key, T value);

    <T> void removeSetValue(String key, T value);

    Set getSet(String key);

    List getSetAsList(String key);

    <T> void addValueToZSet(String key, T value, double score);

    void removeZSetValueByScore(String key, double score);

    Set getZSet(String key);

    long getZSetSize(String key);

    List getZSetAsList(String key);

    List getListByReversedScoreRange(String key, double min, double max, int offset, int num);

    List getListByReversedIndexRange(String key, long start, long end);

    void delete(String key);

    boolean hasKey(String key);
}
