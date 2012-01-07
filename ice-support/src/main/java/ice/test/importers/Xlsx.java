package ice.test.importers;

import ice.dto.TestcaseWrapper;
import ice.dto.support.Reason;
import ice.dto.support.TestImportCallback;
import ice.exception.IceException;
import ice.util.DateUtils;
import ice.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.opencds.ObservationResult;
import org.opencds.RelatedClinicalStatement;
import org.opencds.SubstanceAdministrationEvent;

/**
 *
 * @author HLN Consulting, LLC
 */
public class Xlsx {

    private final static Logger logger = Logger.getLogger(Xlsx.class);

    public static void importFromFile(byte[] data, TestImportCallback callback)
            throws IceException, FileNotFoundException, IOException, DatatypeConfigurationException {
        importFromFile(new ByteArrayInputStream(data), callback);
    }

    public static void importFromFile(String filename, TestImportCallback callback)
            throws IceException, FileNotFoundException, IOException, DatatypeConfigurationException {
        importFromFile(new FileInputStream(filename), callback);
    }

    public static void importFromFile(InputStream inputStream, TestImportCallback callback)
            throws IceException, FileNotFoundException, IOException, DatatypeConfigurationException {
        List<String> importedTestcases = new ArrayList<String>();
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = wb.getSheet("HepB Test Coverage Summary");
        int lastRowNum = sheet.getLastRowNum();
        logger.debug("LastRowNum: " + lastRowNum);
        int i = 0;
        int testCount = 0;
        while (i < lastRowNum) {
            i++;
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(0);
                String cellFormula = cell.getCellFormula();
                String testSheetName = cellFormula.substring(1, cellFormula.indexOf("!") - 1);
                XSSFSheet testSheet = wb.getSheet(testSheetName);
                String testSheetCellStart = cellFormula.substring(cellFormula.indexOf("!") + 1);
                int testCurrentRowNum = Integer.parseInt(testSheetCellStart.substring(3)) - 1;
                XSSFRow testCurrentRow = testSheet.getRow(testCurrentRowNum);
                XSSFCell name = testCurrentRow.getCell(2);
                if (!name.getStringCellValue().isEmpty()) {
                    String localName = name.getStringCellValue();
                    String globalName = testSheetName + ": " + localName;
                    testCount++;
                    TestcaseWrapper testcase = TestcaseWrapper.getTestcaseWrapper();

                    String location = "Row: " + i
                            + " - Test Sheet: " + testSheetName
                            + " - Test Sheet Cell Start: " + testSheetCellStart
                            + " - RowNum: " + testCurrentRowNum
                            + " - Test Name: " + localName;
                    testcase.setFileLocation(location);

                    logger.debug(location);

                    // test name + vaccine group
                    testcase.setName(localName);
                    String encodedName = StringUtils.getShaHashFromString(globalName);
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
                    logger.debug("    Test name: " + testcase.getName());
                    XSSFCell vaccineGroup = testCurrentRow.getCell(10);
                    testcase.setVaccinegroup((int) vaccineGroup.getNumericCellValue());
                    logger.debug("    Vaccine group: " + testcase.getVaccinegroup());

                    // test focus + series
                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell focus = testCurrentRow.getCell(2);
                    testcase.setTestfocus(focus.getStringCellValue());
                    logger.debug("    Test focus: " + testcase.getTestfocus());
                    XSSFCell series = testCurrentRow.getCell(9);
                    testcase.setSeries(series.getStringCellValue());
                    logger.debug("    Series: " + testcase.getSeries());

                    // dose number focis + number of doses
                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell doseFocus = testCurrentRow.getCell(2);
                    String doseFocusValue = doseFocus.getStringCellValue();
                    if (doseFocusValue != null && !doseFocusValue.isEmpty()) {
                        doseFocusValue = doseFocus.getStringCellValue().trim().toLowerCase();
                        if ("extra dose".equals(doseFocusValue)) {
                            testcase.setDosefocus(-1);
                        } else if ("all doses".equals(doseFocusValue)) {
                            testcase.setDosefocus(0);
                        } else {
                            testcase.setDosefocus(Integer.parseInt(doseFocusValue.substring(doseFocusValue.length() - 1)));
                        }
                        logger.debug("    Dose focus: " + testcase.getDosefocus());
                    }
                    XSSFCell numDoses = testCurrentRow.getCell(9);
                    testcase.setNumdoses((int) numDoses.getNumericCellValue());
                    logger.debug("    Number of valid doses: " + testcase.getNumdoses());

                    // rule to test description
                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell ruleToTest = testCurrentRow.getCell(2);
                    testcase.setRuletotest(ruleToTest.getStringCellValue());
                    logger.debug("    Rule to test: " + testcase.getRuletotest());

                    // notes
                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell testNotes = testCurrentRow.getCell(2);
                    testcase.setNotes(testNotes.getStringCellValue());
                    logger.debug("    Test notes: " + testcase.getNotes());

                    // immunity
                    testCurrentRowNum += 3;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell immune = testCurrentRow.getCell(2);
                    String immunity = ("Y".equalsIgnoreCase(immune.getStringCellValue())
                            || "Yes".equalsIgnoreCase(immune.getStringCellValue())) ? "VALID" : "INVALID";
                    if (immune.getStringCellValue() != null && !immune.getStringCellValue().trim().isEmpty()) {
                        testcase.addImmunityObservationResult(String.valueOf(testcase.getVaccinegroup()), immunity, "IS_IMMUNE");
                    }
                    logger.debug("    Immune: " + immunity);

                    // set the execution date
                    testCurrentRowNum += 2;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell executionDate = testCurrentRow.getCell(2);
                    if (executionDate != null && executionDate.getDateCellValue() != null) {
                        testcase.setExecutiondate(executionDate.getDateCellValue());
                        logger.debug("    Execution date: " + testcase.getExecutiondatetime());
                    } else {
                        logger.warn("Execution Date is null.");
                    }

                    // add the recommendation via the substanceAdministrationProposal and observationResult
                    XSSFCell recommendation = testCurrentRow.getCell(8);
                    logger.debug("    Recommendation: " + recommendation);
                    XSSFCell recommendationReason = testCurrentRow.getCell(9);
                    logger.debug("    Reason for recommendation: " + recommendationReason);
                    XSSFCell dueDate = testCurrentRow.getCell(10);
                    logger.debug("    Date due: " + dueDate);

                    // convert DOB to ISO format and add to both vmr output and input
                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell dob = testCurrentRow.getCell(2);
                    if (dob != null && dob.getDateCellValue() != null) {
                        testcase.setPatientBirthTime(dob.getDateCellValue());
                        logger.debug("    DOB: " + testcase.getPatientBirthTime());
                    } else {
                        logger.warn("DOB is null.");
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
                    logger.debug("    Gender: " + testcase.getPatientGender());

                    // not in use yet
                    XSSFCell recommendedVaccine = testCurrentRow.getCell(9);
                    logger.debug("    Recommended vaccine: " + recommendedVaccine);
                    String recommendationReasonValue = recommendationReason.getStringCellValue();
//                    if ("DUE_IN_FUTURE".equalsIgnoreCase(recommendationReasonValue)) {
//                        recommendationReasonValue = "FUTURE_RECOMMENDED";
//                    }
                    try {
                        testcase.addSubstanceAdministrationProposal(
                                testcase.getVaccinegroup(),
                                recommendedVaccine.getStringCellValue(),
                                DateUtils.getISODateFormat(dueDate.getDateCellValue()),
                                String.valueOf(testcase.getVaccinegroup()),
                                recommendation.getStringCellValue(),
                                recommendationReasonValue);
                    } catch (IceException e) {
                        logger.error("addSubstanceAdministrationProposal Error @ " + location);
                        logger.error(e);
                            testcase.setErrorMessage(e.getMessage());
                            callback.callback(testcase, testSheetName, false);
                            continue;
                    }

                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell shot1Vaccine = testCurrentRow.getCell(2);
                    logger.trace("    Shot 1 vaccine: " + shot1Vaccine);
                    XSSFCell shot1Cvx = testCurrentRow.getCell(3);
                    String shot1CvxCode = shot1Cvx.getRawValue();
                    if (shot1CvxCode != null && !shot1CvxCode.isEmpty()) {
                        logger.trace("    Shot 1 CVX: " + shot1Cvx.getRawValue());
                        XSSFCell shot1Evaluation = testCurrentRow.getCell(4);
                        logger.trace("    Shot 1 evaluation: " + shot1Evaluation);
                        XSSFCell shot1InvalidReason1Code = testCurrentRow.getCell(5);
                        logger.trace("    Shot 1 invalid reason 1 code: " + shot1InvalidReason1Code);
                        XSSFCell shot1InvalidReason2Code = testCurrentRow.getCell(6);
                        logger.trace("    Shot 1 invalid reason 2 code: " + shot1InvalidReason2Code);
                        XSSFCell shot1InvalidReason3Code = testCurrentRow.getCell(7);
                        logger.trace("    Shot 1 invalid reason 3 code: " + shot1InvalidReason3Code);
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell shot1DateofAdministration = testCurrentRow.getCell(2);
                        logger.trace("    Shot 1 date of administration: " + shot1DateofAdministration);
                        XSSFCell recommendedReasonText = testCurrentRow.getCell(9);
                        logger.trace("    Recommended reason text: " + recommendedReasonText.getStringCellValue());

                        try {
                            SubstanceAdministrationEvent shot1EvaluationEvent =
                                    testcase.getEvaluationSubstanceAdministrationEvent(
                                    shot1CvxCode,
                                    shot1DateofAdministration.getDateCellValue(),
                                    "Valid".equalsIgnoreCase(shot1Evaluation.getStringCellValue()),
                                    new Reason[]{
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot1Evaluation.getStringCellValue().toUpperCase(),
                                        shot1InvalidReason1Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot1Evaluation.getStringCellValue().toUpperCase(),
                                        shot1InvalidReason2Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot1Evaluation.getStringCellValue().toUpperCase(),
                                        shot1InvalidReason3Code.getStringCellValue())
                                    });
                            testcase.addSubstanceAdministrationEvent(
                                    shot1CvxCode,
                                    shot1DateofAdministration.getDateCellValue(),
                                    new SubstanceAdministrationEvent[]{shot1EvaluationEvent});
                        } catch (IceException e) {
                            logger.error("addSubstanceAdministrationEvent shot 1 Error @ " + location);
                            logger.error(e);
                            testcase.setErrorMessage(e.getMessage());
                            callback.callback(testcase, testSheetName, false);
                            continue;
                        }
                    }

                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell shot2Vaccine = testCurrentRow.getCell(2);
                    logger.trace("    Shot 2 vaccine: " + shot2Vaccine);
                    XSSFCell shot2Cvx = testCurrentRow.getCell(3);
                    String shot2CvxCode = shot2Cvx.getRawValue();
                    if (shot2CvxCode != null && !shot2CvxCode.isEmpty()) {
                        logger.trace("    Shot 2 CVX: " + shot2Cvx.getRawValue());
                        XSSFCell shot2Evaluation = testCurrentRow.getCell(4);
                        logger.trace("    Shot 2 evaluation: " + shot2Evaluation);
                        XSSFCell shot2InvalidReason1Code = testCurrentRow.getCell(5);
                        logger.trace("    Shot 2 invalid reason 1 code: " + shot2InvalidReason1Code);
                        XSSFCell shot2InvalidReason2Code = testCurrentRow.getCell(6);
                        logger.trace("    Shot 2 invalid reason 2 code: " + shot2InvalidReason2Code);
                        XSSFCell shot2InvalidReason3Code = testCurrentRow.getCell(7);
                        logger.trace("    Shot 2 invalid reason 3 code: " + shot2InvalidReason3Code);
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell shot2DateofAdministration = testCurrentRow.getCell(2);
                        logger.trace("    Shot 2 date of administration: " + shot2DateofAdministration);

                        try {
                            SubstanceAdministrationEvent shot2EvaluationEvent =
                                    testcase.getEvaluationSubstanceAdministrationEvent(
                                    shot2CvxCode,
                                    shot2DateofAdministration.getDateCellValue(),
                                    "Valid".equalsIgnoreCase(shot2Evaluation.getStringCellValue()),
                                    new Reason[]{
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot2Evaluation.getStringCellValue().toUpperCase(),
                                        shot2InvalidReason1Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot2Evaluation.getStringCellValue().toUpperCase(),
                                        shot2InvalidReason2Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot2Evaluation.getStringCellValue().toUpperCase(),
                                        shot2InvalidReason3Code.getStringCellValue())
                                    });
                            testcase.addSubstanceAdministrationEvent(
                                    shot2CvxCode,
                                    shot2DateofAdministration.getDateCellValue(),
                                    new SubstanceAdministrationEvent[]{shot2EvaluationEvent});
                        } catch (IceException e) {
                            logger.error("addSubstanceAdministrationEvent shot 2 Error @ " + location);
                            logger.error(e);
                            testcase.setErrorMessage(e.getMessage());
                            callback.callback(testcase, testSheetName, false);
                            continue;
                        }
                    }

                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell shot3Vaccine = testCurrentRow.getCell(2);
                    logger.trace("    Shot 3 vaccine: " + shot3Vaccine);
                    XSSFCell shot3Cvx = testCurrentRow.getCell(3);
                    String shot3CvxCode = shot3Cvx.getRawValue();
                    if (shot3CvxCode != null && !shot3CvxCode.isEmpty()) {
                        logger.trace("    Shot 3 CVX: " + shot3Cvx.getRawValue());
                        XSSFCell shot3Evaluation = testCurrentRow.getCell(4);
                        logger.trace("    Shot 3 evaluation: " + shot3Evaluation);
                        XSSFCell shot3InvalidReason1Code = testCurrentRow.getCell(5);
                        logger.trace("    Shot 3 invalid reason 1 code: " + shot3InvalidReason1Code);
                        XSSFCell shot3InvalidReason2Code = testCurrentRow.getCell(6);
                        logger.trace("    Shot 3 invalid reason 2 code: " + shot3InvalidReason2Code);
                        XSSFCell shot3InvalidReason3Code = testCurrentRow.getCell(7);
                        logger.trace("    Shot 3 invalid reason 3 code: " + shot3InvalidReason3Code);
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell shot3DateofAdministration = testCurrentRow.getCell(2);
                        logger.trace("    Shot 3 date of administration: " + shot3DateofAdministration);

                        try {
                            SubstanceAdministrationEvent shot3EvaluationEvent =
                                    testcase.getEvaluationSubstanceAdministrationEvent(
                                    shot3CvxCode,
                                    shot3DateofAdministration.getDateCellValue(),
                                    "Valid".equalsIgnoreCase(shot3Evaluation.getStringCellValue()),
                                    new Reason[]{
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot3Evaluation.getStringCellValue().toUpperCase(),
                                        shot3InvalidReason1Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot3Evaluation.getStringCellValue().toUpperCase(),
                                        shot3InvalidReason2Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot3Evaluation.getStringCellValue().toUpperCase(),
                                        shot3InvalidReason3Code.getStringCellValue())
                                    });
                            testcase.addSubstanceAdministrationEvent(
                                    shot3CvxCode,
                                    shot3DateofAdministration.getDateCellValue(),
                                    new SubstanceAdministrationEvent[]{shot3EvaluationEvent});
                        } catch (IceException e) {
                            logger.error("addSubstanceAdministrationEvent shot 3 Error @ " + location);
                            logger.error(e);
                            testcase.setErrorMessage(e.getMessage());
                            callback.callback(testcase, testSheetName, false);
                            continue;
                        }
                    }

                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell shot4Vaccine = testCurrentRow.getCell(2);
                    logger.trace("    Shot 4 vaccine: " + shot4Vaccine);
                    XSSFCell shot4Cvx = testCurrentRow.getCell(3);
                    String shot4CvxCode = shot4Cvx.getRawValue();
                    if (shot4CvxCode != null && !shot4CvxCode.isEmpty()) {
                        logger.trace("    Shot 4 CVX: " + shot4Cvx.getRawValue());
                        XSSFCell shot4Evaluation = testCurrentRow.getCell(4);
                        logger.trace("    Shot 4 evaluation: " + shot4Evaluation);
                        XSSFCell shot4InvalidReason1Code = testCurrentRow.getCell(5);
                        logger.trace("    Shot 4 invalid reason 1 code: " + shot4InvalidReason1Code);
                        XSSFCell shot4InvalidReason2Code = testCurrentRow.getCell(6);
                        logger.trace("    Shot 4 invalid reason 2 code: " + shot4InvalidReason2Code);
                        XSSFCell shot4InvalidReason3Code = testCurrentRow.getCell(7);
                        logger.trace("    Shot 4 invalid reason 3 code: " + shot4InvalidReason3Code);
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell shot4DateofAdministration = testCurrentRow.getCell(2);
                        logger.trace("    Shot 4 date of administration: " + shot4DateofAdministration);

                        try {
                            SubstanceAdministrationEvent shot4EvaluationEvent =
                                    testcase.getEvaluationSubstanceAdministrationEvent(
                                    shot4CvxCode,
                                    shot4DateofAdministration.getDateCellValue(),
                                    "Valid".equalsIgnoreCase(shot4Evaluation.getStringCellValue()),
                                    new Reason[]{
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot4Evaluation.getStringCellValue().toUpperCase(),
                                        shot4InvalidReason1Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot4Evaluation.getStringCellValue().toUpperCase(),
                                        shot4InvalidReason2Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot4Evaluation.getStringCellValue().toUpperCase(),
                                        shot4InvalidReason3Code.getStringCellValue())
                                    });
                            testcase.addSubstanceAdministrationEvent(
                                    shot4CvxCode,
                                    shot4DateofAdministration.getDateCellValue(),
                                    new SubstanceAdministrationEvent[]{shot4EvaluationEvent});
                        } catch (IceException e) {
                            logger.error("addSubstanceAdministrationEvent shot 4 Error @ " + location);
                            logger.error(e);
                            testcase.setErrorMessage(e.getMessage());
                            callback.callback(testcase, testSheetName, false);
                            continue;
                        }
                    }

