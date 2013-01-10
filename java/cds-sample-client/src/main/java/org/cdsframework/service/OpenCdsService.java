package org.cdsframework.service;

import com.sun.xml.ws.client.BindingProviderProperties;
import com.sun.xml.ws.developer.JAXWSProperties;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.util.Utilities;
import org.cdsframework.util.support.cds.CdsObjectAssist;
import org.omg.spec.cdss._201105.dss.DataRequirementItemData;
import org.omg.spec.cdss._201105.dss.EntityIdentifier;
import org.omg.spec.cdss._201105.dss.EvaluationRequest;
import org.omg.spec.cdss._201105.dss.EvaluationResponse;
import org.omg.spec.cdss._201105.dss.FinalKMEvaluationResponse;
import org.omg.spec.cdss._201105.dss.InteractionIdentifier;
import org.omg.spec.cdss._201105.dss.ItemIdentifier;
import org.omg.spec.cdss._201105.dss.KMEvaluationRequest;
import org.omg.spec.cdss._201105.dss.KMEvaluationResultData;
import org.omg.spec.cdss._201105.dss.SemanticPayload;
import org.omg.spec.cdss._201105.dsswsdl.DSSRuntimeExceptionFault;
import org.omg.spec.cdss._201105.dsswsdl.DecisionSupportService;
import org.omg.spec.cdss._201105.dsswsdl.Evaluation;
import org.omg.spec.cdss._201105.dsswsdl.EvaluationExceptionFault;
import org.omg.spec.cdss._201105.dsswsdl.InvalidDriDataFormatExceptionFault;
import org.omg.spec.cdss._201105.dsswsdl.InvalidTimeZoneOffsetExceptionFault;
import org.omg.spec.cdss._201105.dsswsdl.RequiredDataNotProvidedExceptionFault;
import org.omg.spec.cdss._201105.dsswsdl.UnrecognizedLanguageExceptionFault;
import org.omg.spec.cdss._201105.dsswsdl.UnrecognizedScopedEntityExceptionFault;
import org.omg.spec.cdss._201105.dsswsdl.UnsupportedLanguageExceptionFault;
import org.opencds.support.CdsInput;
import org.opencds.support.CdsOutput;

/**
 *
 * @author HLN Consulting, LLC
 */
public class OpenCdsService {

    private final static Logger logger = Logger.getLogger(OpenCdsService.class);
    private final static int DEFAULT_TIMEOUT = 10 * 1000;
    private final DecisionSupportService openCdsService = new DecisionSupportService();
    private final Evaluation evaluatePort = openCdsService.getEvaluate();
    private final String endPoint;
    private final int requestTimeout;
    private final int connectTimeout;

