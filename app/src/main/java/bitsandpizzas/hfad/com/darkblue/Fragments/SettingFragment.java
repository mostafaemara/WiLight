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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bitsandpizzas.hfad.com.darkblue.R;

import static android.content.Context.MODE_PRIVATE;


public class SettingFragment extends Fragment {
    EditText editText;
    Button btn;
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
editText=view.findViewById(R.id.txthubid);
btn=view.findViewById(R.id.btn_enterid_fragment_setting);

btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(),"DONE",Toast.LENGTH_LONG).show();

        setHubId(editText.getText().toString());

    }
});
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

    @Override
    public void onStart() {
        Toast.makeText(getActivity(),getHubId(),Toast.LENGTH_LONG).show();
        super.onStart();
    }
}
