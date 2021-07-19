package me.ttt.takatan.account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import me.ttt.takatan.account.Bluetooth.ConnectActivity;
import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.model.CategoryModel;
import me.ttt.takatan.account.db.model.CounterpartModel;
import me.ttt.takatan.account.db.model.TradeModel;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class LendBorrowActivity extends AppCompatActivity {
    private final static String TAG = LendBorrowActivity.class.getSimpleName();

    private final int SEND = 1231;
    private final int RECEIVE = 1232;

    private TradeModel tradeModel;
    private CounterpartModel counterpartModel;
    private CategoryModel categoryModel;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lb);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        tradeModel = new TradeModel(db);
        counterpartModel = new CounterpartModel(db);
        categoryModel = new CategoryModel(db);

        // 貸し借りリストの初期設定
        GridView lbList = (GridView) findViewById(R.id.lb_list);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>());
        category = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>());
        lbList.setAdapter(adapter);
        lbList.setOnItemClickListener((parent, view, pos, id) -> {
            Log.d(TAG, "onClick - item");
            String name = (String) parent.getItemAtPosition(pos-pos%2);
            final TextView text = new TextView(this);
            text.setText("To" + name);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.lend_borrow)
                    .setMessage(R.string.select_method)
                    .setView(text)
                    .setPositiveButton(R.string.lend, (dialog, which) -> {
                        Log.d(TAG, "onClickItem - lend");
                        EditText editText = new EditText(this);
                        editText.setText(name);
                        lendSelect(editText, false);
                    })
                    .setNegativeButton(R.string.borrow, (dialog, which) -> {
                        Log.d(TAG, "onClickItem - borrow");
                        EditText editText = new EditText(this);
                        editText.setText(name);
                        borrowSelect(editText, false);
                    })
                    .setNeutralButton("back", (dialog2, which2) -> {})
                    .show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.lb, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final EditText editText = new EditText(this);
        switch (item.getItemId()) {
            case R.id.menu_lb_borrow:
                Log.d(TAG, "onOptionsItemSelected - Borrow");
                borrowSelect(editText, true);
                // リストの再設定
                adapter.clear();
                counterpartModel.setSumList(adapter);
                return true;
            case R.id.menu_lb_lend:
                Log.d(TAG, "onOptionsItemSelected - Lend");
                lendSelect(editText, true);
                // リストの再設定
                adapter.clear();
                counterpartModel.setSumList(adapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void sendError() {
        Toast.makeText(this, getString(R.string.error_duplicate), Toast.LENGTH_SHORT).show();
    }

    class ErrorHandler extends Handler {
        private final WeakReference<LendBorrowActivity> activityRef;

        ErrorHandler(LendBorrowActivity activity) {
            super(Looper.getMainLooper());
            activityRef = new WeakReference<>(activity);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            LendBorrowActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            sendError();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        // リストの再設定
        adapter.clear();
        counterpartModel.setSumList(adapter);
        categoryModel.getAll(category);
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

    private void lendSelect(EditText editText, boolean insert) {
        String before = editText.getText().toString().trim();
        new AlertDialog.Builder(this)
                .setTitle(R.string.lend)
                .setMessage(R.string.select_method)
                .setView(editText)
                .setPositiveButton(R.string.send_debt, (dialog, which) -> {
                    String name = editText.getText().toString().trim();
                    final TextView text = new TextView(this);
                    text.setText("To" + name);
                    lendSendDebt(name, text, before, insert);
                })
                .setNegativeButton(R.string.register_only, (dialog, which) -> {
                    String name = editText.getText().toString().trim();
                    final TextView text = new TextView(this);
                    text.setText("To" + name);
                    lendOnlyMine(name, text, before, insert);
                })
                .setNeutralButton("back", (dialog, which) -> {})
                .show();
    }

    private void borrowSelect(EditText editText, boolean insert) {
        String before = editText.getText().toString().trim();
        new AlertDialog.Builder(this)
                .setTitle(R.string.borrow)
                .setMessage(R.string.select_method)
                .setView(editText)
                .setPositiveButton(R.string.receive_debt, (dialog, which) -> {
                    String name = editText.getText().toString().trim();
                    final TextView text = new TextView(this);
                    text.setText("To" + name);
                    if(!name.isEmpty()){
                        borrowReceiveDebt(name, before, insert);
                    }
                })
                .setNegativeButton(R.string.register_only, (dialog, which) -> {
                    String name = editText.getText().toString().trim();
                    final TextView text = new TextView(this);
                    text.setText("To" + name);
                    if(!name.isEmpty()) {
                        borrowOnlyMine(name, text, before, insert);
                    }
                })
                .setNeutralButton("back", (dialog, which) -> {})
                .show();
    }

    private void lendSendDebt(String name, TextView text, String before, boolean insert) {
        Log.d(TAG, "onClick - Lend - Send");
        final EditText input_date = new EditText(this);
        final EditText input_method = new EditText(this);
        final EditText input_price = new EditText(this);
        AlertDialog.Builder alertDialog = createDialog(text, R.string.trade_methodlend, input_date, input_method, input_price);
        alertDialog.setTitle(R.string.lend)
                .setPositiveButton("OK", (dialog, which) -> {
                    Log.d(TAG, "onClick - Lend - Send - insert");
                    String date = input_date.getText().toString().trim();
                    String method = input_method.getText().toString().trim();
                    String price = input_price.getText().toString().trim();
                    if(!date.isEmpty() && !method.isEmpty() && !price.isEmpty()) {
                        if(insert) {
                            counterpartModel.insert(name, new ErrorHandler(this));
                        } else if(!before.equals(name)) {
                            counterpartModel.update(before, name, new ErrorHandler(this));
                        }
                        Intent intent = new Intent(LendBorrowActivity.this, ConnectActivity.class);
                        intent.putExtra(ConnectActivity.STATE_EXTRA, SEND);
                        intent.putExtra(ConnectActivity.STATE_DATE, date);
                        intent.putExtra(ConnectActivity.STATE_NAME, name);
                        intent.putExtra(ConnectActivity.STATE_METHOD, method);
                        intent.putExtra(ConnectActivity.STATE_PRICE, price);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("back", (dialog, which) -> {})
                .show();
    }

    private void lendOnlyMine(String name, TextView text, String before, boolean insert) {
        Log.d(TAG, "onClick - Lend - Only");
        final EditText input_date = new EditText(this);
        final EditText input_method = new EditText(this);
        final EditText input_price = new EditText(this);
        AlertDialog.Builder alertDialog = createDialog(text, R.string.trade_methodlend, input_date, input_method, input_price);
        alertDialog.setTitle(R.string.lend)
                .setPositiveButton("OK", (dialog, which) -> {
                    Log.d(TAG, "onClick - Lend - Only - insert");
                    String date = input_date.getText().toString().trim();
                    String method = input_method.getText().toString().trim();
                    String price = input_price.getText().toString().trim();
                    if(!date.isEmpty() && !method.isEmpty() && !price.isEmpty()) {
                        if(insert) {
                            counterpartModel.insert(name, new ErrorHandler(this));
                        } else if(!before.equals(name)) {
                            counterpartModel.update(before, name, new ErrorHandler(this));
                        }
                        tradeModel.insert(date, name, method, getString(R.string.lend), Integer.parseInt(price));
                        Toast.makeText(this, getString(R.string.finish_register), Toast.LENGTH_SHORT).show();

                        // リストの再設定
                        adapter.clear();
                        counterpartModel.setSumList(adapter);
                    }
                })
                .setNegativeButton("back", (dialog, which) -> {})
                .show();
    }

    private void borrowReceiveDebt(String name, String before, boolean insert) {
        Log.d(TAG, "onClick - Borrow - Receive");
        final EditText input_method = new EditText(this);
        final Spinner spinner = new Spinner(this);
        spinner.setAdapter(category);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input_method.setText((CharSequence) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                input_method.setText("");
            }
        });
        new AlertDialog.Builder(this)
                .setTitle(R.string.borrow)
                .setMessage(R.string.trade_methodborrow)
                .setView(spinner)
                .setPositiveButton(R.string.receive_debt, (dialog, which) -> {
                    String method = input_method.getText().toString().trim();
                    if(!method.isEmpty()){
                        if(insert) {
                            counterpartModel.insert(name, new ErrorHandler(this));
                        } else if(!before.equals(name)) {
                            counterpartModel.update(before, name, new ErrorHandler(this));
                        }
                        Intent intent = new Intent(LendBorrowActivity.this, ConnectActivity.class);
                        intent.putExtra(ConnectActivity.STATE_EXTRA, RECEIVE);
                        intent.putExtra(ConnectActivity.STATE_NAME, name);
                        intent.putExtra(ConnectActivity.STATE_METHOD, method);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("back", (dialog, which) -> {})
                .show();
    }

    private void borrowOnlyMine(String name, TextView text, String before, boolean insert) {
        Log.d(TAG, "onClick - Borrow - Only");
        final EditText input_date = new EditText(this);
        final EditText input_method = new EditText(this);
        final EditText input_price = new EditText(this);
        AlertDialog.Builder alertDialog = createDialog(text, R.string.trade_methodborrow,input_date, input_method, input_price);
        alertDialog.setTitle(R.string.borrow)
                .setPositiveButton("OK", (dialog, which) -> {
                    Log.d(TAG, "onClick - Borrow - Only - insert");
                    String date = input_date.getText().toString().trim();
                    String method = input_method.getText().toString().trim();
                    String price = input_price.getText().toString().trim();
                    if(!date.isEmpty() && !method.isEmpty() && !price.isEmpty()) {
                        if(insert) {
                            counterpartModel.insert(name, new ErrorHandler(this));
                        } else if(!before.equals(name)) {
                            counterpartModel.update(before, name, new ErrorHandler(this));
                        }
                        tradeModel.insert(date, method, name, getString(R.string.borrow), Integer.parseInt(price));
                        Toast.makeText(this, getString(R.string.finish_register), Toast.LENGTH_SHORT).show();

                        // リストの再設定
                        adapter.clear();
                        counterpartModel.setSumList(adapter);
                    }
                })
                .setNegativeButton("back", (dialog, which) -> {})
                .show();
    }

    private AlertDialog.Builder createDialog(TextView text, int method, EditText input_date, EditText input_method, EditText input_price) {
        input_date.setOnClickListener(v -> {
            Log.d(TAG, "onClick - date");

            // 日付入力ダイアログ表示
            final Calendar date = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
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
        input_date.setHint(R.string.select_date);
        final TextView text2 = new TextView(this);
        text2.setText(method);
        final Spinner spinner = new Spinner(this);
        spinner.setAdapter(category);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input_method.setText((CharSequence) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                input_method.setText("");
            }
        });
        input_price.setInputType(TYPE_CLASS_NUMBER);
        input_price.setHint(R.string.price);

        //外枠とパーツの作成
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(text);
        layout.addView(input_date);
        layout.addView(text2);
        layout.addView(spinner);
        layout.addView(input_price);

        return new AlertDialog.Builder(this)
                .setMessage(R.string.input_price)
                .setView(layout);
    }
}