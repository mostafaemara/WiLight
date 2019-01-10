package bitsandpizzas.hfad.com.darkblue.NodeData;

import org.json.JSONException;
import org.json.JSONObject;

public class NodeHandShakeMessege {
    JSONObject root;

    public NodeHandShakeMessege() {

        root=new JSONObject();
        try {
            root.put(NodeUtils.MESSEGE_ID_KEY,NodeUtils.NODE_HANDSHAKE_MESSEGE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getMessege() {
        return root.toString();
    }
}
