package ice.test.importers;

import ice.dto.support.TestImportCallback;
import ice.exception.IceException;
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
        } else if ("V2".equals(category)) {
            XlsxV2.importFromFile(wb, callback);
        } else {
            throw new IceException("Import format unspecified - must supply a version in the categories property of the workbook: either V1 or V2 - got: " + category);
        }
    }
}
