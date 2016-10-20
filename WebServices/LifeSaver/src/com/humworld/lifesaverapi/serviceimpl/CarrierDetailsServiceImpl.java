/*
 * Copyright (c) 2016 Humworld INC,
 */

package com.humworld.lifesaverapi.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humworld.lifesaverapi.dao.CarrierDetailsDao;
import com.humworld.lifesaverapi.service.CarrierDetailsService;

/**
 * Service class, containing methods related to fetching nearest 
 * carrier details, which provides transaction support.
 * 
 * @author Humworld
 * @version 1.0
 */
@Service("carrierDetailsService")
public class CarrierDetailsServiceImpl implements CarrierDetailsService {
	
	@Autowired
	private CarrierDetailsDao carrierDetailsDao;
	
	@Transactional
	@Override
	public List<Object[]> getLatiLongi() {	
		List<Object[]> list = carrierDetailsDao.getLatiLongi();	
		return list;
	}
	
	@Transactional
	@Override
	public List<Object[]> getDetailsUsingLatiLongi(double latitude, double longitude) {
		return carrierDetailsDao.getDetailsUsingLatiLongi(latitude, longitude);
	}

}
