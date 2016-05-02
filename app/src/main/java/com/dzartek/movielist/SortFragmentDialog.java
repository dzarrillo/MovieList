package com.dzartek.movielist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dzartek.movielist.datamodel.MovieConst;

/**
 * Created by dzarrillo on 4/5/2016.
 */
public class SortFragmentDialog extends DialogFragment implements View.OnClickListener{
    private Button btnOk, btnCancel;
    private RadioButton rBtnPopularity, rBtnUserRatings, rBtnMyFav;
    public interface OnSortSelectedListener{
        void onSortOrderSelected(String sortorder);
    }
    private OnSortSelectedListener mSortOrderCallback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.sort_fragment_dialog, container, false);
        getDialog().setTitle("Sort-By");

        btnCancel = (Button) v.findViewById(R.id.buttonCancel);
        btnOk = (Button) v.findViewById(R.id.buttonOk);
        rBtnPopularity = (RadioButton) v.findViewById(R.id.radioButtonPopularity);
        rBtnMyFav = (RadioButton) v.findViewById(R.id.radioButtonMyFavorites);
        rBtnUserRatings = (RadioButton) v.findViewById(R.id.radioButtonUserRatings);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        try{
            mSortOrderCallback = (OnSortSelectedListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() +
            " must implement OnSortSelectedListener!");
        }
    }

    @Override
    public void onClick(View v) {
        String sortBy="";
        switch (v.getId()){
            case R.id.buttonCancel:
                sortBy="";
                dismiss();
                break;
            case R.id.buttonOk:

                if (rBtnPopularity.isChecked()){
                    sortBy= MovieConst.SORT_POPULARITY_VALUE;

                } else if(rBtnUserRatings.isChecked()){
                    sortBy= MovieConst.SORT_HIGHEST_RATED_VALUE;
                } else if(rBtnMyFav.isChecked()){
                    sortBy=MovieConst.SORT_FAVORITES_VALUE;
                    MovieConst.refreshDBData=true;

                } else {
                    showMyToast("Nothing has been selected!");
                    return;
                }
                break;
        }

        if(!sortBy.isEmpty()){
            saveToSharedPreferences(sortBy);
        }
        // Send the event back to host to refresh data
        mSortOrderCallback.onSortOrderSelected(sortBy);
        dismiss();
    }

    public void showMyToast(String msg) {
        TextView tv = new TextView(getActivity());
        tv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightgrey));
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.primarytext));
        tv.setPadding(25, 25, 25, 25);
        tv.setTextSize(19);
        tv.setTypeface(null, Typeface.NORMAL);
        tv.setText(msg);

        Toast tt = new Toast(getActivity());
        tt.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 150);
        tt.setView(tv);
        tt.show();
    }

    private void saveToSharedPreferences(String sortBy) {
        SharedPreferences sharedpreferences;

        sharedpreferences = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sharedpreferences.edit();

        editor.putString(MovieConst.PREF_SORT_KEY, sortBy);
        editor.apply();  //.commit();
    }
}
