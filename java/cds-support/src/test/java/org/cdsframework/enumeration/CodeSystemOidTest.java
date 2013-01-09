package org.cdsframework.enumeration;

import org.cdsframework.enumeration.CodeSystemOid;
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
public class CodeSystemOidTest {

    public CodeSystemOidTest() {
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
     * Test of values method, of class CodeSystemOid.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        CodeSystemOid[] result = CodeSystemOid.values();
        for (CodeSystemOid item : result) {
            System.out.println(item);
            String oid = item.getOid();
            System.out.println(oid);
            assertEquals(item, CodeSystemOid.valueOfOid(oid));
            System.out.println("-----------------");
        }
    }

    /**
     * Test of valueOf method, of class CodeSystemOid.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "TARGET_RELATIONSHIP_TO_SOURCE";
        CodeSystemOid expResult = CodeSystemOid.TARGET_RELATIONSHIP_TO_SOURCE;
        CodeSystemOid result = CodeSystemOid.valueOf(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of getOid method, of class CodeSystemOid.
     */
    @Test
    public void testGetOid() {
        System.out.println("getOid");
        CodeSystemOid instance = CodeSystemOid.TARGET_RELATIONSHIP_TO_SOURCE;
        String expResult = "2.16.840.1.113883.5.1002";
        String result = instance.getOid();
        assertEquals(expResult, result);
    }

    /**
     * Test of valueOfOid method, of class CodeSystemOid.
     */
    @Test
    public void testValueOfOid() {
        System.out.println("valueOfOid");
        String oid = "2.16.840.1.113883.5.1002";
        CodeSystemOid expResult = CodeSystemOid.TARGET_RELATIONSHIP_TO_SOURCE;
        CodeSystemOid result = CodeSystemOid.valueOfOid(oid);
        assertEquals(expResult, result);
    }
}
