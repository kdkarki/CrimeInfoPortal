using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BLL.Crawlers
{
    public abstract class CIPCrawler
    {
        private DAL.CriminalActivityRecord _lastUpdatedRecord { get; set; }

        private string _sourceUrl { get; set; }

        private List<DAL.CriminalActivityRecord> _newCriminalRecordList = new List<DAL.CriminalActivityRecord>();

        public CIPCrawler(DAL.CriminalActivityRecord LastUpdatedRecord, string SourceUrl)
        {
            _lastUpdatedRecord = LastUpdatedRecord;
            _sourceUrl = SourceUrl;
        }

        public abstract List<DAL.CriminalActivityRecord> StartCrawling();
    }
}