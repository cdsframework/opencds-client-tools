using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ServiceModel;
using ice.service.OpenCds;
using System.Xml.Serialization;
using System.IO;
using System.Xml;

namespace ice.service
{
    public class OpenCdsService
    {
        private static int DEFAULT_TIMEOUT = 10 * 1000;
        private readonly string endPoint;
        private readonly EvaluationClient service;
        private readonly int requestTimeout;
        private readonly int connectTimeout;

        public OpenCdsService(string endPoint)
            : this(endPoint, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT)
        {
        }

        public OpenCdsService(String endPoint, int timeout)
            : this(endPoint, timeout, timeout)
        {
        }

        public OpenCdsService(String endPoint, int requestTimeout, int connectTimeout)
        {
            this.endPoint = endPoint;
            this.requestTimeout = requestTimeout;
            this.connectTimeout = connectTimeout;
            BasicHttpBinding binding = new BasicHttpBinding();
            binding.Name = "OpenCdsService";
            binding.OpenTimeout = TimeSpan.FromMilliseconds(connectTimeout);
            binding.ReceiveTimeout = TimeSpan.FromMilliseconds(requestTimeout);
            binding.MaxBufferSize = 65536;
            binding.MaxBufferPoolSize = 524288;
            binding.ReaderQuotas.MaxDepth = 32;
            binding.ReaderQuotas.MaxStringContentLength = 65536;
            binding.ReaderQuotas.MaxArrayLength = 16384;
            binding.ReaderQuotas.MaxBytesPerRead = 4096;
            binding.ReaderQuotas.MaxNameTableCharCount = 16384;
            EndpointAddress remoteAddress = new EndpointAddress(endPoint);
            service = new EvaluationClient(binding, remoteAddress);
            Console.WriteLine("Constructing OpenCdsService from " + endPoint);
        }
        public CDSOutput evaluate(CDSInput cdsInput, string scopingEntityId, string businessId, string version)
        {
            CDSOutput cdsOutput = null;
            MemoryStream ms = new MemoryStream();
            XmlWriterSettings settings = new XmlWriterSettings();
            settings.Encoding = Encoding.UTF8;
            settings.Indent = true;
            settings.IndentChars = "    ";
            settings.ConformanceLevel = ConformanceLevel.Document;
            XmlWriter writer = XmlTextWriter.Create(ms, settings);
            XmlSerializer serializer = new XmlSerializer(typeof(CDSInput));
            serializer.Serialize(ms, cdsInput);
            string cdsInputString = Encoding.UTF8.GetString(ms.ToArray());
            ms.Close();
            string response = evaluate(cdsInputString, scopingEntityId, businessId, version);
            serializer = new XmlSerializer(typeof(CDSOutput));
            StringReader sr = new StringReader(response);
            cdsOutput = (CDSOutput)serializer.Deserialize(sr);
            sr.Close();
            return cdsOutput;
        }
        public string evaluate(string cdsInputString, string scopingEntityId, string businessId, string version)
        {
            string METHODNAME = "evaluate ";
            if (true)
            {
                Console.WriteLine(METHODNAME
                        + "calling evaluate with businessId '"
                        + businessId
                        + "; scopingEntityId "
                        + scopingEntityId
                        + "; version "
                        + version
                        + "' @ "
                        + endPoint
                        + " with requestTimeout:"
                        + requestTimeout
                        + " and connectTimeout:"
                        + connectTimeout);
            }
            evaluateResponse response = null;
            string result = null;

            InteractionIdentifier interactionIdentifier = getInteractionIdentifier();
            EvaluationRequest evaluationRequest = getEvaluationRequest(encodeTo64(cdsInputString), scopingEntityId, businessId, version);
            evaluate e = new evaluate();

            e.interactionId = interactionIdentifier;
            e.evaluationRequest = evaluationRequest;

            response = service.evaluate(e);

            if (response == null)
            {
                throw new Exception("response is null!");
            }
            EvaluationResponse er = response.evaluationResponse;
            FinalKMEvaluationResponse[] finalKMEvaluationResponse = er.finalKMEvaluationResponse;

            if (finalKMEvaluationResponse == null)
            {
                throw new Exception("finalKMEvaluationResponse is null!");
            }
            if (finalKMEvaluationResponse.Length != 1)
            {
                throw new Exception("finalKMEvaluationResponse size wrong: " + finalKMEvaluationResponse.Length);
            }

            FinalKMEvaluationResponse kmEvaluationResponse = finalKMEvaluationResponse[0];
            KMEvaluationResultData[] kmEvaluationResultData = kmEvaluationResponse.kmEvaluationResultData;

            if (kmEvaluationResultData == null)
            {
                throw new Exception("kmEvaluationResultData is null!");
            }
            if (kmEvaluationResultData.Length != 1)
            {
                throw new Exception("kmEvaluationResultData size wrong: " + kmEvaluationResultData.Length);
            }
            KMEvaluationResultData resultData = kmEvaluationResultData[0];
            if (resultData == null)
            {
                throw new Exception("resultData is null!");
            }

            SemanticPayload data = resultData.data;
            if (data == null)
            {
                throw new Exception("data is null!");
            }
            result = decodeFrom64(data.base64EncodedPayload);
            if (result == null)
            {
                throw new Exception("result is null!");
            }
            return result;
        }

