package com.wsmrxd.bloglite.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheService {
    <T> void setKeyValue(String key, T value);

    <T> T getValueByKey(String key);

    <T> void putKeyValToHash(String key, String hashKey, T value);

    <T> T getValueByHashKey(String key, String hashKey);

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

    List getZSetAsList(String key);

    List getListByReversedScoreRange(String key, double min, double max, int offset, int num);

    void delete(String key);

    boolean hasKey(String key);
}
