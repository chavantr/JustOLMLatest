package com.mywings.justolm.Model;

import java.io.Serializable;

/**
 * Created by Tatyabhau Chavan on 5/19/2016.
 */
public class Registration implements Serializable {

    //region Variables
    private static Registration ourInstance;
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String profession;
    private String address;
    private String country;
    private String state;
    private String city;
    private String area;

    private String countryName;
    private String stateName;
    private String cityName;
    private String areaName;
    private String pinCode;
    private String email;
    private String mobileNumber;
    private String securityCode;
    private String password;
    //endregion


    public static Registration getInstance() {
        if (null == ourInstance) {
            ourInstance = new Registration();
        }
        return ourInstance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEmptyField() {
        return getFirstName().isEmpty()
                || getMiddleName().isEmpty()
                || getLastName().isEmpty()
                || getDateOfBirth().isEmpty()
                || getGender().equalsIgnoreCase("I am...")
                || getProfession().isEmpty()
                || getAddress().isEmpty()
                || getCountry().equalsIgnoreCase("Select country")
                || getState().equalsIgnoreCase("Select state")
                || getCity().equalsIgnoreCase("Select city")
                || getArea().equalsIgnoreCase("Select area")
                || getPinCode().isEmpty()
                || getEmail().isEmpty()
                || getMobileNumber().isEmpty()
                || getSecurityCode().isEmpty()
                || getPassword().isEmpty();
    }


    public boolean isEmptyFieldUpdate() {
        return getFirstName().isEmpty()
                || getMiddleName().isEmpty()
                || getLastName().isEmpty()
                || getDateOfBirth().isEmpty()
                || getGender().equalsIgnoreCase("I am...")
                || getProfession().isEmpty()
                || getAddress().isEmpty()
                || getCountry().equalsIgnoreCase("Select country")
                || getState().equalsIgnoreCase("Select state")
                || getCity().equalsIgnoreCase("Select city")
                || getArea().equalsIgnoreCase("Select area")
                || getPinCode().isEmpty()
                || getEmail().isEmpty()
                || getMobileNumber().isEmpty();


    }

    public boolean isNotEmptyField() {
        return !getFirstName().isEmpty()
                && !getMiddleName().isEmpty()
                && !getLastName().isEmpty()
                && !getDateOfBirth().isEmpty()
                && !getGender().equalsIgnoreCase("I am...")
                && !getProfession().isEmpty()
                && !getAddress().isEmpty()
                && !getCountry().equalsIgnoreCase("Select country")
                && !getState().equalsIgnoreCase("Select state")
                && !getCity().equalsIgnoreCase("Select city")
                && !getArea().equalsIgnoreCase("Select area")
                && !getPinCode().isEmpty()
                && !getEmail().isEmpty()
                && !getMobileNumber().isEmpty()
                && !getSecurityCode().isEmpty()
                && !getPassword().isEmpty();
    }

    public boolean isNotEmptyFieldUpdate() {
        return !getFirstName().isEmpty()
                && !getMiddleName().isEmpty()
                && !getLastName().isEmpty()
                && !getDateOfBirth().isEmpty()
                && !getGender().equalsIgnoreCase("I am...")
                && !getProfession().isEmpty()
                && !getAddress().isEmpty()
                && !getCountry().equalsIgnoreCase("Select country")
                && !getState().equalsIgnoreCase("Select state")
                && !getCity().equalsIgnoreCase("Select city")
                && !getArea().equalsIgnoreCase("Select area")
                && !getPinCode().isEmpty()
                && !getEmail().isEmpty()
                && !getMobileNumber().isEmpty();
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
