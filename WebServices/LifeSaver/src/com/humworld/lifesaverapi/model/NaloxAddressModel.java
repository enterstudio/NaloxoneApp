/*
 * Copyright (c) 2016 Humworld INC,
 */

package com.humworld.lifesaverapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nalox_address")
public class NaloxAddressModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Addr_ID")
	private int addressId;
	
	@Column(name = "Addr_Address_Line1")
	private String addressLine1;
	
	@Column(name = "Addr_Address_Line2")
	private String addressLine2;
	
	@Column(name = "Addr_City_Name")
	private String addressCity;
	
	@Column(name = "Addr_State_Name")
	private String addressState;
	
	@Column(name = "Addr_Country_Name")
	private String addressCountry;
	
	@Column(name = "Addr_Zip_Code")
	private int addressZipCode;

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public int getAddressZipCode() {
		return addressZipCode;
	}

	public void setAddressZipCode(int addressZipCode) {
		this.addressZipCode = addressZipCode;
	}

}
