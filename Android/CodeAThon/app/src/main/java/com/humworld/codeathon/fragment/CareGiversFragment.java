package com.humworld.codeathon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.humworld.codeathon.R;
import com.humworld.codeathon.activity.AddCareGiverActivity;
import com.humworld.codeathon.adapter.CareGiverAdapter;
import com.humworld.codeathon.database.DatabaseHandler;
import com.humworld.codeathon.model.CareGiver;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sys-3 on 10/17/2016.
 * Company Name Humworld
 */

public class CareGiversFragment extends Fragment {

    private Animation mFabOpen;
    private Animation mFabClose;
    private Animation mRotateForward;
    private Animation mRotateBackward;
    private LinearLayout mAddManual;
    private LinearLayout mAddFromContact;
    private FloatingActionButton mFab;
    private FloatingActionButton mFabContact;
    private FloatingActionButton mFabManually;
    private Button mAddContact;
    private Button mAddManually;
    private List<CareGiver> mCareGiverList;
    private RecyclerView mRecyclerview;
    private DatabaseHandler mDBHandler;
    private Boolean mIsFabOpen = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_care_giver, container, false);
        initialize(rootView);
        mCareGiverList = mDBHandler.getCareGiversList();
        CareGiverAdapter adapter = new CareGiverAdapter(mCareGiverList);
        mRecyclerview.setAdapter(adapter);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });

        mAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddCareGiverActivity("C");
            }
        });

        mAddManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddCareGiverActivity("M");
            }
        });

        mFabContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddCareGiverActivity("C");
            }
        });

        mFabManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddCareGiverActivity("M");
            }
        });


        return rootView;
    }

    private void initialize(View rootView) {
        mAddManual = (LinearLayout) rootView.findViewById(R.id.linear_one);
        mAddFromContact = (LinearLayout) rootView.findViewById(R.id.linear_two);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFabContact = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        mFabManually = (FloatingActionButton) rootView.findViewById(R.id.fab1);

        mAddContact = (Button) rootView.findViewById(R.id.btn_add_from_contact);
        mAddManually = (Button) rootView.findViewById(R.id.btn_add_manually);
        mRecyclerview = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFabOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        mFabClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        mRotateForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        //mFab.setOnClickListener(getActivity());

        mCareGiverList = new ArrayList<>();

        mDBHandler = new DatabaseHandler(getActivity());
    }

    private void animateFAB() {

        if (mIsFabOpen) {

            mFab.startAnimation(mRotateBackward);
            mAddManual.startAnimation(mFabClose);
            mAddFromContact.startAnimation(mFabClose);
            mAddManual.setClickable(false);
            mAddFromContact.setClickable(false);
            mIsFabOpen = false;

        } else {

            mFab.startAnimation(mRotateForward);
            mAddFromContact.startAnimation(mFabOpen);
            mAddManual.startAnimation(mFabOpen);
            mAddManual.setClickable(true);
            mAddFromContact.setClickable(true);
            mIsFabOpen = true;

        }
    }

    private void navigateToAddCareGiverActivity(String m) {

        Intent intent = new Intent(getActivity(), AddCareGiverActivity.class);
        intent.putExtra("How", m);
        startActivity(intent);
    }
}
