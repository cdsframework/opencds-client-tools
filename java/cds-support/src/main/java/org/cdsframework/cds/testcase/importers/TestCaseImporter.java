package org.cdsframework.cds.testcase.importers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.cdsframework.cds.exceptions.CdsException;
import org.cdsframework.cds.util.LogUtils;

/**
 *
 * @author HLN Consulting, LLC
 */
public abstract class TestCaseImporter implements TestCaseImporterInterface {

    protected final LogUtils logger;

    public TestCaseImporter() {
        logger = LogUtils.getLogger(TestCaseImporter.class);
    }

    public TestCaseImporter(Class loggerClass) {
        logger = LogUtils.getLogger(loggerClass);
    }

    @Override
    public void importFromFile(String filename, TestImportCallback callback) throws CdsException {
        final String METHODNAME = "importFromFile ";
        logger.logBegin(METHODNAME);
        try {
            importFromInputStream(new FileInputStream(filename), callback);
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        } finally {
            logger.logEnd(METHODNAME);
        }
    }

}
