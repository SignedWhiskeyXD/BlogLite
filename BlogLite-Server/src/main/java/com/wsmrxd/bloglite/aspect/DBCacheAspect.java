package com.wsmrxd.bloglite.aspect;

import com.wsmrxd.bloglite.annotation.MapperCache;
import com.wsmrxd.bloglite.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class DBCacheAspect {

    @Autowired
    private CacheService cacheService;

    private static final ExpressionParser spelParser = new SpelExpressionParser();

    public static final String KEY_SEPARATOR = "::";

    @Around("@annotation(mapperCache)")
    public Object logMessage(ProceedingJoinPoint pjp, MapperCache mapperCache) throws Throwable {
        String cacheKey = parseCacheKey(pjp, mapperCache);
        return handleCache(cacheKey, pjp);
    }

    private String parseCacheKey(JoinPoint joinPoint, MapperCache cacheAnnotation) {
        var signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        var evaluationContext = new MethodBasedEvaluationContext(
                joinPoint.getTarget(), method, joinPoint.getArgs(), new DefaultParameterNameDiscoverer());

        String parsedKey = spelParser.parseExpression(cacheAnnotation.key()).getValue(evaluationContext, String.class);
        return cacheAnnotation.value() + KEY_SEPARATOR + parsedKey;
    }

    private Object handleCache(String cacheKey, ProceedingJoinPoint pjp) throws Throwable {
        Object cachedTagNames = cacheService.keyVal().getValueByKey(cacheKey);
        if(cachedTagNames != null) return cachedTagNames;

        log.info("Resolved cache key \"{}\" missed", cacheKey);
        Object entity = pjp.proceed();
        if(entity != null) {
            cacheService.keyVal().setKeyValue(cacheKey, entity);
            log.info("Resolved cache key \"{}\" rebuilt", cacheKey);
        }
        return entity;
    }
}
