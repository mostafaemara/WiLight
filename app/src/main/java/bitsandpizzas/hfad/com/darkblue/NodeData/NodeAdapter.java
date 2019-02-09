package bitsandpizzas.hfad.com.darkblue.NodeData;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
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


public class NodeAdapter extends ArrayAdapter<Node> {

    private Context mContext;
    private CloudMqttConnection mCloudMqttConnection;
    private LocalMqttConnection mLocalMqttConnection;

    private ArrayList<Node> mNodes;
    DataBaseHelper dataBaseHelper;
    public NodeAdapter(@NonNull Context context, @NonNull ArrayList<Node> nodes,LocalMqttConnection localMqttConnection,CloudMqttConnection cloudMqttConnection) {
        super(context,0, nodes);
        mContext=context;
        mNodes=nodes;
        mCloudMqttConnection=cloudMqttConnection;
        mLocalMqttConnection=localMqttConnection;

        dataBaseHelper=new DataBaseHelper(context);

    }

    static class ViewHolder {

        ToggleButton btn1;
        ToggleButton btn2;
        ToggleButton btn3;
        ToggleButton btn4;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem=convertView;
        if(listItem==null){


            listItem=LayoutInflater.from(mContext).inflate(R.layout.nodes_list_item,parent,false);
            ViewHolder holder=new ViewHolder();
            holder.btn1=listItem.findViewById(R.id.btn1);
            holder.btn2=listItem.findViewById(R.id.btn2);
            holder.btn3=listItem.findViewById(R.id.btn3);
            holder.btn4=listItem.findViewById(R.id.btn4);

            listItem.setTag(holder);

        }

        final ImageView pop=listItem.findViewById(R.id.pop);

        final ViewHolder holder= (ViewHolder) listItem.getTag();



        final Node currentNode=mNodes.get(position);
        holder.btn1.setOnCheckedChangeListener(null);
        holder.btn2.setOnCheckedChangeListener(null);
        holder.btn3.setOnCheckedChangeListener(null);
        holder.btn4.setOnCheckedChangeListener(null);
        if(currentNode.getRelay1()==1){

            holder.btn1.setChecked(true);

        }else{
            holder.btn1.setChecked(false);

        }
        if(currentNode.getRelay2()==1){

            holder.btn2.setChecked(true);
        }
        if(currentNode.getRelay3()==1){

            holder.btn3.setChecked(true);
        }
        if(currentNode.getRelay4()==1){

            holder.btn4.setChecked(true);
        }
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
        });  holder.btn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.getMenuInflater().inflate(R.menu.popupmenu,
                        popup.getMenu());
                popup.show();    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.editenodename:
                                final Dialog mDialog=new Dialog(getContext());
                                mDialog.setContentView(R.layout.dialog);
                                final EditText editText=mDialog.findViewById(R.id.newnodedname);
                                Button btn=mDialog.findViewById(R.id.dilogbtn);
                                editText.setEnabled(true);
                                btn.setEnabled(true);
                                mDialog.show();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dataBaseHelper.delete(currentNode);
                                        dataBaseHelper.add(dataBaseHelper,new Node(currentNode.getmNodeId(),editText.getText().toString()));
                                        Log.e("Dialog",editText.getText().toString());
                                        mDialog.cancel();
                                    }
                                });


                                break;


                            default:
                                break;
                        }

                        return true;
                    }
                });


            }

        });


        TextView nodename=listItem.findViewById(R.id.nodenametxt);
        nodename.setText(currentNode.getmNodeName());
        List<Node> databaseNodes=dataBaseHelper.getAllNodes();
        for(int i=0;i<databaseNodes.size();i++){
            Node databaseNode=databaseNodes.get(i);

            if(databaseNode.getmNodeId()==currentNode.getmNodeId()){

                nodename.setText(databaseNode.getmNodeName());
                break;

            }


        }




        return listItem;
    }
    void hanleOnState(int position,int btnNumber) throws JSONException, MqttException, UnsupportedEncodingException {




            Node node=mNodes.get(position);
            NodeCommandMessege nodeCommandMessege =new NodeCommandMessege(node.getmNodeId(), NodeUtils.NODE_COMMAND_ON_VALUE,btnNumber);

            String jsonString=JsonHelper.convertNodeCommandMessegeToJsonString(nodeCommandMessege);
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
