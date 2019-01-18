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
        messegeId=root.getInt("messegeid");


return  messegeId;


    }



    public NodeInfoMessege getNodeInfoMessege() throws JSONException {
        JSONObject root;
        String messegeId;
        int nodeId;
        String nodeState;

        int r1,r2,r3,r4;

           root=new JSONObject(mJsonString);

            nodeId=root.getInt("nodeid");
            nodeState=root.getString("nodestate");
        r1=root.getInt("relay1");
        r2=root.getInt("relay2");
        r3=root.getInt("relay3");
        r4=root.getInt("relay4");


NodeInfoMessege nodeInfoMessege=new NodeInfoMessege(nodeId,nodeState,null,r1,r2,r3,r4);


return nodeInfoMessege;

    }

    public NodeStateMessege getNodeStateMessege() throws JSONException {
        JSONObject root;
        String messegeId;
        int nodeId;
      int r1,r2,r3,r4;



        root=new JSONObject(mJsonString);

        nodeId=root.getInt("nodeid");
       r1=root.getInt("relay1");
        r2=root.getInt("relay2");
        r3=root.getInt("relay3");
        r4=root.getInt("relay4");


        NodeStateMessege nodeStateMessege= new NodeStateMessege(nodeId,r1,r2,r3,r4);


        return nodeStateMessege;

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
