package bitsandpizzas.hfad.com.darkblue.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bitsandpizzas.hfad.com.darkblue.NodeData.Node;
import bitsandpizzas.hfad.com.darkblue.R;

import static android.content.Context.MODE_PRIVATE;


public class SettingFragment extends Fragment {
    EditText editTextSignupEmail;
    EditText editTextSignupPassword;
    Button btnSignup;
    EditText editTextSigninEmail;
    EditText editTextSigninPassword;
    Button btnSignin;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference firebaseDatabase;
    private FirebaseDatabase mFirebaseDatabase;
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
editTextSignupEmail =view.findViewById(R.id.editetext_signupemail);
        editTextSignupPassword=view.findViewById(R.id.editetext_signuppassword);
        btnSignup=view.findViewById(R.id.btn_signup);
        editTextSigninEmail =view.findViewById(R.id.editetext_signinemail);
        editTextSigninPassword=view.findViewById(R.id.editetext_signinpassword);
        btnSignin=view.findViewById(R.id.btn_signin);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance().getReference();


        btnSignup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String email=editTextSignupEmail.getText().toString();
        String pass=editTextSignupPassword.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

firebaseUser=firebaseAuth.getCurrentUser();
firebaseDatabase.child(firebaseUser.getUid()).setValue(new Node(123,"Sasa")).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {


        if(task.isSuccessful()){
            Toast.makeText(getActivity(),"Successfully added to data base",Toast.LENGTH_LONG).show();

        }else{

            Toast.makeText(getActivity(),"Failed to add to data base",Toast.LENGTH_LONG).show();


        }

    }
});
                    Toast.makeText(getActivity(),"Signed up Successfully",Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(getActivity(),"Signed up Failed",Toast.LENGTH_LONG).show();
                }

            }
        });



    }
});
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editTextSigninEmail.getText().toString();
                String pass=editTextSigninPassword.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){


                            Toast.makeText(getActivity(),"Signed in Successfully",Toast.LENGTH_LONG).show();
                        }else{

                            Toast.makeText(getActivity(),"Signed in Failed",Toast.LENGTH_LONG).show();
                        }

                    }
                });




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

        super.onStart();

    }
}
