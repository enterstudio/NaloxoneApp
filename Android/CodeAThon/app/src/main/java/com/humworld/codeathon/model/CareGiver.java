package com.humworld.codeathon.model;

import java.io.Serializable;

/**
 * Created by ${Sys-7} on 13/10/16.
 * Company Name Humworld
 */

public class CareGiver implements Serializable {

    private String mId;
    private String mFirstName;
    private String mMiddleName;
    private String mLastName;
    private String mMailId;
    private String mRelation;
    private String mMobileNo;
    private String mAddressOne;
    private String mAddressTwo;
    private String mAddressId;
    private String mCity;
    private String mState;
    private String mCountry;
    private String mPostalCode;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getAddressId() {
        return mAddressId;
    }

    public void setAddressId(String addressId) {
        this.mAddressId = addressId;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String postalCode) {
        this.mPostalCode = postalCode;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getAddressTwo() {
        return mAddressTwo;
    }

    public void setAddressTwo(String addressTwo) {
        this.mAddressTwo = addressTwo;
    }

    public String getMail_id() {
        return mMailId;
    }

    public void setMail_id(String mail_id) {
        this.mMailId = mail_id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getMiddleName() {
        return mMiddleName;
    }

    public void setMiddleName(String middleName) {
        this.mMiddleName = middleName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getRelation() {
        return mRelation;
    }

    public void setRelation(String relation) {
        this.mRelation = relation;
    }

    public String getMobile_no() {
        return mMobileNo;
    }

    public void setMobile_no(String mobile_no) {
        this.mMobileNo = mobile_no;
    }

    public String getAddressOne() {
        return mAddressOne;
    }

    public void setAddressOne(String addressOne) {
        this.mAddressOne = addressOne;
    }

}
