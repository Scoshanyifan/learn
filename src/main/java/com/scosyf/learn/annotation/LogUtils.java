package com.scosyf.learn.annotation;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scosyf.learn.annotation.common.ApiAnnotation;
import com.scosyf.learn.annotation.common.Log;

/**
 * 日志记录工具，利用spring AOP对业务层方法进行调用的记录，以POST为主
 * 
 * 1.记录所有方法的调用时间到日志中
 * 2.记录post请求的日志并落地
 * @author syf
 *
 * 参考：https://www.cnblogs.com/djoker/p/7977166.html
 */
@Component
@Aspect
public class LogUtils {
	
	private static Logger logger = LoggerFactory.getLogger(LogUtils.class);
	
	@Pointcut("execution(* com.scosyf.learn..*.*(..))")
	public void pointCut() {};

	@Around("pointCut()")
	public Object logRecord(ProceedingJoinPoint point) throws Throwable {
		//boolean ifLog = false;
		boolean ifLog = true;

		Log log = new Log();
//		if (checkHttpInfo(log)) {
//			ifLog = true;
//		}
		
		long startTime = System.currentTimeMillis();
		try {
			return point.proceed();
		} catch (Throwable e) {
			throw e;
		} finally {
			long timeCost = System.currentTimeMillis() - startTime;
			Signature signature = point.getSignature();
			if(timeCost >= 300){
                logger.info("consume:{} ms, method:{}",timeCost, signature.toShortString());
            }
			if (ifLog) {
				//方法名
				String targetMethod = signature.getName();
				log.setMethodName(targetMethod);
				//类名
				String targetClass = signature.getDeclaringTypeName();
				log.setClassName(targetClass);
				//参数
				Object[] args = point.getArgs();
				StringBuilder argsBuilder = new StringBuilder();
				for (int i = 0; i < args.length; i++) {
					argsBuilder.append(args[i]).append(";");
				}
				log.setParameter(argsBuilder.toString());
				/*
				 * 功能描述（获取方法上的注解信息）
				 * 参考文章提到用如下方式会获取不到实现类上的注解，而是父类或是接口信息
				 */
//				MethodSignature ms = (MethodSignature) point.getSignature();
//				Method m = ms.getMethod();

				//1.方法一（有缺陷，存在getArgs()拿到的类类型和getMethod()获取的参数类型不匹配）
//				Class<?>[] argTypes = new Class[point.getArgs().length];
//		        for (int i = 0; i < args.length; i++) {
//		              argTypes[i] = args[i].getClass();
//		        }
//		        Method m = point.getTarget().getClass().getMethod(point.getSignature().getName(), argTypes);
//				ApiAnnotation apiOperation = method.getAnnotation(ApiAnnotation.class);
//				if (apiOperation != null) {
//					log.setDescription(apiOperation.notes());
//				}
				//2.方法二
				Method[] ms = point.getTarget().getClass().getDeclaredMethods();
				for (Method method : ms) {
					if (method.getName().equals(signature.getName())) {
						ApiAnnotation apiOperation = method.getAnnotation(ApiAnnotation.class);
						if (apiOperation != null) {
							log.setDescription(apiOperation.notes());
						}
					}
				}
				
				//耗时
				log.setCostTime(timeCost);
				//执行时间
				log.setLogTime(new Date());
				//TODO token的获取
				logger.info(">>> log: " + log);
				System.out.println(log);
				//TODO 导入数据库
			}
		}
	}
	
	/**
	 * 只对post请求进行日志记录
	 */
	private boolean checkHttpInfo(Log log) {
		HttpServletRequest request = 
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String method = request.getMethod();
		if (!"POST".equals(method)) {
			return false;
		}
		String ip = request.getHeader("X-Real-IP");
		if(StringUtils.isEmpty(ip)){
			ip = request.getRemoteAddr();
		}
		log.setIp(ip);
		return true;
	}
}
