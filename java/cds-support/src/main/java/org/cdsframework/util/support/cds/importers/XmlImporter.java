package org.cdsframework.util.support.cds.importers;

import java.io.InputStream;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.util.FileUtils;
import org.cdsframework.util.support.cds.Constants;
import org.cdsframework.util.support.cds.TestCaseWrapper;
import org.cdsframework.util.support.cds.TestImportCallback;
import org.opencds.support.TestCase;

/**
 *
 * @author HLN Consulting, LLC
 */
public class XmlImporter extends TestCaseImporter {

    public XmlImporter() {
        super(XmlImporter.class);
    }

    @Override
    public void importFromInputStream(InputStream inputStream, TestImportCallback callback) throws CdsException {
        final String METHODNAME = "importFromInputStream ";
        logger.logBegin(METHODNAME);
        try {
            TestCase testCase = FileUtils.unmarshallObject(Constants.getCdsNamespace(), inputStream, TestCase.class);
            callback.callback(new TestCaseWrapper(testCase), testCase.getGroupName(), true);
        } catch (CdsException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        } finally {
            logger.logEnd(METHODNAME);
        }
    }
}
