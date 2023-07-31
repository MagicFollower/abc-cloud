package com.abc.system.lock.aop;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseException;
import com.abc.system.lock.annotation.DistributedLock;
import com.abc.system.lock.helper.DistributedLockHelper;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;

/**
 * DistributedLockAop
 *
 * @Description DistributedLockAop分布式锁切面，将在AutoConfiguration中注入IOC
 * @Author Trivis
 * @Date 2023/5/19 14:20
 * @Version 1.0
 */
@Aspect
@Slf4j
public class DistributedLockAop {
    private static final String POINTCUT_SIGN = " @annotation(com.abc.system.lock.annotation.DistributedLock)";

    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {
    }

    /**
     * <pre>
     * 切面逻辑
     *   1.根据@DistributedLock注解的leaseTime、timeUnit进行加锁、解锁；
     *   2.@DistributedLock注解参数缺省时，使用30s锁过期时间。
     * </pre>
     *
     * @param joinPoint ProceedingJoinPoint
     * @return Object, 被代理方法返回值
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        RLock lock = null;
        DistributedLock lockAnnotation;
        String lockKey;
        Object returnValue = null;

        try {
            lockAnnotation = getLockAnnotation(joinPoint);
            lockKey = getLockKey(joinPoint);
            // todo 这里的异常应该封装为单独的异常类
            if (StringUtils.isEmpty(lockKey)) {
                throw new BaseException(SystemRetCodeConstants.SYSTEM_ERROR.getCode(), "key生成失败");
            }
            lock = DistributedLockHelper.lock(lockKey, lockAnnotation.leaseTime(), lockAnnotation.timeUnit());
            returnValue = joinPoint.proceed();
        } catch (Throwable e) {
            throw new BaseException(SystemRetCodeConstants.SYSTEM_ERROR.getCode(), "分布式锁异常: " + e.getMessage());
        } finally {
            if (lock != null) {
                DistributedLockHelper.unlock(lock);
            }
        }
        return returnValue;
    }


    @AfterThrowing(pointcut = "pointcut()", throwing = "ex")
    public void afterThrowingAdvice(Throwable ex) {
        // 异常处理代码
        System.out.println("=====> 出现了异常！" + ex.getMessage());
        System.out.println(JSONObject.toJSONString(ex, JSONWriter.Feature.PrettyFormat));
    }


    /**
     * 获取分布式锁注解
     *
     * @param joinPoint ProceedingJoinPoint
     * @return 分布式锁注解
     */
    private DistributedLock getLockAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(DistributedLock.class);
    }

    /**
     * 生成分布式锁Key：类名_方法名
     * 1.⚠️注意：实际生效的key将会经由`com.abc.system.lock.util.DistributedLockUtils#enrichLockKey`进行包装；
     *
     * @param joinPoint ProceedingJoinPoint
     * @return 分布式锁Key
     */
    private String getLockKey(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String classSimpleName = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getMethod().getName();
        log.info("加锁方法所属类={}, 加锁方法方法名={}", classSimpleName, methodName);
        return classSimpleName + "_" + methodName;
    }
}
