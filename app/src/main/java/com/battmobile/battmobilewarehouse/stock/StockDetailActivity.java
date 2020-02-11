package com.battmobile.battmobilewarehouse.stock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.utility.UtilityConstraints;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class StockDetailActivity extends AppCompatActivity {
    int ALLOCATE_REQUEST_CODE = 101, EDIT_REQUEST_CODE = 102;
    TextView brand, size, quantity, cost, lower, retail, higher, source, name, contact, address, edit, sell;
    RelativeLayout loader, rlSourceContact, rlMain;
    String data;
    boolean isDetailChnaged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        loader = findViewById(R.id.loader);
        rlMain = findViewById(R.id.rl_main);
        rlSourceContact = findViewById(R.id.rl_source_contact);
        brand = findViewById(R.id.tv_brand);
        size = findViewById(R.id.tv_size);
        quantity = findViewById(R.id.tv_quantity);
        cost = findViewById(R.id.tv_cost);
        lower = findViewById(R.id.tv_lower);
        retail = findViewById(R.id.tv_retail);
        higher = findViewById(R.id.tv_higher);
        source = findViewById(R.id.tv_source_info);
        name = findViewById(R.id.tv_name);
        contact = findViewById(R.id.tv_contact);
        address = findViewById(R.id.tv_address);
        edit = findViewById(R.id.tv_edit);
        sell = findViewById(R.id.tv_sell);
        if (getIntent().getStringExtra("battery_type").equals("New")) {
            sell.setText("Allocation");
            Utils.setActionBarScreen("Battery Stock Detail", getSupportActionBar());
        } else {
            sell.setText("Sell");
            Utils.setActionBarScreen("Scrap Battery Detail", getSupportActionBar());
        }
        getDetailAPI();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(StockDetailActivity.this, EditStockActivity.class)
                        .putExtra("id", getIntent().getStringExtra("id"))
                        .putExtra("battery_type", getIntent().getStringExtra("battery_type"))
                        .putExtra("data", data), EDIT_REQUEST_CODE);
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("battery_type").equals("New")) {
                    startActivityForResult(new Intent(StockDetailActivity.this, AllocateStockActivity.class)
                            .putExtra("id", getIntent().getStringExtra("id")), ALLOCATE_REQUEST_CODE);
                } else {
                    startActivityForResult(new Intent(StockDetailActivity.this, SellScrapActivity.class)
                            .putExtra("id", getIntent().getStringExtra("id"))
                            .putExtra("data", data), ALLOCATE_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the request code is same as what is passed  here it is 2

        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getDetailAPI();
            isDetailChnaged = true;
        } else if (requestCode == ALLOCATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getDetailAPI();
            isDetailChnaged = true;
        }
    }

    private void getDetailAPI() {

        loader.setVisibility(View.VISIBLE);

        Velocity.get(UtilityConstraints.UrlLocation.STOCK_DETAIL)
                .withPathParam("id", getIntent().getStringExtra("id"))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        rlMain.setVisibility(View.VISIBLE);
                        try {
                            data = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(data);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                JSONArray dataArray = mainObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject object = dataArray.getJSONObject(i);
                                    brand.setText(object.getString("brand_name"));
                                    size.setText(object.getString("battery_size"));
                                    quantity.setText(object.getString("quantity"));
                                    cost.setText(object.getString("cost_price"));
                                    lower.setText(object.getString("lower_retail_sale_price"));
                                    retail.setText(object.getString("retail_sale_price"));
                                    higher.setText(object.getString("higher_retail_sale_price"));
                                    if (object.getString("battery_source").equals("Supplier")) {
                                        source.setText(object.getString("battery_source") + " Detail");
                                        name.setText(object.getString("supplier_name"));
                                        contact.setText(object.getString("supplier_phone"));
                                        address.setText(object.getString("supplier_address"));
                                        rlSourceContact.setVisibility(View.VISIBLE);
                                    } else {
                                        source.setText(object.getString("battery_source") + " Detail");
                                        name.setText(object.getString("technician_name"));
                                        rlSourceContact.setVisibility(View.GONE);

                                    }
                                }

                            } else {
                                showAlert(mainObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        loader.setVisibility(View.GONE);
                        Timber.e("onVelocityFailed: " + error.body);
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(StockDetailActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(StockDetailActivity.this)
                .setMessage(message)
                .setTitle("Alert!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getDetailAPI();
                    }
                }).create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isDetailChnaged)
            setResult(Activity.RESULT_OK, new Intent());
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isDetailChnaged)
            setResult(Activity.RESULT_OK, new Intent());
        finish();
    }
}
