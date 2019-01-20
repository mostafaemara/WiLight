package bitsandpizzas.hfad.com.darkblue.Fragments;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.orm.SugarContext;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import bitsandpizzas.hfad.com.darkblue.Json.JsonHelper;
import bitsandpizzas.hfad.com.darkblue.Mqtt.CloudMqttConnection;
import bitsandpizzas.hfad.com.darkblue.Mqtt.LocalMqttConnection;
import bitsandpizzas.hfad.com.darkblue.NodeData.Node;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeAdapter;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeHandShakeMessege;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeInfoMessege;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeUtils;
import bitsandpizzas.hfad.com.darkblue.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


private CloudMqttConnection mCloudMqttConnection;
private LocalMqttConnection mLocalMqttConnection;


    ImageView cloudServerStat;
    ImageView localServerStat;
    ListView listView;
    NodeAdapter nodeAdapter;
    ArrayList<Node> nodes;
    Button refreshBtn;
    AnimationDrawable d=null;



    int count;


    public MainFragment() {

        // Required empty public constructor

    }

    @Override
    public void onStart() {
        super.onStart();
        final View view=getView();
        if(view!=null) {
            refreshBtn=view.findViewById(R.id.refreshbtn);
             d=(AnimationDrawable)refreshBtn.getCompoundDrawables()[0];

            localServerStat = view.findViewById(R.id.localserverstat);
            cloudServerStat = view.findViewById(R.id.cloudserverstat);
            listView = view.findViewById(R.id.nodelist);
            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              //    refreshBtn.setProgress(0,false);
d.start();
                    if(mLocalMqttConnection.getLocalMqttAndroidClient()!=null){

                        if( mLocalMqttConnection.getLocalMqttAndroidClient().isConnected()){

                            localServerStat.setImageResource(R.drawable.connected);
                            try {
                                mLocalMqttConnection.publishMessage(new NodeHandShakeMessege().getMessege(),0,NodeUtils.NODE_INF_PUBLISH_TOPIC);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                        }else{
                            localMqttStart();
                            localServerStat.setImageResource(R.drawable.disconnected);

                        }
                    }
                    if(mCloudMqttConnection.getCloudMqttAndroidClient()!=null){




                        if( mCloudMqttConnection.getCloudMqttAndroidClient().isConnected()){

                            cloudServerStat.setImageResource(R.drawable.connected);
                            try {
                                mCloudMqttConnection.publishMessage(new NodeHandShakeMessege().getMessege(),0,NodeUtils.NODE_INF_PUBLISH_TOPIC);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }else{

                            cloudMqttStart();
                            cloudServerStat.setImageResource(R.drawable.disconnected);

                        }

                    }

                }
            });

        }

        //SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
      //  String restoredText = prefs.getString(MY_PREFS_KEY, null);
      //  if (restoredText != null) {
       //  Log.e("serverIPRestored",restoredText);
           // String host = "tcp://" + restoredText + ":1883";


        // Log.e("DataBase",serverIp.ip);



        nodes = new ArrayList<Node>();
          localMqttStart();
          cloudMqttStart();
        checkServerStateTimer();

       // }else{


           // startSearchForLocalServer();
      //  }





        //cloudMqttStart();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);




        // Inflate the layout for this fragment
        return view;
    }

    private void checkServerStateTimer() {


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("TIMER", "Timer Running");

 handleClientsState();
 handler.postDelayed(this,1000 );


            //    cloudMqttCheck();



            }
        };
        handler.post(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("ONPause","Pauseed");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("ONStop","Stoped");
      //  if(cloudMqttHelper!=null){ cloudMqttHelper.getCloudMqttAndroidClient().close();}
       // if(localMqttHelper!=null){ localMqttHelper.getCloudMqttAndroidClient().close();}
     //   nsdHelper.stopDiscovery();

    }

    @Override
    public void onDestroy() {


        super.onDestroy();


        Log.e("ONDESTROY","Destroyed");



    }

   private void localMqttStart() {



        mLocalMqttConnection.setCallback(new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean b, String s) {
              localServerStat.setImageResource(R.drawable.connected);
                NodeHandShakeMessege nodeHandShakeMessege=new NodeHandShakeMessege();
                try {
                    mLocalMqttConnection.publishMessage(nodeHandShakeMessege.getMessege(),0,NodeUtils.NODE_INF_PUBLISH_TOPIC);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                localServerStat.setImageResource(R.drawable.disconnected);

                checkServerStateTimer();
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {


                handleMqttMessege(mqttMessage);

                Log.e("MQTT Debug", mqttMessage.toString());

                // dataReceived.setText(mqttMessage.toString());
                //publishMessage(localMqttHelper.mqttAndroidClient,"{"id":5,"topic":"LED","cmd":1,"number":1}",0,localMqttHelper.subscriptionTopic);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

    }

    private void cloudMqttStart() {

        mCloudMqttConnection.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                cloudServerStat.setImageResource(R.drawable.connected);


                NodeHandShakeMessege nodeHandShakeMessege=new NodeHandShakeMessege();
                try {
                   mCloudMqttConnection.publishMessage(nodeHandShakeMessege.getMessege(),0,NodeUtils.NODE_INF_PUBLISH_TOPIC);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {

                cloudServerStat.setImageResource(R.drawable.disconnected);

                checkServerStateTimer();
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {


                handleMqttMessege(mqttMessage);
                // Log.w("Debug", mqttMessage.toString());
                // dataReceived.setText(mqttMessage.toString());
                //publishMessage(localMqttHelper.mqttAndroidClient,"{"id":5,"topic":"LED","cmd":1,"number":1}",0,localMqttHelper.subscriptionTopic);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

    }






/*
    @Override
    public void onNsdServiceResolved(NsdService nsdService) {

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(MY_PREFS_KEY, nsdService.getHostIp());

        editor.apply();

        String host = "tcp://" + nsdService.getHostIp() + ":1883";

    //    localMqttStart();

    }*/






    void handleNodeInfoMessege(NodeInfoMessege nodeInfoMessege) {


        if (nodeInfoMessege.getmNodeState().equals("connect")) {
            int nodeId = nodeInfoMessege.getmNodeID();
            int r1=nodeInfoMessege.getRelay1State();
            int r2=nodeInfoMessege.getRelay2State();
            int r3=nodeInfoMessege.getRelay3State();
            int r4=nodeInfoMessege.getRelay4State();

            Node node = new Node(nodeId, "Node ID " + Integer.toString(nodeId),r1,r2,r3,r4);





            for (int i = 0; i < nodes.size(); i++) {


                if (nodes.get(i).getmNodeId()==nodeId) {
                    nodes.remove(i);


break;


                }





            }
            nodes.add(node);

            nodeAdapter = new NodeAdapter(getActivity(), nodes,mLocalMqttConnection,mCloudMqttConnection);
            listView.setAdapter(nodeAdapter);
        }
        if (nodeInfoMessege.getmNodeState().equals("disconnect")) {

            int nodeId = nodeInfoMessege.getmNodeID();

            Node node = new Node(nodeId, "Darkblue Node " + Integer.toString(nodeId));

            for (int i = 0; i < nodes.size(); i++) {

                if (nodes.get(i).getmNodeId()==nodeId) {


                    nodes.remove(i);

                }

            }
            Log.e("Node Remove", Integer.toString(nodes.indexOf(node)));
            nodes.remove(node);

            nodeAdapter = new NodeAdapter(getActivity(), nodes,mLocalMqttConnection,mCloudMqttConnection);
            listView.setAdapter(nodeAdapter);



        }


    }

    void handleMqttMessege(MqttMessage mqttMessage) throws JSONException {
d.stop();
        JsonHelper jsonHelper = new JsonHelper(mqttMessage.toString());
        int messegeId = jsonHelper.getMessegeID();

        switch (messegeId) {
            case (NodeUtils.NODE_INFO_MESSEGE_ID):
                NodeInfoMessege nodeInfoMessege = jsonHelper.getNodeInfoMessege();
                handleNodeInfoMessege(nodeInfoMessege);
                break;

            default:
                break;

        }
    }

    public void setCloudMqttConnection(CloudMqttConnection mCloudMqttConnection) {
        this.mCloudMqttConnection = mCloudMqttConnection;
    }

    public void setLocalMqttConnection(LocalMqttConnection mLocalMqttConnection) {
        this.mLocalMqttConnection = mLocalMqttConnection;
    }

    void handleClientsState() {

        MqttAndroidClient localMqttAndroidClient = mLocalMqttConnection.getLocalMqttAndroidClient();
        MqttAndroidClient cloudMqttAndroidClient = mCloudMqttConnection.getCloudMqttAndroidClient();
        if (localMqttAndroidClient != null) {
            if (localMqttAndroidClient.isConnected()) {

                localServerStat.setImageResource(R.drawable.connected);
                Log.e("LocalServer", "CONNECTED");

            } else {
                Log.e("LocalServer", "DISCONNECTED");
                localServerStat.setImageResource(R.drawable.disconnected);
            }
        }
        if (cloudMqttAndroidClient != null) {

            if (cloudMqttAndroidClient.isConnected()) {

                cloudServerStat.setImageResource(R.drawable.connected);
            } else {


                cloudServerStat.setImageResource(R.drawable.disconnected);

            }
        }
    }
}