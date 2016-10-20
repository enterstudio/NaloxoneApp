/*
 * Copyright (c) 2016 Humworld INC,
 */

package com.humworld.lifesaverapi.service;

import java.util.List;

/**
 * Service interface, containing abstract methods.
 * 
 * @author Humworld
 * @version 1.0
 */
public interface CarrierDetailsService {

	public List<Object[]> getLatiLongi();
	
	public List<Object[]> getDetailsUsingLatiLongi(double latitude, double longitude);
}
