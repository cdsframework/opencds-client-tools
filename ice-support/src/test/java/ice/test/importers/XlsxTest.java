package ice.test.importers;

import org.junit.Ignore;
import java.io.IOException;
import java.io.FileNotFoundException;
import ice.dto.support.TestImportCallback;
import ice.dto.support.CdsObjectAssist;
import ice.dto.TestcaseWrapper;
import ice.exception.IceException;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author HLN Consulting, LLC
 */
public class XlsxTest {

    private final static Logger logger = Logger.getLogger(XlsxTest.class);

    public XlsxTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of importFromFile method, of class Xlsx.
     * @throws Exception
     */
    @Test
    @Ignore
    public void testImportFromFile() throws Exception {
        logger.info("importFromFile");
        String filename = "ICE3 Test Cases - HepB.xlsx";
        Xlsx.importFromFile(filename, new TestImportCallback() {

            @Override
            public void callback(TestcaseWrapper testcase) throws IceException {
                try {
                    CdsObjectAssist.cdsObjectToFile(testcase.getTestcase(), "imported-tests", testcase.getName());
                } catch (FileNotFoundException e) {
                    throw new IceException(e.getMessage());
                } catch (IOException e) {
                    throw new IceException(e.getMessage());
                }
            }
        });
        assertTrue(true);
    }
}
