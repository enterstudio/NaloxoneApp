package com.humworld.codeathon.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.humworld.codeathon.R;
import com.humworld.codeathon.database.DatabaseHandler;
import com.humworld.codeathon.service.GpsTrackerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Sys-3 on 10/17/2016.
 * Company Name Humworld
 */

public class HomeFragment extends Fragment {

    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    private DatabaseHandler mDatabaseHandler;
    private String mCarrierOne = "Primary Naloxone Carrier : \n\n";
    private String mCarrierTwo = "Secondary Naloxone Carrier : \n\n";
    private TextView mTxtCarrierOne;
    private TextView mTxtCarrierTwo;
    private List<String> mCarrierMobileNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mDatabaseHandler = new DatabaseHandler(getActivity());
        TextView txtAlertButton = (TextView) rootView.findViewById(R.id.txt_alert_button);
        mTxtCarrierOne = (TextView) rootView.findViewById(R.id.txt_carrier_one);
        mTxtCarrierTwo = (TextView) rootView.findViewById(R.id.txt_carrier_two);

        registerBroadCastReceivers();
        txtAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check emergency contact list from SQLite Database
                int count = mDatabaseHandler.getCareGiverCount();
                //if the list is zero then show error message
                //else show alert window
                if (count == 0) {
                    Toast.makeText(getActivity(), "Your Emergency contact list was empty", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Confirm Alert");
                    alertDialog.setMessage("If you press OK, then it will trigger SMS to all of your emergency contact list");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(getActivity(), "Your Alert will be send to your emergency contact list", Toast.LENGTH_SHORT).show();
                                    //Get Current Location From GPS
                                    mCarrierMobileNumber = new ArrayList<>();
                                    GpsTrackerService gpsTrackerService = new GpsTrackerService(getActivity());
                                    if (gpsTrackerService.canGetLocation()) {
                                        double latitude = gpsTrackerService.getLatitude();
                                        double longitude = gpsTrackerService.getLongitude();
                                        Log.e("Latitude", "" + latitude);
                                        Log.e("Longitude", "" + longitude);

                                        //get two Nearest Naloxone Carriers from API
                                        getNaloxoneCarrier(String.valueOf(latitude), String.valueOf(longitude));

                                    } else {
                                        // can't get location\
                                        // GPS or Network is not enabled
                                        // Ask user to enable GPS/network in settings
                                        gpsTrackerService.showSettingsAlert();
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        return rootView;
    }

    private void getNaloxoneCarrier(final String latitude, final String longitude) {
        String carrierURL = "http://naloxone.u7z6kympfr.us-west-2.elasticbeanstalk.com/CarrierDetails";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Finding Nearest Naloxone Carriers...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Volley Request Queue Object
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Prepare String Request from Volley
        //StringRequest stringRequest = new StringRequest()

        StringRequest stringRequest = new StringRequest(Request.Method.POST, carrierURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LoginResponse", response);
                try {
                    JSONObject jObject = new JSONObject(response);

                    JSONObject jsonObject = jObject.getJSONObject("naloxCarrierModel1");
                    mCarrierMobileNumber.add(jsonObject.getString("naloxCarrPrimaryPhone"));
                    mCarrierOne = mCarrierOne + jsonObject.getString("naloxCarrName") + "\nMobile : " + jsonObject.getString("naloxCarrPrimaryPhone") + "\n";
                    JSONObject jsonObject1 = jsonObject.getJSONObject("naloxCarrAddress");
                    mCarrierOne = mCarrierOne + jsonObject1.getString("addressLine1") + "\n" + jsonObject1.getString("addressCity") + ", " + jsonObject1.getString("addressState") + ", " + jsonObject1.getString("addressCountry");

                    JSONObject jsonObject2 = jObject.getJSONObject("naloxCarrierModel2");
                    mCarrierMobileNumber.add(jsonObject2.getString("naloxCarrPrimaryPhone"));
                    mCarrierTwo = mCarrierTwo + jsonObject2.getString("naloxCarrName") + "\nMobile : " + jsonObject2.getString("naloxCarrPrimaryPhone") + "\n";
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("naloxCarrAddress");
                    mCarrierTwo = mCarrierTwo + jsonObject3.getString("addressLine1") + "\n" + jsonObject3.getString("addressCity") + ", " + jsonObject3.getString("addressState") + ", " + jsonObject3.getString("addressCountry");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                mTxtCarrierOne.setText(mCarrierOne);
                //noinspection deprecation
                mTxtCarrierOne.setBackgroundColor(getResources().getColor(R.color.txt_background_color));
                mTxtCarrierTwo.setText(mCarrierTwo);
                //noinspection deprecation
                mTxtCarrierTwo.setBackgroundColor(getResources().getColor(R.color.txt_background_color));

                sendSms(mCarrierMobileNumber);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //token = "Check your internet connection";
                //token = error.getMessage();
                Toast.makeText(getActivity(), "Error Listener : " + error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postParams = new HashMap<>();
                postParams.put("latitude", latitude);
                postParams.put("longitude", longitude);
                return postParams;
            }
        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Toast.makeText(getActivity(), "Policy Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        requestQueue.add(stringRequest);

    }

    private void sendSms(List<String> carrierMobileNumber) {
        String message = "Name - Smith Anderson\n" +
                "Age : 45\n" +
                "Location : 2944 Grand view Street Northwest, Grand Rapids, Michigan 49504, United States\n" +
                "Alcoholic : Y/N - Y\n" +
                "opioid prescription Name : codeine\n" +
                "Allergies : Swallowing and/or speaking\n" +
                "BMI : Normal Weight\n" +
                "Heart Rate : 45 Bpm";

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        int numParts = parts.size();
        int i = 0;
        int j = 0;

        ArrayList<PendingIntent> sentPI = new ArrayList<>();
        ArrayList<PendingIntent> deliveredPI = new ArrayList<>();

        for (String num : carrierMobileNumber) {
            for (int k = 0; k < numParts; k++) {
                sentPI.add(PendingIntent.getBroadcast(getActivity().getApplicationContext(), i, new Intent(SENT), 0));
                deliveredPI.add(PendingIntent.getBroadcast(getActivity().getApplicationContext(), j, new Intent(DELIVERED), 0));
            }
            smsManager.sendMultipartTextMessage(num, "Overdose Alert", parts, sentPI, deliveredPI);
            //smsManager.sendTextMessage(num, "Overdose Alert", message, sentPI, deliveredPI);
            Toast.makeText(getActivity(), "SMS sent to " + num, Toast.LENGTH_SHORT).show();
            i++;
            j++;
        }
    }

    private void registerBroadCastReceivers() {

        getActivity().getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        Toast.makeText(getActivity(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getActivity(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getActivity(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getActivity(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getActivity(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        getActivity().getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {

                    case Activity.RESULT_OK:
                        Toast.makeText(getActivity(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
    }
}
