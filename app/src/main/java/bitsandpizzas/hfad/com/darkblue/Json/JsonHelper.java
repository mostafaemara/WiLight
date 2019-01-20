package bitsandpizzas.hfad.com.darkblue.Json;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import bitsandpizzas.hfad.com.darkblue.NodeData.NodeCommandMessege;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeInfoMessege;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeStateMessege;
import bitsandpizzas.hfad.com.darkblue.NodeData.NodeUtils;

public class JsonHelper {


    private String mJsonString;


    public JsonHelper(String mJsonString) {
        this.mJsonString = mJsonString;
    }



    public int getMessegeID() throws JSONException {

       int messegeId;

        JSONObject root;
        root=new JSONObject(mJsonString);
        messegeId=root.getInt(NodeUtils.MESSEGE_ID_KEY);


return  messegeId;


    }



    public NodeInfoMessege getNodeInfoMessege() throws JSONException {
        JSONObject root;
        String messegeId;
        int nodeId;
        String nodeState;

        int r1,r2,r3,r4;

           root=new JSONObject(mJsonString);

            nodeId=root.getInt(NodeUtils.NODE_ID_KEY);
            nodeState=root.getString(NodeUtils.NODE_STATE_KEY);
        r1=root.getInt(NodeUtils.NODE_RELAY_1_STATE_KEY);
        r2=root.getInt(NodeUtils.NODE_RELAY_2_STATE_KEY);
        r3=root.getInt(NodeUtils.NODE_RELAY_3_STATE_KEY);
        r4=root.getInt(NodeUtils.NODE_RELAY_4_STATE_KEY);


NodeInfoMessege nodeInfoMessege=new NodeInfoMessege(nodeId,nodeState,null,r1,r2,r3,r4);


return nodeInfoMessege;

    }







public static String  convertNodeCommandMessegeToJsonString(NodeCommandMessege nodeCommandMessege) throws JSONException {

    JSONObject root=new JSONObject();


    root.put(NodeUtils.MESSEGE_ID_KEY,NodeUtils.NODE_MESSEGE_COMMAND_ID_VALUE);
    root.put(NodeUtils.NODE_ID_KEY,nodeCommandMessege.getmNodeId());
    root.put(NodeUtils.NODE_COMMAND_KEY,nodeCommandMessege.getmNodeCommand());
    root.put(NodeUtils.NODE_RELAY_NUMBER_KEY,nodeCommandMessege.getmNodeRelayNumber());
    Log.e("JASON HELPER",root.toString());
return root.toString();

}




}
