package org.springframework.web.context.support;

import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.NestedCheckedException;
import org.springframework.core.NestedIOException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.servlet.DispatcherServlet.EXCEPTION_ATTRIBUTE;

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

    @Value("${spring.application.name:unknown}")
    private String appName;

    @Value("${spring.http.warningDelay:200}")
    private int warningDelay;

    @Value("${spring.http.errorDelay:1000}")
    private int errorDelay;

    // errorDelay * 5
    @Value("#{${spring.http.errorDelay:1000}*5}")
    private int fatalDelay;

    private int fatalCount;

    @Value("${spring.http.request.sampleRate:1.0}")
    private double sampleRate = 1.0;

    @Autowired(required = false)
    private StatsDClient statsDClient;

    @Value("${management.metrics.web.request.auto-time-requests:true}")
    private boolean autoTimeRequests = true;

    @Value("${management.metrics.web.request.requests-metric-name:http.server.requests}")
    private String requestsMetricName = "http.server.requests";

    private static final String ERROR_KEY_SUFFIX = ".error";


    @Override
    public void onApplicationEvent(RequestHandledEvent event) {
        long timed = event.getProcessingTimeMillis();
        recordExecutionTime(event, timed);
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
            fatalCount++;
            logger.error("<== " + event.getDescription() + " .FATAL fatalCount=" + fatalCount);
        }

    }


    private String buildKey(CharSequence tags) {
        StringBuilder builder = new StringBuilder();
        builder.append(requestsMetricName).append(",application=").append(appName).append(tags);

        return builder.toString();
    }

    private String buildErrorKey(CharSequence tags, CharSequence errorTags) {
        StringBuilder builder = new StringBuilder();
        builder.append(requestsMetricName).append(ERROR_KEY_SUFFIX).append(",application=").append(appName).append(tags);
        if (errorTags != null) {
            builder.append(errorTags);
        }
        return builder.toString();
    }

    private StringBuilder buildTags(ServletRequestHandledEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append(",method=").append(event.getMethod());
        String uri =  getMatchingPattern();
        builder.append(",uri=").append(uri != null ? uri : event.getRequestUrl());
        builder.append(",status=").append(event.getStatusCode());
        return builder;
    }

    public static String buildTags(Throwable e) {
        //String message;
        String typeName;
        if (e instanceof NestedRuntimeException || e instanceof NestedIOException || e instanceof NestedCheckedException) {
            Throwable cause = e.getCause();
            if (cause != null) {
                //message = ExceptionHandlerUtils.getMessage(cause, 64);
                typeName = cause.getClass().getName();
            } else {
                //message = ExceptionHandlerUtils.getMessage(e, 64);
                typeName = e.getClass().getName();
            }
        } else {
            //message = ExceptionHandlerUtils.getMessage(e, 64);
            typeName = e.getClass().getName();

        }
        StringBuilder builder = new StringBuilder();
        builder.append(",exception=").append(typeName);
        /*if(StringUtils.hasLength(message)){
            builder.append(",message=").append(message.replaceAll(" ","_").replaceAll(",","_"));
        }*/
        return builder.toString();
    }


    // io.micrometer.spring.web.servlet.WebMvcTags#getMatchingPattern
    private static String getMatchingPattern(HttpServletRequest request) {
        return (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    }

    private static String getMatchingPattern() {
        RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
        return (String) previousAttributes.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE,RequestAttributes.SCOPE_REQUEST);
    }

    private void recordExecutionTime(RequestHandledEvent event, long timed) {
        if (autoTimeRequests && statsDClient != null && event instanceof ServletRequestHandledEvent) {
            StringBuilder tags = buildTags((ServletRequestHandledEvent) event);
            String key = buildKey(tags);
            statsDClient.recordExecutionTime(key, timed, sampleRate);

            // exception report error count
            Throwable failureCause = event.getFailureCause();
            if (failureCause == null) {
                RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
                Object exception = previousAttributes.getAttribute(EXCEPTION_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
                if (exception != null && exception instanceof Throwable) {
                    String exceptionTags = buildTags((Throwable) exception);
                    String errorKey = buildErrorKey(tags, exceptionTags);
                    statsDClient.count(errorKey, 1);
                } else {
                    exception = previousAttributes.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
                    if (exception != null && exception instanceof Throwable) {
                        String exceptionTags = buildTags((Throwable) exception);
                        String errorKey = buildErrorKey(tags, exceptionTags);
                        statsDClient.count(errorKey, 1);
                    }

                }

            } else {
                String exceptionTags = buildTags(failureCause);
                String errorKey = buildErrorKey(tags, exceptionTags);
                statsDClient.count(errorKey, 1);
            }

        }
    }


}
