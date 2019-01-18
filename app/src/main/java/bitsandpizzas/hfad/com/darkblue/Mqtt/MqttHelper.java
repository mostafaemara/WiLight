package bitsandpizzas.hfad.com.darkblue.Mqtt;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rafakob.nsdhelper.NsdHelper;
import com.rafakob.nsdhelper.NsdListener;
import com.rafakob.nsdhelper.NsdService;

import org.eclipse.paho.android.service.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import bitsandpizzas.hfad.com.darkblue.NodeData.NodeUtils;

import static android.content.Context.MODE_PRIVATE;
import static bitsandpizzas.hfad.com.darkblue.Mqtt.MqttHelper.CloudMqttHelper.cloudMqttAndroidClient;


public class MqttHelper {


    public MqttHelper(Context context) {
        new CloudMqttHelper(context);
        new LocalMqttHelper(context);

    }
public static class LocalMqttHelper implements NsdListener {
static NsdHelper mNsdHelper;
    final static String HUB_SERVICE_NAME="_._tcp.";
    final static String MY_PREFS_NAME="Setting";
    final static String MY_PREFS_KEY="serverip";

    public static MqttAndroidClient localMqttAndroidClient = null;

    final String LOCAL_CLIENT_ID =UUID.randomUUID().toString();

   private Context mContext;
   String getLocalServerIp() {

       SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
       String restoredText = prefs.getString(MY_PREFS_KEY, null);
       if (restoredText != null) {
           Log.e("serverIPRestored", restoredText);
           String host =  restoredText ;
           return host;
       } else {


           return null;


       }
   }
   void setLocalServerIp(String host){

       SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
       editor.putString(MY_PREFS_KEY,"tcp://" + host + ":1883");
       editor.apply();

   }