    public OpenCdsService(String endPoint) {
        this(endPoint, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
    }


    public OpenCdsService(String endPoint, int timeout) {
        this(endPoint, timeout, timeout);
    }

    public OpenCdsService(String endPoint, int requestTimeout, int connectTimeout) {
        super();
        this.endPoint = endPoint;
        this.requestTimeout = requestTimeout;
        this.connectTimeout = connectTimeout;
        Map<String, Object> ctxt = ((BindingProvider) evaluatePort).getRequestContext();
        ctxt.put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, 8192);
        ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint);
        ctxt.put(BindingProviderProperties.REQUEST_TIMEOUT, requestTimeout);
        ctxt.put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeout);
        ctxt.put("com.sun.xml.internal.ws.connect.timeout", connectTimeout);
        ctxt.put("com.sun.xml.internal.ws.request.timeout", requestTimeout);
        ctxt.put("org.jboss.ws.timeout", requestTimeout);
        ctxt.put(JAXWSProperties.CONNECT_TIMEOUT, connectTimeout);
        ctxt.put(JAXWSProperties.REQUEST_TIMEOUT, requestTimeout);
    }

    public static OpenCdsService getOpenCDS(String endPoint) {
        return new OpenCdsService(endPoint);
    }

    public static OpenCdsService getOpenCDS(String endPoint, int timeout) {
        return new OpenCdsService(endPoint, timeout);
    }

    public static OpenCdsService getOpenCDS(String endPoint, int requestTimeout, int connectTimeout) {
        return new OpenCdsService(endPoint, requestTimeout, connectTimeout);
    }

    public CdsOutput evaluate(CdsInput cdsInput, String scopingEntityId, String businessId, String version, Date executionDate) throws CdsException {
        byte[] cdsObjectToByteArray = CdsObjectAssist.cdsObjectToByteArray(cdsInput, CdsInput.class);
        byte[] evaluation = evaluate(cdsObjectToByteArray, scopingEntityId, businessId, version, executionDate);
        CdsOutput cdsOutput = CdsObjectAssist.cdsObjectFromByteArray(evaluation, CdsOutput.class);
        return cdsOutput;
    }

    public byte[] evaluate(byte[] cdsInputByteArray, String scopingEntityId, String businessId, String version, Date executionDate) throws CdsException {
        final String METHODNAME = "evaluate ";
        if (true) {
            logger.info(METHODNAME
                    + "calling evaluateAtSpecifiedTime with businessId '"
                    + businessId
                    + "; scopingEntityId "
                    + scopingEntityId
                    + "; version "
                    + version
                    + "; executionDate "
                    + executionDate
                    + "' @ "
                    + endPoint
                    + " with requestTimeout:"
                    + requestTimeout
                    + " and connectTimeout:"
                    + connectTimeout);
        }
        long start = System.nanoTime();
        EvaluationResponse response = null;
        byte[] result = null;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(executionDate);

        try {
            XMLGregorianCalendar executionTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

            InteractionIdentifier interactionIdentifier = getInteractionIdentifier();
            EvaluationRequest evaluationRequest = getEvaluationRequest(cdsInputByteArray, scopingEntityId, businessId, version);
            Utilities.logDuration("evaluate init", start);

            start = System.nanoTime();
            response = evaluatePort.evaluateAtSpecifiedTime(interactionIdentifier, executionTime, evaluationRequest);
            Utilities.logDuration("evaluate execute", start);

            start = System.nanoTime();
            if (response == null) {
                throw new CdsException("response is null!");
            }
            List<FinalKMEvaluationResponse> finalKMEvaluationResponse = response.getFinalKMEvaluationResponse();
            if (finalKMEvaluationResponse == null) {
                throw new CdsException("finalKMEvaluationResponse is null!");
            }
            if (finalKMEvaluationResponse.size() != 1) {
                throw new CdsException("finalKMEvaluationResponse size wrong: " + finalKMEvaluationResponse.size());
            }
            FinalKMEvaluationResponse kmEvaluationResponse = finalKMEvaluationResponse.get(0);
            List<KMEvaluationResultData> kmEvaluationResultData = kmEvaluationResponse.getKmEvaluationResultData();
            if (kmEvaluationResultData == null) {
                throw new CdsException("kmEvaluationResultData is null!");
            }
            if (kmEvaluationResultData.size() != 1) {
                throw new CdsException("kmEvaluationResultData size wrong: " + kmEvaluationResultData.size());
            }
            KMEvaluationResultData resultData = kmEvaluationResultData.get(0);
            if (resultData == null) {
                throw new CdsException("resultData is null!");
            }
            SemanticPayload data = resultData.getData();
            if (data == null) {
                throw new CdsException("data is null!");
            }
            List<byte[]> base64EncodedPayload = data.getBase64EncodedPayload();
            if (base64EncodedPayload == null) {
                throw new CdsException("base64EncodedPayload is null!");
            }
            if (base64EncodedPayload.size() != 1) {
                throw new CdsException("base64EncodedPayload size wrong: " + base64EncodedPayload.size());
            }
            result = base64EncodedPayload.get(0);
            if (result == null) {
                throw new CdsException("bytes is null!");
            }

        } catch (DSSRuntimeExceptionFault e) {
            throw new CdsException(e.getMessage());
        } catch (EvaluationExceptionFault e) {
            throw new CdsException(e.getMessage());
        } catch (InvalidDriDataFormatExceptionFault e) {
            throw new CdsException(e.getMessage());
        } catch (InvalidTimeZoneOffsetExceptionFault e) {
            throw new CdsException(e.getMessage());
        } catch (RequiredDataNotProvidedExceptionFault e) {
            throw new CdsException(e.getMessage());
        } catch (UnrecognizedLanguageExceptionFault e) {
            throw new CdsException(e.getMessage());
        } catch (UnrecognizedScopedEntityExceptionFault e) {
            throw new CdsException(e.getMessage());
        } catch (UnsupportedLanguageExceptionFault e) {
            throw new CdsException(e.getMessage());
        } catch (DatatypeConfigurationException e) {
            throw new CdsException(e.getMessage());
        } finally {
            logger.debug(METHODNAME + "end...");
        }
        Utilities.logDuration("evaluate post process", start);
        return result;
    }

    private EntityIdentifier getIIEntityIdentifier(String businessId) {
        EntityIdentifier iiEntityIdentifier = new EntityIdentifier();
        iiEntityIdentifier.setScopingEntityId("gov.nyc.health");
        iiEntityIdentifier.setVersion("1.0.0.0");
        iiEntityIdentifier.setBusinessId(businessId + "Data");
        return iiEntityIdentifier;
    }

    private ItemIdentifier getItemIdentifier(String businessId) {
        ItemIdentifier itemIdentifier = new ItemIdentifier();
        itemIdentifier.setItemId("cdsPayload");
        itemIdentifier.setContainingEntityId(getIIEntityIdentifier(businessId));
        return itemIdentifier;
    }

    private EntityIdentifier getSPEntityIdentifier() {
        EntityIdentifier spEntityIdentifier = new EntityIdentifier();
        spEntityIdentifier.setBusinessId("VMR");
        spEntityIdentifier.setScopingEntityId("org.opencds.vmr");
        spEntityIdentifier.setVersion("1.0");
        return spEntityIdentifier;
    }

    private SemanticPayload getSemanticPayload(byte[] cdsInputByteArray) {
        SemanticPayload semanticPayload = new SemanticPayload();
        semanticPayload.setInformationModelSSId(getSPEntityIdentifier());
        semanticPayload.getBase64EncodedPayload().add(cdsInputByteArray);
        return semanticPayload;
    }

    private DataRequirementItemData getDataRequirementItemData(byte[] cdsInputByteArray, String businessId) {
        DataRequirementItemData dataRequirementItemData = new DataRequirementItemData();
        dataRequirementItemData.setDriId(getItemIdentifier(businessId));
        dataRequirementItemData.setData(getSemanticPayload(cdsInputByteArray));
        return dataRequirementItemData;
    }

    private InteractionIdentifier getInteractionIdentifier() throws DatatypeConfigurationException {
        InteractionIdentifier interactionIdentifier = new InteractionIdentifier();
        interactionIdentifier.setInteractionId("123456");
        interactionIdentifier.setScopingEntityId("gov.nyc.health");
        // interactionIdentifier.setSubmissionTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
        return interactionIdentifier;
    }

    private EntityIdentifier getKMEntityIdentifier(String scopingEntityId, String businessId, String version) {
        EntityIdentifier kmEntityIdentifier = new EntityIdentifier();
        kmEntityIdentifier.setScopingEntityId(scopingEntityId);
        kmEntityIdentifier.setVersion(version);
        kmEntityIdentifier.setBusinessId(businessId);
        return kmEntityIdentifier;
    }

    private KMEvaluationRequest getKMEvaluationRequest(String scopingEntityId, String businessId, String version) {
        KMEvaluationRequest kmEvaluationRequest = new KMEvaluationRequest();
        kmEvaluationRequest.setKmId(getKMEntityIdentifier(scopingEntityId, businessId, version));
        return kmEvaluationRequest;
    }

    private EvaluationRequest getEvaluationRequest(byte[] cdsInputByteArray, String scopingEntityId, String businessId, String version) {
        EvaluationRequest evaluationRequest = new EvaluationRequest();
        evaluationRequest.setClientLanguage("");
        evaluationRequest.setClientTimeZoneOffset("");
        evaluationRequest.getKmEvaluationRequest().add(getKMEvaluationRequest(scopingEntityId, businessId, version));
        evaluationRequest.getDataRequirementItemData().add(getDataRequirementItemData(cdsInputByteArray, businessId));
        return evaluationRequest;
    }
}
