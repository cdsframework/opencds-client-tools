package org.cdsframework.cds.testcase.importers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.cdsframework.cds.exceptions.CdsException;
import org.cdsframework.cds.testcase.importers.TestImportCallback;

/**
 *
 * @author HLN Consulting, LLC
 */
public class ZipXmlImporter extends TestCaseImporter {

    public ZipXmlImporter() {
        super(ZipXmlImporter.class);
    }

    @Override
    public void importFromInputStream(InputStream inputStream, TestImportCallback callback) throws CdsException {
        final String METHODNAME = "importFromInputStream ";
        logger.logBegin(METHODNAME);
        ZipEntry zipEntry = null;
        File tmpFile = null;
        FileOutputStream tmpFileOS = null;
        ZipInputStream zipInputStream = null;
        try {
            if (inputStream != null) {
                zipInputStream = new ZipInputStream(inputStream);
                if (zipInputStream != null) {
                    XmlImporter xmlImporter = new XmlImporter();
                    while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                        logger.info("Processing: ", zipEntry.getName());
                        if (zipEntry.getName() != null && zipEntry.getName().endsWith("xml")) {
                            tmpFile = File.createTempFile("g54bbrtbe", ".zip");
                            if (tmpFile != null) {
                                tmpFile.deleteOnExit();
                                tmpFileOS = new FileOutputStream(tmpFile);
                                if (tmpFileOS != null) {
                                    int nRead;
                                    byte[] buffer = new byte[16384];
                                    while ((nRead = zipInputStream.read(buffer, 0, buffer.length)) != -1) {
                                        tmpFileOS.write(buffer, 0, nRead);
                                    }
                                    zipInputStream.closeEntry();
                                    tmpFileOS.close();
                                    InputStream entryInputStream = null;
                                    try {
                                        entryInputStream = new FileInputStream(tmpFile);
                                        xmlImporter.importFromInputStream(entryInputStream, callback);
                                    } finally {
                                        try {
                                            entryInputStream.close();
                                        } catch (Exception e) {
                                            // do nothing...
                                        }
                                    }
                                } else {
                                    logger.error("tmpFileOS is null!");
                                }
                            } else {
                                logger.error("tmpFile is null!");
                            }
                        } else {
                            logger.warn("Skipping: ", zipEntry.getName());
                            zipInputStream.closeEntry();
                        }
                    }
                } else {
                    logger.error("zipInputStream is null!");
                }
            } else {
                logger.error("inputStream is null!");
            }
        } catch (IOException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        } finally {
            try {
                zipInputStream.close();
            } catch (Exception e) {
                // do nothing.
            }
            try {
                tmpFileOS.close();
            } catch (Exception e) {
                // do nothing.
            }
        }
    }
}