   void localMqttStart(String host){



       localMqttAndroidClient = new MqttAndroidClient(mContext, host, LOCAL_CLIENT_ID);
       localMqttAndroidClient.setCallback(new MqttCallbackExtended() {
           @Override
           public void connectComplete(boolean b, String s) {
               Log.w("MQTT", s+" CONNECTED OnConnectComplete");
           }

           @Override
           public void connectionLost(Throwable throwable) {
               Log.w("MQTT", " CONNECTION LOST OnConnectionLost");

           }

           @Override
           public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
               Log.w("MQTT", "Mqtt Messeage Arrived " +mqttMessage.toString());
           }

           @Override
           public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

               Log.w("MQTT", "Mqtt deliveryComplete " );

           }
       });
       connect();


   }
   void nsdServiceInit(){

       mNsdHelper = new NsdHelper(mContext,this);
       mNsdHelper.setLogEnabled(true);
       mNsdHelper.setAutoResolveEnabled(true);
       mNsdHelper.setDiscoveryTimeout(5);

   }
   void nsdServiceStart(){

       if(mNsdHelper!=null) {

           mNsdHelper.startDiscovery("_darkblue._tcp.");
       }



   }

    public LocalMqttHelper(Context context) {

        mContext=context;
        nsdServiceInit();

       String host= getLocalServerIp();
        if (host != null) {
            localMqttStart(host);


        }
            nsdServiceStart();











    }




    @Override
    public void onNsdRegistered(NsdService nsdService) {
        Log.e("NSD", " ONNSDRegistered");


    }

    @Override
    public void onNsdDiscoveryFinished() {
        if(localMqttAndroidClient==null){

           nsdServiceStart();

        }
        Log.e("NSD", " ONDiscovery fineshed");

    }

    @Override
    public void onNsdServiceFound(NsdService nsdService) {
        Log.e("NSDSERVICE", " SERVER Found");
    }

    @Override
    public void onNsdServiceResolved(NsdService nsdService) {

        Log.e("NSD", " ONNSDResolved");
        setLocalServerIp(nsdService.getHostIp());
localMqttStart( nsdService.getHostIp());

    }
    public static void setCallback(MqttCallbackExtended callback) {
       if(localMqttAndroidClient!=null) {
           localMqttAndroidClient.setCallback(callback);
       }
    }

    public static void connect(){
       if(localMqttAndroidClient!=null){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setKeepAliveInterval(10);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName("sasa");
        mqttConnectOptions.setPassword("asas".toCharArray());





    try {

        localMqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.w("MQTT", "Mqtt Connected onSuccess ");

                    localMqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());

                subscribeToTopic(NodeUtils.NODE_INFO_SUSCRIBE_TOPIC);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                mNsdHelper.startDiscovery("_darkblue._tcp.");

                //    Log.w("Mqtt", "Failed to connect to: " +cloudServerUri + exception.toString());
            }
        });


    } catch (MqttException ex) {
        ex.printStackTrace();
    }
}
    }


    private static void subscribeToTopic(String topic) {
        if (localMqttAndroidClient != null) {
            try {
                localMqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("MQTT", "Mqtt on Subscribed On Succes");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Subscribed fail!");
                    }
                });

            } catch (MqttException ex) {
                System.err.println("Exception whilst subscribing");
                ex.printStackTrace();
            }
        }
    }


    public static void publishMessage(

            @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException {
        if(localMqttAndroidClient!=null){
        byte[] encodedPayload = new byte[0];
        encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        message.setId(5866);
        message.setRetained(true);
        message.setQos(qos);
        localMqttAndroidClient.publish(topic, message);
    }
    }



    public static DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(true);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;







    }

    @Override
    public void onNsdServiceLost(NsdService nsdService) {
        Log.e("NSD", " ONNSD Service LOST");

    }

    @Override
    public void onNsdError(String s, int i, String s1) {
        Log.e("NSD", " ON Eroor");

    }
}
    public static class  CloudMqttHelper{





        public static MqttAndroidClient cloudMqttAndroidClient = null;


        static String cloudServerUri = "tcp://m20.cloudmqtt.com:15228";

        final String CLOUD_CLIENT_ID = UUID.randomUUID().toString();
        final String subscriptionTopic = "BASE";

        final static String username = "lrcpeafb";
        final static String password = "wOGwP2ntRAso";

        public CloudMqttHelper(Context context){
            cloudMqttAndroidClient = new MqttAndroidClient(context,cloudServerUri, CLOUD_CLIENT_ID);

            cloudMqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean b, String s) {
                    Log.w("MQTT", s+" CONNECTED OnConnectComplete");
                }

                @Override
                public void connectionLost(Throwable throwable) {
                    Log.w("MQTT", " CONNECTION LOST OnConnectionLost");

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    Log.w("MQTT", "Mqtt Messeage Arrived " +mqttMessage.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                    Log.w("MQTT", "Mqtt deliveryComplete " );

                }
            });
            connect();
        }

        public static void setCallback(MqttCallbackExtended callback) {
            if(cloudMqttAndroidClient!=null) {
                cloudMqttAndroidClient.setCallback(callback);
            }
        }

        public static void connect() {
            if (cloudMqttAndroidClient != null) {
                MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
                mqttConnectOptions.setKeepAliveInterval(10);
                mqttConnectOptions.setAutomaticReconnect(true);
                mqttConnectOptions.setCleanSession(true);
                mqttConnectOptions.setUserName(username);
                mqttConnectOptions.setPassword(password.toCharArray());





                try {

                    cloudMqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.w("MQTT", "Mqtt Connected onSuccess ");


                                cloudMqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());

                            subscribeToTopic(NodeUtils.NODE_INFO_SUSCRIBE_TOPIC);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                            Log.w("Mqtt", "Failed to connect to: " + cloudServerUri + exception.toString());
                            connect();
                        }
                    });


                } catch (MqttException ex) {
                    ex.printStackTrace();
                }
            }
        }


        private static void subscribeToTopic(String topic) {

            if (cloudMqttAndroidClient != null) {
                try {
                    cloudMqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.w("MQTT", "Mqtt on Subscribed On Succes");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.w("Mqtt", "Subscribed fail!");
                        }
                    });

                } catch (MqttException ex) {
                    System.err.println("Exception whilst subscribing");
                    ex.printStackTrace();
                }
            }
        }

        public static void publishMessage(
                                          @NonNull String msg, int qos, @NonNull String topic)
                throws MqttException, UnsupportedEncodingException {
            if (cloudMqttAndroidClient != null) {
                byte[] encodedPayload = new byte[0];
                encodedPayload = msg.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                message.setId(5866);
                message.setRetained(true);
                message.setQos(qos);
                cloudMqttAndroidClient.publish(topic, message);
            }
        }



       public static DisconnectedBufferOptions getDisconnectedBufferOptions() {
            DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
            disconnectedBufferOptions.setBufferEnabled(true);
            disconnectedBufferOptions.setBufferSize(100);
            disconnectedBufferOptions.setPersistBuffer(true);
            disconnectedBufferOptions.setDeleteOldestMessages(false);
            return disconnectedBufferOptions;







    }



    }

    public static void reset()  {

        try {
            MqttHelper.disconnect(LocalMqttHelper.localMqttAndroidClient);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        try {
            MqttHelper.disconnect(CloudMqttHelper.cloudMqttAndroidClient);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        if(LocalMqttHelper.localMqttAndroidClient!=null) {
    LocalMqttHelper.localMqttAndroidClient.close();
}
        if(CloudMqttHelper.cloudMqttAndroidClient!=null) {
            CloudMqttHelper.cloudMqttAndroidClient.close();
        }

        LocalMqttHelper.localMqttAndroidClient=null;
        CloudMqttHelper.cloudMqttAndroidClient=null;
        LocalMqttHelper.mNsdHelper.stopDiscovery();
        LocalMqttHelper.mNsdHelper.unregisterService();
        LocalMqttHelper.mNsdHelper=null;





    }
    public static  void disconnect(@NonNull MqttAndroidClient client)
            throws MqttException {
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d("DISCONNECT", "Successfully disconnected");
            }
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d("DISCONNECT", "Failed to disconnected " + throwable.toString());
            }
        });
    }

}