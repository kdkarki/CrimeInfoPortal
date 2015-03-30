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
                    UriTemplate = "GetUserByUserName/{username}")]
        User GetUserByUserName(string UserName);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "AuthenticateUser?username={username}&password={password}")]
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

        #region Address

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetUserAddressByUserName/{username}")]
        UserAddressDetailView[] GetUserAddressByUserName(string Username);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetUserAddressByUserId/{userid}")]
        UserAddressDetailView[] GetUserAddressByUserId(string UserId);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetStatesByCountryCode/{countrycode}")]
        State[] GetStatesByCountryCode(string CountryCode);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetCitiesByCountryCodeStateCode/{countrycode}/{statecode}")]
        CityDetailView[] GetCitiesByCountryCodeStateCode(string CountryCode, string StateCode);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetCityByStateId/{stateid}")]
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
                    UriTemplate = "GetCriminalRecordsByCityId/{cityid}")]
        CriminalActivityRecordDetailView[] GetCriminalRecordsByCityId(string CityId);

        [OperationContract]
        [WebInvoke(Method = "GET",
                    ResponseFormat = WebMessageFormat.Xml,
                    RequestFormat = WebMessageFormat.Xml,
                    BodyStyle = WebMessageBodyStyle.Wrapped,
                    UriTemplate = "GetCriminalRecordsByStateIdCityName/{stateid}/{cityname}")]
        CriminalActivityRecordDetailView[] GetCriminalRecordsByStateIdCityName(string StateId, string CityName);

        #endregion
    }
}
