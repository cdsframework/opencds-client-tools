/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdsframework.cds.testcase.importers;

import java.io.InputStream;
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
public class XlsxImporterTest {

    public XlsxImporterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
//
//    /**
//     * Test of importFromFile method, of class XlsxV1Helper.
//     * @throws Exception
//     */
//    @Ignore
//    @Test
//    public void testImportFromFileV1() throws Exception {
//        logger.info("importFromFile");
//        String filename = "ICE3 Test Cases - HepB - V1.xlsx";
//        XlsxImporter.importFromFile(filename, new TestImportCallback() {
//
//            @Override
//            public void callback(TestcaseWrapper testcase, String Group, boolean success) throws CdsException {
//                try {
//                    CdsObjectAssist.cdsObjectToFile(testcase.getTestcase(), "imported-tests", testcase.getEncodedName());
//                } catch (FileNotFoundException e) {
//                    throw new CdsException(e.getMessage());
//                } catch (IOException e) {
//                    throw new CdsException(e.getMessage());
//                }
//            }
//        });
//        assertTrue(true);
//    }
//
//    /**
//     * Test of importFromFile method, of class XlsxV1Helper.
//     * @throws Exception
//     */
//    @Ignore
//    @Test
//    public void testImportFromFileV2b() throws Exception {
//        logger.info("importFromFile");
//        String filename = "ICE3 Test Cases - HepB - V2.xlsx";
//        XlsxImporter.importFromFile(filename, new TestImportCallback() {
//
//            @Override
//            public void callback(TestcaseWrapper testcase, String Group, boolean success) throws CdsException {
//                try {
//                    CdsObjectAssist.cdsObjectToFile(testcase.getTestcase(), "imported-tests", testcase.getEncodedName());
//                } catch (FileNotFoundException e) {
//                    throw new CdsException(e.getMessage());
//                } catch (IOException e) {
//                    throw new CdsException(e.getMessage());
//                }
//            }
//        });
//        assertTrue(true);
//    }
//
//    /**
//     * Test of importFromFile method, of class XlsxV2Helper.
//     * @throws Exception
//     */
//    @Ignore
//    @Test
//    public void testImportFromFileV2a() throws Exception {
//        logger.info("importFromFile");
//        String filename = "ICE3 Test Cases - HepA.xlsx";
//        XlsxImporter.importFromFile(filename, new TestImportCallback() {
//
//            @Override
//            public void callback(TestcaseWrapper testcase, String Group, boolean success) throws CdsException {
//                try {
//                    CdsObjectAssist.cdsObjectToFile(testcase.getTestcase(), "imported-tests-v2", testcase.getEncodedName());
//                } catch (FileNotFoundException e) {
//                    throw new CdsException(e.getMessage());
//                } catch (IOException e) {
//                    throw new CdsException(e.getMessage());
//                }
//            }
//        });
//        assertTrue(true);
//    }
}
