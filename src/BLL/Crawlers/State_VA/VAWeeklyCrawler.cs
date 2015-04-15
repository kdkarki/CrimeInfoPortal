using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net;

namespace BLL.Crawlers
{
    public class VAWeeklyCrawler : CIPCrawler
    {
        public VAWeeklyCrawler(DAL.CriminalActivityRecord LastUpdatedRecord, string SourceUrl, int StateId) : base(LastUpdatedRecord, SourceUrl, StateId)
        {

        }

        public override List<DAL.CriminalActivityRecord> StartCrawling()
        {
            List<DAL.CriminalActivityRecord> activityRecords = new List<DAL.CriminalActivityRecord>();

            HttpWebRequest webReq = (HttpWebRequest)HttpWebRequest.Create(_sourceUrl);
            using (HttpWebResponse webResp = webReq.GetResponse() as HttpWebResponse)
            {
                if (webResp != null)
                {
                    if (webResp.StatusCode == HttpStatusCode.OK)
                    {
                        using (System.IO.StreamReader strm = new System.IO.StreamReader(webResp.GetResponseStream()))
                        {
                            var response = strm.ReadToEnd();

                            if (!String.IsNullOrEmpty(response))
                            {
                                activityRecords = ParseTextAndRetrieveRecords(response);
                            }
                        }
                    }
                }
            }

            return activityRecords;
        }

        private List<DAL.CriminalActivityRecord> ParseTextAndRetrieveRecords(string StringRecords)
        {
            List<DAL.CriminalActivityRecord> recordList = new List<DAL.CriminalActivityRecord>();
            string[] recordsStringArray = StringRecords.Split(new string[] { Environment.NewLine }, StringSplitOptions.RemoveEmptyEntries);

            if(recordsStringArray != null && recordsStringArray.Length > 0)
            {
                using (DAL.CrimeInfoPortalDAO dao = new DAL.CrimeInfoPortalDAO())
                {
                    var cityList = dao.Cities.Where(c => c.State.StateID == _stateId).OrderBy(c => c.Name);
                    foreach (string cRecord in recordsStringArray)
                    {
                        if (String.IsNullOrWhiteSpace(cRecord) || cRecord.Length < 210) continue;
                        /* the source is a text file which returns ciminal activities logged in past week
                         * the text file is a flat file with each column starting at a specific position
                         * 
                         * Column            Position
                         * ----------       -----------
                         * LName                0
                         * FName                40
                         * MName                60
                         * Age                  100
                         * DateArr              105
                         * Charge               135
                         * Charge Descript      160
                         * Address              210
                         */

                        try
                        {
                            string lastName = cRecord.Substring(0, 40).Trim();
                            if (lastName.Trim() == "LName") continue;
                            string firstname = cRecord.Substring(40, 20).Trim();
                            string middleName = cRecord.Substring(60, 40).Trim();
                            string age = cRecord.Substring(100, 4).Trim();
                            string dateArrested = cRecord.Substring(105, 30).Trim();
                            string charge = cRecord.Substring(135, 25).Trim();
                            string chargeDescript = cRecord.Substring(160, 50).Trim();
                            string address = cRecord.Substring(210).Trim();
                            
                            if (String.IsNullOrWhiteSpace(charge) || String.IsNullOrWhiteSpace(address)) continue;
                            DAL.City city = null;

                            string[] addressParts = address.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                            if (addressParts != null && addressParts.Length == 3)
                            {
                                var currentCity = cityList.ToList().FirstOrDefault(c=>c.Name.ToLower() == addressParts[1].Trim().ToLower());
                                if (currentCity != null)
                                    city = currentCity;
                                else
                                    continue;
                            }
                            else
                            {
                                continue;
                            }

                            short intAge = 0;
                            DAL.CriminalActivityRecord car = new DAL.CriminalActivityRecord()
                                                            {
                                                                FirstName = !String.IsNullOrWhiteSpace(firstname) ? firstname : null,
                                                                MiddleName = !String.IsNullOrWhiteSpace(middleName) ? middleName : null,
                                                                LastName = !String.IsNullOrWhiteSpace(lastName) ? lastName : null,
                                                                Age = short.TryParse(age, out intAge) ? intAge : new short(),
                                                                DateArrested = DateTime.Parse(dateArrested),
                                                                ChargeCode = charge,
                                                                Address = address,
                                                                CityId = city.CityID 
                                                            };
                            recordList.Add(car);
                        }
                        catch (Exception ex)
                        {

                        }
                    }
                }
            }

            return recordList;
        }
    }
}