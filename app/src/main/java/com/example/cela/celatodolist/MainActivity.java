package com.example.cela.celatodolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DbAdapter dbAdapter;
    TextView no_memo;
    ListView list_memos;
    int item_id;
    private Intent intent;
    ListAdater dataSimpleAdapter;
    ArrayList<Memo> memos = new ArrayList<>();
    Cursor cursor;
    private AlertDialog dialog = null;
    AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbAdapter = new DbAdapter(this);
        Log.i("dbCount=",String.valueOf(dbAdapter.listMemos().getCount()));
        list_memos = findViewById(R.id.list_memos);

        //判斷目前是否有聯絡人資料並設定顯示元件，如果是0，就顯示「目前無資料」


        displayList();//新增方法 ，表示取得資料，先設定才能取得setOnItemClickListener
        list_memos = findViewById(R.id.list_memos);

        // 設定編輯便條　step1.取得資料
        //setOnItemClickListener　layout點擊item_view右方鉛筆圖案，跳入編輯便條頁
        list_memos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item_position = parent.getItemAtPosition(position);
                //找到位置
                cursor.move(position);
                //item_id 要先宣告 ，要取得item欄位索引值
                item_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

                intent = new Intent();
                //資料較少使用putExtra，edit值  給名字type
                intent.putExtra("item_id", item_id);
                intent.putExtra("type","edit");
                intent.setClass(MainActivity.this, EditMemoActivity.class);
                startActivity(intent);
            }
        });

        //宣告AlertDialog 彈出訊息, 宣告 Builder
        //長按刪除便條 step.1  接者去DbAdapter
        list_memos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.move(position);
                item_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                dialog = builder.create();
                dialog.show();

                return true;
            }
        });
        //長按刪除便條 step.3
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("訊息")
                .setMessage("確定刪除此便條?")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    //設定確定按鈕
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        boolean is_deleted = dbAdapter.deleteMemo(item_id);
                        if(is_deleted) {
                            Toast.makeText(MainActivity.this, "已刪除!", Toast.LENGTH_SHORT).show();
                            memos = new ArrayList<>();
                            //displayList表示刪除資料後，重整顯示目前list
                            displayList();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    //設定取消按鈕
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

    }//onCreate END

    //使用Cursor資料，丟入ArrayList，再放入ListView
    private void displayList() {
        cursor = dbAdapter.listMemos();
        if(cursor != null){
            cursor.moveToFirst();
        }
        if(cursor.moveToFirst()){//moveToFirst對應DbAdaper
            do{
                memos.add(new Memo(
                        cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("memo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("bgcolor")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("remind"))));
             }while(cursor.moveToNext());
        }

        cursor.moveToFirst();
        dataSimpleAdapter = new ListAdater(this,memos);
        list_memos.setAdapter(dataSimpleAdapter);
    }

//右鍵>建構子>override   叫出onCreateOptionsMenu . onOptionsItemSelected 方法,套用在menu上

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);


    }//onCreate End

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Intent i = new Intent(this,EditMemoActivity.class);
                i .putExtra("type","add");//接收Extra ADD值
                startActivity(i);
                break;
        }

          return super.onOptionsItemSelected(item);

    }//onOptionsItemSelected END
}//MainActivity End