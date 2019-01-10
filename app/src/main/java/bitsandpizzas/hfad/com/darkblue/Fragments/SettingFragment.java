package bitsandpizzas.hfad.com.darkblue.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bitsandpizzas.hfad.com.darkblue.R;

import static android.content.Context.MODE_PRIVATE;


public class SettingFragment extends Fragment {


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
 View view=inflater.inflate(R.layout.fragment_setting, container, false);



    return view;
    }

    String getHubId() {

        SharedPreferences prefs = getActivity().getSharedPreferences("HUBID", MODE_PRIVATE);
        String restoredText = prefs.getString("id", null);
        if (restoredText != null) {

            String id =  restoredText ;
            return id;
        } else {


            return null;


        }
    }
    void setHubId(String id){

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("HUBID", MODE_PRIVATE).edit();
        editor.putString("id",id);
        editor.apply();

    }
}
