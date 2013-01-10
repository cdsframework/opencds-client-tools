package org.cdsframework.util.support.cds.importers;

import java.io.InputStream;

import org.cdsframework.exceptions.CdsException;
import org.cdsframework.util.support.cds.TestImportCallback;

/**
 *
 * @author HLN Consulting, LLC
 */
public interface TestCaseImporterInterface {

    public void importFromFile(String filename, TestImportCallback callback) throws CdsException;

    public void importFromInputStream(InputStream inputStream, TestImportCallback callback) throws CdsException;
}
