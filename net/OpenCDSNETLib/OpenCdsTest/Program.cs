using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using ice.service;
using System.Xml.Serialization;
using System.Xml;

namespace OpenCdsTest
{
    class Program
    {
        private static string endPoint = "http://client.hln.com/opencds-decision-support-service-1.0.0-SNAPSHOT/evaluate";
        private static string scopingEntityId = "org.nyc.cir";
        private static string businessId = "ICE";
        private static string version = "1.0.0";
        private static string sampleFilename = "sampleCdsInput.xml";

        static void Main(string[] args)
        {
            callServiceWithXmlFile();
            callServiceWithObject();
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey(false);
        }

        private static void callServiceWithXmlFile()
        {
            StreamReader sr = new StreamReader(sampleFilename);
            XmlSerializer serializer = new XmlSerializer(typeof(CDSInput));
            CDSInput cdsInput = (CDSInput)serializer.Deserialize(sr);
            sr.Close();
            CDSOutput response = getResponse(cdsInput);
            processResponse(response);
        }

        private static void callServiceWithObject()
        {
            string clientSystemPatientId = "1234567890";
            CDSInput cdsInput = new CDSInput();

            II templateId = new II();
            templateId.root = "2.16.840.1.113883.3.795.11.1.1";
            cdsInput.templateId = new II[] { templateId };

            CDSContext cdsContext = new CDSContext();
            CD preferredLanguage = new CD();
            preferredLanguage.code = "en";
            preferredLanguage.codeSystem = "1.2.3";
            preferredLanguage.displayName = "English";
            cdsContext.cdsInformationRecipientPreferredLanguage = preferredLanguage;
            cdsInput.cdsContext = cdsContext;

            VMR vmr = new VMR();
            cdsInput.vmrInput = vmr;

            II vmrTemplateId = new II();
            vmrTemplateId.root = "2.16.840.1.113883.3.795.11.1.1";
            vmr.templateId = new II[] { vmrTemplateId };

            EvaluatedPerson patient = new EvaluatedPerson();

            II personTemplateId = new II();
            personTemplateId.root = "2.16.840.1.113883.3.795.11.2.1.1";
            patient.templateId = new II[] { personTemplateId };

            II id = new II();
            id.root = "2.16.840.1.113883.3.795.12.100.11";
            id.extension = clientSystemPatientId;
            patient.id = id;

            EvaluatedPersonDemographics demographics = new EvaluatedPersonDemographics();

            TS birthTime = new TS();
            birthTime.value = "20110401";
            demographics.birthTime = birthTime;

            CD gender = new CD();
            gender.code = "F";
            gender.codeSystem = "2.16.840.1.113883.5.1";
            demographics.gender = gender;

            patient.demographics = demographics;
            vmr.patient = patient;

            EvaluatedPersonClinicalStatements statements = new EvaluatedPersonClinicalStatements();
            patient.clinicalStatements = statements;

            SubstanceAdministrationEvent statement1 = getSubstanceAdministrationEvent("185", "20110430");
            SubstanceAdministrationEvent statement2 = getSubstanceAdministrationEvent("184", "20110523");
            SubstanceAdministrationEvent statement3 = getSubstanceAdministrationEvent("183", "20110721");
            SubstanceAdministrationEvent statement4 = getSubstanceAdministrationEvent("182", "20110911");
            SubstanceAdministrationEvent statement5 = getSubstanceAdministrationEvent("181", "20110912");

            statements.substanceAdministrationEvents = new SubstanceAdministrationEvent[] { statement1, statement2, statement3, statement4, statement5 };
            CDSOutput response = getResponse(cdsInput);
            processResponse(response);
        }

        private static CDSOutput getResponse(CDSInput cdsInput)
        {
            OpenCdsService service = new OpenCdsService(endPoint);
            CDSOutput response = service.evaluate(cdsInput, scopingEntityId, businessId, version);
            return response;
        }

