package com.socialstartup.mockenger.core.log;

import com.socialstartup.mockenger.data.model.persistent.account.Account;
import com.socialstartup.mockenger.data.model.persistent.log.Event;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Aspect
@Component
public class AccountAspect extends CommonAspect {

    @Pointcut(EVENTABLE_ANNOTATION + " && args(account))")
    public void onAccountChanged(final Account account) {}


    @After("onAccountChanged(account)")
    public void createEvent(final JoinPoint joinPoint, final Account account) {
		save(Event.<Account>builder().eventType(getEventType(joinPoint)).entity(account));
    }
}
