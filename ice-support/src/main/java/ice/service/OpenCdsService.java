package ice.service;

import com.sun.xml.ws.developer.JAXWSProperties;
import ice.dto.support.CdsObjectAssist;
import ice.exception.IceException;
import ice.util.Utilities;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;
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
import org.opencds.CdsInput;
import org.opencds.CdsOutput;

/**
 *
 * @author HLN Consulting, LLC
 */
public class OpenCdsService {

    private final static Logger logger = Logger.getLogger(OpenCdsService.class);
    private final DecisionSupportService openCdsService = new DecisionSupportService();
    private final Evaluation evaluatePort = openCdsService.getEvaluate();
    private final String endPoint;

    public OpenCdsService() {
        endPoint = null;
    }

    public OpenCdsService(String endPoint) {
        super();
        this.endPoint = endPoint;
        Map<String, Object> ctxt = ((BindingProvider) evaluatePort).getRequestContext();
        ctxt.put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, 8192);
        ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint);
    }

    public static OpenCdsService getOpenCDS() {
        return new OpenCdsService();
    }

    public static OpenCdsService getOpenCDS(String endPoint) {
        return new OpenCdsService(endPoint);
    }

    public CdsOutput evaluate(CdsInput cdsInput, String businessId, Date executionDate) throws IceException {
        byte[] cdsObjectToByteArray = CdsObjectAssist.cdsObjectToByteArray(cdsInput, CdsInput.class);
        byte[] evaluation = evaluate(cdsObjectToByteArray, businessId, executionDate);
        CdsOutput cdsOutput = CdsObjectAssist.cdsObjectFromByteArray(evaluation, CdsOutput.class);
        return cdsOutput;
    }

    public byte[] evaluate(byte[] cdsInputByteArray, String businessId, Date executionDate) throws IceException {
        final String METHODNAME = "evaluate ";
        logger.debug(METHODNAME + "begin...");
        long start = System.nanoTime();
        EvaluationResponse response = null;
        byte[] result = null;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(executionDate);

        try {
            InteractionIdentifier interactionIdentifier = getInteractionIdentifier(gc);
            EvaluationRequest evaluationRequest = getEvaluationRequest(cdsInputByteArray, businessId);
            Utilities.logDuration("evaluate init", start);

            start = System.nanoTime();
            response = evaluatePort.evaluate(interactionIdentifier, evaluationRequest);
            Utilities.logDuration("evaluate execute", start);

            start = System.nanoTime();
            if (response == null) {
                throw new IceException("response is null!");
            }
            List<FinalKMEvaluationResponse> finalKMEvaluationResponse = response.getFinalKMEvaluationResponse();
            if (finalKMEvaluationResponse == null) {
                throw new IceException("finalKMEvaluationResponse is null!");
            }
            if (finalKMEvaluationResponse.size() != 1) {
                throw new IceException("finalKMEvaluationResponse size wrong: " + finalKMEvaluationResponse.size());
            }
            FinalKMEvaluationResponse kmEvaluationResponse = finalKMEvaluationResponse.get(0);
            List<KMEvaluationResultData> kmEvaluationResultData = kmEvaluationResponse.getKmEvaluationResultData();
            if (kmEvaluationResultData == null) {
                throw new IceException("kmEvaluationResultData is null!");
            }
            if (kmEvaluationResultData.size() != 1) {
                throw new IceException("kmEvaluationResultData size wrong: " + kmEvaluationResultData.size());
            }
            KMEvaluationResultData resultData = kmEvaluationResultData.get(0);
            if (resultData == null) {
                throw new IceException("resultData is null!");
            }
            SemanticPayload data = resultData.getData();
            if (data == null) {
                throw new IceException("data is null!");
            }
            List<byte[]> base64EncodedPayload = data.getBase64EncodedPayload();
            if (base64EncodedPayload == null) {
                throw new IceException("base64EncodedPayload is null!");
            }
            if (base64EncodedPayload.size() != 1) {
                throw new IceException("base64EncodedPayload size wrong: " + base64EncodedPayload.size());
            }
            result = base64EncodedPayload.get(0);
            if (result == null) {
                throw new IceException("bytes is null!");
            }

        } catch (DSSRuntimeExceptionFault e) {
            throw new IceException(e.getMessage());
        } catch (EvaluationExceptionFault e) {
            throw new IceException(e.getMessage());
        } catch (InvalidDriDataFormatExceptionFault e) {
            throw new IceException(e.getMessage());
        } catch (InvalidTimeZoneOffsetExceptionFault e) {
            throw new IceException(e.getMessage());
        } catch (RequiredDataNotProvidedExceptionFault e) {
            throw new IceException(e.getMessage());
        } catch (UnrecognizedLanguageExceptionFault e) {
            throw new IceException(e.getMessage());
        } catch (UnrecognizedScopedEntityExceptionFault e) {
            throw new IceException(e.getMessage());
        } catch (UnsupportedLanguageExceptionFault e) {
            throw new IceException(e.getMessage());
        } catch (DatatypeConfigurationException e) {
            throw new IceException(e.getMessage());
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

    private InteractionIdentifier getInteractionIdentifier(GregorianCalendar gc) throws DatatypeConfigurationException {
        InteractionIdentifier interactionIdentifier = new InteractionIdentifier();
        interactionIdentifier.setInteractionId("123456");
        interactionIdentifier.setScopingEntityId("gov.nyc.health");
        interactionIdentifier.setSubmissionTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
        return interactionIdentifier;
    }

    private EntityIdentifier getKMEntityIdentifier(String businessId) {
        EntityIdentifier kmEntityIdentifier = new EntityIdentifier();
        kmEntityIdentifier.setScopingEntityId("org.opencds");
        kmEntityIdentifier.setVersion("1.5.3");
        kmEntityIdentifier.setBusinessId(businessId);
        return kmEntityIdentifier;
    }

    private KMEvaluationRequest getKMEvaluationRequest(String businessId) {
        KMEvaluationRequest kmEvaluationRequest = new KMEvaluationRequest();
        kmEvaluationRequest.setKmId(getKMEntityIdentifier(businessId));
        return kmEvaluationRequest;
    }

    private EvaluationRequest getEvaluationRequest(byte[] cdsInputByteArray, String businessId) {
        EvaluationRequest evaluationRequest = new EvaluationRequest();
        evaluationRequest.setClientLanguage("");
        evaluationRequest.setClientTimeZoneOffset("");
        evaluationRequest.getKmEvaluationRequest().add(getKMEvaluationRequest(businessId));
        evaluationRequest.getDataRequirementItemData().add(getDataRequirementItemData(cdsInputByteArray, businessId));
        return evaluationRequest;
    }
}
