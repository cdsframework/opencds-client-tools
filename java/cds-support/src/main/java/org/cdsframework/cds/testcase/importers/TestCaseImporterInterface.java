package org.cdsframework.cds.testcase.importers;

import java.io.InputStream;
import org.cdsframework.cds.exceptions.CdsException;

/**
 *
 * @author HLN Consulting, LLC
 */
public interface TestCaseImporterInterface {

    public void importFromFile(String filename, TestImportCallback callback) throws CdsException;

    public void importFromInputStream(InputStream inputStream, TestImportCallback callback) throws CdsException;
}
