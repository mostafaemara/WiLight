package bitsandpizzas.hfad.com.darkblue.NodeData;

public class Node {
    private int mNodeId;
    private String mNodeName;
    private int relay1;
    private int relay2;
    private int relay3;
    private int relay4;

    public Node(int mNodeId, String mNodeName, int relay1, int relay2, int relay3, int relay4) {
        this.mNodeId = mNodeId;
        this.mNodeName = mNodeName;
        this.relay1 = relay1;
        this.relay2 = relay2;
        this.relay3 = relay3;
        this.relay4 = relay4;
    }

    public Node(int mNodeId, String mNodeName) {
        this.mNodeId = mNodeId;
        this.mNodeName = mNodeName;
    }

    @Override
    public boolean equals(Object obj) {



        if(obj instanceof Node){
            if(((Node) obj).mNodeId==this.mNodeId){


                return  true;
            }else{


                return false;
            }

        }
        else{

            return false;
        }

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public int getRelay1() {
        return relay1;
    }

    public int getRelay2() {
        return relay2;
    }

    public int getRelay3() {
        return relay3;
    }

    public int getRelay4() {
        return relay4;
    }

    public int getmNodeId() {
        return mNodeId;
    }

    public String getmNodeName() {
        return mNodeName;
    }
}
