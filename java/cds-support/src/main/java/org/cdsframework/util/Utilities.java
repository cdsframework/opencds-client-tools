package org.cdsframework.util;

/**
 *
 * @author HLN Consulting, LLC
 */
public class Utilities {
    private final static LogUtils logger = LogUtils.getLogger(Utilities.class);

    public static void logDuration(String operation, long start) {
        StringBuilder sb = new StringBuilder();
        logger.debug(sb.append(operation).append(" duration(ms): ").append((System.nanoTime() - start)/1000000.0).toString());
    }

}