        private static void processResponse(CDSOutput response)
        {
            XmlSerializer serializer = new XmlSerializer(typeof(CDSOutput));
            MemoryStream ms = new MemoryStream();
            XmlWriterSettings settings = new XmlWriterSettings();
            settings.Encoding = Encoding.UTF8;
            settings.Indent = true;
            settings.IndentChars = "    ";
            settings.ConformanceLevel = ConformanceLevel.Document;
            XmlWriter writer = XmlTextWriter.Create(ms, settings);
            serializer.Serialize(writer, response);
            Console.WriteLine("Raw Output:");
            Console.WriteLine(Encoding.UTF8.GetString(ms.ToArray()));
            ms.Close();
            VMR vmr = response.vmrOutput;
            if (vmr == null)
            {
                throw new Exception("vmr was null!");
            }
            EvaluatedPerson patient = vmr.patient;
            if (patient == null)
            {
                throw new Exception("patient wasd null!");
            }
            EvaluatedPersonClinicalStatements statements = patient.clinicalStatements;
            if (statements == null)
            {
                throw new Exception("statments was null!");
            }
            SubstanceAdministrationProposal[] proposals = statements.substanceAdministrationProposals;
            if (proposals == null || proposals.Length == 0)
            {
                throw new Exception("proposals was null or empty!");
            }
            for (int i = 0; i < proposals.Length; i++)
            {
                SubstanceAdministrationProposal proposal = proposals[i];
                if (proposal == null)
                {
                    throw new Exception("proposal was null!");
                }
                Console.WriteLine("Substance administration proposal:");
                AdministrableSubstance substance = proposal.substance;
                if (substance == null)
                {
                    throw new Exception("substance was null!");
                }
                CD substanceCode = substance.substanceCode;
                if (substanceCode != null)
                {
                    Console.WriteLine("    proposed substance code: " + substanceCode.code);
                }
                IVL_TS proposedTimeInterval = proposal.proposedAdministrationTimeInterval;
                if (proposedTimeInterval != null)
                {
                    Console.WriteLine("    proposed administration time: " + proposedTimeInterval.low);
                }
                RelatedClinicalStatement[] reasons = proposal.relatedClinicalStatement;

                if (reasons != null)
                {
                    for (int c = 0; c < reasons.Length; c++)
                    {
                        RelatedClinicalStatement reason = reasons[c];
                        ObservationResult result = (ObservationResult)reason.Item;
                        if (result != null)
                        {
                            Console.WriteLine("    proposed reason - focus: " + result.observationFocus.displayName);
                            Console.WriteLine("    proposed reason - value: " + ((CD) result.observationValue.Item).code);
                            Console.WriteLine("    proposed reason - interpretation: " + result.interpretation[0].code);
                        }
                    }
                }
            }
        }

        private static SubstanceAdministrationEvent getSubstanceAdministrationEvent(string clientImmunizationId, string administrationTime)
        {
            II statementTemplateId = new II();
            statementTemplateId.root = "2.16.840.1.113883.3.795.11.9.1.1";

            CD substanceAdminGeneralPurpose = new CD();
            substanceAdminGeneralPurpose.code = "384810002";
            substanceAdminGeneralPurpose.codeSystem = "2.16.840.1.113883.6.5";

            CD substanceCode = new CD();
            substanceCode.code = "08";
            substanceCode.codeSystem = "2.16.840.1.113883.12.292";

            II id = new II();
            id.root = "2.16.840.1.113883.3.795.12.100.10";
            id.extension = clientImmunizationId;

            SubstanceAdministrationEvent statement = new SubstanceAdministrationEvent();
            statement.templateId = new II[] { statementTemplateId };
            statement.id = id;
            statement.substanceAdministrationGeneralPurpose = substanceAdminGeneralPurpose;

            AdministrableSubstance substance = new AdministrableSubstance();
            II substanceId = new II();
            substanceId.root = System.Guid.NewGuid().ToString("D");
            substance.id = substanceId;

            substance.substanceCode = substanceCode;
            statement.substance = substance;

            IVL_TS administrationTimeInterval = new IVL_TS();
            administrationTimeInterval.high = administrationTime;
            administrationTimeInterval.low = administrationTime;
            statement.administrationTimeInterval = administrationTimeInterval;

            return statement;
        }
    }
}
