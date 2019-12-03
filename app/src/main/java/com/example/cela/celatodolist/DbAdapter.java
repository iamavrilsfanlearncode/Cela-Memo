package com.example.cela.celatodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DbAdapter {
    public static final String KEY_ID = "_id";
    public static final String KEY_MEMO = "memo";
    public static final String KEY_DATE = "date";
    public static final String KEY_BGCOLOR = "bgcolor";
    public static final String KEY_REMIND = "remind";

    private static final String TABLE_NAME = "memo";
    private DbHelper mDbHelper;   //自訂資料庫
    private SQLiteDatabase mdb;  //
    private final Context m_Ctx;// 新增建構子
    private ContentValues values; //(對應createMemo) 新增要用到

    //對照DBHelper 第16行
    public DbAdapter(Context m_Ctx) {
        this.m_Ctx = m_Ctx;
        open();
    }
    public void open(){  //打開資料庫
        mDbHelper = new DbHelper(m_Ctx);
        mdb = mDbHelper.getWritableDatabase();  //可寫入
        Log.i("DB=",mdb.toString());//印出LOG 顯示資料存放檔案對應位置
    }

    //createMemo 函式被呼叫條件:按鈕新增點擊後 (menu add>按下確認btn_ok )



    public long createMemo(String date, String memo, String remind, String bgcolor ){
        long id = 0;
        try{
            values = new ContentValues();
            values.put(KEY_DATE, date);
            values.put(KEY_MEMO, memo);
            values.put(KEY_REMIND, remind);
            values.put(KEY_BGCOLOR, bgcolor);
            id = mdb.insert(TABLE_NAME,null,values);

        }catch(Exception e){
            e.printStackTrace();
        }finally {

            mdb.close();//關閉資料庫
            Toast.makeText(m_Ctx,"新增成功!", Toast.LENGTH_SHORT).show();//新增便條成功通知

        }
        return id ;
    }
    //Cursor是SQLiteDatabase內的方法，指向整組資料表
    public Cursor listMemos(){
        //query 針對MYSQL select方法
        Cursor mCursor = mdb.query(TABLE_NAME, new String[]{KEY_ID, KEY_DATE,KEY_MEMO, KEY_REMIND, KEY_BGCOLOR},
        null,null,null,null,null);
        if(mCursor != null){
            mCursor.moveToFirst();//指標移到第一筆作顯示
        }
        return mCursor;
        //接者去MainActivity作DbAdapter . ListView宣告
    }
    //設定編輯便條　step3.bundle接收
    //編輯便條多了index &  update
    public long updateMemo(int id, String date, String memo, String remind, String bgcolor){
        long update = 0;
        try{
            //將資料丟到contentValues
            ContentValues values = new ContentValues();
            values.put(KEY_DATE, date);
            values.put(KEY_MEMO, memo);
            values.put(KEY_REMIND, remind);
            values.put(KEY_BGCOLOR, bgcolor);
            update = mdb.update(TABLE_NAME, values, "_id=" + id,null);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Toast.makeText(m_Ctx, "成功更新一筆資料!",Toast.LENGTH_LONG).show();
        }
        return update;
    }

    //設定queryByID函式，接著去MainActivity
    public Cursor queryById(int item_id){
        //用Cursor 回傳ID值給query函式,selection 條件式
        Cursor  mCursor = mdb.query(TABLE_NAME, new String[] {KEY_ID, KEY_DATE,KEY_MEMO, KEY_REMIND, KEY_BGCOLOR},
                KEY_ID + "=" + item_id, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }
    //長按刪除便條 step.2  刪除函式   ID轉字串陣列
    public boolean deleteMemo(int id) {
        String[] args = {Integer.toString(id)};
        mdb.delete(TABLE_NAME, "_id= ?",args);
        return true;
    }
}// DbAdapter End
