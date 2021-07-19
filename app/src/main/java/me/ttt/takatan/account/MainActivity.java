package me.ttt.takatan.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.ttt.takatan.account.db.AppDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private final int BANKACCOUNT = 1231;
    private final int ICCARD = 1232;
    private final int QRPAY = 1233;
    private final int CREDITCARD = 1234;
    private final int COST = 1235;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        // 画面遷移登録
        findViewById(R.id.asset).setOnClickListener(this);
        findViewById(R.id.pl).setOnClickListener(this);
        findViewById(R.id.bank_icon).setOnClickListener(this);
        findViewById(R.id.icCard_icon).setOnClickListener(this);
        findViewById(R.id.qr_icon).setOnClickListener(this);
        findViewById(R.id.creditCard_icon).setOnClickListener(this);
        findViewById(R.id.income_icon).setOnClickListener(this);
        findViewById(R.id.expense_icon).setOnClickListener(this);
        findViewById(R.id.deposit_withdraw).setOnClickListener(this);
        findViewById(R.id.chargeIC).setOnClickListener(this);
        findViewById(R.id.chargeQR).setOnClickListener(this);
        findViewById(R.id.payCreditCard).setOnClickListener(this);
        findViewById(R.id.registerIncome).setOnClickListener(this);
        findViewById(R.id.registerExpense).setOnClickListener(this);
        findViewById(R.id.lend_borrow).setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.asset:
                // 資産情報画面へ
                Log.d(TAG, "onClick - Asset");
                intent = new Intent(MainActivity.this, AssetActivity.class);
                startActivity(intent);
                break;
            case R.id.pl:
                // 収支閲覧画面へ
                Log.d(TAG, "onClick - PL");
                intent = new Intent(MainActivity.this, PLActivity.class);
                startActivity(intent);
                break;
            case R.id.bank_icon:
            case R.id.deposit_withdraw:
                // 銀行取引入力画面へ
                Log.d(TAG, "onClick - tradeBank");
                intent = new Intent(MainActivity.this, TradeBankActivity.class);
                startActivity(intent);
                break;
            case R.id.icCard_icon:
            case R.id.chargeIC:
                // ICカードへのチャージ入力画面へ
                Log.d(TAG, "onClick - chargeIC");
                intent = new Intent(MainActivity.this, TradeActivity.class);
                intent.putExtra(TradeActivity.STATE_EXTRA, ICCARD);
                startActivity(intent);
                break;
            case R.id.qr_icon:
            case R.id.chargeQR:
                // QRPayへのチャージ入力画面へ
                Log.d(TAG, "onClick - chargeQR");
                intent = new Intent(MainActivity.this, TradeActivity.class);
                intent.putExtra(TradeActivity.STATE_EXTRA, QRPAY);
                startActivity(intent);
                break;
            case R.id.creditCard_icon:
            case R.id.payCreditCard:
                // クレジットカードの支払い入力画面へ
                Log.d(TAG, "onClick - payCreditCard");
                intent = new Intent(MainActivity.this, TradeActivity.class);
                intent.putExtra(TradeActivity.STATE_EXTRA, CREDITCARD);
                startActivity(intent);
                break;
            case R.id.income_icon:
            case R.id.registerIncome:
                // 収入入力画面へ
                Log.d(TAG, "onClick - registerIncome");
                intent = new Intent(MainActivity.this, IncomeActivity.class);
                startActivity(intent);
                break;
            case R.id.expense_icon:
            case R.id.registerExpense:
                // 支出入力画面へ
                Log.d(TAG, "onClick - registerExpense");
                intent = new Intent(MainActivity.this, TradeActivity.class);
                intent.putExtra(TradeActivity.STATE_EXTRA, COST);
                startActivity(intent);
                break;
            case R.id.lend_borrow:
                // 貸し借り画面へ
                Log.d(TAG, "onClick - Lend & Borrow");
                intent = new Intent(MainActivity.this, LendBorrowActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        switch (item.getItemId()) {
            case R.id.menu_main_bankaccount:
                Log.d(TAG, "onOptionsItemSelected - BankAccount");
                intent.putExtra(EditActivity.STATE_EXTRA, BANKACCOUNT);
                startActivity(intent);
                return true;
            case R.id.menu_main_iccard:
                Log.d(TAG, "onOptionsItemSelected - ICCard");
                intent.putExtra(EditActivity.STATE_EXTRA, ICCARD);
                startActivity(intent);
                return true;
            case R.id.menu_main_qrpay:
                Log.d(TAG, "onOptionsItemSelected - QRPay");
                intent.putExtra(EditActivity.STATE_EXTRA, QRPAY);
                startActivity(intent);
                return true;
            case R.id.menu_main_creditcard:
                Log.d(TAG, "onOptionsItemSelected - CreditCard");
                intent.putExtra(EditActivity.STATE_EXTRA, CREDITCARD);
                startActivity(intent);
                return true;
            case R.id.menu_main_cost:
                Log.d(TAG, "onOptionsItemSelected - Cost");
                intent.putExtra(EditActivity.STATE_EXTRA, COST);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
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