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
        public string GetData(string value)
        {
            return string.Format("You entered: {0}", value);
        }

        public CompositeType GetDataUsingDataContract(CompositeType composite)
        {
            if (composite == null)
            {
                throw new ArgumentNullException("composite");
            }
            if (composite.BoolValue)
            {
                composite.StringValue += "Suffix";
            }
            return composite;
        }

        public DAL.User GetUserByUserName(string UserName)
        {
            CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO();
            return objDAO.Users.FirstOrDefault(u => u.Username == UserName);
        }

        public bool AuthenticateUser(string Username, string Password)
        {
            CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO();
            if (objDAO.Users.FirstOrDefault(u => u.Username == Username && u.Password == Password) != null)
                return true;
            return false;
        }

        public bool CreateNewUser(DAL.User NewUser)
        {
            CrimeInfoPortalDAO objDAO = new CrimeInfoPortalDAO();
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
}
