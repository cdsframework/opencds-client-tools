package org.cdsframework.util.support.cds;

import org.cdsframework.exceptions.CdsException;


/**
 *
 * @author HLN Consulting, LLC
 */
public abstract class TestImportCallback {

    public abstract void callback(TestCaseWrapper testCase, String group, boolean success) throws CdsException;
}
