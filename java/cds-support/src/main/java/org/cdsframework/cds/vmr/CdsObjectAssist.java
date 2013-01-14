package org.cdsframework.cds.vmr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.cdsframework.cds.exceptions.CdsException;
import org.cdsframework.cds.util.MarshalUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsObjectAssist {

    protected final static Logger logger = Logger.getLogger(CdsObjectAssist.class);

    public static <S> byte[] cdsObjectToByteArray(S cdsObject, Class<S> cdsObjectClass)
            throws CdsException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        MarshalUtils.marshal(cdsObject, stream);
        return stream.toByteArray();
    }

    public static <S> String cdsObjectToFile(S cdsObject, String path, String filename)
            throws CdsException, FileNotFoundException, IOException {
        String fullPath = (path == null || path.isEmpty() ? "" : path + "/") + filename + ".xml";

        File file = new File(fullPath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            MarshalUtils.marshal(cdsObject, fileOutputStream);
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return fullPath;
    }

    public static <S> S cdsObjectFromFile(String fileName, Class<S> cdsObjectClass)
            throws CdsException {
        S cdsObject = null;
        try {
            cdsObject = cdsObjectFromStream(new FileInputStream(fileName), cdsObjectClass);
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        }
        return cdsObject;
    }

    public static <S> S cdsObjectFromByteArray(byte[] bytes, Class<S> cdsObjectClass)
            throws CdsException {
        return cdsObjectFromStream(new ByteArrayInputStream(bytes), cdsObjectClass);
    }

    public static <S> S cdsObjectFromStream(InputStream inputStream, Class<S> cdsObjectClass)
            throws CdsException {
        S cdsObject = null;
        cdsObject = MarshalUtils.unmarshal(inputStream, cdsObjectClass);

        return cdsObject;
    }

    public static <S> String cdsObjectToString(S cdsObject, Class<S> cdsObjectClass)
            throws CdsException {
        return new String(cdsObjectToByteArray(cdsObject, cdsObjectClass));
    }
}
