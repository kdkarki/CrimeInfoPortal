using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BLL.Crawlers.State_VA
{
    public class VAWeeklyCrawler : CIPCrawler
    {
        public VAWeeklyCrawler(DAL.CriminalActivityRecord LastUpdatedRecord, string SourceUrl) : base(LastUpdatedRecord, SourceUrl)
        {

        }

        public override List<DAL.CriminalActivityRecord> StartCrawling()
        {
            throw new NotImplementedException();
        }
    }
}