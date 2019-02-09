package bitsandpizzas.hfad.com.darkblue.NodeData;

public class NodeInfoMessege {


    private int mNodeID;
    private String mNodeState;
    private String mNodeModel;

    private int relay1State;
    private int relay2State;
    private int relay3State;
    private int relay4State;

    public NodeInfoMessege(int mNodeID, String mNodeState, String mNodeModel, int relay1State, int relay2State, int relay3State, int relay4State) {
        this.mNodeID = mNodeID;
        this.mNodeState = mNodeState;
        this.mNodeModel = mNodeModel;
        this.relay1State = relay1State;
        this.relay2State = relay2State;
        this.relay3State = relay3State;
        this.relay4State = relay4State;
    }

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
    public int getRelay1State() {
        return relay1State;
    }

    public int getRelay2State() {
        return relay2State;
    }

    public int getRelay3State() {
        return relay3State;
    }

    public int getRelay4State() {
        return relay4State;
    }
}
