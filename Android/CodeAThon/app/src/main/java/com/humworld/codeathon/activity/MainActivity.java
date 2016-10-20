package com.humworld.codeathon.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.humworld.codeathon.R;
import com.humworld.codeathon.database.DatabaseHelper;
import com.humworld.codeathon.fragment.CareGiversFragment;
import com.humworld.codeathon.fragment.HomeFragment;
import com.humworld.codeathon.service.MainService;

import java.util.Iterator;

/**
 * --------------------------------------------------------------------------------------------------------------------
 * This code snippet will use Random Number Generation logic to generate heart rate every minute.
 * Program will compare the current value and previous value generated, to check if it's outside the range (60 to 80)
 * for example if the current value is 55 and previous value is 57 then it will pull
 * the two nearest naloxone carriers from server based on user's current location (geo location latitude and longitude).
 * it will send the alert message to two sample mobile numbers for the purpose of the prototype.
 * Created by Sys-3 on 10/19/2016.
 * ---------------------------------------------------------------------------------------------------------------------
 * Company Name : Humworld
 * Team Name : Humteam
 */

public class MainActivity extends AppCompatActivity {

    //Get Run time Permission for first time
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION

    };
    private static final int INITIAL_REQUEST = 1007;

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create Database
        createDB();
        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nvView);
        //Inflate Navigation Header View @ Runtime
        @SuppressWarnings("UnusedAssignment") View navHeaderView = mNavigationView.inflateHeaderView(R.layout.nav_header);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new HomeFragment());
        tx.commit();
        setupDrawerContent(mNavigationView);

        checkNeccessaryPermissions();

        if (!isServiceRunning()) {
            startService(new Intent(MainActivity.this, MainService.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        /*switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }*/
        //noinspection SimplifiableIfStatement
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void createDB() {
        SharedPreferences sPreference = getApplicationContext().getSharedPreferences("Database", 0);
        boolean databaseCreate = sPreference.getBoolean("isDatabaseCreate", false);

        //Check database availability and Create
        if (!databaseCreate) {
            createAndOpenDatabase();
        }
    }

    private void checkNeccessaryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /*if (!(PackageManager.PERMISSION_GRANTED == checkSelfPermission((Manifest.permission.ACCESS_FINE_LOCATION)))) {
                requestPermissions(PERMISSIONS, INITIAL_REQUEST);            }*/
            if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) || !(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(PERMISSIONS, INITIAL_REQUEST);
            }

        }
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_care_giver:
                fragmentClass = CareGiversFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        mDrawerLayout.closeDrawers();
    }

    private void setupDrawerContent(NavigationView mNavigationView) {
        //noinspection NullableProblems
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void createAndOpenDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //Create the Database at very first Time
        try {
            dbHelper.createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            dbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SharedPreferences sPreference = getApplicationContext().getSharedPreferences("Database", 0);
        SharedPreferences.Editor editor = sPreference.edit();
        editor.putBoolean("isDatabaseCreate", true);
        //editor.putInt("Value1", 0);
        //editor.putInt("Value2", 0);
        editor.apply();
    }

    private boolean isServiceRunning() {
        Iterator localIterator = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE).iterator();
        ActivityManager.RunningServiceInfo localRunningServiceInfo;
        do {
            if (!localIterator.hasNext()) {
                return false;
            }
            localRunningServiceInfo = (ActivityManager.RunningServiceInfo) localIterator.next();

        }
        while (!MainService.class.getName().equals(localRunningServiceInfo.service.getClassName()));
        return true;
    }

}
