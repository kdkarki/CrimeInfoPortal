﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using DAL;

namespace BLL
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service1.svc or Service1.svc.cs at the Solution Explorer and start debugging.
    public class Service : IService
    {
        #region User Management

        public DAL.User GetUserByUserName(string UserName)
        {
            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                return objDAO.Users.FirstOrDefault(u => u.Username == UserName);
            }
        }

        public bool AuthenticateUser(string Username, string Password)
        {
            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                if (objDAO.Users.FirstOrDefault(u => u.Username == Username && u.Password == Password) != null)
                    return true;
                return false;
            }
        }

        public bool CreateUser(DAL.User NewUser)
        {
            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                objDAO.AddToUsers(NewUser);
                try
                {
                    int saveResult = objDAO.SaveChanges();
                    if (saveResult == 0)
                        return false;
                    return true;
                }
                catch (Exception ex)
                {
                    return false;
                }
            }
        }

        public User CreateNewUser(string Username, string Password, string FirstName, string MiddleName, string LastName)
        {
            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                User newUser = new User()
                                {
                                    Username = Username,
                                    Password = Password,
                                    FirstName = FirstName,
                                    MiddleName = String.IsNullOrWhiteSpace(MiddleName) ? null : MiddleName,
                                    LastName = LastName
                                };

                objDAO.AddToUsers(newUser);
                try
                {
                    int savedRecordCount = objDAO.SaveChanges();
                    User objUser = objDAO.Users.FirstOrDefault(u => u.Username == Username);
                    if (objUser != null)
                        return objUser;
                }
                catch (Exception ex)
                {

                }
            }

            return null;
        }

        public User UpdateUserInfo(string UserId, string Username, string Password, string FirstName, string MiddleName, string LastName)
        {
            long uId = 0;
            if (String.IsNullOrWhiteSpace(UserId) || !Int64.TryParse(UserId, out uId))
                return null;
            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                User objUser = objDAO.Users.FirstOrDefault(u => u.UserId == uId);

                if (objUser != null)
                {
                    if (!string.IsNullOrWhiteSpace(Username))
                        objUser.Username = Username;
                    if (!String.IsNullOrWhiteSpace(Password))
                        objUser.Password = Password;
                    if (!String.IsNullOrWhiteSpace(FirstName))
                        objUser.FirstName = FirstName;
                    objUser.MiddleName = MiddleName;
                    if (!String.IsNullOrWhiteSpace(LastName))
                        objUser.LastName = LastName;

                    try
                    {
                        int updatedRecordCount = objDAO.SaveChanges();
                        if (updatedRecordCount == 1)
                            return objUser;
                        else
                            return null;
                    }
                    catch (Exception ex)
                    {
                        return null;
                    }
                }
                else
                    return null;
            }
        }


        #region User Preference

        public UserEventTypePreference[] GetUserPreference(string UserId)
        {
            int userId = 0;
            if (String.IsNullOrWhiteSpace(UserId) || !Int32.TryParse(UserId, out userId))
                return new UserEventTypePreference[0];
            using(CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var userPrefList = objDAO.UserEventTypePreferences.Where(up => up.UserId == userId);
                if (userPrefList != null && userPrefList.Count() > 0)
                    return userPrefList.ToArray();
                else
                    return new UserEventTypePreference[0];
            }
        }

        public UserEventTypePreference AddUserPreference(string UserId, string Preference)
        {
            int userId = 0;
            if (String.IsNullOrWhiteSpace(UserId) || String.IsNullOrWhiteSpace(Preference)
                || !Int32.TryParse(UserId, out userId))
                return null;
            UserEventTypePreference uetp = new UserEventTypePreference() { UserId = userId, CriminalEventType = Preference };
            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                try
                {
                    objDAO.AddToUserEventTypePreferences(uetp);
                    int addedrecords = objDAO.SaveChanges();
                    if (addedrecords == 0)
                        uetp = null;
                }
                catch (Exception ex) { uetp = null; }
            }

            return uetp;
        }

        public bool DeleteUserPreference(string UserId, string Preference)
        {
            int userId = 0;
            if (String.IsNullOrWhiteSpace(UserId) || String.IsNullOrWhiteSpace(Preference)
                || !Int32.TryParse(UserId, out userId))
                return false;
            
            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                try
                {
                    UserEventTypePreference uetp = objDAO.UserEventTypePreferences.FirstOrDefault(u => u.UserId == userId 
                                                                                                    && u.CriminalEventType == Preference);

                    if (uetp != null)
                    {
                        objDAO.DeleteObject(uetp);
                        int deletedRecords = objDAO.SaveChanges();
                        if (deletedRecords == 0)
                            return false;
                        return true;
                    }
                }
                catch (Exception ex) { }
            }

            return false;
        }

        public CriminalEventType[] GetCriminalEventTypeList()
        {
            using(CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                return objDAO.CriminalEventTypes.ToArray();
            }
        }

        #endregion
        #endregion

        #region Address

        public Address CreateNewUserAddressByUsername(string Username, string Address1, string Address2, string cityId, string Zipcode, string IsPreferred)
        {
            if (String.IsNullOrWhiteSpace(Username))
                return null;
            if (String.IsNullOrWhiteSpace(Address1) && String.IsNullOrWhiteSpace(cityId))
                return null;
            int cId = 0;

            if (!Int32.TryParse(cityId, out cId))
                return null;

            Address objAddress = new Address()
                                 {
                                     Address1 = Address1,
                                     Address2 = Address2,
                                     CityId = cId,
                                     Zipcode = Zipcode
                                 };

            using(CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                User objUser = objDAO.Users.FirstOrDefault(u => u.Username == Username);
                if(objUser != null)
                {
                    UserAddress objUserAddress = new UserAddress() { Address = objAddress, 
                                                                     IsPreferred = (!String.IsNullOrEmpty(IsPreferred) && IsPreferred == "true")?true:false };
                    objUser.UserAddresses.Add(objUserAddress);
                    int updatedRecordCount = objDAO.SaveChanges();

                    return objAddress;
                }
            }

            return null;
        }

        public Address CreateNewUserAddressByUserId(string UserId, string Address1, string Address2, string cityId, string Zipcode, string IsPreferred)
        {
            if (String.IsNullOrWhiteSpace(UserId))
                return null;
            if (String.IsNullOrWhiteSpace(Address1) && String.IsNullOrWhiteSpace(cityId))
                return null;
            int cId = 0;
            long uId = 0;

            if (!Int64.TryParse(UserId, out uId))
                return null;

            if (!Int32.TryParse(cityId, out cId))
                return null;

            Address objAddress = new Address()
            {
                Address1 = Address1,
                Address2 = Address2,
                CityId = cId,
                Zipcode = Zipcode
            };

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                User objUser = objDAO.Users.FirstOrDefault(u => u.UserId == uId);
                if (objUser != null)
                {
                    UserAddress objUserAddress = new UserAddress()
                                                {
                                                    Address = objAddress,
                                                    IsPreferred = (!String.IsNullOrEmpty(IsPreferred) && IsPreferred == "true") ? true : false
                                                };
                    objUser.UserAddresses.Add(objUserAddress);
                    int updatedRecordCount = objDAO.SaveChanges();

                    return objAddress;
                }
            }

            return null;
        }

        public bool DeleteUserAddress(string UserId, string AddressId)
        {
            if (String.IsNullOrWhiteSpace(UserId) && String.IsNullOrWhiteSpace(AddressId))
                return false;
            int aId = 0;
            long uId = 0;

            if (!Int64.TryParse(UserId, out uId))
                return false;

            if (!Int32.TryParse(AddressId, out aId))
                return false;

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                Address objAddress = objDAO.Addresses.FirstOrDefault(a => a.Id == aId);
                if(objAddress != null)
                {
                    objDAO.DeleteObject(objAddress);
                    int deletedRecordCount = objDAO.SaveChanges();
                    return true;
                } 
            }

            return false;
        }

        public UserAddressDetailView[] GetUserAddressByUserName(string Username)
        {
            if (String.IsNullOrWhiteSpace(Username))
                return new UserAddressDetailView[0];

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var userAddressDetailsView = objDAO.UserAddressDetailViews.Where(ua => ua.Username == Username);

                if (userAddressDetailsView != null && userAddressDetailsView.Count() > 0)
                    return userAddressDetailsView.ToArray();
                return new UserAddressDetailView[0];
            }
        }

        public UserAddressDetailView[] GetUserAddressByUserId(string UserId)
        {
            long uId = 0;
            if (String.IsNullOrWhiteSpace(UserId) || !Int64.TryParse(UserId, out uId))
                return new UserAddressDetailView[0];

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var userAddressDetailsView = objDAO.UserAddressDetailViews.Where(ua => ua.UserId == uId);

                if (userAddressDetailsView != null && userAddressDetailsView.Count() > 0)
                    return userAddressDetailsView.ToArray();
                else
                    return new UserAddressDetailView[0];
            }
        }

        public State[] GetStatesByCountryCode(string CountryCode)
        {
            if (String.IsNullOrWhiteSpace(CountryCode))
                return new State[0];

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var stateList = objDAO.States.Where(s => s.Country.CountryCode == CountryCode)
                                             .OrderBy(s => s.StateName);

                if (stateList != null && stateList.Count() > 0)
                    return stateList.ToArray();
                else
                    return new State[0];
            }
        }

        public CityDetailView[] GetCitiesByCountryCodeStateCode(string CountryCode, string StateCode)
        {
            if (String.IsNullOrWhiteSpace(CountryCode) || String.IsNullOrWhiteSpace(StateCode))
                return new CityDetailView[0];

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var cityDetailsList = objDAO.CityDetailViews.Where(c => c.CountryCode == CountryCode
                                                                     && c.StateCode == StateCode)
                                                            .OrderBy(c => c.Name);

                if (cityDetailsList != null && cityDetailsList.Count() > 0)
                    return cityDetailsList.ToArray();
                else
                    return new CityDetailView[0];
            }
        }

        public CityDetailView[] GetCitiesByStateId(string StateId)
        {
            int stId = 0;
            if (String.IsNullOrWhiteSpace(StateId) || !Int32.TryParse(StateId, out stId))
                return new CityDetailView[0];
            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var cityDetailsList = objDAO.CityDetailViews.Where(c => c.StateID == stId)
                                                            .OrderBy(c => c.Name);

                if (cityDetailsList != null && cityDetailsList.Count() > 0)
                    return cityDetailsList.ToArray();
                else
                    return new CityDetailView[0];
            }
        }

        #endregion

        #region Criminal Records

        public bool UpdateStateChargeCodeDomain(string StateCode)
        {
            StateCodeDomain.UpdateStateChargeCodeDomain(StateCode);
            return true;
        }

        public CriminalActivityRecordDetailView[] GetCriminalRecordsByCityId(string CityId)
        {
            int cId = 0;
            if (String.IsNullOrWhiteSpace(CityId) || !Int32.TryParse(CityId, out cId))
                return new CriminalActivityRecordDetailView[0];

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var test = objDAO.CriminalActivityRecordDetailViews;
                var cardViewList = objDAO.CriminalActivityRecordDetailViews.Where(c => c.CityId == cId);

                if (cardViewList != null && cardViewList.Count() > 0)
                    return cardViewList.OrderByDescending(c=>c.DateArrested).ToArray();
            }

            return new CriminalActivityRecordDetailView[0];
        }

        public CriminalActivityRecordDetailView[] GetCriminalRecordsByStateIdCityName(string StateId, string CityName)
        {
            int sId = 0;
            if (String.IsNullOrWhiteSpace(StateId) || String.IsNullOrWhiteSpace(CityName)
                || !Int32.TryParse(StateId, out sId))
                return new CriminalActivityRecordDetailView[0];

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var cardViewList = objDAO.CriminalActivityRecordDetailViews.Where(c => c.StateId == sId
                                                                                    && c.CityName == CityName);
                if (cardViewList != null && cardViewList.Count() > 0)
                    return cardViewList.OrderByDescending(c=>c.DateArrested).ToArray();
            }

            return new CriminalActivityRecordDetailView[0];
        }

        public bool InitiateDataCrawling()
        {
            Crawlers.DataCrawler.InitiateCrawling();
            return true;
        }

        #endregion
    }
}
