package org.cdsframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author HLN Consulting, LLC
 */
public class Constants {

    private static final String CODE_SYSTEM_PROPERTY_FILE_LOCATION = "META-INF/codeSystems.properties";
    private static final String CDS_SYSTEM_DEFAULTS_FILE_LOCATION = "META-INF/cdsSystemDefaults.properties";
    private static String CDS_NAMESPACE;
    private static URL OPENCDS_WSDL_URL;
    private static String DEFAULT_LANG_CODE;
    private static String DEFAULT_LANG_DISPLAY_NAME;
    private static String DEFAULT_LANG_OID;
    private static String GENERAL_PURPOSE_CODE;
    private static final Map<String, String> codeSystemNameOidMap = new HashMap<String, String>();

    public static String getCodeSystemOid(String codeSystemName) {
        final String METHODNAME = "getCodeSystemOid ";
        if (codeSystemName == null) {
            throw new IllegalArgumentException(METHODNAME + "codeSystemName was null!");
        }
        if (codeSystemNameOidMap.isEmpty()) {
            InputStream resource = Constants.class.getClassLoader().getResourceAsStream(CODE_SYSTEM_PROPERTY_FILE_LOCATION);
            Properties codeSystemMapProperties = new Properties();
            try {
                codeSystemMapProperties.load(resource);
            } catch (IOException e) {
                throw new IllegalArgumentException(METHODNAME + e.getMessage() + " - error retrieving " + CODE_SYSTEM_PROPERTY_FILE_LOCATION);
            }
            for (String codeSystem : codeSystemMapProperties.stringPropertyNames()) {
                codeSystemNameOidMap.put(codeSystem.trim(), codeSystemMapProperties.getProperty(codeSystem).trim());
            }
        }
        return codeSystemNameOidMap.get(codeSystemName);
    }

    public static String getCdsNamespace() {
        if (CDS_NAMESPACE == null) {
            CDS_NAMESPACE = getCdsSystemDefaultProperty("CDS_NAMESPACE");
        }
        return CDS_NAMESPACE;
    }

    public static URL getOpenCdsWsdlUrl() {
        if (OPENCDS_WSDL_URL == null) {
            OPENCDS_WSDL_URL = Constants.class.getClassLoader().getResource(getCdsSystemDefaultProperty("OPENCDS_WSDL_URL"));
        }
        return OPENCDS_WSDL_URL;
    }

    public static String getDefaultLanguageCode() {
        if (DEFAULT_LANG_CODE == null) {
            DEFAULT_LANG_CODE = getCdsSystemDefaultProperty("DEFAULT_LANG_CODE");
        }
        return DEFAULT_LANG_CODE;
    }

    public static String getDefaultLanguageDisplayName() {
        if (DEFAULT_LANG_DISPLAY_NAME == null) {
            DEFAULT_LANG_DISPLAY_NAME = getCdsSystemDefaultProperty("DEFAULT_LANG_DISPLAY_NAME");
        }
        return DEFAULT_LANG_DISPLAY_NAME;
    }

    public static String getDefaultLanguageOid() {
        if (DEFAULT_LANG_OID == null) {
            DEFAULT_LANG_OID = getCodeSystemOid("LANG");
        }
        return DEFAULT_LANG_OID;
    }

    public static String getGeneralPurposeCode() {
        if (GENERAL_PURPOSE_CODE == null) {
            GENERAL_PURPOSE_CODE = getCdsSystemDefaultProperty("GENERAL_PURPOSE_CODE");
        }
        return GENERAL_PURPOSE_CODE;
    }

    private static String getCdsSystemDefaultProperty(String propertyName) {
        final String METHODNAME = "getCdsSystemDefaultProperty ";
        String result = null;
        if (propertyName == null) {
            throw new IllegalArgumentException(METHODNAME + "propertyName is null!");
        }
        InputStream resource = Constants.class.getClassLoader().getResourceAsStream(CDS_SYSTEM_DEFAULTS_FILE_LOCATION);
        Properties cdsSystemDefaultProperties = new Properties();
        try {
            cdsSystemDefaultProperties.load(resource);
            result = cdsSystemDefaultProperties.getProperty(propertyName);
        } catch (IOException e) {
            throw new IllegalArgumentException(METHODNAME + e.getMessage() + " - error retrieving " + CDS_SYSTEM_DEFAULTS_FILE_LOCATION);
        }
        if (result == null) {
            throw new IllegalStateException(METHODNAME + "result was null!");
        }
        return result;
    }
}