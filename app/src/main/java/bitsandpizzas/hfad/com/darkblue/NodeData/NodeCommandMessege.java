package bitsandpizzas.hfad.com.darkblue.NodeData;

public class NodeCommandMessege {

    private int mNodeId;
    private int mMessegeId=NodeUtils.NODE_MESSEGE_COMMAND_ID_VALUE;
    private int mNodeCommand;
    private int mNodeRelayNumber;

    public NodeCommandMessege(int mNodeId, int mNodeCommand, int mNodeRelayNumber) {
        this.mNodeId = mNodeId;
        this.mNodeCommand = mNodeCommand;
        this.mNodeRelayNumber = mNodeRelayNumber;
    }

    public int getmNodeId() {
        return mNodeId;
    }

    public int getmNodeCommand() {
        return mNodeCommand;
    }

    public int getmNodeRelayNumber() {
        return mNodeRelayNumber;
    }

}
