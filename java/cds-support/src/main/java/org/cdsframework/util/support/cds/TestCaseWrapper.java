package org.cdsframework.util.support.cds;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.cdsframework.enumeration.TestCasePropertyType;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.util.DateUtils;
import org.cdsframework.util.LogUtils;
import org.opencds.support.CdsInput;
import org.opencds.support.CdsOutput;
import org.opencds.support.EvaluatedPerson.ClinicalStatements.ObservationResults;
import org.opencds.support.EvaluatedPerson.ClinicalStatements.SubstanceAdministrationEvents;
import org.opencds.support.EvaluatedPerson.ClinicalStatements.SubstanceAdministrationProposals;
import org.opencds.support.ObservationResult;
import org.opencds.support.SubstanceAdministrationEvent;
import org.opencds.support.SubstanceAdministrationProposal;
import org.opencds.support.TS;
import org.opencds.support.TestCase;
import org.opencds.support.TestCaseProperty;
import org.opencds.support.Vmr;

/**
 *
 * @author HLN Consulting, LLC
 */
public class TestCaseWrapper {

    protected static LogUtils logger = LogUtils.getLogger(TestCaseWrapper.class);
    final private TestCase testCase;
    private CdsInputWrapper input;
    private CdsOutputWrapper output;
    private String encodedName;
    private String fileLocation;
    private String errorMessage;

    public TestCaseWrapper() {
        this(CdsObjectAssist.getTestCase());
    }

    public TestCaseWrapper(TestCase testCase) {
        this.testCase = testCase;
        input = CdsInputWrapper.getCdsInputWrapper(testCase.getCdsInput());
        output = CdsOutputWrapper.getCdsOutputWrapper(testCase.getCdsOutput());
        if (testCase.getCdsInput() == null) {
            testCase.setCdsInput(input.getCdsObject());
        }
        if (testCase.getCdsOutput() == null) {
            testCase.setCdsOutput(output.getCdsObject());
        }
        logger.debug("1testCase.getCdsInput()=" + testCase.getCdsInput());
        logger.debug("1testCase.getCdsOutput()=" + testCase.getCdsOutput());
        logger.debug("1input.getCdsInput()=" + input.getCdsObject());
        logger.debug("1output.getCdsOutput()=" + output.getCdsObject());
    }

    public TestCaseWrapper(TestCase testCase, CdsInput cdsInput, CdsOutput cdsOutput) {
        this.testCase = testCase;
        input = CdsInputWrapper.getCdsInputWrapper(cdsInput);
        output = CdsOutputWrapper.getCdsOutputWrapper(cdsOutput);
        testCase.setCdsInput(input.getCdsObject());
        testCase.setCdsOutput(output.getCdsObject());
        logger.debug("2testCase.getCdsInput()=" + testCase.getCdsInput());
        logger.debug("2testCase.getCdsOutput()=" + testCase.getCdsOutput());
        logger.debug("2input.getCdsInput()=" + input.getCdsObject());
        logger.debug("2output.getCdsOutput()=" + output.getCdsObject());
    }

    public static TestCaseWrapper getTestCaseWrapper(TestCase testCase, CdsInput cdsInput, CdsOutput cdsOutput) {
        return new TestCaseWrapper(testCase, cdsInput, cdsOutput);
    }

    public static TestCaseWrapper getTestCaseWrapper(TestCase testCase) {
        return new TestCaseWrapper(testCase);
    }

