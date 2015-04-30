using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BLL.Crawlers
{
    public class VADailyCrawler : CIPCrawler
    {
        public VADailyCrawler(DAL.CriminalActivityRecord LastUpdatedRecord, string SourceUrl, int StateId):base(LastUpdatedRecord, SourceUrl, StateId)
        {

        }

        public override List<DAL.CriminalActivityRecord> StartCrawling()
        {
            return new List<DAL.CriminalActivityRecord>();
            throw new NotImplementedException();
        }
    }
}