package bitsandpizzas.hfad.com.darkblue.NodeData;

public class NodeInfoMessege {


    private int mNodeID;
    private String mNodeState;
    private String mNodeModel;

    public NodeInfoMessege(int mNodeID, String mNodeState, String mNodeModel) {
        this.mNodeID = mNodeID;
        this.mNodeState = mNodeState;
        this.mNodeModel = mNodeModel;
    }


    public int getmNodeID() {
        return mNodeID;
    }

    public String getmNodeModel() {
        return mNodeModel;
    }

    public String getmNodeState() {
        return mNodeState;
    }
}
