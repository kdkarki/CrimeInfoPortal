using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BLL.Crawlers
{
    public abstract class CIPCrawler
    {
        protected DAL.CriminalActivityRecord _lastUpdatedRecord { get; set; }

        protected int _stateId { get; set; }

        protected string _sourceUrl { get; set; }

        private List<DAL.CriminalActivityRecord> _newCriminalRecordList = new List<DAL.CriminalActivityRecord>();

        public CIPCrawler(DAL.CriminalActivityRecord LastUpdatedRecord, string SourceUrl, int StateId)
        {
            _lastUpdatedRecord = LastUpdatedRecord;
            _sourceUrl = SourceUrl;
            _stateId = StateId;
        }

        public abstract List<DAL.CriminalActivityRecord> StartCrawling();
    }
}