package ge.taxistgela.model;

import ge.taxistgela.bean.Company;
import ge.taxistgela.dao.CompanyDaoAPI;
import ge.taxistgela.helper.RegistrationHelper;

/**
 * Created by GIO on 5/25/2015.
 */
public class CompanyManager extends   CompanyManagerAPI {
    public  CompanyManager(CompanyDaoAPI companyDao){super(companyDao);}

    @Override
    public Company loginCompany(String email, String password) {
        return  companyDao.loginCompany(email, password);
    }

    @Override
    public boolean registerCompany(Company company) {
        boolean ret;
        if(!RegistrationHelper.isValid(company))
            ret = true;
        else
            ret = companyDao.registerCompany(company);
        return  ret;
    }

    @Override
    public boolean updateCompany(Company company) {
        return companyDao.updateCompany(company);
    }

    @Override
    public boolean checkEmail(String email) {
        return companyDao.checkEmail(email);
    }

    @Override
    public boolean checkPhoneNumber(String phoneNumber) {
        return companyDao.checkPhoneNumber(phoneNumber);
    }

    @Override
    public boolean checkCompanyCode(String companyCode) {
        return companyDao.checkCompanyCode(companyCode);
    }

    @Override
    public boolean checkFacebookID(String facebookID) {
        return companyDao.checkFacebookID(facebookID);
    }

    @Override
    public boolean checkGoogleID(String googleID) {
        return companyDao.checkGoogleID(googleID);
    }
}
