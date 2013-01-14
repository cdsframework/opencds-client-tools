package org.cdsframework.cds.service;

import java.util.Date;
import org.cdsframework.cds.util.Configuration;
import org.cdsframework.cds.util.LogUtils;
import org.cdsframework.cds.vmr.CdsInputWrapper;
import org.cdsframework.cds.vmr.CdsObjectAssist;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencds.support.CdsInput;
import org.opencds.support.CdsOutput;

/**
 *
 * @author HLN Consulting, LLC
 */
public class OpenCdsServiceTest {

    private static LogUtils logger = LogUtils.getLogger(OpenCdsServiceTest.class);

    public OpenCdsServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of evaluate method, of class OpenCdsService.
     * @throws Exception
     */
    @Test
    public void testEvaluate() throws Exception {
        long start;
        logger.info("Starting testEvaluate...");

        String endPoint = Configuration.getCdsDefaultEndpoint();

        // preform first to time get the static initialization of OpenCdsService out of the way
        OpenCdsService.getOpenCDS(endPoint);

        start = System.nanoTime();
        OpenCdsService service = OpenCdsService.getOpenCDS(endPoint);
        logger.logDuration("OpenCdsService init", start);

        // preform first to time get the static initialization of CdsObjectAssist out of the way
        CdsInputWrapper.getCdsInputWrapper();

        start = System.nanoTime();
        CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
        input.setPatientGender("F");
        input.setPatientBirthTime("19830630");
        input.addSubstanceAdministrationEvent("45", "20080223", null);
        logger.logDuration("getCdsInputWrapper", start);

        input.addSubstanceAdministrationEvent("10", "20080223", "12345");
        input.addImmunityObservationResult(new Date(), "070.30", "DISEASE_DOCUMENTED", "IS_IMMUNE");

        input.addSubstanceAdministrationEvent("43", "20080223", "12346");

        input.addSubstanceAdministrationEvent("08", "20090223", "12347");

        input.addSubstanceAdministrationEvent("43", "20080223", "12348");

        String inputXml = CdsObjectAssist.cdsObjectToString(input.getCdsObject(), CdsInput.class);

        String scopingEntityId = "org.nyc.cir";
        String businessId = "ICE";
        String version = "1.0.0";
        Date executionDate = new Date();

        start = System.nanoTime();
        byte[] cdsObjectToByteArray = CdsObjectAssist.cdsObjectToByteArray(input.getCdsObject(), CdsInput.class);
        logger.logDuration("cdsObjectToByteArray", start);

        start = System.nanoTime();
        byte[] evaluation = service.evaluate(cdsObjectToByteArray, scopingEntityId, businessId, version, executionDate);
        logger.logDuration("evaluate test 1", start);

        start = System.nanoTime();
        evaluation = service.evaluate(cdsObjectToByteArray, scopingEntityId, businessId, version, executionDate);
        logger.logDuration("evaluate test 2", start);

        start = System.nanoTime();
        evaluation = service.evaluate(cdsObjectToByteArray, scopingEntityId, businessId, version, executionDate);
        logger.logDuration("evaluate test 3", start);

        start = System.nanoTime();
        evaluation = service.evaluate(cdsObjectToByteArray, scopingEntityId, businessId, version, executionDate);
        logger.logDuration("evaluate test 4", start);

        start = System.nanoTime();
        evaluation = service.evaluate(cdsObjectToByteArray, scopingEntityId, businessId, version, executionDate);
        logger.logDuration("evaluate test 5", start);

        start = System.nanoTime();
        CdsOutput result = CdsObjectAssist.cdsObjectFromByteArray(evaluation, CdsOutput.class);
        logger.logDuration("cdsObjectFromByteArray", start);

        logger.info("Finished testEvaluate...");
        assertTrue(result instanceof CdsOutput);
    }
}
