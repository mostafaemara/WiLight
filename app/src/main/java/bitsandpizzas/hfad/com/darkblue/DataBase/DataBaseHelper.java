package bitsandpizzas.hfad.com.darkblue.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bitsandpizzas.hfad.com.darkblue.NodeData.Node;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database.db";

    public DataBaseHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);

    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DataBaseContract.DataBaseEntry.TABLE_NAME + " (" +
                    DataBaseContract.DataBaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DataBaseContract.DataBaseEntry.COLUMN_NAME_NODE_ID+" INTEGER ,"+
                    DataBaseContract.DataBaseEntry.COLUMN_NAME_NODE_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " +DataBaseContract.DataBaseEntry.TABLE_NAME;



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }


    public void add(DataBaseHelper db,Node node){
        SQLiteDatabase sq=db.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(DataBaseContract.DataBaseEntry.COLUMN_NAME_NODE_ID,node.getmNodeId());
        contentValues.put(DataBaseContract.DataBaseEntry.COLUMN_NAME_NODE_NAME,node.getmNodeName());
        long k=sq.insert(DataBaseContract.DataBaseEntry.TABLE_NAME,null,contentValues);



        Log.e("DataBase","Insert "+Long.toString(k));









    }

    public List<Node> getAllNodes() {
        List<Node> nodes = new ArrayList<Node>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DataBaseContract.DataBaseEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Node node = new Node(Integer.parseInt(cursor.getString(1)), cursor.getString(2));
                Log.e("DataBase","NodeName= "+node.getmNodeName()+"Node ID =" +Integer.toString(node.getmNodeId()));
                // Adding contact to list
                nodes.add(node);
            } while (cursor.moveToNext());
        }
        return  nodes;
    }

        public String get(DataBaseHelper db, int id){

        SQLiteDatabase sq=db.getReadableDatabase();
        String[] COLUMNS = {DataBaseContract.DataBaseEntry.COLUMN_NAME_NODE_NAME};
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor =
                sq.query(DataBaseContract.DataBaseEntry.TABLE_NAME, // a. table
                        COLUMNS, // b. column names
                        selection, // c. selections
                        selectionArgs, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null);

String s = null;
        if (cursor != null) {


            cursor.moveToFirst();
s=cursor.getString(0);

            Log.e("DataBase","get "+s);
        }

return s;
    }

    public  void delete(Node node){
        String[] string=new String[]{Integer.toString(node.getmNodeId())};

        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(DataBaseContract.DataBaseEntry.TABLE_NAME,DataBaseContract.DataBaseEntry.COLUMN_NAME_NODE_ID +"=?",string);



    }
}
