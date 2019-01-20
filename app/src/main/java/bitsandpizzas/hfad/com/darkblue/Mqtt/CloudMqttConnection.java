package bitsandpizzas.hfad.com.darkblue.Mqtt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

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
import java.util.UUID;

import bitsandpizzas.hfad.com.darkblue.NodeData.NodeUtils;

public class CloudMqttConnection {

    private MqttAndroidClient mCloudMqttAndroidClient = null;



    static String cloudServerUri = "tcp://m20.cloudmqtt.com:15228";

    final String CLOUD_CLIENT_ID = UUID.randomUUID().toString();
    final String subscriptionTopic = "BASE";

    final static String username = "lrcpeafb";
    final static String password = "wOGwP2ntRAso";
    public CloudMqttConnection(Context context){
        mCloudMqttAndroidClient = new MqttAndroidClient(context,cloudServerUri, CLOUD_CLIENT_ID);

        mCloudMqttAndroidClient.setCallback(new MqttCallbackExtended() {
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

    public  void setCallback(MqttCallbackExtended callback) {
        if(mCloudMqttAndroidClient!=null) {
            mCloudMqttAndroidClient.setCallback(callback);
        }
    }

    public  void connect() {
        if (mCloudMqttAndroidClient != null) {
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setKeepAliveInterval(10);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setUserName(username);
            mqttConnectOptions.setPassword(password.toCharArray());





            try {

                mCloudMqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("MQTT", "Mqtt Connected onSuccess ");


                        mCloudMqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());

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


    private  void subscribeToTopic(String topic) {

        if (mCloudMqttAndroidClient != null) {
            try {
                mCloudMqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
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

    public  void publishMessage(
            @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException {
        if (mCloudMqttAndroidClient != null) {
            byte[] encodedPayload = new byte[0];
            encodedPayload = msg.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setId(5866);
            message.setRetained(true);
            message.setQos(qos);
            mCloudMqttAndroidClient.publish(topic, message);
        }
    }



    public  DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(true);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;







    }

    public MqttAndroidClient getCloudMqttAndroidClient() {
        return mCloudMqttAndroidClient;
    }
}


