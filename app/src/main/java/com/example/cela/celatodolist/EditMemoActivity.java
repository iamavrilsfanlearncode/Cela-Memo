package com.example.cela.celatodolist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//編輯新增便條
public class EditMemoActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtTitle;
    EditText edt_memo;
    Button btn_ok, btn_back;
    Spinner sp_color;
    Bundle bundle;
    String[] colors;
    int index;
    Intent intent;
    private DbAdapter dbAdapter;
    //設定顏色陣列
    ArrayList<ColorData> color_list = null;
    SpinnerAdapter spinnerAdapter;
    String selected_color;//紀錄目前所選顏色
    //宣告日期與memo變數
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String new_memo, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);
        intiView();
        dbAdapter = new DbAdapter(this);  //資料跟MainActivity 連動

        //宣告bundle & index
        // 設定編輯便條　step2.接收資料給bundle
        // 要去MainActivity  147行  增加頁面時,接收Extras值
        bundle = this.getIntent().getExtras();

        //判斷目前是否為編輯狀態
        if (bundle.getString("type").equals("edit")) {
            txtTitle.setText("編輯便條");
            index = bundle.getInt("item_id");
            //取得Cursor資料
            Cursor cursor = dbAdapter.queryById(index);
            edt_memo.setText(cursor.getString(cursor.getColumnIndexOrThrow("memo")));
            //Log.i("color=", cursor.getString(4));
            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                if (color_list.get(i).code.equals(cursor.getColumnIndexOrThrow("bgcolor"))) {
                    sp_color.setSelection(i);
                }
            }
        }
    }//onCreate  END

    private void intiView() {
        txtTitle = findViewById(R.id.txtTitle);
        edt_memo = findViewById(R.id.edtMemo);
        edt_memo.setOnClickListener(this);

        sp_color = findViewById(R.id.sp_colors);

        //使用陣列存取顏色 以做選取
        colors = getResources().getStringArray(R.array.colors);
        //spinnerAdapter = new SpinnerAdapter(this,colors);
        Log.i("color=", String.valueOf(colors));
        LinearLayout container = new LinearLayout(this);
        color_list = new ArrayList<ColorData>();
        color_list.add(new ColorData("Red", "#F55C66"));
        color_list.add(new ColorData("Green", "#00c7a4"));
        color_list.add(new ColorData("Blue", "#4b7bd8"));
        color_list.add(new ColorData("Orange", "#fc8200"));
        color_list.add(new ColorData("Cyan", "#18ffff"));
        spinnerAdapter = new com.example.cela.celatodolist.SpinnerAdapter(this, color_list);
        sp_color.setAdapter(spinnerAdapter);

        //OnItemSelectedListener 叫出方法
        sp_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                //取的選取的顏色代碼
                selected_color = color_list.get(position).code;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_ok = findViewById(R.id.btn_ok);
        btn_back = findViewById(R.id.btn_back);
        btn_ok.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }//intiView  END

    @Override
    //新增備忘 返回  switch寫法
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                //返回MainActivity
                intent = new Intent(EditMemoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            //判斷要新增還是編輯備忘
            //編輯便條後，條件判斷
            case R.id.btn_ok:
                currentTime = df.format(new Date(System.currentTimeMillis()));//取得並格式化目前時間日期
                new_memo = edt_memo.getText().toString();//用String印出
                // Log.i("memo=", new_memo);
                if (bundle.getString("type").equals("edit")) {
                    try {
                        //更新資料庫中的資料
                        dbAdapter.updateMemo(index, currentTime, new_memo, null, selected_color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //回到ShowActivity
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }
                } else {

                    //新增便條後，條件判斷
                    currentTime = df.format(new Date(System.currentTimeMillis()));
                    try {
                        //呼叫adapter的方法處理新增
                        dbAdapter.createMemo(currentTime, new_memo, null, selected_color);//呼叫dbAdapter的函式createMemo
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //回到列表
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }

                    break;


                }
             }
        }//onClick END
    }//onCreate End
