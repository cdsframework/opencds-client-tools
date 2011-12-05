package ice.service;

import ice.dto.support.CdsObjectAssist;
import ice.exception.IceException;
import java.util.Date;
import java.util.List;
import org.omg.spec.cdss._201105.dss.EvaluationResponse;
import org.omg.spec.cdss._201105.dss.FinalKMEvaluationResponse;
import org.omg.spec.cdss._201105.dss.KMEvaluationResultData;
import org.omg.spec.cdss._201105.dss.SemanticPayload;
import org.omg.spec.cdss._201105.dsswsdl.DSSRuntimeExceptionFault;
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
public class OpenCdsAssist {

    private final static OpenCdsService openCdsService = new OpenCdsService();

    public static CdsOutput evaluate(CdsInput cdsInput, String businessId, Date executionDate) throws IceException {
        byte[] cdsObjectToByteArray = CdsObjectAssist.cdsObjectToByteArray(cdsInput, CdsInput.class);
        byte[] evaluation = evaluate(cdsObjectToByteArray, businessId, executionDate);
        CdsOutput cdsOutput = CdsObjectAssist.cdsObjectFromByteArray(evaluation, CdsOutput.class);
        return cdsOutput;
    }

    public static byte[] evaluate(byte[] input, String businessId, Date executionDate) throws IceException {
        EvaluationResponse response = null;
        try {
            response = openCdsService.evaluate(input, businessId, new Date());
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
        }
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
        byte[] bytes = base64EncodedPayload.get(0);
        if (bytes == null) {
            throw new IceException("bytes is null!");
        }
        return bytes;
    }

}
