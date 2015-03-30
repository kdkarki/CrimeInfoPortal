using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net;
using System.Web.Script.Serialization;
using System.Collections;
using DAL;

namespace BLL
{
    public class StateCodeDomain
    {
        public static void UpdateStateChargeCodeDomain(string StateCode)
        {
            //There might be more than one page of domain file so will have to start with page 1
            int currentPageCount = 0;
            
            while (true)
            {
                currentPageCount++;
                HttpWebRequest webReq
                    = (HttpWebRequest)HttpWebRequest.Create(GetStateChargeCodeDomainUrl(StateCode, currentPageCount));
                HttpWebResponse webResp = webReq.GetResponse() as HttpWebResponse;

                if (webResp != null)
                {
                    if (webResp.StatusCode == HttpStatusCode.OK)
                    {
                        using (System.IO.StreamReader strm = new System.IO.StreamReader(webResp.GetResponseStream()))
                        {
                            var response = strm.ReadToEnd();
                            UpdateStateChargeCodeDomainFromJsonResponse(StateCode, response);
                        }
                        webResp.Close();
                    }
                    else
                    {
                        webResp.Close();
                        break;
                    }
                }
                else
                    break;
            }
        }

        private static string GetStateChargeCodeDomainUrl(string StateCode, int CurrentPageCount)
        {
            //Right now we do this only for state of VA
            if (StateCode.ToLower() != "va") return String.Empty;
            return String.Format(@"http://www.fairfaxcountyarrests.com/searchgridvcc.php?oper=grid&_search=false&nd=1427311453699&rows=100&page={0}&sidx=HEADING&sord=desc"
                                            , CurrentPageCount);
        }

        private static void UpdateStateChargeCodeDomainFromJsonResponse(string StateCode, string JsonResponse)
        {
            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                DAL.State currentState = objDAO.States.FirstOrDefault(s => s.StateCode.ToLower() == StateCode.ToLower());

                JavaScriptSerializer ser = new JavaScriptSerializer();
                Dictionary<string, Object> respDict = ser.Deserialize<Dictionary<string, Object>>(JsonResponse);

                if (respDict != null && respDict.Count > 0)
                {
                    ArrayList rows = respDict["rows"] as ArrayList;
                    if (rows != null)
                    {
                        foreach (Dictionary<string, Object> item in rows)
                        {
                            try
                            {
                                if (String.IsNullOrEmpty(item["VCC_CODE"].ToString()))
                                    continue;
                                StateCriminalCodeDomain sccd = new StateCriminalCodeDomain();
                                sccd.Code = item["VCC_CODE"].ToString();
                                sccd.Statute = item["STATUTE"].ToString();
                                sccd.Sentence = item["SENTENCE"].ToString();
                                sccd.Heading = item["HEADING"].ToString();
                                sccd.Subheading = item["SUBHEAD"].ToString();
                                sccd.Description = item["DESCRIPTION"].ToString();
                                sccd.StateId = currentState.StateID;
                                //Temporary until we have a domain for this.
                                sccd.CriminalEventType = "UNKNOWN";

                                objDAO.AddToStateCriminalCodeDomains(sccd);
                            }
                            catch (Exception ex)
                            {
                                System.Diagnostics.Debug.Write(ex.Message);
                            }                            
                        }

                        try
                        {
                            int output = objDAO.SaveChanges();
                        }
                        catch (Exception ex)
                        {
                            System.Diagnostics.Debug.Write(ex.Message);
                        }
                    }
                }
            }
        }
    }
}