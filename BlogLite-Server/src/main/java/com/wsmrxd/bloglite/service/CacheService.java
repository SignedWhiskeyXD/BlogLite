package com.wsmrxd.bloglite.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheService {

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
