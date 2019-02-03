package bitsandpizzas.hfad.com.darkblue.NodeData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import bitsandpizzas.hfad.com.darkblue.DataBase.DataBaseHelper;
import bitsandpizzas.hfad.com.darkblue.Json.JsonHelper;
import bitsandpizzas.hfad.com.darkblue.Mqtt.CloudMqttConnection;
import bitsandpizzas.hfad.com.darkblue.Mqtt.LocalMqttConnection;
import bitsandpizzas.hfad.com.darkblue.R;

public class EnhancedNodeAdapter extends RecyclerView.Adapter<EnhancedNodeAdapter.NodeViewHolder> {
    DataBaseHelper dataBaseHelper;

    private List<Node> mNodes;
    private LocalMqttConnection mLocalMqttConnection;
    private CloudMqttConnection mCloudMqttConnection;
    private Context mContext;

    public EnhancedNodeAdapter(@NonNull Context context, @NonNull List<Node> nodes, LocalMqttConnection localMqttConnection, CloudMqttConnection cloudMqttConnection) {

   mNodes=nodes;
mCloudMqttConnection=cloudMqttConnection;
mLocalMqttConnection=localMqttConnection;
mContext=context;
        dataBaseHelper=new DataBaseHelper(mContext);
    }

    public class NodeViewHolder extends RecyclerView.ViewHolder {
        public TextView nodeTitle;
        public ToggleButton btn1,btn2,btn3,btn4;

        public NodeViewHolder(View view) {
            super(view);
            nodeTitle = (TextView) view.findViewById(R.id.nodenametxt);
            btn1=(ToggleButton)view.findViewById(R.id.btn1);
            btn2=(ToggleButton)view.findViewById(R.id.btn2);
            btn3=(ToggleButton)view.findViewById(R.id.btn3);
            btn4=(ToggleButton)view.findViewById(R.id.btn4);

        }
    }

    @NonNull
    @Override
    public NodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.nodes_list_item,parent,false);
        NodeViewHolder nodeViewHolder=new NodeViewHolder(view);
        return nodeViewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull NodeViewHolder holder, final int position) {


        final Node node=mNodes.get(position);
       holder.btn1.setOnCheckedChangeListener(null);
      holder.btn2.setOnCheckedChangeListener(null);
     holder.btn3.setOnCheckedChangeListener(null);
      holder.btn4.setOnCheckedChangeListener(null);
        if(node.getRelay1()==1){

            holder.btn1.setChecked(true);

        }else{
            holder.btn1.setChecked(false);

        }
        if(node.getRelay2()==1){

            holder.btn2.setChecked(true);
        }
        if(node.getRelay3()==1){

            holder.btn3.setChecked(true);
        }
        if(node.getRelay4()==1){

            holder.btn4.setChecked(true);
        }

        holder.nodeTitle.setText(node.getmNodeName());

        holder.btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){

                    try {
                        hanleOnState(position,NodeUtils.NODE_COMMAND_RELAY_1_VALUE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }
                else{

                    try {
                        hanleOffState(position,NodeUtils.NODE_COMMAND_RELAY_1_VALUE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        holder.btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){
                    try {
                        hanleOnState(position,NodeUtils.NODE_COMMAND_RELAY_2_VALUE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }
                else{


                    try {
                        hanleOffState(position,NodeUtils.NODE_COMMAND_RELAY_2_VALUE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });   holder.btn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    try {
                        hanleOnState(position,NodeUtils.NODE_COMMAND_RELAY_3_VALUE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }
                else{


                    try {
                        hanleOffState(position,NodeUtils.NODE_COMMAND_RELAY_3_VALUE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        holder.btn4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {
                        hanleOnState(position,NodeUtils.NODE_COMMAND_RELAY_4_VALUE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }
                else{


                    try {
                        hanleOffState(position,NodeUtils.NODE_COMMAND_RELAY_4_VALUE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        List<Node> databaseNodes=dataBaseHelper.getAllNodes();
        for(int i=0;i<databaseNodes.size();i++){
            Node databaseNode=databaseNodes.get(i);

            if(databaseNode.getmNodeId()==node.getmNodeId()){

                holder.nodeTitle.setText(databaseNode.getmNodeName());
                break;

            }


        }



    }

    @Override
    public int getItemCount() {
        return mNodes.size();
    }
    void hanleOnState(int position,int btnNumber) throws JSONException, MqttException, UnsupportedEncodingException {




        Node node=mNodes.get(position);
        NodeCommandMessege nodeCommandMessege =new NodeCommandMessege(node.getmNodeId(), NodeUtils.NODE_COMMAND_ON_VALUE,btnNumber);

        String jsonString= JsonHelper.convertNodeCommandMessegeToJsonString(nodeCommandMessege);
        if(mCloudMqttConnection.getCloudMqttAndroidClient()!=null) {
            mCloudMqttConnection.publishMessage(jsonString, 0, NodeUtils.NODE_COMMAND_TOPIC);
        }
        if(mLocalMqttConnection.getLocalMqttAndroidClient()!=null) {
            mLocalMqttConnection.publishMessage(jsonString, 0, NodeUtils.NODE_COMMAND_TOPIC);
        }








    }



    void hanleOffState(int position,int btnNumber) throws JSONException, MqttException, UnsupportedEncodingException {




        Node node=mNodes.get(position);
        NodeCommandMessege nodeCommandMessege =new NodeCommandMessege(node.getmNodeId(), NodeUtils.NODE_COMMAND_OFF_VALUE,btnNumber);

        String jsonString=JsonHelper.convertNodeCommandMessegeToJsonString(nodeCommandMessege);
        if(mCloudMqttConnection.getCloudMqttAndroidClient()!=null) {
            mCloudMqttConnection.publishMessage(jsonString, 0, NodeUtils.NODE_COMMAND_TOPIC);
        }
        if(mLocalMqttConnection.getLocalMqttAndroidClient()!=null) {
            mLocalMqttConnection.publishMessage(jsonString, 0, NodeUtils.NODE_COMMAND_TOPIC);
        }









    }


}
