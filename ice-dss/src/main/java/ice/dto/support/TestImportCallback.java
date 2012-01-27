package ice.dto.support;

import ice.dto.TestcaseWrapper;
import ice.exception.IceException;

/**
 *
 * @author HLN Consulting, LLC
 */
public abstract class TestImportCallback {

    public abstract void callback(TestcaseWrapper testcase, String Group, boolean success) throws IceException;
}
