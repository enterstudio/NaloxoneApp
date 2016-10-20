/*
 * Copyright (c) 2016 Humworld INC,
 */

package com.humworld.lifesaverapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "nalox_carrier")
public class NaloxCarrierModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Ncar_Id")
	private int naloxCarrId;
	
	@Column(name = "Ncar_Name")
	private String naloxCarrName;
	
	@Column(name = "Ncar_Email_Id")
	private String naloxCarrEmail;
	
	@Column(name = "Ncar_Prim_Phone_No")
	private String naloxCarrPrimaryPhone;
	
	@Column(name = "Ncar_Secon_Phone_No")
	private String naloxCarrSecondaryPhone;
	
	@OneToOne
	@JoinColumn(name = "Ncar_Addr_Id")
	private NaloxAddressModel naloxCarrAddress;
	
	@Column(name = "Ncar_Latitude")
	private float naloxCarrLatitude;
	
	@Column(name = "Ncar_Longitude")
	private float naloxCarrLongitude;

	public int getNaloxCarrId() {
		return naloxCarrId;
	}

	public void setNaloxCarrId(int naloxCarrId) {
		this.naloxCarrId = naloxCarrId;
	}

	public String getNaloxCarrName() {
		return naloxCarrName;
	}

	public void setNaloxCarrName(String naloxCarrName) {
		this.naloxCarrName = naloxCarrName;
	}

	public String getNaloxCarrEmail() {
		return naloxCarrEmail;
	}

	public void setNaloxCarrEmail(String naloxCarrEmail) {
		this.naloxCarrEmail = naloxCarrEmail;
	}

	public String getNaloxCarrPrimaryPhone() {
		return naloxCarrPrimaryPhone;
	}

	public void setNaloxCarrPrimaryPhone(String naloxCarrPrimaryPhone) {
		this.naloxCarrPrimaryPhone = naloxCarrPrimaryPhone;
	}

	public String getNaloxCarrSecondaryPhone() {
		return naloxCarrSecondaryPhone;
	}

	public void setNaloxCarrSecondaryPhone(String naloxCarrSecondaryPhone) {
		this.naloxCarrSecondaryPhone = naloxCarrSecondaryPhone;
	}

	public NaloxAddressModel getNaloxCarrAddress() {
		return naloxCarrAddress;
	}

	public void setNaloxCarrAddress(NaloxAddressModel naloxCarrAddress) {
		this.naloxCarrAddress = naloxCarrAddress;
	}

	public float getNaloxCarrLatitude() {
		return naloxCarrLatitude;
	}

	public void setNaloxCarrLatitude(float naloxCarrLatitude) {
		this.naloxCarrLatitude = naloxCarrLatitude;
	}

	public float getNaloxCarrLongitude() {
		return naloxCarrLongitude;
	}

	public void setNaloxCarrLongitude(float naloxCarrLongitude) {
		this.naloxCarrLongitude = naloxCarrLongitude;
	}

}
