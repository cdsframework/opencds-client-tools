package org.cdsframework.test.importers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cdsframework.dto.TestcaseWrapper;
import org.cdsframework.dto.support.TestImportCallback;
import org.cdsframework.enumeration.EvaluationValidityType;
import org.cdsframework.exception.IceException;
import org.cdsframework.util.DateUtils;
import org.cdsframework.util.StringUtils;
import org.opencds.ObservationResult;
import org.opencds.RelatedClinicalStatement;
import org.opencds.SubstanceAdministrationEvent;

/**
 *
 * @author HLN Consulting, LLC
 */
public class XlsxV2 {

    private final static Logger logger = Logger.getLogger(XlsxV2.class);

    public static void importFromFile(XSSFWorkbook wb, TestImportCallback callback)
            throws IceException, FileNotFoundException, IOException, DatatypeConfigurationException {
        List<String> importedTestcases = new ArrayList<String>();
        POIXMLProperties properties = wb.getProperties();
        CoreProperties coreProperties = properties.getCoreProperties();
        String category = coreProperties.getCategory();
        logger.trace("category: " + category);
        XSSFSheet sheet = wb.getSheet("Test Coverage Summary");
        int lastRowNum = sheet.getLastRowNum();
        logger.trace("LastRowNum: " + lastRowNum);
        int i = 0;
        int testCount = 0;
        while (i < lastRowNum) {
            i++;
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(0);
                if (cell != null) {
                    String cellFormula = cell.getCellFormula();
                    logger.info("importing from cellFormula=" + cellFormula);
                    String testSheetName = cellFormula.substring(1, cellFormula.indexOf("!") - 1);
                    logger.trace("testSheetName=" + testSheetName);
                    XSSFSheet testSheet = wb.getSheet(testSheetName);
                    if (testSheet == null) {
                        continue;
                    }
                    logger.trace("testSheet=" + testSheet);
                    String testSheetCellStart = cellFormula.substring(cellFormula.indexOf("!") + 1);
                    logger.trace("testSheetCellStart=" + testSheetCellStart);
                    int testCurrentRowNum = Integer.parseInt(testSheetCellStart.substring(3)) - 1;
                    XSSFRow testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell testNumberCell = testCurrentRow.getCell(0);
                    String testNumber = testNumberCell.getStringCellValue();
                    logger.trace("testNumber=" + testNumber);
                    XSSFCell name = testCurrentRow.getCell(2);
                    if (!name.getStringCellValue().isEmpty()) {
                        String localName = name.getStringCellValue();
                        logger.trace("localName=" + localName);
                        String globalName = testSheetName + ": " + localName;
                        logger.trace("globalName=" + globalName);
                        testCount++;
                        TestcaseWrapper testcase = TestcaseWrapper.getTestcaseWrapper();

                        // change to filename, tab name, test #, row #
                        String location = testSheetName + ", test # " + testNumber + ", row # " + (testCurrentRowNum + 1);
                        testcase.setFileLocation(location);

                        logger.trace(location);

                        // test name + vaccine group
                        testcase.setName(localName);
                        String encodedName = StringUtils.getShaHashFromString(globalName);
                        logger.trace("encodedName=" + encodedName);
                        testcase.setEncodedName(encodedName);
                        if (importedTestcases.contains(encodedName)) {
                            int c = 1;
                            while (importedTestcases.contains(encodedName)) {
                                localName = localName + "(" + c + ")";
                                globalName = testSheetName + ": " + localName;
                                encodedName = StringUtils.getShaHashFromString(globalName);
                                testcase.setName(localName);
                                testcase.setEncodedName(encodedName);
                            }
                        }
                        importedTestcases.add(encodedName);
                        logger.trace("    Test name: " + testcase.getName());
                        XSSFCell vaccineGroup = testCurrentRow.getCell(10);
                        try {
                            double numericCellValue = vaccineGroup.getNumericCellValue();
                            testcase.setVaccinegroup(String.valueOf((int) numericCellValue));
                        } catch (IllegalStateException e) {
                            testcase.setVaccinegroup(vaccineGroup.getStringCellValue());
                        }
                        logger.trace("    Vaccine group: " + testcase.getVaccinegroup());

                        // test focus
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell focusCell = testCurrentRow.getCell(2);
                        String focus = focusCell.getStringCellValue();
                        testcase.setTestfocus(focus);
                        logger.trace("    Test focus: " + focus);
                        if (focus == null || focus.trim().isEmpty()) {
                            throw new UnsupportedOperationException("    Test focus: " + focus + " - " + location);
                        }

                        // Combo test?
                        XSSFCell comboCell = testCurrentRow.getCell(6);
                        String comboString = comboCell.getStringCellValue();
                        if (comboString != null) {
                            comboString = comboString.split(" ")[0].trim();
                        }
                        boolean combo = "Yes".equalsIgnoreCase(comboString);
                        logger.trace("    Test combo: " + combo);

                        // series
                        XSSFCell series = testCurrentRow.getCell(9);
                        testcase.setSeries(series.getStringCellValue());
                        logger.trace("    Series: " + testcase.getSeries());

                        // dose number focus
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell doseFocus = testCurrentRow.getCell(2);
                        testcase.setDosefocus(doseFocus.getStringCellValue());
                        logger.trace("    Dose focus: " + testcase.getDosefocus());

                        // rule to test description
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell ruleToTest = testCurrentRow.getCell(2);
                        testcase.setRuletotest(ruleToTest.getStringCellValue());
                        logger.trace("    Rule to test: " + testcase.getRuletotest());

                        // notes
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell testNotes = testCurrentRow.getCell(2);
                        testcase.setNotes(testNotes.getStringCellValue());
                        logger.trace("    Test notes: " + testcase.getNotes());

                        // antigen
                        testCurrentRowNum += 2;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell antigenCell = testCurrentRow.getCell(3);
                        String antigen = antigenCell.getStringCellValue();
                        if (antigen != null && antigen.trim().isEmpty()) {
                            antigen = null;
                        }

                        // immunityValue
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell immunityValueCell = testCurrentRow.getCell(3);
                        String immunityValue = immunityValueCell.getStringCellValue();
                        if ("T".equalsIgnoreCase(immunityValue)) {
                            immunityValue = "DISEASE_DOCUMENTED";
                        } else if ("H".equals(immunityValue)) {
                            immunityValue = "PROOF_OF_IMMUNITY";
                        }
                        if (immunityValue != null && immunityValue.trim().isEmpty()) {
                            immunityValue = null;
                        }

                        // immunityDate
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell immunityDateCell = testCurrentRow.getCell(2);
                        Date immunityDate = null;
                        try {
                            immunityDate = immunityDateCell.getDateCellValue();
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }

                        if (antigen != null && immunityValue != null && immunityDate != null) {
                            testcase.addImmunityObservationResult(immunityDate, antigen, immunityValue, "IS_IMMUNE");
                            logger.trace("    Immune: " + antigen + " - " + immunityValue + " - " + immunityDate);
                        } else if (antigen != null || immunityValue != null || immunityDate != null) {
                            logger.error("Missing values on disease/immunity input:");
                            logger.error("    antigen: " + antigen);
                            logger.error("    immunityValue: " + immunityValue);
                            logger.error("    immunityDate: " + immunityDate);
                            testcase.setErrorMessage(
                                    location
                                    + ", RowNum "
                                    + testCurrentRowNum
                                    + " - Missing values on disease/immunity input: antigen="
                                    + antigen
                                    + "; immunityValue="
                                    + immunityValue
                                    + "; immunityDate="
                                    + immunityDate);
                        }

                        // set the execution date
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell executionDate = testCurrentRow.getCell(2);
                        if (executionDate != null && executionDate.getDateCellValue() != null) {
                            testcase.setExecutiondate(executionDate.getDateCellValue());
                            logger.trace("    Execution date: " + testcase.getExecutiondatetime());
                        } else {
                            logger.error("Execution Date is null.");
                        }

                        // add the recommendation via the substanceAdministrationProposal and observationResult
                        XSSFCell recommendationCell = testCurrentRow.getCell(8);
                        String recommendation = recommendationCell.getStringCellValue();
                        logger.trace("    Recommendation: " + recommendation);
                        XSSFCell recommendationReasonCell = testCurrentRow.getCell(9);
                        String recommendationReason = recommendationReasonCell.getStringCellValue();
                        logger.trace("    Reason for recommendation: " + recommendationReasonCell);
                        XSSFCell dueDateCell = testCurrentRow.getCell(10);
                        Date dueDate = dueDateCell.getDateCellValue();
                        logger.trace("    Date due: " + dueDate);

                        // convert DOB to ISO format and add to both vmr output and input
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell dobCell = testCurrentRow.getCell(2);
                        if (dobCell != null && dobCell.getDateCellValue() != null) {
                            testcase.setPatientBirthTime(dobCell.getDateCellValue());
                            logger.trace("    DOB: " + testcase.getPatientBirthTime());
                        } else {
                            logger.error("DOB is null.");
                        }

                        // add the gender to both the vmr output and input
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell gender = testCurrentRow.getCell(2);
                        String genderValue = gender.getStringCellValue();
                        if ("female".equalsIgnoreCase(genderValue)) {
                            genderValue = "F";
                        } else if ("male".equalsIgnoreCase(genderValue)) {
                            genderValue = "M";
                        } else {
                            logger.error("Gender unsupported - setting to UN: " + genderValue + " @ " + location);
                            genderValue = "UN";
                        }
                        testcase.setPatientGender(genderValue);
                        logger.trace("    Gender: " + testcase.getPatientGender());

                        // not in use yet
                        XSSFCell recommendedVaccineCell = testCurrentRow.getCell(10);
                        String recommendedVaccine = null;
                        try {
                            double recommendedVaccineDouble = recommendedVaccineCell.getNumericCellValue();
                            recommendedVaccine = String.valueOf((int) recommendedVaccineDouble);
                        } catch (IllegalStateException e) {
                            // ignore - its empty
                        }
                        logger.trace("    Recommended vaccine: " + recommendedVaccine);

                        if (testcase.getVaccinegroup() == null && recommendedVaccine == null) {
                            logger.error("Neither a vaccine group or code recommendation exists!");
                            testcase.setErrorMessage("Neither a vaccine group or code recommendation exists!");
                            callback.callback(testcase, testSheetName, false);
                            continue;
                        }
                        try {
                            testcase.addSubstanceAdministrationProposal(
                                    testcase.getVaccinegroup(),
                                    recommendedVaccine,
                                    DateUtils.getISODateFormat(dueDate),
                                    testcase.getVaccinegroup(),
                                    recommendation,
                                    recommendationReason);
                        } catch (IceException e) {
                            logger.error("addSubstanceAdministrationProposal Error @ " + location + " - " + e.getMessage());
                            logger.error(e.getMessage(), e);
                            testcase.setErrorMessage(e.getMessage());
                            callback.callback(testcase, testSheetName, false);
                            continue;
                        }

                        for (int shotNum : new int[]{1, 2, 3, 4, 5}) {
                            testCurrentRowNum++;
                            testCurrentRow = testSheet.getRow(testCurrentRowNum);
                            XSSFCell administeredVaccine = testCurrentRow.getCell(2);
                            logger.trace("    Shot " + shotNum + " vaccine: " + administeredVaccine);
                            XSSFCell administeredVaccineCvxCell = testCurrentRow.getCell(3);
                            String administeredVaccineCvx = administeredVaccineCvxCell.getRawValue();
                            if (administeredVaccineCvx != null && !administeredVaccineCvx.isEmpty()) {
                                logger.trace("    Shot " + shotNum + " CVX: " + administeredVaccineCvx);
                                Date administrationDate = null;
                                List<SubstanceAdministrationEvent> componentEvents = new LinkedList<SubstanceAdministrationEvent>();
                                if (combo) {
                                    logger.trace("  Processing combo evaluation...");
                                    // date of administration
                                    testCurrentRowNum++;
                                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                                    XSSFCell administrationDateCell = testCurrentRow.getCell(2);
                                    administrationDate = administrationDateCell.getDateCellValue();
                                    logger.trace("    Shot " + shotNum + " date of administration: " + administrationDate);

                                    for (int compNum : new int[]{1, 2, 3, 4}) {
                                        testCurrentRowNum++;
                                        testCurrentRow = testSheet.getRow(testCurrentRowNum);

                                        // vac code
                                        XSSFCell componentVaccineCell = testCurrentRow.getCell(3);
                                        String componentVaccine = null;
                                        try {
                                            double numericCellValue = componentVaccineCell.getNumericCellValue();
                                            componentVaccine = String.valueOf((int) numericCellValue);
                                        } catch (IllegalStateException e) {
                                            // ignore - it is null
                                        }
                                        logger.trace("    Component Vaccine " + compNum + ": " + componentVaccine);

                                        // evaluation
                                        XSSFCell evaluationCell = testCurrentRow.getCell(4);
                                        String evaluation = evaluationCell.getStringCellValue();
                                        logger.trace("    Component Vaccine evaluation " + compNum + ": " + evaluation);

                                        List<String> reasons = new ArrayList<String>();

                                        for (int reasonNum : new int[]{1, 2, 3}) {
                                            XSSFCell evaluationReasonCell = testCurrentRow.getCell(4 + reasonNum);
                                            String evaluationReason = evaluationReasonCell.getStringCellValue();
                                            if (evaluationReason != null && !evaluationReason.trim().isEmpty()) {
                                                reasons.add(evaluationReason);
                                                logger.trace("      found reason code: " + evaluationReason);
                                            }
                                        }
                                        if (componentVaccine != null) {
                                            SubstanceAdministrationEvent substanceAdministrationEvent =
                                                    testcase.getEvaluationSubstanceAdministrationEvent(
                                                    componentVaccine,
                                                    administrationDate,
                                                    EvaluationValidityType.valueOf(evaluation.toUpperCase()),
                                                    testcase.getVaccinegroup(),
                                                    reasons.toArray(new String[0]));
                                            componentEvents.add(substanceAdministrationEvent);
                                        }
                                    }
                                } else {
                                    logger.trace("Processing single component evaluation...");
                                    // evaluation
                                    XSSFCell evaluationCell = testCurrentRow.getCell(4);
                                    String evaluation = evaluationCell.getStringCellValue();
                                    logger.trace("    Vaccine evaluation " + shotNum + ": " + evaluation);

                                    List<String> reasons = new ArrayList<String>();

                                    for (int reasonNum : new int[]{1, 2, 3}) {
                                        XSSFCell evaluationReasonCell = testCurrentRow.getCell(4 + reasonNum);
                                        String evaluationReason = evaluationReasonCell.getStringCellValue();
                                        if (evaluationReason != null && !evaluationReason.trim().isEmpty()) {
                                            reasons.add(evaluationReason);
                                            logger.trace("      found reason code: " + evaluationReason);
                                        }
                                    }

                                    testCurrentRowNum++;
                                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                                    XSSFCell administrationDateCell = testCurrentRow.getCell(2);
                                    administrationDate = administrationDateCell.getDateCellValue();
                                    logger.trace("    Shot " + shotNum + " date of administration: " + administrationDate);

                                    SubstanceAdministrationEvent substanceAdministrationEvent =
                                            testcase.getEvaluationSubstanceAdministrationEvent(
                                            administeredVaccineCvx,
                                            administrationDate,
                                            EvaluationValidityType.valueOf(evaluation.toUpperCase()),
                                            testcase.getVaccinegroup(),
                                            reasons.toArray(new String[0]));
                                    componentEvents.add(substanceAdministrationEvent);
                                    testCurrentRowNum += 4;
                                }

                                try {
                                    testcase.addSubstanceAdministrationEvent(
                                            administeredVaccineCvx,
                                            administrationDate,
                                            null,
                                            componentEvents.toArray(new SubstanceAdministrationEvent[0]));
                                } catch (IceException e) {
                                    logger.error("addSubstanceAdministrationEvent shot " + shotNum + " Error @ " + location);
                                    logger.error(e);
                                    testcase.setErrorMessage(e.getMessage());
                                    callback.callback(testcase, testSheetName, false);
                                    continue;
                                }
                            }
                        }

                        int count = 0;
                        for (SubstanceAdministrationEvent sae : testcase.getSubstanceAdministrationEvents()) {
                            count++;
                            logger.trace("Shot " + count
                                    + " present: " + sae.getSubstance().getSubstanceCode().getCode()
                                    + "/" + sae.getAdministrationTimeInterval().getHigh());
                            int count2 = 0;
                            for (RelatedClinicalStatement rcs : sae.getRelatedClinicalStatements()) {
                                count2++;
                                SubstanceAdministrationEvent csae = rcs.getSubstanceAdministrationEvent();
                                logger.trace("Shot " + count
                                        + " Component " + count2
                                        + " present: " + csae.getSubstance().getSubstanceCode().getCode()
                                        + "/" + csae.getAdministrationTimeInterval().getHigh()
                                        + "/" + csae.getIsValid().isValue());
                                int count3 = 0;
                                for (RelatedClinicalStatement crcs : csae.getRelatedClinicalStatements()) {
                                    count3++;
                                    ObservationResult cor = crcs.getObservationResult();
                                    logger.trace("Shot " + count
                                            + " Component " + count2
                                            + " OR " + count3
                                            + " present: " + cor.getObservationFocus().getCode()
                                            + "/" + cor.getObservationValue().getConcept().getCode());
                                }
                            }
                        }
                        callback.callback(testcase, testSheetName, true);
                    } else {
                        // logger.trace("Sheet '" + testSheetName + "' - Row " + i + ": test name was null.");
                    }
                } else {
                    // logger.trace("cell is null");
                }
            } else {
                // logger.trace("Row " + i + " was null.");
            }
        }
        logger.info("Number of tests imported: " + testCount);
        if (testCount != importedTestcases.size()) {
            logger.error("Count mismatch: " + testCount + " - " + importedTestcases.size());
        }
    }
}
