package org.cdsframework.cds.testcase.importers;

import java.io.InputStream;
import org.cdsframework.cds.testcase.TestCaseWrapper;
import org.cdsframework.cds.exceptions.CdsException;
import org.cdsframework.cds.util.MarshalUtils;
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
            TestCase testCase = MarshalUtils.unmarshal(inputStream, TestCase.class);
            callback.callback(new TestCaseWrapper(testCase), testCase.getGroupName(), true);
        } catch (CdsException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        } finally {
            logger.logEnd(METHODNAME);
        }
    }
}
