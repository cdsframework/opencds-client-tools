package org.cdsframework.cds.testcase.importers;

import org.cdsframework.cds.testcase.TestCaseWrapper;
import org.cdsframework.cds.exceptions.CdsException;


/**
 *
 * @author HLN Consulting, LLC
 */
public abstract class TestImportCallback {

    public abstract void callback(TestCaseWrapper testCase, String group, boolean success) throws CdsException;
}