                    testCurrentRowNum++;
                    testCurrentRow = testSheet.getRow(testCurrentRowNum);
                    XSSFCell shot5Vaccine = testCurrentRow.getCell(2);
                    logger.trace("    Shot 5 vaccine: " + shot5Vaccine);
                    XSSFCell shot5Cvx = testCurrentRow.getCell(3);
                    String shot5CvxCode = shot5Cvx.getRawValue();
                    if (shot5CvxCode != null && !shot5CvxCode.isEmpty()) {
                        logger.trace("    Shot 5 CVX: " + shot5Cvx.getRawValue());
                        XSSFCell shot5Evaluation = testCurrentRow.getCell(4);
                        logger.trace("    Shot 5 evaluation: " + shot5Evaluation);
                        XSSFCell shot5InvalidReason1Code = testCurrentRow.getCell(5);
                        logger.trace("    Shot 5 invalid reason 1 code: " + shot5InvalidReason1Code);
                        XSSFCell shot5InvalidReason2Code = testCurrentRow.getCell(6);
                        logger.trace("    Shot 5 invalid reason 2 code: " + shot5InvalidReason2Code);
                        XSSFCell shot5InvalidReason3Code = testCurrentRow.getCell(7);
                        logger.trace("    Shot 5 invalid reason 3 code: " + shot5InvalidReason3Code);
                        testCurrentRowNum++;
                        testCurrentRow = testSheet.getRow(testCurrentRowNum);
                        XSSFCell shot5DateofAdministration = testCurrentRow.getCell(2);
                        logger.trace("    Shot 5 date of administration: " + shot5DateofAdministration);

                        try {
                            SubstanceAdministrationEvent shot5EvaluationEvent =
                                    testcase.getEvaluationSubstanceAdministrationEvent(
                                    shot5CvxCode,
                                    shot5DateofAdministration.getDateCellValue(),
                                    "Valid".equalsIgnoreCase(shot5Evaluation.getStringCellValue()),
                                    new Reason[]{
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot5Evaluation.getStringCellValue().toUpperCase(),
                                        shot5InvalidReason1Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot5Evaluation.getStringCellValue().toUpperCase(),
                                        shot5InvalidReason2Code.getStringCellValue()),
                                        new Reason(
                                        String.valueOf(testcase.getVaccinegroup()),
                                        shot5Evaluation.getStringCellValue().toUpperCase(),
                                        shot5InvalidReason3Code.getStringCellValue())
                                    });
                            testcase.addSubstanceAdministrationEvent(
                                    shot5CvxCode,
                                    shot5DateofAdministration.getDateCellValue(),
                                    new SubstanceAdministrationEvent[]{shot5EvaluationEvent});
                        } catch (IceException e) {
                            logger.error("addSubstanceAdministrationEvent shot 5 Error @ " + location);
                            logger.error(e);
                            testcase.setErrorMessage(e.getMessage());
                            callback.callback(testcase, testSheetName, false);
                            continue;
                        }
                    }

                    int count = 0;
                    for (SubstanceAdministrationEvent sae : testcase.getSubstanceAdministrationEvents()) {
                        count++;
                        logger.debug("Shot " + count
                                + " present: " + sae.getSubstance().getSubstanceCode().getCode()
                                + "/" + sae.getAdministrationTimeInterval().getHigh());
                        int count2 = 0;
                        for (RelatedClinicalStatement rcs : sae.getRelatedClinicalStatements()) {
                            count2++;
                            SubstanceAdministrationEvent csae = rcs.getSubstanceAdministrationEvent();
                            logger.debug("Shot " + count
                                    + " Component " + count2
                                    + " present: " + csae.getSubstance().getSubstanceCode().getCode()
                                    + "/" + csae.getAdministrationTimeInterval().getHigh()
                                    + "/" + csae.getIsValid().isValue());
                            int count3 = 0;
                            for (RelatedClinicalStatement crcs : csae.getRelatedClinicalStatements()) {
                                count3++;
                                ObservationResult cor = crcs.getObservationResult();
                                logger.debug("Shot " + count
                                        + " Component " + count2
                                        + " OR " + count3
                                        + " present: " + cor.getObservationFocus().getCode()
                                        + "/" + cor.getObservationValue().getConcept().getCode()
                                        + "/" + cor.getInterpretations().get(0).getCode());
                            }
                        }
                    }

                    callback.callback(testcase, testSheetName, true);
                } else {
                    // logger.warn("Sheet '" + testSheetName + "' - Row " + i + ": test name was null.");
                }
            } else {
                // logger.warn("Row " + i + " was null.");
            }
        }
        logger.info("Number of tests imported: " + testCount);
        if (testCount != importedTestcases.size()) {
            logger.warn("Count mismatch: " + testCount + " - " + importedTestcases.size());
        }
    }
}
