/*
 * Copyright (c) 2016 Humworld INC,
 */

package com.humworld.lifesaverapi.dao;

import java.util.List;

/**
 * Data Access Object interface, containing abstract methods.
 * 
 * @author Humworld
 * @version 1.0
 */
public interface CarrierDetailsDao {

	public List<Object[]> getLatiLongi();
	
	public List<Object[]> getDetailsUsingLatiLongi(double latitude, double longitude);
}