        private EntityIdentifier getIIEntityIdentifier(string businessId)
        {
            EntityIdentifier iiEntityIdentifier = new EntityIdentifier();
            iiEntityIdentifier.scopingEntityId = "gov.nyc.health";
            iiEntityIdentifier.version = "1.0.0.0";
            iiEntityIdentifier.businessId = businessId + "Data";
            return iiEntityIdentifier;
        }

        private ItemIdentifier getItemIdentifier(String businessId)
        {
            ItemIdentifier itemIdentifier = new ItemIdentifier();
            itemIdentifier.itemId = "cdsPayload";
            itemIdentifier.containingEntityId = getIIEntityIdentifier(businessId);
            return itemIdentifier;
        }

        private EntityIdentifier getSPEntityIdentifier()
        {
            EntityIdentifier spEntityIdentifier = new EntityIdentifier();
            spEntityIdentifier.businessId = "VMR";
            spEntityIdentifier.scopingEntityId = "org.opencds.vmr";
            spEntityIdentifier.version = "1.0";
            return spEntityIdentifier;
        }

        private SemanticPayload getSemanticPayload(string cdsInput)
        {
            SemanticPayload semanticPayload = new SemanticPayload();
            semanticPayload.informationModelSSId = getSPEntityIdentifier();
            semanticPayload.base64EncodedPayload = cdsInput;
            return semanticPayload;
        }

        private DataRequirementItemData getDataRequirementItemData(string cdsInput, string businessId)
        {
            DataRequirementItemData dataRequirementItemData = new DataRequirementItemData();
            dataRequirementItemData.driId = getItemIdentifier(businessId);
            dataRequirementItemData.data = getSemanticPayload(cdsInput);
            return dataRequirementItemData;
        }

        private InteractionIdentifier getInteractionIdentifier()
        {
            InteractionIdentifier interactionIdentifier = new InteractionIdentifier();
            interactionIdentifier.interactionId = "123456";
            interactionIdentifier.scopingEntityId = "gov.nyc.health";
            return interactionIdentifier;
        }

        private EntityIdentifier getKMEntityIdentifier(string scopingEntityId, string businessId, string version)
        {
            EntityIdentifier kmEntityIdentifier = new EntityIdentifier();
            kmEntityIdentifier.scopingEntityId = scopingEntityId;
            kmEntityIdentifier.version = version;
            kmEntityIdentifier.businessId = businessId;
            return kmEntityIdentifier;
        }

        private KMEvaluationRequest getKMEvaluationRequest(string scopingEntityId, string businessId, string version)
        {
            KMEvaluationRequest kmEvaluationRequest = new KMEvaluationRequest();
            kmEvaluationRequest.kmId = getKMEntityIdentifier(scopingEntityId, businessId, version);
            return kmEvaluationRequest;
        }

        private EvaluationRequest getEvaluationRequest(string cdsInput, string scopingEntityId, string businessId, string version)
        {
            EvaluationRequest evaluationRequest = new EvaluationRequest();
            evaluationRequest.clientLanguage = "";
            evaluationRequest.clientTimeZoneOffset = "";

            evaluationRequest.kmEvaluationRequest = new KMEvaluationRequest[] { getKMEvaluationRequest(scopingEntityId, businessId, version) };
            evaluationRequest.dataRequirementItemData = new DataRequirementItemData[] { getDataRequirementItemData(cdsInput, businessId) };
            return evaluationRequest;
        }

        static public string encodeTo64(string toEncode)
        {
            byte[] toEncodeAsBytes
                  = System.Text.ASCIIEncoding.ASCII.GetBytes(toEncode);
            string returnValue
                  = System.Convert.ToBase64String(toEncodeAsBytes);
            return returnValue;
        }

        static public string decodeFrom64(string encodedData)
        {
            byte[] encodedDataAsBytes
                  = System.Convert.FromBase64String(encodedData);
            string returnValue =
               System.Text.ASCIIEncoding.ASCII.GetString(encodedDataAsBytes);
            return returnValue;
        }
    }
}
