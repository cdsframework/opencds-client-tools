package ice.support;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.apache.log4j.Logger;
import org.omg.spec.cdss._201105.dss.DataRequirementItemData;
import org.omg.spec.cdss._201105.dss.EntityIdentifier;
import org.omg.spec.cdss._201105.dss.EvaluationRequest;
import org.omg.spec.cdss._201105.dss.EvaluationResponse;
import org.omg.spec.cdss._201105.dss.InteractionIdentifier;
import org.omg.spec.cdss._201105.dss.ItemIdentifier;
import org.omg.spec.cdss._201105.dss.KMEvaluationRequest;
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

/**
 *
 * @author HLN Consulting, LLC
 */
public class OpenCdsService {
    private final static Logger logger = Logger.getLogger(OpenCdsService.class);
    private DecisionSupportService dss;
    private Evaluation service;
    private InteractionIdentifier interactionIdentifier;
    private KMEvaluationRequest kmEvaluationRequest;
    private EntityIdentifier kmEntityIdentifier;
    private EvaluationRequest evaluationRequest;
    private ItemIdentifier itemIdentifier;
    private EntityIdentifier iiEntityIdentifier;
    private EntityIdentifier spEntityIdentifier;
    private SemanticPayload semanticPayload;
    private DataRequirementItemData dataRequirementItemData;

    public OpenCdsService() {
        dss = new DecisionSupportService();
        service = dss.getEvaluate();
        interactionIdentifier = new InteractionIdentifier();
        interactionIdentifier.setInteractionId("123456");
        interactionIdentifier.setScopingEntityId("gov.nyc.health");

        kmEntityIdentifier = new EntityIdentifier();
        kmEntityIdentifier.setScopingEntityId("org.opencds");
        kmEntityIdentifier.setVersion("1.5.3");

        kmEvaluationRequest = new KMEvaluationRequest();
        kmEvaluationRequest.setKmId(kmEntityIdentifier);

        iiEntityIdentifier = new EntityIdentifier();
        iiEntityIdentifier.setScopingEntityId("gov.nyc.health");
        iiEntityIdentifier.setVersion("1.0.0.0");

        itemIdentifier = new ItemIdentifier();
        itemIdentifier.setItemId("cdsPayload");
        itemIdentifier.setContainingEntityId(iiEntityIdentifier);

        spEntityIdentifier = new EntityIdentifier();
        spEntityIdentifier.setBusinessId("VMR");
        spEntityIdentifier.setScopingEntityId("org.opencds.vmr");
        spEntityIdentifier.setVersion("1.0");

        semanticPayload = new SemanticPayload();
        semanticPayload.setInformationModelSSId(spEntityIdentifier);

        dataRequirementItemData = new DataRequirementItemData();
        dataRequirementItemData.setDriId(itemIdentifier);
        dataRequirementItemData.setData(semanticPayload);

        evaluationRequest = new EvaluationRequest();
        evaluationRequest.setClientLanguage("");
        evaluationRequest.setClientTimeZoneOffset("");
        evaluationRequest.getKmEvaluationRequest().add(kmEvaluationRequest);
        evaluationRequest.getDataRequirementItemData().add(dataRequirementItemData);
    }

    public EvaluationResponse evaluate(byte[] cdsInputByteArray, String businessId, Date executionDate)
            throws DSSRuntimeExceptionFault, EvaluationExceptionFault, InvalidDriDataFormatExceptionFault,
            InvalidTimeZoneOffsetExceptionFault, RequiredDataNotProvidedExceptionFault, UnrecognizedLanguageExceptionFault,
            UnrecognizedScopedEntityExceptionFault, UnsupportedLanguageExceptionFault {
        EvaluationResponse result = null;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(executionDate);
        try {
            interactionIdentifier.setSubmissionTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
            kmEntityIdentifier.setBusinessId(businessId);
            iiEntityIdentifier.setBusinessId(businessId + "Data");
            semanticPayload.getBase64EncodedPayload().add(cdsInputByteArray);
            result = service.evaluate(interactionIdentifier, evaluationRequest);
        } catch (DatatypeConfigurationException e) {

        } finally {
        }
        return result;
    }

    public static OpenCdsService getOpenCDS() {
        return new OpenCdsService();
    }
}
