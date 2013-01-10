package org.cdsframework.dto;

import org.cdsframework.service.CdsConceptWrapper;

import java.util.List;
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
public class CdsConceptWrapperTest {

    public CdsConceptWrapperTest() {
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
//
//    /**
//     * Test of getCdsConceptMap method, of class CdsConceptWrapper.
//     * @throws CdsException
//     */
//    @Test
//    public void testGetCdsConceptMap() throws CdsException {
//        System.out.println("getCdsConceptMap");
//        CdsConceptWrapper instance = new CdsConceptWrapper("src/main/resources/testCodeSet.xml");
//        String expResult = "ICE08";
//        String result = instance.getCdsConceptMap().getOpenCdsConcept().getCode();
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of getCodeSystem method, of class CdsConceptWrapper.
//     * @throws CdsException
//     */
//    @Test
//    public void testGetCodeSystem() throws CdsException {
//        System.out.println("getCodeSystem");
//        CdsConceptWrapper instance = new CdsConceptWrapper("src/main/resources/testCodeSet.xml");
//        String expResult = "2.16.840.1.113883.12.292";
//        String result = instance.getCodeSystem();
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of getCodeSystemName method, of class CdsConceptWrapper.
//     * @throws CdsException
//     */
//    @Test
//    public void testGetCodeSystemName() throws CdsException {
//        System.out.println("getCodeSystemName");
//        CdsConceptWrapper instance = new CdsConceptWrapper("src/main/resources/testCodeSet.xml");
//        String expResult = "Vaccines administered (CVX)";
//        String result = instance.getCodeSystemName();
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of getCodeSystemMemberCodes method, of class CdsConceptWrapper.
//     * @throws CdsException
//     */
//    @Test
//    public void testGetCodeSystemMemberCodes() throws CdsException {
//        System.out.println("getCodeSystemMemberCodes");
//        CdsConceptWrapper instance = new CdsConceptWrapper("src/main/resources/testCodeSet.xml");
//        boolean contains08 = false;
//        boolean contains51 = false;
//        boolean contains110 = false;
//        for (CD cd : instance.getCodeSystemMemberCodes()) {
//            if ("08".equals(cd.getCode())) {
//                contains08 = true;
//            }
//            if ("51".equals(cd.getCode())) {
//                contains51 = true;
//            }
//            if ("110".equals(cd.getCode())) {
//                contains110 = true;
//            }
//        }
//        assertTrue(contains08);
//        assertTrue(contains51);
//        assertTrue(contains110);
//    }
}
