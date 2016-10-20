package com.humworld.codeathon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.humworld.codeathon.model.CareGiver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ${Sys-7} on 13/10/16.
 * Company Name Humworld
 */

public class DatabaseHandler {

    private final SQLiteDatabase DATABASE;

    public DatabaseHandler(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        DATABASE = databaseHelper.getWritableDatabase();
    }

    public int getCareGiverCount() {
        int count = 0;
        String query = "SELECT COUNT (" + DatabaseHelper.CGIV_ID + ") FROM " + DatabaseHelper.TABLE_CARE_GIVER;
        Cursor cursor = DATABASE.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return count;
    }

    public List<CareGiver> getCareGiversList() {

        List<CareGiver> careGiverList = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CARE_GIVER;

        Cursor cursor = DATABASE.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                CareGiver careGiver = new CareGiver();

                careGiver.setId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CGIV_ID)));
                careGiver.setFirstName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CGIV_FIRST_NAME)));
                careGiver.setMiddleName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CGIV_MIDDLE_NAME)));
                careGiver.setLastName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CGIV_LAST_NAME)));
                careGiver.setRelation(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CGIV_RELATIONSHIP_TYPE)));
                careGiver.setMobile_no(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CGIV_MOBILE_NO)));
                careGiver.setAddressId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CGIV_ADDRESS_ID)));
                careGiver.setMail_id(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CGIV_EMAIL_ID)));

                query = "SELECT * FROM " + DatabaseHelper.TABLE_ADDRESS + " WHERE " + DatabaseHelper.ADDR_ID +
                        " = '" + careGiver.getAddressId() + "'";
                cursor = DATABASE.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    careGiver.setAddressOne(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDR_LINE_ONE)));
                    careGiver.setAddressTwo(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDR_LINE_TWO)));
                    careGiver.setCity(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDR_CITY)));
                    careGiver.setState(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDR_STATE)));
                    careGiver.setCountry(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDR_COUNTRY)));
                    careGiver.setPostalCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDR_POSTAL_CODE)));
                }


                careGiverList.add(careGiver);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return careGiverList;
    }

    public void storeCareGiver(CareGiver careGiver) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.ADDR_LINE_ONE, careGiver.getAddressOne());
        contentValues.put(DatabaseHelper.ADDR_LINE_TWO, careGiver.getAddressTwo());
        contentValues.put(DatabaseHelper.ADDR_CITY, careGiver.getCity());
        contentValues.put(DatabaseHelper.ADDR_STATE, careGiver.getState());
        contentValues.put(DatabaseHelper.ADDR_COUNTRY, careGiver.getCountry());
        contentValues.put(DatabaseHelper.ADDR_POSTAL_CODE, careGiver.getPostalCode());
        contentValues.put(DatabaseHelper.ADDR_CREATED_DATE, new Date().toString());

        long id = DATABASE.insert(DatabaseHelper.TABLE_ADDRESS,
                null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CGIV_FIRST_NAME, careGiver.getFirstName());
        contentValues.put(DatabaseHelper.CGIV_MIDDLE_NAME, careGiver.getMiddleName());
        contentValues.put(DatabaseHelper.CGIV_LAST_NAME, careGiver.getLastName());
        contentValues.put(DatabaseHelper.CGIV_MOBILE_NO, careGiver.getMobile_no());
        contentValues.put(DatabaseHelper.CGIV_RELATIONSHIP_TYPE, careGiver.getRelation());
        contentValues.put(DatabaseHelper.CGIV_EMAIL_ID, careGiver.getMail_id());
        contentValues.put(DatabaseHelper.CGIV_ADDRESS_ID, id);
        contentValues.put(DatabaseHelper.CGIV_CREATED_DATE, new Date().toString());

        DATABASE.insert(DatabaseHelper.TABLE_CARE_GIVER,
                null, contentValues);
    }

    public void updateCareGiver(CareGiver careGiver) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.ADDR_LINE_ONE, careGiver.getAddressOne());
        contentValues.put(DatabaseHelper.ADDR_LINE_TWO, careGiver.getAddressTwo());
        contentValues.put(DatabaseHelper.ADDR_CITY, careGiver.getCity());
        contentValues.put(DatabaseHelper.ADDR_STATE, careGiver.getState());
        contentValues.put(DatabaseHelper.ADDR_COUNTRY, careGiver.getCountry());
        contentValues.put(DatabaseHelper.ADDR_POSTAL_CODE, careGiver.getPostalCode());

        DATABASE.update(DatabaseHelper.TABLE_ADDRESS,
                contentValues,
                DatabaseHelper.ADDR_ID + " = ? ",
                new String[]{careGiver.getAddressId()});

        contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CGIV_FIRST_NAME, careGiver.getFirstName());
        contentValues.put(DatabaseHelper.CGIV_MIDDLE_NAME, careGiver.getMiddleName());
        contentValues.put(DatabaseHelper.CGIV_LAST_NAME, careGiver.getLastName());
        contentValues.put(DatabaseHelper.CGIV_MOBILE_NO, careGiver.getMobile_no());
        contentValues.put(DatabaseHelper.CGIV_RELATIONSHIP_TYPE, careGiver.getRelation());
        contentValues.put(DatabaseHelper.CGIV_EMAIL_ID, careGiver.getMail_id());
        contentValues.put(DatabaseHelper.CGIV_ADDRESS_ID, careGiver.getAddressId());

        DATABASE.update(DatabaseHelper.TABLE_CARE_GIVER,
                contentValues,
                DatabaseHelper.CGIV_ID + " = ? ",
                new String[]{careGiver.getId()});
    }
}
