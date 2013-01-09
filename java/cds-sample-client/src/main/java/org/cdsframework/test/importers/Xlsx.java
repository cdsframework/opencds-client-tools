package org.cdsframework.test.importers;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cdsframework.dto.support.TestImportCallback;
import org.cdsframework.exception.IceException;

/**
 *
 * @author HLN Consulting, LLC
 */
public class Xlsx {

    private final static Logger logger = Logger.getLogger(Xlsx.class);

    public static void importFromFile(byte[] data, TestImportCallback callback)
            throws IceException, FileNotFoundException, IOException, DatatypeConfigurationException {
        importFromFile(new ByteArrayInputStream(data), callback);
    }

    public static void importFromFile(String filename, TestImportCallback callback)
            throws IceException, FileNotFoundException, IOException, DatatypeConfigurationException {
        importFromFile(new FileInputStream(filename), callback);
    }

    public static void importFromFile(InputStream inputStream, TestImportCallback callback)
            throws IceException, FileNotFoundException, IOException, DatatypeConfigurationException {
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        POIXMLProperties properties = wb.getProperties();
        CoreProperties coreProperties = properties.getCoreProperties();
        String category = coreProperties.getCategory();
        logger.info("category: " + category);
        if ("V1".equals(category)) {
            XlsxV1.importFromFile(wb, callback);
        } else {
            XlsxV2.importFromFile(wb, callback);
        }
    }
}
