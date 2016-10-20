/*
 * Copyright (c) 2016 Humworld INC,
 */

package com.humworld.lifesaverapi.daoimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.humworld.lifesaverapi.dao.CarrierDetailsDao;

/**
 * Data Access Object class, containing methods related to fetching nearest
 * carrier details.
 * 
 * @author Humworld
 * @version 1.0
 */
@Repository("carrierDetailsDao")
public class CarrierDetailsDaoImpl implements CarrierDetailsDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Fetches the latitude and longitude.
	 * 
	 * @return List<Object[]> List containing latitude and longitude.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getLatiLongi() {
		Session session = sessionFactory.getCurrentSession();
		String sql = " select round(Ncar_Latitude, 6), round(Ncar_Longitude, 6) "
						+ " from nalox_carrier ";
		SQLQuery query = session.createSQLQuery(sql);
		List<Object[]> list = query.list();
		return list;
	}
	
	/**
	 * Fetches nearest carrier details.
	 * 
	 * @param latitude Latitude of nearest carrier.
	 * @param longitude Longitude of nearest carrier.
	 * 
	 * @return List<Object[]> List containing the nearest carrier details.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDetailsUsingLatiLongi(double latitude, double longitude) {
		Session session = sessionFactory.getCurrentSession();
		String hql = " from NaloxCarrierModel ncm "
						+ " inner join ncm.naloxCarrAddress "
						+ " where ncm.naloxCarrLatitude=:latitude "
						+ " and ncm.naloxCarrLongitude=:longitude ";
		Query query = session.createQuery(hql);
		query.setDouble("latitude", latitude);
		query.setDouble("longitude", longitude);
		List<Object[]> list = query.list();
		return list;
	}

}
