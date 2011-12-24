package ice.dto;

import ice.exception.IceException;
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

    /**
     * Test of getCdsConceptMap method, of class CdsConceptWrapper.
     * @throws IceException
     */
    @Test
    public void testGetCdsConceptMap() throws IceException {
        System.out.println("getCdsConceptMap");
        CdsConceptWrapper instance = new CdsConceptWrapper("src/main/resources/testCodeSet.xml");
        String expResult = "ICE08";
        String result = instance.getCdsConceptMap().getOpenCdsConcept().getCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCodeSystem method, of class CdsConceptWrapper.
     * @throws IceException
     */
    @Test
    public void testGetCodeSystem() throws IceException {
        System.out.println("getCodeSystem");
        CdsConceptWrapper instance = new CdsConceptWrapper("src/main/resources/testCodeSet.xml");
        String expResult = "2.16.840.1.113883.12.292";
        String result = instance.getCodeSystem();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCodeSystemName method, of class CdsConceptWrapper.
     * @throws IceException
     */
    @Test
    public void testGetCodeSystemName() throws IceException {
        System.out.println("getCodeSystemName");
        CdsConceptWrapper instance = new CdsConceptWrapper("src/main/resources/testCodeSet.xml");
        String expResult = "Vaccines administered (CVX)";
        String result = instance.getCodeSystemName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCodeSystemMemberCodes method, of class CdsConceptWrapper.
     * @throws IceException
     */
    @Test
    public void testGetCodeSystemMemberCodes() throws IceException {
        System.out.println("getCodeSystemMemberCodes");
        CdsConceptWrapper instance = new CdsConceptWrapper("src/main/resources/testCodeSet.xml");
        List result = instance.getCodeSystemMemberCodes();
        assertTrue(result.contains("08"));
        assertTrue(result.contains("51"));
        assertTrue(result.contains("110"));
        assertTrue(result.size() == 3);
    }
}
