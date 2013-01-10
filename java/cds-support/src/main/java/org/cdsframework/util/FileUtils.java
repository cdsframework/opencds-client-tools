package org.cdsframework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.cdsframework.exceptions.CdsException;

/**
 *
 * @author HLN Consulting, LLC
 */
public class FileUtils {

    private static final LogUtils logger = LogUtils.getLogger(FileUtils.class);
    private static final int CHARS_PER_PAGE = 5000;

    public static byte[] getZipOfFiles(Map<String, byte[]> fileMap) throws CdsException {
        final String METHODNAME = "getZipOfFiles ";
        byte[] result = new byte[]{};
        ByteArrayOutputStream bos = null;
        ZipOutputStream zipfile = null;
        try {
            bos = new ByteArrayOutputStream();
            if (bos != null) {
                zipfile = new ZipOutputStream(bos);
                zipfile.setLevel(Deflater.BEST_COMPRESSION);
                if (zipfile != null) {
                    for (Map.Entry<String, byte[]> fileEntry : fileMap.entrySet()) {
                        if (fileEntry != null) {
                            if (logger.isTraceEnabled()) {
                                logger.info("processing: ", fileEntry.getKey());
                                logger.info("data: ", fileEntry.getValue());
                                if (fileEntry.getValue() != null) {
                                    logger.info("data: ", new String(fileEntry.getValue()));
                                }
                            }
                            ZipEntry zipentry = new ZipEntry(fileEntry.getKey());
                            zipfile.putNextEntry(zipentry);
                            zipfile.write(fileEntry.getValue());
                            zipfile.closeEntry();
                        } else {
                            logger.error("fileEntry was null!");
                        }
                    }
                    zipfile.close();
                    result = bos.toByteArray();
                } else {
                    logger.error("zipfile was null!");
                }
            } else {
                logger.error("bos was null!");
            }
        } catch (IOException e) {
            throw new CdsException(METHODNAME + "IOException: " + e.getMessage());
        } finally {
            try {
                zipfile.close();
            } catch (Exception e) {
                //do nothing
            }
            try {
                bos.close();
            } catch (Exception e) {
                //do nothing
            }
        }
        return result;
    }

    public static String getStringFromJarFile(String path) {
        final String METHODNAME = "getStringFromJarFile ";
        String result = null;
        InputStreamReader input = null;
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(path);
            if (resourceAsStream != null) {
                input = new InputStreamReader(resourceAsStream);
                if (input != null) {
                    final char[] buffer = new char[CHARS_PER_PAGE];
                    StringBuilder sb = new StringBuilder(CHARS_PER_PAGE);
                    try {
                        for (int read = input.read(buffer, 0, buffer.length);
                                read != -1;
                                read = input.read(buffer, 0, buffer.length)) {
                            sb.append(buffer, 0, read);
                        }
                    } catch (IOException ioe) {
                        // ignore
                    }
                    result = sb.toString();
                } else {
                    logger.error(METHODNAME, "InputStreamReader was null for: " + path);
                }
            } else {
                logger.info(METHODNAME, "No data resource exists for: " + path);
            }
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                //do nothing
            }
            try {
                resourceAsStream.close();
            } catch (Exception e) {
                //do nothing
            }
        }
        return result;
    }

    public static byte[] marshallObject(String nameSpace, Object dataObject) {
        byte[] result = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(nameSpace);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            marshaller.marshal(dataObject, stream);
            result = stream.toByteArray();
        } catch (JAXBException e) {
            logger.error("JAXB marshall error: ", e.getMessage());
        } finally {
        }
        return result;
    }

    public static <S> S unmarshallObject(String nameSpace, InputStream inputStream, Class<S> cdsObjectClass) {
        S result = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(nameSpace);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            result = (S) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            logger.error("JAXB unmarshall error: ", e.getMessage());
        } finally {
        }
        return result;
    }
}
