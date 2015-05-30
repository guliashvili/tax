package ge.taxistgela.dao;

import ge.taxistgela.bean.*;
import ge.taxistgela.db.DBConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 5/25/2015.
 */
public class DriverDao implements DriverDaoAPI, OperationCodes {
    private final static  String base_select_STMT = " SELECT * FROM Drivers INNER JOIN Cars ON Drivers.CarID=Cars.CarID INNER JOIN DriverPreferences ON " +
            "DriverPreferences.driverPreferenceID=Drivers.driverPreferenceID ";
    private final static String login_STMT = base_select_STMT + " WHERE email=? AND password=?";
    private final static String driverById_STMT = base_select_STMT + "WHERE driverID = ?";

    private final static String register_STMT = "INSERT INTO Drivers (personalID,password,email,companyID,firstName,lastName,gender,phoneNumber,carID,facebookID,googleID,rating,DriverPreferenceID,latitude,longitude,isActive)" +
            " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private final static String update_STMT = "UPDATE Drivers " +
            "SET personalID=?,password=?,email=?,companyID=?,firstName=?,lastName=?,gender=?,phoneNumber=?,carID=?,facebookID=?,googleID=?,rating=?,driverPreferenceID=?,latitude=?,longitude=?,isActive=?" +
            "WHERE driverID = ?";

    private final static  String preferences_STMT = base_select_STMT +
            " WHERE " +
            "Drivers.rating >= ? AND " +
            "(NOT ? OR Cars.conditioning) AND " +
            "Cars.carYear >= ? AND " +
            "Cars.numPassengers >= ? AND " +
            "? >= DriverPreferences.minimumUserRating";



    private Driver getDriver(ResultSet res){
        Car car = new Car();
        Driver output=new Driver();

        try {
                output.setDriverID(res.getInt("Drivers.driverID"));
                output.setPersonalID(res.getString("Drivers.personalID"));
                output.setPassword(res.getString("Drivers.password"));
                output.setEmail(res.getString("Drivers.email"));
                output.setCompanyID(res.getInt("Drivers.companyID"));
                output.setFirstName(res.getString("Drivers.firstName"));
                output.setLastName(res.getString("Drivers.lastName"));
                output.setGender(Gender.valueOf(res.getString("Drivers.gender")));
                output.setPhoneNumber(res.getString("Drivers.phoneNumber"));



                car.setCarID(res.getString("Cars.CarID"));
                car.setCarDescription(res.getString("Cars.carDescription"));
                car.setCarYear(res.getInt("Cars.carYear"));
                car.setConditioning(res.getBoolean("Cars.conditioning"));
                car.setNumPassengers(res.getInt("Cars.numPassengers"));

                output.setCar(car);

                output.setFacebookID(res.getString("Drivers.facebookID"));
                output.setGoogleID(res.getString("Drivers.googleID"));
                output.setRating(res.getDouble("Drivers.rating"));

                DriverPreference pref = new DriverPreference();
                pref.setDriverPreferenceID(res.getInt("DriverPreferences.driverPreferenceID"));
                pref.setCoefficientPer(res.getDouble("DriverPreferences.coefficientPer"));
                pref.setMinimumUserRating(res.getDouble("DriverPreferences.minimumUserRating"));

                output.setPreferences(pref);

                output.setLocation(new Location(res.getBigDecimal("Drivers.latitude"), res.getBigDecimal("Drivers.longitude")));
                output.setIsActive(res.getBoolean("Drivers.isActive"));

        }catch (SQLException e){
            output = null;
            //TODO log
        }
        return  output;
    }

