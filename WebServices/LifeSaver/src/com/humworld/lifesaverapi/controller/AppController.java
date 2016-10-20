/*
 * Copyright (c) 2016 Humworld INC,
 */

package com.humworld.lifesaverapi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.humworld.lifesaverapi.model.CustomNaloxCarrierDetails;
import com.humworld.lifesaverapi.model.NaloxCarrierModel;
import com.humworld.lifesaverapi.service.CarrierDetailsService;
import com.humworld.lifesaverapi.util.WebServiceUtils;

/**
 * A REST controller class.
 * 
 * @author Humworld
 * @version 1.0
 */
@RestController
public class AppController {
	
	private static final String CODE = "effe43c93264fc4e82715ebbbbe13cfabe9ba5d8080188bd1a898ade8c5611b4";
	
	@Autowired
	private CarrierDetailsService carrierDetailsService;
	
	/**
	 * Handles all the request for URL /notification.
	 * 
	 * @param request Contains the request object.
	 * 
	 * @return {@link ResponseEntity} Returns http status based on condition.
	 */
	@RequestMapping(value = "/notification", method = RequestMethod.GET)
	public ResponseEntity<CustomNaloxCarrierDetails> testUrl(HttpServletRequest request) {
		
		String code = request.getParameter("verify");
		
		if (code != null && code.equals(CODE)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Handles all the request for URL /CarrierDetails.
	 * 
	 * @param latitude Latitude sent from mobile app.
	 * @param longitude Longitude sent from mobile app.
	 * 
	 * @return {@link ResponseEntity} Returns nearest carrier details.
	 */
	@RequestMapping(value = "/CarrierDetails", method = RequestMethod.POST)
	public ResponseEntity<?> getCarrierDetails(@RequestParam(value = "latitude") float latitude,
											@RequestParam(value = "longitude") float longitude) {
		
		List<Object[]> list = carrierDetailsService.getLatiLongi();
		
		List<Double> distancesToBeSorted = new ArrayList<Double>();
		List<Double> latitudeList = new ArrayList<Double>();
		List<Double> longitudeList = new ArrayList<Double>();
		
		for (Object[] objects : list) {
			double latitudeTaken = (double) objects[0];
			double longitudeTaken = (double) objects[1];
			latitudeList.add(latitudeTaken);
			longitudeList.add(longitudeTaken);
			double distance = WebServiceUtils.distanceTo(latitude, longitude, latitudeTaken,
																				longitudeTaken);
			distancesToBeSorted.add(distance);
		}
		
		List<Double> distancesUnSorted = new ArrayList<Double>();
		distancesUnSorted.addAll(distancesToBeSorted);
		
		Collections.sort(distancesToBeSorted);
		
		double nearDistance1 = distancesToBeSorted.get(0);
		double nearDistance2 = distancesToBeSorted.get(1);
		
		int nearIndex1 = distancesUnSorted.indexOf(nearDistance1);
		int nearIndex2 = distancesUnSorted.indexOf(nearDistance2);
		
		double nearLatitude1 = latitudeList.get(nearIndex1);		
		double nearLongitude1 = longitudeList.get(nearIndex1);
		
		double nearLatitude2 = latitudeList.get(nearIndex2);		
		double nearLongitude2 = longitudeList.get(nearIndex2);
		
		List<Object[]> details1 = carrierDetailsService.getDetailsUsingLatiLongi(nearLatitude1,
																				nearLongitude1);
		
		List<Object[]> details2 = carrierDetailsService.getDetailsUsingLatiLongi(nearLatitude2,
																				nearLongitude2);
		
		CustomNaloxCarrierDetails naloxCarrierDetails = new CustomNaloxCarrierDetails();

		for (Object[] details : details1) {
			NaloxCarrierModel naloxCarrierModel = (NaloxCarrierModel) details[0];
			naloxCarrierDetails.setNaloxCarrierModel1(naloxCarrierModel);
		}
		
		for (Object[] details : details2) {
			NaloxCarrierModel naloxCarrierModel = (NaloxCarrierModel) details[0];
			naloxCarrierDetails.setNaloxCarrierModel2(naloxCarrierModel);
		}
		
		return new ResponseEntity<CustomNaloxCarrierDetails>(naloxCarrierDetails, HttpStatus.OK);
		
	}
}
