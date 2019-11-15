package org.nbone.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.logging.Level;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-22
 */
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger("LogUtils");

    public static String MESSAGE = "method execute time used: %s ms, methodName : %s";


    public static void timedLog(Logger logger, long start, long end, int warningDelay, String method) {
        long xx = end - start;
        int errorDelay = 1000;
        if (warningDelay > 1000) {
            errorDelay = warningDelay + 1000;
        }
        if (xx < warningDelay) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format(MESSAGE, xx, method));
            }
        } else if (xx >= warningDelay && xx < errorDelay) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format(MESSAGE, xx, method));
            }
        } else {
            logger.error(String.format(MESSAGE, xx, method));
        }

    }

    public static void timedLog(Logger logger, long start, long end, String method) {
        timedLog(logger, start, end, 100, method);
    }

    public static void timedLog(Logger logger, long start, int warningDelay, String method) {
        timedLog(logger, start, System.currentTimeMillis(), warningDelay, method);
    }

    public static void timedLog(Logger logger, long start, String method) {
        timedLog(logger, start, System.currentTimeMillis(), 100, method);
    }


    public static void timedLog(long start, long end, String method) {
        timedLog(logger, start, end, 100, method);
    }

    public static void timedLog(long start, int warningDelay, String method) {
        timedLog(logger, start, System.currentTimeMillis(), warningDelay, method);
    }

    public static void timedLog(long start, String method) {
        timedLog(logger, start, System.currentTimeMillis(), 100, method);
    }


    public static void error(Logger logger, long start, String method, Exception exception) {
        long xx = System.currentTimeMillis() - start;
        logger.error(String.format(MESSAGE, xx, method), exception);
    }

    public static void error(long start, String method, Exception exception) {
        long xx = System.currentTimeMillis() - start;
        logger.error(String.format(MESSAGE, xx, method), exception);
    }

    public static void error(Logger logger, String method, Object request, Object response) {
        logger.error(method + " request: " + request);
        logger.error(method + " response: " + response);
    }

    public static void warn(Logger logger, String method, Object request, Object response) {
        logger.warn(method + " request: " + request);
        logger.warn(method + " response: " + response);
    }

    public static void isEmptyLog(Logger logger, Level level, String method, Object request, Object response) {
        if (ObjectUtils.isEmpty(response)) {
            String message = method + " isEmpty request: " + request;
            if (Level.SEVERE == level) {
                logger.error(message);
            } else if (Level.WARNING == level) {
                logger.warn(message);
            } else if (Level.INFO == level) {
                logger.info(message);
            } else {
                logger.debug(message);
            }
        }
    }

    public static void isEmptyLog(Logger logger, Level level, Object response,String method,
                                  String format, Object arg) {
        if (ObjectUtils.isEmpty(response)) {
            String formatMessage = method + " isEmpty " +format;
            if (Level.SEVERE == level) {
                logger.error(formatMessage, arg);
            } else if (Level.WARNING == level) {
                logger.warn(formatMessage, arg);
            } else if (Level.INFO == level) {
                logger.info(formatMessage, arg);
            } else {
                logger.debug(formatMessage, arg);
            }
        }
    }

    public static void isEmptyLog(Logger logger, Level level,Object response,String method,
                                  String format, Object arg1, Object arg2) {
        if (ObjectUtils.isEmpty(response)) {
            String formatMessage = method + " isEmpty " +format;
            if (Level.SEVERE == level) {
                logger.error(formatMessage, arg1, arg2);
            } else if (Level.WARNING == level) {
                logger.warn(formatMessage, arg1, arg2);
            } else if (Level.INFO == level) {
                logger.info(formatMessage, arg1, arg2);
            } else {
                logger.debug(formatMessage, arg1, arg2);
            }
        }
    }

    public static void isEmptyLog(Logger logger, Level level,Object response,String method,
                                  String format, Object... arguments) {
        if (ObjectUtils.isEmpty(response)) {
            String formatMessage = method + " isEmpty " +format;
            if (Level.SEVERE == level) {
                logger.error(formatMessage, arguments);
            } else if (Level.WARNING == level) {
                logger.warn(formatMessage, arguments);
            } else if (Level.INFO == level) {
                logger.info(formatMessage, arguments);
            } else {
                logger.debug(formatMessage, arguments);
            }
        }
    }

}
