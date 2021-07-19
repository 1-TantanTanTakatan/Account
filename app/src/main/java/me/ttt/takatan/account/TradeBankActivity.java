package me.ttt.takatan.account;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Calendar;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.model.BankAccountModel;
import me.ttt.takatan.account.db.model.CategoryModel;
import me.ttt.takatan.account.db.model.TradeModel;

public class TradeBankActivity extends AppCompatActivity {
    private final static String TAG = TradeBankActivity.class.getSimpleName();

    private CategoryModel categoryModel;
    private TradeModel tradeModel;
    private EditText input_date;
    private boolean input_deposit;
    private String input_debit;
    private EditText input_description;
    private EditText input_price;
    private ArrayAdapter<String> debitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        TextView title = findViewById(R.id.trade_title);
        TextView credit = findViewById(R.id.credit);
        title.setText(R.string.deposit_withdraw);
        credit.setText(R.string.trade);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        categoryModel = new BankAccountModel(db);
        tradeModel = new TradeModel(db);

        ArrayList<String> lst = new ArrayList<String>();
        lst.add(getString(R.string.trade_deposit));
        lst.add(getString(R.string.trade_withdraw));

        Spinner creditSpinner = findViewById(R.id.spinner_credit);
        creditSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                lst));
        creditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) {
                    input_deposit = true;
                } else if(position==1) {
                    input_deposit = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner debitSpinner = findViewById(R.id.spinner_debit);
        debitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>());
        debitSpinner.setAdapter(debitAdapter);
        debitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input_debit = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                input_debit = "";
            }
        });

        // 日付入力
        input_date =findViewById(R.id.input_date);
        input_date.setOnClickListener(v -> {
            Log.d(TAG, "onClick - date");

            // 日付入力ダイアログ表示
            final Calendar date = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TradeBankActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            //setした日付を取得して表示
                            input_date.setText(String.format("%d-%02d-%02d", year, month+1, dayOfMonth));
                        }
                    },
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DATE)
            );
            datePickerDialog.show();
        });

        // 内容、価格入力と登録ボタンの処理
        input_description = findViewById(R.id.input_description);
        input_price = findViewById(R.id.input_price);
        Button button = findViewById(R.id.trade_button);
        button.setOnClickListener(v -> {
            Log.d(TAG, "onClick - insert");
            String date = input_date.getText().toString().trim();
            String description = input_description.getText().toString().trim();
            String price = input_price.getText().toString().trim();

            if(!date.isEmpty() &&  !input_debit.isEmpty() && !price.isEmpty()) {
                if(input_deposit) {
                    tradeModel.insert(date, input_debit, "Cash", description, Integer.parseInt(price));
                } else {
                    tradeModel.insert(date, "Cash", input_debit, description, Integer.parseInt(price));
                }
                Toast.makeText(this, getString(R.string.finish_register), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        // スピナーの再設定
        debitAdapter.clear();
        categoryModel.getAll(debitAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}