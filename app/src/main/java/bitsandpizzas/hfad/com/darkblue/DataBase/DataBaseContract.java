package bitsandpizzas.hfad.com.darkblue.DataBase;

import android.provider.BaseColumns;

public final class DataBaseContract {



    private DataBaseContract(){}

    public static class DataBaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "nodes";
        public static final String COLUMN_NAME_NODE_NAME = "nodename";
        public static final String COLUMN_NAME_NODE_ID = "nodeid";

    }




}
