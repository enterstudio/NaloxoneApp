package com.humworld.codeathon.receivers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.humworld.codeathon.service.GpsTrackerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Sys-3 on 10/14/2016.
 * Company Name Humworld
 */

public class MainReceiver extends BroadcastReceiver {

    private static final int HEART_RATE_MIN = 60;
    private static final int HEART_RATE_MAX = 100;

    private static final int RANDOM_NUM_MIN = 40;
    private static final int RANDOM_NUM_MAX = 100;

    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";

    private List<String> mCarrierMobileNumber;

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sPreference = context.getApplicationContext().getSharedPreferences("Database", 0);
        int value1 = sPreference.getInt("Value1", 0);
        int value2 = sPreference.getInt("Value2", 0);
        boolean alertTriggered = sPreference.getBoolean("isAlertStart", false);
        SharedPreferences.Editor editor = sPreference.edit();

        if (!alertTriggered) {
            //No alert in QUEUE
            Random rand = new Random();
            int randomNum = rand.nextInt((RANDOM_NUM_MAX - RANDOM_NUM_MIN) + 1) + RANDOM_NUM_MIN;

            if (value1 != 0) {
                if (value2 != 0) {

                } else {
                    Log.e("InsertValue2", "" + randomNum);
                    editor.putInt("Value2", randomNum);
                    if ((value1 < HEART_RATE_MIN || value1 > HEART_RATE_MAX) && (value2 < HEART_RATE_MIN || value2 > HEART_RATE_MAX)) {


                    }else {
                        //Replace value1 by value2
                        editor.putInt("Value1", randomNum);
                    }
                }
            } else {
                Log.e("InsertValue1", "" + randomNum);
                editor.putInt("Value1", randomNum);
            }


            if (value1 != 0) {
                Log.e("ReadValue1", "" + value1);
                if (value2 != 0) {
                    Log.e("ReadValue2", "" + value2);
                    if ((value1 < HEART_RATE_MIN || value1 > HEART_RATE_MAX) && (value2 < HEART_RATE_MIN || value2 > HEART_RATE_MAX)) {
                        //Get user current location
                        registerBroadCastReceivers(context);
                        mCarrierMobileNumber = new ArrayList<>();
                        GpsTrackerService gpsTrackerService = new GpsTrackerService(context);
                        if (gpsTrackerService.canGetLocation()) {
                            double latitude = gpsTrackerService.getLatitude();
                            double longitude = gpsTrackerService.getLongitude();
                            Log.e("Latitude", "" + latitude);
                            Log.e("Longitude", "" + longitude);

                            //get two Nearest Naloxone Carriers from API
                            getNaloxoneCarrier(context, String.valueOf(latitude), String.valueOf(longitude));

                        } else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gpsTrackerService.showSettingsAlert();
                        }
                        //Find Primary and Secondary Naloxone Carriers
                        //Send SMS to Nearest Naloxone Carrier and Friends and Family
                        Log.e("FinalCheck", "SMS triggered");
                        editor.putBoolean("isAlertStart", true);
                    } else {
                        //Last Two Readings are normal set Shared Preferences to ZERO
                        Log.e("FinalCheck", "Heart Rate Normal");
                        editor.putInt("Value1", 0);
                        editor.putInt("Value2", 0);
                    }

                } else {
                    Log.e("InsertValue2", "" + randomNum);
                    editor.putInt("Value2", randomNum);
                }
            } else {
                Log.e("InsertValue1", "" + randomNum);
                editor.putInt("Value1", randomNum);
            }
        } else {
            //Alert Already in QUEUE
            Log.e("AlertStatus", "Alert Already in QUEUE");
        }

        editor.apply();

    }

    private void getNaloxoneCarrier(final Context mContext, final String latitude, final String longitude) {
        String carrierURL = "http://naloxone.u7z6kympfr.us-west-2.elasticbeanstalk.com/CarrierDetails";
        //Volley Request Queue Object
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        //Prepare String Request from Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, carrierURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("CarrierResponse", response);
                try {
                    JSONObject jObject = new JSONObject(response);

                    JSONObject jsonObject = jObject.getJSONObject("naloxCarrierModel1");
                    mCarrierMobileNumber.add(jsonObject.getString("naloxCarrPrimaryPhone"));
                    //carrierOne = carrierOne + jsonObject.getString("naloxCarrName") + "\nMobile : " + jsonObject.getString("naloxCarrPrimaryPhone") + "\n";
                    //JSONObject jsonObject1 = jsonObject.getJSONObject("naloxCarrAddress");
                    //carrierOne = carrierOne + jsonObject1.getString("addressLine1") + "\n" + jsonObject1.getString("addressCity") + ", " + jsonObject1.getString("addressState") + ", " + jsonObject1.getString("addressCountry");

                    JSONObject jsonObject2 = jObject.getJSONObject("naloxCarrierModel2");
                    mCarrierMobileNumber.add(jsonObject2.getString("naloxCarrPrimaryPhone"));
                    //carrierTwo = carrierTwo + jsonObject2.getString("naloxCarrName") + "\nMobile : " + jsonObject2.getString("naloxCarrPrimaryPhone") + "\n";
                    //JSONObject jsonObject3 = jsonObject2.getJSONObject("naloxCarrAddress");
                    //carrierTwo = carrierTwo + jsonObject3.getString("addressLine1") + "\n" + jsonObject3.getString("addressCity") + ", " + jsonObject3.getString("addressState") + ", " + jsonObject3.getString("addressCountry");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sendSms(mContext, mCarrierMobileNumber);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error Listener : " + error.toString());
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
                Log.e("Volley Error", "Policy Error: " + error.toString());
            }
        });


        requestQueue.add(stringRequest);

    }

    private void sendSms(Context mContext, List<String> carrierMobileNumber) {
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
                sentPI.add(PendingIntent.getBroadcast(mContext.getApplicationContext(), i, new Intent(SENT), 0));
                deliveredPI.add(PendingIntent.getBroadcast(mContext.getApplicationContext(), j, new Intent(DELIVERED), 0));
            }
            smsManager.sendMultipartTextMessage(num, "Overdose Alert", parts, sentPI, deliveredPI);
            Log.e("Receiver:SMS", "SMS sent to : " + num);
            i++;
            j++;
        }
    }

    private void registerBroadCastReceivers(Context mContext) {

        mContext.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.e("SMS Status", "SMS sent");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.e("SMS Status", "Generic failure");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Log.e("SMS Status", "No service");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Log.e("SMS Status", "Null PDU");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Log.e("SMS Status", "Radio off");
                        break;
                }
            }
        }, new IntentFilter(SENT));

        mContext.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {

                    case Activity.RESULT_OK:
                        Log.e("SMS Status", "SMS delivered");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e("SMS Status", "SMS not delivered");
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
    }
}
