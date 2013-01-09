package org.cdsframework.dto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;

import org.cdsframework.dto.support.CdsObjectAssist;
import org.cdsframework.enumeration.EvaluationValidityType;
import org.cdsframework.exception.IceException;
import org.cdsframework.util.DateUtils;
import org.opencds.EvaluatedPerson.ClinicalStatements.ObservationResults;
import org.opencds.EvaluatedPerson.ClinicalStatements.SubstanceAdministrationEvents;
import org.opencds.ObservationResult;
import org.opencds.SubstanceAdministrationEvent;
import org.opencds.SubstanceAdministrationProposal;
import org.opencds.TS;
import org.opencds.Testcase;

/**
 *
 * @author HLN Consulting, LLC
 */
public class TestcaseWrapper {

    private Testcase testcase = CdsObjectAssist.getTestcase();
    private CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
    private CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
    private String encodedName;
    private String fileLocation;
    private String errorMessage;

    public TestcaseWrapper() {
        testcase.setCdsInput(input.getCdsObject());
        testcase.setCdsOutput(output.getCdsObject());
    }

    public static TestcaseWrapper getTestcaseWrapper() {
        return new TestcaseWrapper();
    }

    public Testcase getTestcase() {
        return testcase;
    }

    public CdsInputWrapper getCdsInputWrapper() {
        return input;
    }

    public CdsOutputWrapper getCdsOutputWrapper() {
        return output;
    }

    public String getAuthor() {
        return testcase.getAuthor();
    }

    public void setAuthor(String value) {
        testcase.setAuthor(value);
    }

    public String getCreatedate() {
        return DateUtils.getISODateFormat(testcase.getCreatedate());
    }

    public void setCreatedate(String value) throws DatatypeConfigurationException, ParseException {
        testcase.setCreatedate(DateUtils.parseISODateFormat(value));
    }

    public void setCreatedate(Date value) throws DatatypeConfigurationException {
        testcase.setCreatedate(value);
    }

    public String getDosefocus() {
        return testcase.getDosefocus();
    }

    public void setDosefocus(String value) {
        testcase.setDosefocus(value);
    }

    public String getExecutiondatetime() {
        return DateUtils.getISODateFormat(testcase.getExecutiondate());
    }

    public Date getExecutiondate() throws ParseException {
        return testcase.getExecutiondate();
    }

    public void setExecutiondate(String value) throws DatatypeConfigurationException, ParseException {
        testcase.setExecutiondate(DateUtils.parseISODateFormat(value));
    }

    public void setExecutiondate(Date value) throws DatatypeConfigurationException {
        testcase.setExecutiondate(value);
    }

    public String getName() {
        return testcase.getName();
    }

    public void setName(String value) {
        testcase.setName(value);
    }

    public String getNotes() {
        return testcase.getNotes();
    }

    public void setNotes(String value) {
        testcase.setNotes(value);
    }

    public String getRuletotest() {
        return testcase.getRuletotest();
    }

    public void setRuletotest(String value) {
        testcase.setRuletotest(value);
    }

    public String getSeries() {
        return testcase.getSeries();
    }

    public void setSeries(String value) {
        testcase.setSeries(value);
    }

    public String getTestfocus() {
        return testcase.getTestfocus();
    }

    public void setTestfocus(String value) {
        testcase.setTestfocus(value);
    }

    public String getVaccinegroup() {
        return testcase.getVaccinegroup();
    }

    public void setVaccinegroup(String value) {
        testcase.setVaccinegroup(value);
    }

    public String getEncodedName() {
        return encodedName;
    }

