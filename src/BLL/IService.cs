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
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract]
    public interface IService
    {
        #region User Management

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    UriTemplate = "GetUserByUserName?uname={username}")]
        User GetUserByUserName(string UserName);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "AuthenticateUser?uname={username}&pwd={password}")]
        bool AuthenticateUser(string Username, string Password);

        [OperationContract]
        [WebInvoke(Method = "POST",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "CreateUser")]
        bool CreateUser(DAL.User NewUser);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "CreateNewUser?uname={username}&pwd={password}&fname={firstname}&mname={middlename}&lname={lastname}")]
        User CreateNewUser(string Username, string Password, string FirstName, string MiddleName, string LastName);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "UpdateUserInfo?uid={userid}&uname={username}&pwd={password}&fname={firstname}&mname={middlename}&lname={lastname}")]
        User UpdateUserInfo(string UserId,string Username, string Password, string FirstName, string MiddleName, string LastName);

        #region User Preference

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    UriTemplate = "GetCriminalEventTypeList")]
        CriminalEventType[] GetCriminalEventTypeList();

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    UriTemplate = "GetUserPreference?uid={userid}")]
        UserEventTypePreference[] GetUserPreference(string UserId);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    UriTemplate = "AddUserPreference?uid={userid}&pref={preference}")]
        UserEventTypePreference AddUserPreference(string UserId, string Preference);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    UriTemplate = "DeleteUserPreference?uid={userid}&pref={preference}")]
        bool DeleteUserPreference(string UserId, string Preference);

        #endregion

        #region Address

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "CreateNewUserAddressByUsername?uname={username}&add1={Address1}&add2={address2}&cityid={cityid}&zipcode={zipcode}&isperf={ispreferred}")]
        Address CreateNewUserAddressByUsername(string Username, string Address1, string Address2, string cityId, string Zipcode, string IsPreferred);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "CreateNewUserAddressByUserId?uid={userid}&add1={Address1}&add2={address2}&cityid={cityid}&zipcode={zipcode}&isperf={ispreferred}")]
        Address CreateNewUserAddressByUserId(string UserId, string Address1, string Address2, string cityId, string Zipcode, string IsPreferred);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "DeleteUserAddress?uid={userid}&addId={addressid}")]
        bool DeleteUserAddress(string UserId, string AddressId);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetUserAddressByUserName?uname={username}")]
        UserAddressDetailView[] GetUserAddressByUserName(string Username);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetUserAddressByUserId?uid={userid}")]
        UserAddressDetailView[] GetUserAddressByUserId(string UserId);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetStatesByCountryCode?countrycode={countrycode}")]
        State[] GetStatesByCountryCode(string CountryCode);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetCitiesByCountryCodeStateCode?countrycode={countrycode}&statecode={statecode}")]
        CityDetailView[] GetCitiesByCountryCodeStateCode(string CountryCode, string StateCode);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetCityByStateId?stateid={stateid}")]
        CityDetailView[] GetCitiesByStateId(string StateId);

        #endregion

        #endregion

        #region Criminal Records

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "UpdateStateChargeCodeDomain/{statecode}")]
        bool UpdateStateChargeCodeDomain(string StateCode);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetCriminalRecordsByCityId?cityid={cityid}")]
        CriminalActivityRecordDetailView[] GetCriminalRecordsByCityId(string CityId);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetCriminalRecordsByStateIdCityName?stateid={stateid}&cityname={cityname}")]
        CriminalActivityRecordDetailView[] GetCriminalRecordsByStateIdCityName(string StateId, string CityName);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "InitiateDataCrawling")]
        bool InitiateDataCrawling();

        #endregion
    }
}
