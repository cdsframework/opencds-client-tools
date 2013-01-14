package org.cdsframework.cds.testcase.importers;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cdsframework.cds.exceptions.CdsException;

/**
 *
 * @author HLN Consulting, LLC
 */
public class XlsxImporter extends TestCaseImporter {

	public XlsxImporter() {
		super(XlsxImporter.class);
	}

	public void importFromFile(byte[] data, TestImportCallback callback)
			throws CdsException, FileNotFoundException, IOException,
			DatatypeConfigurationException {
		importFromInputStream(new ByteArrayInputStream(data), callback);
	}

    @Override
	public void importFromFile(String filename, TestImportCallback callback)
			throws CdsException {
		try {
			importFromInputStream(new FileInputStream(filename), callback);
		} catch (FileNotFoundException e) {
			throw new CdsException(e.getMessage());
		} finally {

		}
	}

	@Override
	public void importFromInputStream(InputStream inputStream,
			TestImportCallback callback) throws CdsException {
		final String METHODNAME = "importFromInputStream ";
		logger.logBegin(METHODNAME);
		try {

			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			POIXMLProperties properties = wb.getProperties();
			CoreProperties coreProperties = properties.getCoreProperties();
			String category = coreProperties.getCategory();
			logger.info("category: " + category);
			if ("V1".equals(category)) {
				XlsxV1Helper.importFromWorkBook(wb, callback);
			} else if ("CDC".equals(category)) {
				XlsxImporterUtils.importFromWorkBook(wb, callback);
			} else {
				XlsxV2Helper.importFromWorkBook(wb, callback);
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
			throw new CdsException(e.getMessage());
		} catch (CdsException e) {
			logger.error(e);
			throw new CdsException(e.getMessage());
		} catch (IOException e) {
			logger.error(e);
			throw new CdsException(e.getMessage());
		} catch (DatatypeConfigurationException e) {
			logger.error(e);
			throw new CdsException(e.getMessage());
		} finally {
			logger.logEnd(METHODNAME);
		}
	}
}
