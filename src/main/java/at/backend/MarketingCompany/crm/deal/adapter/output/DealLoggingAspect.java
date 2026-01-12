package at.backend.MarketingCompany.crm.deal.adapter.output;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class DealLoggingAspect {

  @Around("execution(* application.at.backend.MarketingCompany.crm.deal.DealApplicationServiceImpl.deleteServicePackage(..))")
  public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    log.info("Executing {} with args: {}", methodName, args);

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {
      Object result = joinPoint.proceed();
      stopWatch.stop();

      log.info("Completed {} in {} ms", methodName, stopWatch.getTotalTimeMillis());
      return result;

    } catch (Exception e) {
      stopWatch.stop();
      log.error("Failed {} after {} ms with error: {}",
          methodName, stopWatch.getTotalTimeMillis(), e.getMessage());
      throw e;
    }
  }
}
