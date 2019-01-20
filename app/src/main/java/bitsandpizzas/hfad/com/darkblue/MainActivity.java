package bitsandpizzas.hfad.com.darkblue;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import bitsandpizzas.hfad.com.darkblue.Fragments.MainFragment;
import bitsandpizzas.hfad.com.darkblue.Fragments.SettingFragment;
import bitsandpizzas.hfad.com.darkblue.Fragments.WifiConfigFragment;
import bitsandpizzas.hfad.com.darkblue.Mqtt.CloudMqttConnection;
import bitsandpizzas.hfad.com.darkblue.Mqtt.LocalMqttConnection;

public class MainActivity extends AppCompatActivity{

    private ListView mainList;
    private LocalMqttConnection mLocalMqttConnection;
    private CloudMqttConnection mCloudMqttConnection;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    DrawerLayout mDrawerLayout;


    private class DrawerItemClickListener implements ListView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            selectItem(i);


        }
    }


    private void selectItem(int i) {

        Log.e("CLICK",Integer.toString(i));
        Fragment fragment = null;
        String title=null;

        switch (i){
            case(0):


title="Home";

               fragment = new MainFragment();

((MainFragment) fragment).setCloudMqttConnection(mCloudMqttConnection);
((MainFragment) fragment).setLocalMqttConnection(mLocalMqttConnection);


                break;
            case(1):
                title="WifiConfig";

                fragment=new WifiConfigFragment();
               // Intent z = new Intent(this, WifiConfigActivity.class);
              //  startActivity(z);
                break;
            case(2):
                title="Setting";
                fragment = new SettingFragment();

                default:
                    break;





        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.commit();


        getSupportActionBar().setTitle(getResources().getStringArray(R.array.mainlist)[i]);
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
mLocalMqttConnection=new LocalMqttConnection(this);
mCloudMqttConnection=new CloudMqttConnection(this);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.mDrawerLayout);
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.drawer_open_content_desc,R.string.drawer_close_content_desc);


        mainList=findViewById(R.id.mainlist);
        ArrayAdapter<String> songsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.mainlist));
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mainList.setAdapter(songsAdapter);
        mainList.setOnItemClickListener(new DrawerItemClickListener());
        selectItem(0);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(mDrawerToggle.onOptionsItemSelected(item)){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
