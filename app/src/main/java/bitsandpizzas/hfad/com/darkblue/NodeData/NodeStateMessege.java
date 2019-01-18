package bitsandpizzas.hfad.com.darkblue.NodeData;

public class NodeStateMessege {




    private int mNodeID;

    private int relay1State;
    private int relay2State;
    private int relay3State;
    private int relay4State;

    public NodeStateMessege(int mNodeID, int relay1State, int relay2State, int relay3State, int relay4State) {
        this.mNodeID = mNodeID;

        this.relay1State = relay1State;
        this.relay2State = relay2State;
        this.relay3State = relay3State;
        this.relay4State = relay4State;
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

    public int getmNodeID() {
        return mNodeID;
    }



}