    public static TestCaseWrapper getTestCaseWrapper() {
        return new TestCaseWrapper();
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public CdsInputWrapper getCdsInputWrapper() {
        return input;
    }

    public CdsOutputWrapper getCdsOutputWrapper() {
        return output;
    }

    public List<TestCaseProperty> getProperties() {
        return testCase.getProperties();
    }

    public void addProperty(String name, String value, TestCasePropertyType testCasePropertyType) {
        TestCaseProperty testCaseProperty = new TestCaseProperty();
        testCaseProperty.setPropertyName(name);
        testCaseProperty.setPropertyValue(value);
        testCaseProperty.setPropertyType(testCasePropertyType.getTypeString());
        List<TestCaseProperty> properties = testCase.getProperties();
        properties.add(testCaseProperty);
    }

    public String getSuiteName() {
        return testCase.getSuiteName();
    }

    public void setSuiteName(String suiteName) {
        testCase.setSuiteName(suiteName);
    }

    public String getGroupName() {
        return testCase.getGroupName();
    }

    public void setGroupName(String groupName) {
        testCase.setGroupName(groupName);
    }

    public boolean isIgnore() {
        return testCase.isIgnore();
    }

    public void setIgnore(boolean ignore) {
        testCase.setIgnore(ignore);
    }

    public String getExecutiondatetime() {
        return DateUtils.getISODateFormat(testCase.getExecutiondate());
    }

    public Date getExecutiondate() throws ParseException {
        return testCase.getExecutiondate();
    }

    public void setExecutiondate(String value) throws DatatypeConfigurationException, ParseException {
        testCase.setExecutiondate(DateUtils.parseISODateFormat(value));
    }

    public void setExecutiondate(Date value) throws DatatypeConfigurationException {
        testCase.setExecutiondate(value);
    }

    public String getName() {
        return testCase.getName();
    }

    public void setName(String value) {
        testCase.setName(value);
    }

    public String getNotes() {
        return testCase.getNotes();
    }

    public void setNotes(String value) {
        testCase.setNotes(value);
    }

    public String getRuletotest() {
        return testCase.getRuletotest();
    }

    public void setRuletotest(String value) {
        testCase.setRuletotest(value);
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

    public void setPatientBirthTime(String value) throws CdsException {
        input.setPatientBirthTime(value);
        output.setPatientBirthTime(value);
    }

    public void setPatientBirthTime(Date value) throws CdsException {
        input.setPatientBirthTime(value);
        output.setPatientBirthTime(value);
    }

    public String getPatientGender() {
        return output.getCdsObject().getVmrOutput().getPatient().getDemographics().getGender().getCode();
    }

    public void setPatientGender(String value) throws CdsException {
        input.setPatientGender(value);
        output.setPatientGender(value);
    }

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            String validity,
            String focus,
            String interpretation) throws CdsException {
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
            String validity,
            String focus,
            String interpretation) throws CdsException {
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
            String validity,
            String focus,
            String[] reasons) throws CdsException {
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
            String validity,
            String focus,
            String[] reasons) throws CdsException {
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
            SubstanceAdministrationEvent[] components) throws CdsException {
        input.addSubstanceAdministrationEvent(substanceCode, administrationTimeInterval, immId);
        return output.addSubstanceAdministrationEvent(substanceCode, administrationTimeInterval, immId, components);
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            String immId,
            SubstanceAdministrationEvent[] components) throws CdsException {
        input.addSubstanceAdministrationEvent(substanceCode, administrationTimeIntervalDate, immId);
        return output.addSubstanceAdministrationEvent(substanceCode, administrationTimeIntervalDate, immId, components);
    }

    public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            String vaccineGroup,
            String substanceCode,
            String administrationTimeInterval,
            String focus,
            String value,
            String interpretation) throws CdsException {
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
            String interpretation) throws CdsException {
        return output.addSubstanceAdministrationProposal(
                vaccineGroup,
                substanceCode,
                administrationTimeIntervalDate,
                focus,
                value,
                interpretation);
    }

    public ObservationResult addImmunityObservationResult(Date observationEventTime, String focus, String value, String interpretation)
            throws CdsException {
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

    public List<SubstanceAdministrationProposal> getSubstanceAdministrationProposals() throws CdsException {
        CdsOutput cdsObject = output.getCdsObject();
        if (cdsObject == null) {
            throw new CdsException("cdsObject is null!");
        }
        Vmr vmrOutput = cdsObject.getVmrOutput();
        if (vmrOutput == null) {
            throw new CdsException("vmrOutput is null!");
        }
        SubstanceAdministrationProposals substanceAdministrationProposals = CdsOutputWrapper.getSubstanceAdministrationProposals(vmrOutput);
        if (substanceAdministrationProposals == null) {
            throw new CdsException("substanceAdministrationProposals is null!");
        }
        return substanceAdministrationProposals.getSubstanceAdministrationProposals();
    }

    public List<SubstanceAdministrationEvent> getSubstanceAdministrationEvents() {
        SubstanceAdministrationEvents substanceAdministrationEvents = output.getCdsObject().getVmrOutput().getPatient().getClinicalStatements().getSubstanceAdministrationEvents();
        if (substanceAdministrationEvents == null) {
            substanceAdministrationEvents = new SubstanceAdministrationEvents();
            output.getCdsObject().getVmrOutput().getPatient().getClinicalStatements().setSubstanceAdministrationEvents(substanceAdministrationEvents);
        }
        return substanceAdministrationEvents.getSubstanceAdministrationEvents();
    }

    public void setPatientId(String patientId) throws CdsException {
        input.setPatientId(patientId);
        output.setPatientId(patientId);
    }
}
