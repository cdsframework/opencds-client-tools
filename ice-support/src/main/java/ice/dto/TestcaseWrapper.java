package ice.dto;

import ice.dto.support.CdsObjectAssist;
import ice.exception.IceException;
import ice.util.DateUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.opencds.SubstanceAdministrationEvent;
import org.opencds.SubstanceAdministrationProposal;
import org.opencds.Testcase;

/**
 *
 * @author HLN Consulting, LLC
 */
public class TestcaseWrapper {

    private Testcase testcase = CdsObjectAssist.getTestcase();
    private CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
    private CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();

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

    public String getAuthor() throws IceException {
        return testcase.getAuthor();
    }

    public void setAuthor(String value) throws IceException {
        testcase.setAuthor(value);
    }

    public String getCreatedate() throws IceException {
        return DateUtils.getISODateFormat(testcase.getCreatedate());
    }

    public void setCreatedate(String value) throws DatatypeConfigurationException, ParseException {
        testcase.setCreatedate(DateUtils.parseISODateFormat(value));
    }

    public void setCreatedate(Date value) throws DatatypeConfigurationException {
        testcase.setCreatedate(value);
    }

    public String getDescription() throws IceException {
        return testcase.getDescription();
    }

    public void setDescription(String value) {
        testcase.setDescription(value);
    }

    public int getDosefocus() throws IceException {
        return testcase.getDosefocus();
    }

    public void setDosefocus(String value) {
        testcase.setDosefocus(Integer.parseInt(value));
    }

    public void setDosefocus(int value) {
        testcase.setDosefocus(value);
    }

    public String getExecutiondate() throws IceException {
        return DateUtils.getISODateFormat(testcase.getExecutiondate());
    }

    public void setExecutiondate(String value) throws DatatypeConfigurationException, ParseException {
        testcase.setExecutiondate(DateUtils.parseISODateFormat(value));
    }

    public void setExecutiondate(Date value) throws DatatypeConfigurationException {
        testcase.setExecutiondate(value);
    }

    public String getName() throws IceException {
        return testcase.getName();
    }

    public void setName(String value) {
        testcase.setName(value);
    }

    public String getNotes() throws IceException {
        return testcase.getNotes();
    }

    public void setNotes(String value) {
        testcase.setNotes(value);
    }

    public boolean isImmune() throws IceException {
        return testcase.isImmune();
    }

    public void setImmune(boolean value) {
        testcase.setImmune(value);
    }

    public int getNumdoses() throws IceException {
        return testcase.getNumdoses();
    }

    public void setNumdoses(String value) {
        testcase.setNumdoses(Integer.parseInt(value));
    }

    public void setNumdoses(int value) {
        testcase.setNumdoses(value);
    }

    public String getRuletotest() throws IceException {
        return testcase.getRuletotest();
    }

    public void setRuletotest(String value) {
        testcase.setRuletotest(value);
    }

    public String getSeries() throws IceException {
        return testcase.getSeries();
    }

    public void setSeries(String value) {
        testcase.setSeries(value);
    }

    public String getTestfocus() throws IceException {
        return testcase.getTestfocus();
    }

    public void setTestfocus(String value) {
        testcase.setTestfocus(value);
    }

    public String getVaccinegroup() throws IceException {
        return testcase.getVaccinegroup();
    }

    public void setVaccinegroup(String value) {
        testcase.setVaccinegroup(value);
    }

    public String getVersion() throws IceException {
        return testcase.getVersion();
    }

    public void setVersion(String value) {
        testcase.setVersion(value);
    }

    public String getPatientBirthTime() throws IceException {
        return input.getCdsObject().getVmrInput().getPatient().getDemographics().getBirthTime().getValue();
    }

    public void setPatientBirthTime(String value) throws IceException {
        input.setPatientBirthTime(value);
        output.setPatientBirthTime(value);
    }

    public void setPatientBirthTime(Date value) throws IceException {
        setPatientBirthTime(DateUtils.getISODateFormat(value));
    }

    public String getPatientGender() throws IceException {
        return output.getCdsObject().getVmrOutput().getPatient().getDemographics().getGender().getCode();
    }

    public void setPatientGender(String value) throws IceException {
        input.setPatientGender(value);
        output.setPatientGender(value);
    }

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            boolean valid,
            String focus,
            String value,
            String interpretation) throws IceException {
        return output.getEvaluationSubstanceAdministrationEvent(substanceCode, administrationTimeInterval, valid, focus, value, interpretation);
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            List<SubstanceAdministrationEvent> components) throws IceException {
        input.addSubstanceAdministrationEvent(substanceCode, administrationTimeInterval);
        return output.addSubstanceAdministrationEvent(substanceCode, administrationTimeInterval, components);
    }

    public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            String vaccineGroup,
            String substanceCode,
            String administrationTimeInterval,
            String focus,
            String value,
            String interpretation) throws IceException {
        return output.addSubstanceAdministrationProposal(vaccineGroup, substanceCode, administrationTimeInterval, focus, value, interpretation);
    }

}
