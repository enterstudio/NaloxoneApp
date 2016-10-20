package com.humworld.codeathon.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.humworld.codeathon.R;
import com.humworld.codeathon.ValidationClass;
import com.humworld.codeathon.database.DatabaseHandler;
import com.humworld.codeathon.model.CareGiver;

import java.util.ArrayList;

public class AddCareGiverActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener {

    private final ArrayList<String> mContactNumbers = new ArrayList<>();
    private final ArrayList<String> mContactNames = new ArrayList<>();
    private final ArrayList<String> mContactEmails = new ArrayList<>();
    private final ArrayList<String> mContactAddress = new ArrayList<>();
    private ArrayList<String> mRelations;
    private EditText mFirstName;
    private EditText mMiddleName;
    private EditText mMobileNo;
    private EditText mAddressOne;
    private EditText mEmailId;
    private EditText mAddressTwo;
    private EditText mCity;
    private EditText mState;
    private EditText mCountry;
    private EditText mPostalCode;

    private AutoCompleteTextView mLastName;
    private Spinner mSpinRelationType;
    private LinearLayout mLinearLayout;
    private ArrayAdapter<String> mAdapter;
    private String mCareGiverId = null;
    private String mAddressId = null;
    private boolean mManually;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_care_giver);
        initializeWidgets();
        setSpinnerValues();
        checkComeFromWhere();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                storeInDb();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        // Get Array index value for selected name
        int i = mContactNames.indexOf("" + arg0.getItemAtPosition(arg2));
        // If name exist in name ArrayList
        if (i >= 0) {
            // Get Phone Number
            String toNumberValue = mContactNumbers.get(i);
            mMobileNo.setText(toNumberValue);
            String emailValue = mContactEmails.get(i);
            if (emailValue != null) {
                mEmailId.setText(emailValue);
            }
            String addressValue = mContactAddress.get(i);
            if (addressValue != null) {
                mAddressOne.setText(addressValue);
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            //noinspection ConstantConditions
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    private void checkComeFromWhere() {

        CareGiver careGiver = (CareGiver) getIntent().getSerializableExtra("FromAdapter");
        if (careGiver != null) {
            setFieldValues(careGiver);
        } else {
            checkManuallyOrNot();
        }
    }

    private void setFieldValues(CareGiver careGiver) {

        mCareGiverId = careGiver.getId();
        String first = careGiver.getFirstName();
        if (ValidationClass.isValidString(first)) {
            mFirstName.setText(first);
            mMiddleName.setText(careGiver.getMiddleName());
            mManually = true;
        } else {
            prepareDataForContactSelection();
        }
        mLastName.setText(careGiver.getLastName());
        mMobileNo.setText(careGiver.getMobile_no());
        mEmailId.setText(careGiver.getMail_id());
        mAddressOne.setText(careGiver.getAddressOne());
        mAddressTwo.setText(careGiver.getAddressTwo());
        mCity.setText(careGiver.getCity());
        mState.setText(careGiver.getState());
        mCountry.setText(careGiver.getCountry());
        mPostalCode.setText(careGiver.getPostalCode());
        int pos = mRelations.indexOf(careGiver.getRelation());
        mSpinRelationType.setSelection(pos);
        mAddressId = careGiver.getAddressId();

    }

    private void setSpinnerValues() {
        mRelations = new ArrayList<>();
        mRelations.add("Select Type");
        mRelations.add("Mother");
        mRelations.add("Father");
        mRelations.add("Brother");
        mRelations.add("Sister");
        mRelations.add("Friend");
        mRelations.add("Husband");
        mRelations.add("Wife");
        mRelations.add("Grand Father");
        mRelations.add("Grand Mother");
        ArrayAdapter<String> severityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mRelations);
        severityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinRelationType.setAdapter(severityAdapter);
        mSpinRelationType.setSelection(0);
    }

    private void storeInDb() {

        String firstName = mFirstName.getText().toString();
        String middleName = mMiddleName.getText().toString();
        String lastName = mLastName.getText().toString();
        String emailId = mEmailId.getText().toString();
        String mobileNo = mMobileNo.getText().toString();
        String addressOne = mAddressOne.getText().toString();
        String addressTwo = mAddressTwo.getText().toString();
        String city = mCity.getText().toString();
        String state = mState.getText().toString();
        String country = mCountry.getText().toString();
        String postalCode = mPostalCode.getText().toString();
        String relationType = mSpinRelationType.getSelectedItem().toString();

        CareGiver careGiver = new CareGiver();
        if (mManually) {
            if (ValidationClass.isValidString(firstName)) {
                careGiver.setFirstName(firstName);
                careGiver.setMiddleName(middleName);
            } else {
                setError(mFirstName);
                return;
            }
        } else {
            careGiver.setFirstName("");
            careGiver.setMiddleName("");
        }

        if (ValidationClass.isValidString(lastName)) {
            if (ValidationClass.isValidMail(emailId)) {
                if (ValidationClass.isValidMobile(mobileNo)) {
                    if (!relationType.equals("Select Type")) {
                        if (ValidationClass.isValidString(addressOne)) {
                            if (ValidationClass.isValidString(city)) {
                                if (ValidationClass.isValidString(state)) {
                                    if (ValidationClass.isValidString(country)) {
                                        if (ValidationClass.isValidString(postalCode)) {

                                            careGiver.setLastName(lastName);
                                            careGiver.setMobile_no(mobileNo);
                                            careGiver.setRelation(relationType);
                                            careGiver.setMail_id(emailId);
                                            careGiver.setAddressOne(addressOne);
                                            careGiver.setAddressTwo(addressTwo);
                                            careGiver.setCity(city);
                                            careGiver.setState(state);
                                            careGiver.setCountry(country);
                                            careGiver.setPostalCode(postalCode);

                                            DatabaseHandler dbHandler =
                                                    new DatabaseHandler(getApplicationContext());
                                            if (mCareGiverId == null) {
                                                dbHandler.storeCareGiver(careGiver);
                                            } else {
                                                careGiver.setId(mCareGiverId);
                                                careGiver.setAddressId(mAddressId);
                                                dbHandler.updateCareGiver(careGiver);
                                            }
                                            navigateToMainActivity();

                                        } else {
                                            setError(mPostalCode);
                                        }
                                    } else {
                                        setError(mCountry);
                                    }
                                } else {
                                    setError(mState);
                                }
                            } else {
                                setError(mCity);
                            }
                        } else {
                            setError(mAddressOne);
                        }
                    } else {
                        Toast.makeText(this, "Select the relationship type",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setError(mMobileNo);
                }
            } else {
                setError(mEmailId);
            }

        } else {
            setError(mLastName);
        }

    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setError(EditText editText) {
        editText.setError("Enter Valid One");
    }

    private void initializeWidgets() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirstName = (EditText) findViewById(R.id.etx_first_name);
        mMiddleName = (EditText) findViewById(R.id.etx_middle_name);
        mLastName = (AutoCompleteTextView) findViewById(R.id.etx_last_name);
        mMobileNo = (EditText) findViewById(R.id.etx_mobile_no);
        mEmailId = (EditText) findViewById(R.id.etx_email_id);
        mAddressOne = (EditText) findViewById(R.id.etx_address_line_one);
        mAddressTwo = (EditText) findViewById(R.id.etx_address_line_two);
        mCity = (EditText) findViewById(R.id.etx_city);
        mState = (EditText) findViewById(R.id.etx_state);
        mCountry = (EditText) findViewById(R.id.etx_country);
        mPostalCode = (EditText) findViewById(R.id.etx_postal_code);

        mSpinRelationType = (Spinner) findViewById(R.id.spin_relation_type);
        mLinearLayout = (LinearLayout) findViewById(R.id.layout_linear_one);
    }

    private void checkManuallyOrNot() {

        String how = getIntent().getStringExtra("How");
        mManually = how.equals("M");

        if (!mManually) {
            prepareDataForContactSelection();
        }
    }

    private void prepareDataForContactSelection() {
        mLinearLayout.setVisibility(View.GONE);
        mMobileNo.setFocusable(false);

        mAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        //noinspection unchecked
        new ReadContactTask().execute();

        mLastName.setHint("Type Here");
        mLastName.setAdapter(mAdapter);
        mLastName.setOnItemClickListener(this);
    }

    private void readContactData() {

        try {

            /*********** Reading Contacts Name And Number **********/
            String phoneNumber;
            ContentResolver cr = getBaseContext()
                    .getContentResolver();
            //Query to get contact name
            Cursor cur = cr
                    .query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
            // If data data found in contacts
            //noinspection ConstantConditions
            if (cur.getCount() > 0) {

                Log.i("AutocompleteContacts", "Reading   contacts........");

                int k = 0;
                String name;

                while (cur.moveToNext()) {
                    String id = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //Check contact have phone number
                    if (Integer
                            .parseInt(cur
                                    .getString(cur
                                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        //Create query to get phone number by contact id
                        Cursor pCur = cr
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = ?",
                                        new String[]{id},
                                        null);
                        int j = 0;

                        //noinspection ConstantConditions
                        while (pCur
                                .moveToNext()) {
                            // Sometimes get multiple data
                            if (j == 0) {
                                // Get Phone number
                                phoneNumber = "" + pCur.getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                // Add contacts names to mAdapter
                                mAdapter.add(name);

                                // Add ArrayList names to mAdapter
                                mContactNumbers.add(phoneNumber);
                                mContactNames.add(name);

                                String[] PROJECTION =
                                        {
                                                ContactsContract.CommonDataKinds.Email._ID,
                                                ContactsContract.CommonDataKinds.Email.ADDRESS,
                                                ContactsContract.CommonDataKinds.Email.LABEL,
                                                ContactsContract.CommonDataKinds.Email.DATA

                                        };
                                Cursor eCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                        PROJECTION,
                                        ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                                + " = ?",
                                        new String[]{id},
                                        null);
                                //noinspection ConstantConditions
                                if (eCur.moveToFirst()) {
                                    String email = eCur.getString(eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
                                    if (email != null) {
                                        mContactEmails.add(email);
                                    } else {
                                        mContactEmails.add(null);
                                    }
                                } else {
                                    mContactEmails.add(null);
                                }

                                eCur.close();

                                Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                                Cursor postal_cursor = getContentResolver().query(postal_uri, null, ContactsContract.Data.CONTACT_ID + "=" + id, null, null);

                                //noinspection ConstantConditions
                                if (postal_cursor.moveToFirst()) {
                                    String street = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                                    String city = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                                    String country = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

                                    String fullAddress = "";
                                    if (ValidationClass.isValidString(street))
                                        fullAddress += street;
                                    if (ValidationClass.isValidString(city))
                                        fullAddress += "\n" + city;
                                    if (ValidationClass.isValidString(country))
                                        fullAddress += "\n" + country;

                                    if (ValidationClass.isValidString(fullAddress)) {
                                        mContactAddress.add(fullAddress);
                                    } else {
                                        mContactAddress.add(null);
                                    }
                                } else {
                                    mContactAddress.add(null);
                                }
                                postal_cursor.close();

                                j++;
                                k++;
                            }
                        }  // End while loop
                        pCur.close();
                    } // End if

                }  // End while loop

            } // End Cursor value check
            cur.close();


        } catch (Exception e) {
            Log.i("AutocompleteContacts", "Exception : " + e);
        }


    }

    private class ReadContactTask extends AsyncTask {

        private final ProgressDialog pDialog = new ProgressDialog(AddCareGiverActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("please wait");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            readContactData();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            //noinspection unchecked
            super.onPostExecute(o);
            pDialog.dismiss();
        }
    }
}