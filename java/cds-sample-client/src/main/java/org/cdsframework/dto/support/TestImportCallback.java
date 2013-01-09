package org.cdsframework.dto.support;

import org.cdsframework.dto.TestcaseWrapper;
import org.cdsframework.exception.IceException;


/**
 *
 * @author HLN Consulting, LLC
 */
public abstract class TestImportCallback {

    public abstract void callback(TestcaseWrapper testcase, String Group, boolean success) throws IceException;
}
