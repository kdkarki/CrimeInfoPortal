using System;
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

        #endregion

        #region Address

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

        public bool UpdateStateChargeCodeDomain(string StateCode)
        {
            StateCodeDomain.UpdateStateChargeCodeDomain(StateCode);
            return true;
        }

        public CriminalActivityRecordDetailView[] GetCriminalRecordsByCityId(string CityId)
        {
            int cId = 0;
            if (String.IsNullOrWhiteSpace(CityId) || Int32.TryParse(CityId, out cId))
                return new CriminalActivityRecordDetailView[0];

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var cardViewList = objDAO.CriminalActivityRecordDetailViews.Where(c => c.CityId == cId);

                if (cardViewList != null && cardViewList.Count() > 0)
                    return cardViewList.ToArray();
            }

            return new CriminalActivityRecordDetailView[0];
        }

        public CriminalActivityRecordDetailView[] GetCriminalRecordsByStateIdCityName(string StateId, string CityName)
        {
            int sId = 0;
            if (String.IsNullOrWhiteSpace(StateId) || String.IsNullOrWhiteSpace(CityName)
                || Int32.TryParse(StateId, out sId))
                return new CriminalActivityRecordDetailView[0];

            using (CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO())
            {
                var cardViewList = objDAO.CriminalActivityRecordDetailViews.Where(c => c.StateId == sId
                                                                                    && c.CityName == CityName);
                if (cardViewList != null && cardViewList.Count() > 0)
                    return cardViewList.ToArray();

                if (cardViewList != null && cardViewList.Count() > 0)
                    return cardViewList.ToArray();
            }

            return new CriminalActivityRecordDetailView[0];
        }
    }
}
