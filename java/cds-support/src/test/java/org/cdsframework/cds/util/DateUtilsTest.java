package org.cdsframework.cds.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author HLN Consulting, LLC
 */
public class DateUtilsTest {

    public DateUtilsTest() {
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

    /**
     * Test of getISODateFormat method, of class DateUtils.
     */
    @Test
    public void testGetISODateFormat() {
        System.out.println("getISODateFormat");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String expResult = formatter.format(date);
        String result = DateUtils.getISODateFormat(date);
        assertEquals(expResult, result);
    }

    /**
     * Test of parseISODateFormat method, of class DateUtils.
     * @throws Exception
     */
    @Test
    public void testParseISODateFormat() throws Exception {
        System.out.println("parseISODateFormat");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String expResult = formatter.format(new Date());
        String result = formatter.format(DateUtils.parseISODateFormat(expResult));
        assertEquals(expResult, result);
    }
}
