using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Threading;
using DAL;

namespace BLL.Crawlers
{
    public static class DataCrawler
    {
        static Object lockObject = new object();
        static Dictionary<Thread, DAL.CriminalRecordSource> crawlingThreads = new Dictionary<Thread, DAL.CriminalRecordSource>();
        static List<DAL.CriminalActivityRecord> recordList = new List<DAL.CriminalActivityRecord>();

        public static void InitiateCrawling()
        {
            List<DAL.CriminalRecordSource> dataSourceList = DataCrawler.GetCriminalRecordSource();

            if (dataSourceList.Count > 0)
            {
                foreach(DAL.CriminalRecordSource crs in dataSourceList)
                {
                    if (crs != null)
                    {
                        try
                        {
                            if(crawlingThreads.FirstOrDefault(th => th.Key.Name == crs.SourceId.ToString()).Key == null)
                            {
                                Type classType = Type.GetType("BLL.Crawlers." + crs.ClassFileName);

                                var crawlerClass = Activator.CreateInstance(classType, crs.CriminalActivityRecord, crs.BaseUrl, crs.State.StateID) as Crawlers.CIPCrawler;

                                System.Threading.Thread t = new Thread(() => { lock (lockObject) { recordList.AddRange(crawlerClass.StartCrawling()); } });
                                t.Name = crs.SourceId.ToString();

                                lock (lockObject)
                                {
                                    crawlingThreads.Add(t, crs);
                                }
                            }
                        }
                        catch (Exception ex)
                        {

                        }
                    }
                }
            }

            try
            {
                //Thread crawlThread = new Thread(new ThreadStart(StartCrawlingThreads));
                DataCrawler.StartCrawlingThreads();
            }
            catch(Exception ex)
            {

            }

            try
            {
                System.Threading.Thread sthread = new Thread(new ThreadStart(StoreCriminalRecordsToDatabase));
                sthread.Start();
            }
            catch(Exception ex)
            { }
        }

        private static List<DAL.CriminalRecordSource> GetCriminalRecordSource()
        {
            using(DAL.CrimeInfoPortalDAO objCrimeInfoPortalDAO = new DAL.CrimeInfoPortalDAO())
            {
                var recordSourceList = objCrimeInfoPortalDAO.CriminalRecordSources.ToList();
                recordSourceList.ForEach(rs => rs.StateReference.Load());

                return recordSourceList;
            }
        }

        private static void StartCrawlingThreads()
        {
            foreach(KeyValuePair<Thread, DAL.CriminalRecordSource> kvPair in crawlingThreads)
            {
                try
                {
                    kvPair.Key.Start();
                }
                catch(Exception ex)
                {

                }
            }
        }

        private static void RetrieveNewDataSourceFromDatabase()
        {
            while (true)
            {
                List<DAL.CriminalRecordSource> dataSourceList = DataCrawler.GetCriminalRecordSource();

                if (dataSourceList != null && dataSourceList.Count > 0)
                {
                    foreach (DAL.CriminalRecordSource crs in dataSourceList)
                    {
                        if (crs != null)
                        {
                            try
                            {
                                if (crawlingThreads.FirstOrDefault(th => th.Key.Name == crs.SourceId.ToString()).Key == null)
                                {
                                    Type classType = Type.GetType("BLL.Crawlers." + crs.ClassFileName);

                                    var crawlerClass = Activator.CreateInstance(classType, crs.CriminalActivityRecord, crs.BaseUrl) as Crawlers.CIPCrawler;
                                    var recordList = new List<DAL.CriminalActivityRecord>();
                                    System.Threading.Thread t = new Thread(() => { recordList = crawlerClass.StartCrawling(); });
                                    t.Name = crs.SourceId.ToString();

                                    lock (crawlingThreads)
                                    {
                                        crawlingThreads.Add(t, crs);
                                    }
                                }
                            }
                            catch (Exception ex)
                            {

                            }
                        }
                    }
                }

                TimeSpan ts = new TimeSpan(24,0,0);
                Thread.Sleep(ts);
            }
        }

        private static void StoreCriminalRecordsToDatabase()
        {
            try
            {
                while(true)
                {
                    List<DAL.CriminalActivityRecord> tempRecordList = new List<CriminalActivityRecord>();
                    lock(lockObject)
                    {
                        tempRecordList = new List<CriminalActivityRecord>(recordList);
                        recordList = new List<CriminalActivityRecord>();
                    }

                    foreach (DAL.CriminalActivityRecord car in tempRecordList)
                    {
                        try
                        {
                            using (DAL.CrimeInfoPortalDAO dao = new CrimeInfoPortalDAO())
                            {
                                dao.AddToCriminalActivityRecords(car);
                                dao.SaveChanges();
                            }
                        }
                        catch (Exception ex)
                        {

                        }
                    }
                    //break;
                    TimeSpan ts = new TimeSpan(0, 1, 0);
                    Thread.Sleep(ts);
                }
            }
            catch(Exception ex)
            {

            }
        }
    }
}