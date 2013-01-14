package org.cdsframework.cds.util;

import java.io.Serializable;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LogUtils implements Serializable {

    protected Logger logger;
    private final Level initialLevel;
    public static final LogLevel LOG_LEVEL_INFO = LogLevel.INFO;
    public static final LogLevel LOG_LEVEL_ERROR = LogLevel.ERROR;
    public static final LogLevel LOG_LEVEL_FATAL = LogLevel.FATAL;
    public static final LogLevel LOG_LEVEL_TRACE = LogLevel.TRACE;
    public static final LogLevel LOG_LEVEL_WARN = LogLevel.WARN;
    public static final LogLevel LOG_LEVEL_DEBUG = LogLevel.DEBUG;

    public LogUtils(String loggerString) {
        logger = Logger.getLogger(loggerString);
        initialLevel = logger.getLevel();
    }

    public LogUtils(Class childClass) {
        logger = Logger.getLogger(childClass);
        initialLevel = logger.getLevel();
        logger.debug("Initializing logger for class: " + childClass.getSimpleName());
    }

    public static LogUtils getLogger(Class loggerClass) {
        return new LogUtils(loggerClass);
    }

    public static LogUtils getLogger(String loggerString) {
        return new LogUtils(loggerString);
    }

    private String logMessage(LogLevel logLevel, Object... msgStrings) {

        Throwable t = null;
        if (msgStrings.length > 0) {
            if (msgStrings[msgStrings.length-1] instanceof Throwable) {
                t = (Throwable) msgStrings[msgStrings.length-1];
            }
        }

        StringBuilder msg = new StringBuilder();
        for (Object item : msgStrings) {
            if (item == null) {
                msg.append("null");
            } else if (item.getClass().isPrimitive()) {
                msg.append(item + "");
            } else if (item instanceof Throwable) {
                msg.append(((Throwable) item).getMessage());
            } else if (item instanceof Class) {
                msg.append(((Class)item).getSimpleName());
            } else {
                try {
                    msg.append(item.toString());
                } catch (Exception e) {
                    logger.error("Error converting msg item to String: " + item);
                    msg.append(item + "");
                }
            }
        }
        String msgString = msg.toString();
        switch(logLevel) {
            case TRACE:
                if (t != null) {
                    logger.trace(msgString, t);
                } else {
                    logger.trace(msgString);
                }
                break;
            case DEBUG:
                if (t != null) {
                    logger.debug(msgString, t);
                } else {
                    logger.debug(msgString);
                }
                break;
            case INFO:
                if (t != null) {
                    logger.info(msgString, t);
                } else {
                    logger.info(msgString);
                }
                break;
            case WARN:
                if (t != null) {
                    logger.warn(msgString, t);
                } else {
                    logger.warn(msgString);
                }
                break;
            case ERROR:
                if (t != null) {
                    logger.error(msgString, t);
                } else {
                    logger.error(msgString);
                }
                break;
            case FATAL:
                if (t != null) {
                    logger.fatal(msgString, t);
                } else {
                    logger.fatal(msgString);
                }
                break;
            default:
                logger.info(msgString);
        }
        return msgString;
    }

    public String log(Object... msgs) {
        return info(msgs);
    }

    public String trace(Object... msgs) {
        if (logger.isTraceEnabled()) {
            return logMessage(LogLevel.TRACE, msgs);
        }
        return null;
    }

    public String debug(Object... msgs) {
        if (logger.isDebugEnabled()) {
            return logMessage(LogLevel.DEBUG, msgs);
        }
        return null;
    }

    public String info(Object... msgs) {
        if (logger.isInfoEnabled()) {
            return logMessage(LogLevel.INFO, msgs);
        }
        return null;
    }

    public String warn(Object... msgs) {
        return logMessage(LogLevel.WARN, msgs);
    }

    public String error(Object... msgs) {
        return logMessage(LogLevel.ERROR, msgs);
    }

    public String fatal(Object... msgs) {
        return logMessage(LogLevel.FATAL, msgs);
    }

    public void fullStack(Throwable e) {
        if (e != null) {
            error(e);
            if (e.getCause() != null) {
                error("CAUSE: ", e.getCause().getMessage());
                fullStack(e.getCause());
            }
        } else {
            error("Null exception passed to error().");
        }
    }

    public void logInit(String methodName, Object... vargs) {
        trace(methodName, "INIT", vargs);
    }

    public void logBegin(String methodName, Object... vargs) {
        trace(methodName, "BEGIN", vargs);
    }

    public void logEnd(String methodName, Object... vargs) {
        trace(methodName, "END", vargs);
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void setDebugEnabled() {
        logger.setLevel(Level.DEBUG);
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public void setTraceEnabled() {
        logger.setLevel(Level.TRACE);
    }

    public void resetLevel() {
        logger.setLevel(initialLevel);
    }

    public enum LogLevel {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL
    }

    /**
     * pass in the result of System.nanoTime() as a start value
     *
     * @param operation
     * @param start
     * @param logLevel
     */
    public void logDuration(String operation, long start, LogLevel logLevel) {
        if (logLevel == LogLevel.DEBUG && !isDebugEnabled()) {
            return;
        }
        if (logLevel == LogLevel.TRACE && !isTraceEnabled()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        logMessage(logLevel, sb.append(operation).append(" duration(ms): ").append((System.nanoTime() - start)/1000000.0).toString());
    }

    /**
     * pass in the result of System.nanoTime() as a start value
     *
     * @param operation
     * @param start
     */
    public void logDuration(String operation, long start) {
        logDuration(operation, start, LOG_LEVEL_INFO);
    }
}