    public void setEncodedName(String encodedName) {
        this.encodedName = encodedName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getPatientBirthTime() {
        TS birthTime = input.getCdsObject().getVmrInput().getPatient().getDemographics().getBirthTime();
        String birthtimeValue = null;
        if (birthTime != null) {
            birthtimeValue = birthTime.getValue();
        }
        return birthtimeValue;
    }

    public Date getPatientBirthDate() throws ParseException {
        return DateUtils.parseISODateFormat(getPatientBirthTime());
    }

    public void setPatientBirthTime(String value) throws IceException {
        input.setPatientBirthTime(value);
        output.setPatientBirthTime(value);
    }

    public void setPatientBirthTime(Date value) throws IceException {
        input.setPatientBirthTime(value);
        output.setPatientBirthTime(value);
    }

    public String getPatientGender() {
        return output.getCdsObject().getVmrOutput().getPatient().getDemographics().getGender().getCode();
    }

    public void setPatientGender(String value) throws IceException {
        input.setPatientGender(value);
        output.setPatientGender(value);
    }

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            EvaluationValidityType validity,
            String focus,
            String interpretation) throws IceException {
        return CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent(
                substanceCode,
                administrationTimeInterval,
                validity,
                focus,
                interpretation);
    }

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            EvaluationValidityType validity,
            String focus,
            String interpretation) throws IceException {
        return CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent(
                substanceCode,
                administrationTimeIntervalDate,
                validity,
                focus,
                interpretation);
    }

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            EvaluationValidityType validity,
            String focus,
            String[] reasons) throws IceException {
        return CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent(
                substanceCode,
                administrationTimeInterval,
                validity,
                focus,
                reasons);
    }

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            EvaluationValidityType validity,
            String focus,
            String[] reasons) throws IceException {
        return CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent(
                substanceCode,
                administrationTimeIntervalDate,
                validity,
                focus,
                reasons);
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            String immId,
            SubstanceAdministrationEvent[] components) throws IceException {
        input.addSubstanceAdministrationEvent(substanceCode, administrationTimeInterval, immId);
        return output.addSubstanceAdministrationEvent(substanceCode, administrationTimeInterval, immId, components);
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            String immId,
            SubstanceAdministrationEvent[] components) throws IceException {
        input.addSubstanceAdministrationEvent(substanceCode, administrationTimeIntervalDate, immId);
        return output.addSubstanceAdministrationEvent(substanceCode, administrationTimeIntervalDate, immId, components);
    }

    public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            String vaccineGroup,
            String substanceCode,
            String administrationTimeInterval,
            String focus,
            String value,
            String interpretation) throws IceException {
        return output.addSubstanceAdministrationProposal(
                vaccineGroup,
                substanceCode,
                administrationTimeInterval,
                focus,
                value,
                interpretation);
    }

    public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            String vaccineGroup,
            String substanceCode,
            Date administrationTimeIntervalDate,
            String focus,
            String value,
            String interpretation) throws IceException {
        return output.addSubstanceAdministrationProposal(
                vaccineGroup,
                substanceCode,
                administrationTimeIntervalDate,
                focus,
                value,
                interpretation);
    }

    public ObservationResult addImmunityObservationResult(Date observationEventTime, String focus, String value, String interpretation)
            throws IceException {
        ObservationResult addImmunityObservationResult = input.addImmunityObservationResult(observationEventTime, focus, value, interpretation);
        output.addObservationResult(addImmunityObservationResult);
        return addImmunityObservationResult;
    }

    public List<ObservationResult> getImmunityObservationResults() {
        List<ObservationResult> result;
        ObservationResults observationResults = input.getCdsObject().getVmrInput().getPatient().getClinicalStatements().getObservationResults();
        if (observationResults == null) {
            result = new ArrayList<ObservationResult>();
        } else {
            result = observationResults.getObservationResults();
        }
        return result;
    }


    public List<SubstanceAdministrationProposal> getSubstanceAdministrationProposals() {
        return output.getCdsObject().getVmrOutput().getPatient().getClinicalStatements().getSubstanceAdministrationProposals().getSubstanceAdministrationProposals();
    }

    public List<SubstanceAdministrationEvent> getSubstanceAdministrationEvents() {
        List<SubstanceAdministrationEvent> result;
        SubstanceAdministrationEvents substanceAdministrationEvents = output.getCdsObject().getVmrOutput().getPatient().getClinicalStatements().getSubstanceAdministrationEvents();
        if (substanceAdministrationEvents == null) {
            result = new ArrayList<SubstanceAdministrationEvent>();
        } else {
            result = substanceAdministrationEvents.getSubstanceAdministrationEvents();
        }
        return result;
    }

    public void setPatientId(String patientId) throws IceException {
        input.setPatientId(patientId);
        output.setPatientId(patientId);
    }
}
