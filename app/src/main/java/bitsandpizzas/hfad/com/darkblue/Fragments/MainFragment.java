package bitsandpizzas.hfad.com.darkblue.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import com.orm.SugarContext;
import com.rafakob.nsdhelper.NsdHelper;
import com.rafakob.nsdhelper.NsdListener;
import com.rafakob.nsdhelper.NsdService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import bitsandpizzas.hfad.com.darkblue.Json.JsonHelper;
import bitsandpizzas.hfad.com.darkblue.MainActivity;
import bitsandpizzas.hfad.com.darkblue.Mqtt.MqttHelper;
import bitsandpizzas.hfad.com.darkblue.NodeData.Node;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeAdapter;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeHandShakeMessege;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeInfoMessege;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeStateMessege;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeUtils;
import bitsandpizzas.hfad.com.darkblue.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {





    ImageView cloudServerStat;
    ImageView localServerStat;
    ListView listView;
    NodeAdapter nodeAdapter;
    ArrayList<Node> nodes;


    int count;


    public MainFragment() {

        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        View view=getView();
        if(view!=null) {
            SugarContext.init(getActivity());
            localServerStat = view.findViewById(R.id.localserverstat);
            cloudServerStat = view.findViewById(R.id.cloudserverstat);
            listView = view.findViewById(R.id.nodelist);

        }

        //SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
      //  String restoredText = prefs.getString(MY_PREFS_KEY, null);
      //  if (restoredText != null) {
       //  Log.e("serverIPRestored",restoredText);
           // String host = "tcp://" + restoredText + ":1883";
        MqttHelper mqttHelper = new MqttHelper(getActivity());

        // Log.e("DataBase",serverIp.ip);


        checkServerStateTimer();
        nodes = new ArrayList<Node>();
          //localMqttStart();

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
private void startSearchForLocalServer(){




}
    private void checkServerStateTimer() {


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("TIMER", "Timer Running");








                if(MqttHelper.LocalMqttHelper.localMqttAndroidClient!=null){

                 if( MqttHelper.LocalMqttHelper.localMqttAndroidClient.isConnected()){

                     localServerStat.setImageResource(R.drawable.connected);


                 }else{
                     localMqttStart();
                     localServerStat.setImageResource(R.drawable.disconnected);

                 }
                }else{


                    handler.post(this );


                }
                if(MqttHelper.CloudMqttHelper.cloudMqttAndroidClient!=null){




                    if( MqttHelper.CloudMqttHelper.cloudMqttAndroidClient.isConnected()){

                        cloudServerStat.setImageResource(R.drawable.connected);


                    }else{

                        cloudMqttStart();
                        cloudServerStat.setImageResource(R.drawable.disconnected);

                    }

                }else{

                    handler.post(this );
                }

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

MqttHelper.reset();
        Log.e("ONDESTROY","Destroyed");



    }

   private void localMqttStart() {



        MqttHelper.LocalMqttHelper.setCallback(new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean b, String s) {
              localServerStat.setImageResource(R.drawable.connected);
                NodeHandShakeMessege nodeHandShakeMessege=new NodeHandShakeMessege();
                try {
                    MqttHelper.LocalMqttHelper.publishMessage(nodeHandShakeMessege.getMessege(),0,NodeUtils.NODE_INF_PUBLISH_TOPIC);
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

        MqttHelper.CloudMqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                cloudServerStat.setImageResource(R.drawable.connected);


                NodeHandShakeMessege nodeHandShakeMessege=new NodeHandShakeMessege();
                try {
                    MqttHelper.CloudMqttHelper.publishMessage(nodeHandShakeMessege.getMessege(),0,NodeUtils.NODE_INF_PUBLISH_TOPIC);
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

            nodeAdapter = new NodeAdapter(getActivity(), nodes);
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

            nodeAdapter = new NodeAdapter(getActivity(), nodes);
            listView.setAdapter(nodeAdapter);



        }


    }

    void handleMqttMessege(MqttMessage mqttMessage) throws JSONException {

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
}