    @Override
    public Driver getDriveByID(int driverID) {
        Driver output;
        try(Connection con = DBConnectionProvider.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(driverById_STMT)) {

                st.setInt(1,driverID);

                System.out.println(st.toString());
                ResultSet res = st.executeQuery();
                if(res.next()) output = getDriver(res);
                else output = null;
            }
        }catch (SQLException e){
            output = null;
        }
        return output;
    }

    @Override
    public List<Driver> getDriverByPreferences(User user) {
        List<Driver> output = new ArrayList<>();
        try(Connection con = DBConnectionProvider.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(preferences_STMT)) {

                st.setDouble(1, user.getPreference().getMinimumDriverRating());
                st.setBoolean(2,user.getPreference().isConditioning());
                st.setInt(3,user.getPreference().getCarYear());
                st.setInt(4,user.getPreference().getPassengersCount());
                st.setDouble(5, user.getRating());

                System.out.println(st.toString());
                ResultSet res = st.executeQuery();
                while (res.next())
                    output.add(getDriver(res));
            }
        }catch (SQLException e){
            output = null;
        }
        return output;
    }

    @Override
    public Driver loginDriver(String email, String password) {
        Driver output;
        try(Connection con = DBConnectionProvider.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(login_STMT)) {

                st.setString(1,email);
                st.setString(2,password);

                System.out.println(st.toString());
                ResultSet res = st.executeQuery();

                if(res.next()) output = getDriver(res);
                else output = null;
            }
        }catch (SQLException e){
            output = null;
        }
        return output;
    }

    /*(personalID,password,email,companyID,firstName,lastName,gender,phoneNumber,carID,facebookID,googleID,rating,driverPreferenceID,latitude,longitude,isActive)
        sets strings with that order
    */
    private int setStrings(PreparedStatement st,Driver driver){
        int errorCode = 0;
        try {
            st.setString(1, driver.getPersonalID());
            st.setString(2, driver.getPassword());
            st.setString(3, driver.getEmail());
            st.setInt(4, driver.getCompanyID());
            st.setString(5, driver.getFirstName());
            st.setString(6, driver.getLastName());
            st.setString(7, driver.getGender().toString());
            st.setString(8, driver.getPhoneNumber());
            st.setString(9, driver.getCar().getCarID());
            st.setString(10, driver.getFacebookID());
            st.setString(11, driver.getGoogleID());
            st.setDouble(12, driver.getRating());
            st.setInt(13, driver.getPreferences().getDriverPreferenceID());
            st.setBigDecimal(14, driver.getLocation().getLatitude());
            st.setBigDecimal(15, driver.getLocation().getLongitute());
            st.setBoolean(16, driver.isActive());
        }catch (SQLException e){
            errorCode = -1;// TODO
        }
        return  errorCode;
    }

    @Override
    public int registerDriver(Driver driver) {
        int errorCode = 0;
        try(Connection con = DBConnectionProvider.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(register_STMT,PreparedStatement.RETURN_GENERATED_KEYS)) {
                System.out.println(st.toString());

                errorCode |= setStrings(st,driver);

                st.executeUpdate();
                ResultSet res = st.getGeneratedKeys();
                if(res.next()){
                    driver.setDriverID(res.getInt("driverID"));
                }else{
                    errorCode = -1;
                    //TODO ERRORCODE
                }

            }
        }catch (SQLException e){
            errorCode = -1;
            //TODO ERRORCODE

        }
        return errorCode;

    }

    @Override
    public int updateDriver(Driver driver) {
        int errorCode = 0;
        try(Connection con = DBConnectionProvider.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(update_STMT)) {
                errorCode |= setStrings(st,driver);
                st.setInt(17,driver.getDriverID());

                System.out.println(st.toString());
                st.executeUpdate();
            }
        }catch (SQLException e){
            errorCode = -1;
            //TODO error code

        }
        return errorCode;
    }

    @Override
    public boolean checkCarID(String carID) {
        try(Connection con = DBConnectionProvider.getConnection()){
            try(PreparedStatement st = con.prepareStatement("SELECT driverID FROM drivers WHERE  carID = ?")) {
                st.setString(1,carID);
                ResultSet res = st.executeQuery();
                return res.next();
            }
        }catch(SQLException e){

        }
        return false;
    }

    @Override
    public boolean checkEmail(String email) {
        try(Connection con = DBConnectionProvider.getConnection()){
            try(PreparedStatement st = con.prepareStatement("SELECT driverID FROM drivers WHERE  email = ?")) {
                st.setString(1,email);
                ResultSet res = st.executeQuery();
                return res.next();
            }
        }catch(SQLException e){

        }
        return false;
    }

    @Override
    public boolean checkPhoneNumber(String phoneNumber) {
        try(Connection con = DBConnectionProvider.getConnection()){
            try(PreparedStatement st = con.prepareStatement("SELECT driverID FROM drivers WHERE  phoneNumber = ?")) {
                st.setString(1,phoneNumber);
                ResultSet res = st.executeQuery();
                return res.next();
            }
        }catch(SQLException e){

        }
        return false;
    }

    @Override
    public boolean checkFacebookID(String facebookID) {
        if(facebookID != null)
        try(Connection con = DBConnectionProvider.getConnection()){
            try(PreparedStatement st = con.prepareStatement("SELECT driverID FROM drivers WHERE  facebookID = ?")) {
                st.setString(1,facebookID);
                ResultSet res = st.executeQuery();
                return res.next();
            }
        }catch(SQLException e){

        }
        return false;
    }

    @Override
    public boolean checkGoogleID(String googleID) {
        if(googleID != null)
        try(Connection con = DBConnectionProvider.getConnection()){
            try(PreparedStatement st = con.prepareStatement("SELECT driverID FROM drivers WHERE  googleID = ?")) {
                st.setString(1,googleID);
                ResultSet res = st.executeQuery();
                return res.next();
            }
        }catch(SQLException e){

        }
        return false;
    }
}
