package bitsandpizzas.hfad.com.darkblue.Mqtt;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rafakob.nsdhelper.NsdHelper;
import com.rafakob.nsdhelper.NsdListener;
import com.rafakob.nsdhelper.NsdService;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.UUID;

import bitsandpizzas.hfad.com.darkblue.NodeData.NodeUtils;

import static android.content.Context.MODE_PRIVATE;

public class LocalMqttConnection implements NsdListener {


    private NsdHelper mNsdHelper;
    final static String LOCAL_CLIENT_ID = UUID.randomUUID().toString();
    final static String HUB_SERVICE_NAME = "_darkblue._tcp.";
    final static String MY_PREFS_NAME = "Setting";
    final static String MY_PREFS_KEY = "serverip";
    final static String SERVER_PORT = "1883";

    private MqttAndroidClient localMqttAndroidClient = null;


    private Context mContext;

    String getLocalServerIp() {

        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString(MY_PREFS_KEY, null);
        if (restoredText != null) {
            Log.e("serverIPRestored", restoredText);
            String host = restoredText;
            return host;
        } else {


            return null;


        }
    }

    void setLocalServerIp(String host) {

        SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(MY_PREFS_KEY, host);
        editor.apply();

    }

    void localMqttStart(String host) {


        localMqttAndroidClient = new MqttAndroidClient(mContext, "tcp://" + host + ":" + SERVER_PORT, LOCAL_CLIENT_ID);
        localMqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("MQTT", s + " CONNECTED OnConnectComplete");
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.w("MQTT", " CONNECTION LOST OnConnectionLost");

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("MQTT", "Mqtt Messeage Arrived " + mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                Log.w("MQTT", "Mqtt deliveryComplete ");

            }
        });
        connect();


    }

    void nsdServiceInit() {

        mNsdHelper = new NsdHelper(mContext, this);
        mNsdHelper.setLogEnabled(true);
        mNsdHelper.setAutoResolveEnabled(true);
        mNsdHelper.setDiscoveryTimeout(5);

    }

    void nsdServiceStart() {

        if (mNsdHelper != null) {

            mNsdHelper.startDiscovery(HUB_SERVICE_NAME);
        }


    }

    public LocalMqttConnection(Context context) {

        mContext = context;
        nsdServiceInit();

        String host = getLocalServerIp();
        if (host != null) {
            localMqttStart(host);


        } else {
            nsdServiceStart();
        }


    }


    @Override
    public void onNsdRegistered(NsdService nsdService) {
        Log.e("NSD", " ONNSDRegistered");


    }

    @Override
    public void onNsdDiscoveryFinished() {

        if (localMqttAndroidClient != null) {
            if (!localMqttAndroidClient.isConnected()) {

                nsdServiceStart();
            }

        }else{

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

        localMqttStart(nsdService.getHostIp());


    }

    public void setCallback(MqttCallbackExtended callback) {
        if (localMqttAndroidClient != null) {
            localMqttAndroidClient.setCallback(callback);
        }
    }

    public void connect() {
        if (localMqttAndroidClient != null) {
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
                        nsdServiceStart();

                        Log.e("Mqtt", "Faild to connect to local broker" + exception.toString());
                    }
                });


            } catch (MqttException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void subscribeToTopic(String topic) {
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


    public void publishMessage(

            @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException {
        if (localMqttAndroidClient != null) {
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

    public MqttAndroidClient getLocalMqttAndroidClient() {
        return localMqttAndroidClient;
    }
}

