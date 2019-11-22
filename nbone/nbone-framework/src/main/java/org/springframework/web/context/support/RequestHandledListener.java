package org.springframework.web.context.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * [0-200-1000]
 *
 * @author thinking
 * @version 1.0
 * @see RequestHandledEvent
 * @see ServletRequestHandledEvent
 * @since 2019-10-29
 */
@Configuration
public class RequestHandledListener implements ApplicationListener<RequestHandledEvent> {

    private static final Logger logger = LoggerFactory.getLogger("requestHandledListener");

    @Value("${spring.http.warningDelay:200}")
    private int warningDelay;

    @Value("${spring.http.errorDelay:1000}")
    private int errorDelay;

    // errorDelay * 5
    @Value("#{${spring.http.errorDelay:1000}*5}")
    private int fatalDelay;

    private int fatalCount;

    @Override
    public void onApplicationEvent(RequestHandledEvent event) {
        long timed = event.getProcessingTimeMillis();
        if (timed < warningDelay) {
            if (logger.isDebugEnabled()) {
                logger.debug("<== " + event.getDescription());
            }
        } else if (timed >= warningDelay && timed < errorDelay) {
            if (logger.isWarnEnabled()) {
                logger.warn("<== " + event.getDescription());
            }
        } else if (timed >= errorDelay && timed < fatalDelay) {
            logger.error("<== " + event.getDescription());
        } else {
            // The FATAL Tag very severe error events
            // that will presumably lead the application to abort.
            fatalCount ++;
            logger.error("<== " + event.getDescription() + " .FATAL fatalCount=" + fatalCount);
        }

    }


}
