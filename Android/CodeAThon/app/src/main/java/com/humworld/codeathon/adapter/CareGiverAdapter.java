package com.humworld.codeathon.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humworld.codeathon.R;
import com.humworld.codeathon.ValidationClass;
import com.humworld.codeathon.model.CareGiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Sys-7} on 13/10/16.
 * Company Name : Humworld Inc.,
 */
@SuppressWarnings("ALL")
public class CareGiverAdapter extends RecyclerView.Adapter<CareGiverAdapter.CareGiverViewHolder> {

    private List<CareGiver> mCareGivers;

    public CareGiverAdapter(List<CareGiver> list) {

        mCareGivers = new ArrayList<>();
        mCareGivers = list;

    }

    @Override
    public CareGiverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_provider, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        return new CareGiverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CareGiverViewHolder holder, int position) {

        CareGiver careGiver = mCareGivers.get(position);

        String firstName = careGiver.getFirstName();
        String midName = careGiver.getMiddleName();
        String lastName = careGiver.getLastName();

        String fullName = "";

        if (ValidationClass.isValidString(firstName)) fullName += firstName + " ";
        if (ValidationClass.isValidString(midName)) fullName += midName + " ";
        if (ValidationClass.isValidString(lastName)) fullName += lastName;

        setTextViewData(holder.mName, fullName);
        setTextViewData(holder.mRelationship, careGiver.getRelation());
        setTextViewData(holder.mPhone, careGiver.getMobile_no());
        setTextViewData(holder.mEmail, careGiver.getMail_id());

    }

    @Override
    public int getItemCount() {
        return mCareGivers.size();
    }

    /**
     * @param textView
     * @param text
     */
    private void setTextViewData(TextView textView, String text) {

        if (text != null && text.trim().length() > 0) {
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public class CareGiverViewHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mRelationship;
        TextView mPhone;
        TextView mEmail;

        public CareGiverViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mRelationship = (TextView) itemView.findViewById(R.id.tv_designation);
            mPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            mEmail = (TextView) itemView.findViewById(R.id.tv_email);
        }
    }
}