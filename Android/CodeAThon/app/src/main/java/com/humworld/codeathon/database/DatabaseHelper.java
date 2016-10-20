package com.humworld.codeathon.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Sys-3 on 10/17/2016.
 * Company Name Humworld
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Naloxone_db.db"; //Database Name
    //Table Name
    public static final String TABLE_CARE_GIVER = "nalox_care_giver";
    public static final String TABLE_ADDRESS = "nalox_address";

    //    public static String CGIV_ROW_ID = "rowid";
    public static final String CGIV_ID = "Cgiv_Id";
    @SuppressWarnings("unused")
    public static String CGIV_PATI_ID = "Cgiv_Pati_Id";
    public static final String CGIV_FIRST_NAME = "Cgiv_First_Name";
    public static final String CGIV_LAST_NAME = "Cgiv_Last_Name";
    public static final String CGIV_MIDDLE_NAME = "Cgiv_Middle_Name";
    public static final String CGIV_RELATIONSHIP_TYPE = "Cgiv_Relationship_Type";
    public static final String CGIV_EMAIL_ID = "Cgiv_Email_Id";
    public static final String CGIV_MOBILE_NO = "Cgiv_Mobile_No";
    public static final String CGIV_ADDRESS_ID = "Cgiv_Addr_Id";
    public static final String CGIV_CREATED_DATE = "Cgiv_Created_Date";


    public static final String ADDR_ID = "Addr_ID";
    public static final String ADDR_LINE_ONE = "Addr_Address_Line1";
    public static final String ADDR_LINE_TWO = "Addr_Address_Line2";
    public static final String ADDR_CITY = "Addr_City_Name";
    public static final String ADDR_STATE = "Addr_State_Name";
    public static final String ADDR_COUNTRY = "Addr_Country_Name";
    public static final String ADDR_POSTAL_CODE = "Addr_Zip_Code";
    public static final String ADDR_CREATED_DATE = "Addr_Created_Date";

    private final Context mContext;
    private final String DB_PATH;
    private SQLiteDatabase mDataBase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        DB_PATH = mContext.getDatabasePath(DB_NAME).toString();
        Log.e("Database Path", DB_PATH);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public synchronized void close() {

        if (mDataBase != null)
            mDataBase.close();

        super.close();

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
            Log.d("Current Time", "Db Time is Less than Sys Time");
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH;
        //String myPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    private boolean checkDataBase() {
        // TODO Auto-generated method stub
        SQLiteDatabase checkDB = null;

        try {
            //String myPath = DB_PATH + DB_NAME;
            String myPath = DB_PATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            Log.e("checkDB", checkDB.toString());

        } catch (SQLiteException e) {

            //database does't exist yet.
            Log.e("CheckDatabaseError", e.getMessage());

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null;
        //return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        // TODO Auto-generated method stub
        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }


}
