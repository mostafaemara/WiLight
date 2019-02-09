package bitsandpizzas.hfad.com.darkblue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bitsandpizzas.hfad.com.darkblue.NodeData.UserData;

public class LoginActivity extends AppCompatActivity {
    private static String MY_PREFS_NAME="loginstate";
    private static String MY_PREFS_KEY="login";
    EditText editTextSigninEmail;
    EditText editTextSigninPassword;
    Button btnSignin;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference firebaseDatabase;
    UserData userData;
    String username;
    String password;
    String host;
    String port;
    long hubid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance().getReference();
        editTextSigninEmail =findViewById(R.id.editetext_signinemail);
        editTextSigninPassword=findViewById(R.id.editetext_signinpassword);
        btnSignin=findViewById(R.id.btn_signin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email="wilights1234@wilights.com";
                String pass="123456";


                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
firebaseUser=firebaseAuth.getCurrentUser();

firebaseDatabase.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        hubid= (long) dataSnapshot.child("hubid").getValue();




    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
                            firebaseDatabase.child("serverconfig").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    username=dataSnapshot.child("username").getValue().toString();
                                    password=dataSnapshot.child("password").getValue().toString();
                                    host=dataSnapshot.child("host").getValue().toString();
                                   port=dataSnapshot.child("port").getValue().toString();

                                    Toast.makeText(getApplicationContext(),"username : "+username+" Password : "+password+"Hub id : "+hubid,Toast.LENGTH_LONG).show();
                                    UserData.setData(getApplicationContext(),username,password,host,port,hubid);

                                    Intent homeIntent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(homeIntent);



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }else{

                            Toast.makeText(getApplicationContext(),"Signed in Failed",Toast.LENGTH_LONG).show();
                        }

                    }
                });




            }
        });
    }

}
