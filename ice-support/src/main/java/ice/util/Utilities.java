package ice.util;

import org.apache.log4j.Logger;

/**
 *
 * @author HLN Consulting, LLC
 */
public class Utilities {
    private final static Logger logger = Logger.getLogger(Utilities.class);

    public static void logDuration(String operation, long start) {
        StringBuilder sb = new StringBuilder();
        logger.info(sb.append(operation).append(" duration(ms): ").append((System.nanoTime() - start)/1000000.0).toString());
    }

}
