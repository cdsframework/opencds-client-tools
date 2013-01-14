package org.cdsframework.cds.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
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
public class ConfigurationTest {

    private Properties defaultSystemProperties;
    private Properties codeSystemProperties;

    public ConfigurationTest() throws IOException {
        defaultSystemProperties = new Properties();
        defaultSystemProperties.load(new FileInputStream("src/main/resources/META-INF/cdsSystemDefaults.properties"));
        codeSystemProperties = new Properties();
        codeSystemProperties.load(new FileInputStream("src/main/resources/META-INF/codeSystems.properties"));
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
     * Test of getCodeSystemOid method, of class Configuration.
     */
    @Test
    public void testGetCodeSystemOid() {
        System.out.println("getCodeSystemOid");
        String codeSystemName = "DISEASE";
        String expResult = codeSystemProperties.getProperty(codeSystemName);
        String result = Configuration.getCodeSystemOid(codeSystemName);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCdsNamespace method, of class Configuration.
     */
    @Test
    public void testGetCdsNamespace() {
        System.out.println("getCdsNamespace");
        String expResult = defaultSystemProperties.getProperty("CDS_NAMESPACE");
        String result = Configuration.getCdsNamespace();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCdsWsdlUrl method, of class Configuration.
     * @throws MalformedURLException
     * @throws IOException
     */
    @Test
    public void testGetCdsWsdlUrl() throws MalformedURLException, IOException {
        System.out.println("getCdsWsdlUrl");
        URL expResult = new File("src/main/resources/wsdl/evaluate.wsdl").toURI().toURL();
        String expResultContent = "";
        String resultContent = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(expResult.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            expResultContent += inputLine;
        }
        in.close();
        URL result = Configuration.getCdsWsdlUrl();
        in = new BufferedReader(new InputStreamReader(result.openStream()));
        while ((inputLine = in.readLine()) != null) {
            resultContent += inputLine;
        }
        in.close();
        assertEquals(expResultContent, resultContent);
    }

    /**
     * Test of getCdsNamespaceUri method, of class Configuration.
     */
    @Test
    public void testGetCdsNamespaceUri() {
        System.out.println("getCdsNamespaceUri");
        String expResult = defaultSystemProperties.getProperty("CDS_NAMESPACE_URI");
        String result = Configuration.getCdsNamespaceUri();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCdsLocalPart method, of class Configuration.
     */
    @Test
    public void testGetCdsLocalPart() {
        System.out.println("getCdsLocalPart");
        String expResult = defaultSystemProperties.getProperty("CDS_LOCAL_PART");
        String result = Configuration.getCdsLocalPart();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDefaultLanguageCode method, of class Configuration.
     */
    @Test
    public void testGetDefaultLanguageCode() {
        System.out.println("getDefaultLanguageCode");
        String expResult = defaultSystemProperties.getProperty("DEFAULT_LANG_CODE");
        String result = Configuration.getDefaultLanguageCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDefaultLanguageDisplayName method, of class Configuration.
     */
    @Test
    public void testGetDefaultLanguageDisplayName() {
        System.out.println("getDefaultLanguageDisplayName");
        String expResult = defaultSystemProperties.getProperty("DEFAULT_LANG_DISPLAY_NAME");
        String result = Configuration.getDefaultLanguageDisplayName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDefaultLanguageOid method, of class Configuration.
     */
    @Test
    public void testGetDefaultLanguageOid() {
        System.out.println("getDefaultLanguageOid");
        String expResult = codeSystemProperties.getProperty("LANG");
        String result = Configuration.getDefaultLanguageOid();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeneralPurposeCode method, of class Configuration.
     */
    @Test
    public void testGetGeneralPurposeCode() {
        System.out.println("getGeneralPurposeCode");
        String expResult = defaultSystemProperties.getProperty("GENERAL_PURPOSE_CODE");
        String result = Configuration.getGeneralPurposeCode();
        assertEquals(expResult, result);
    }
}
