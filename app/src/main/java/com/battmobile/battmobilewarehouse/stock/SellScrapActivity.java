package com.battmobile.battmobilewarehouse.stock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.common.SelectBrandActivity;
import com.battmobile.battmobilewarehouse.utility.SessionManager;
import com.battmobile.battmobilewarehouse.utility.UtilityConstraints;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class SellScrapActivity extends AppCompatActivity {
    TextView tvAdd, tvCancel;
    EditText etQuantity, etPrice;
    RelativeLayout loader;
    SessionManager sessionManager;
    TextView tvBrand, tvSize, tvQuantity, tvCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_scrap);
        Utils.setActionBarScreen("Sell Scrap Battery", getSupportActionBar());
        etQuantity = findViewById(R.id.et_quantity);
        etPrice = findViewById(R.id.et_price);
        tvBrand = findViewById(R.id.tv_brand);
        tvSize = findViewById(R.id.tv_size);
        tvQuantity = findViewById(R.id.tv_quantity);
        tvCost = findViewById(R.id.tv_cost);
        sessionManager = new SessionManager(SellScrapActivity.this);
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        tvAdd = findViewById(R.id.tv_add);
        tvCancel = findViewById(R.id.tv_cancel);
        showData();
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etQuantity.getText().toString().trim().length() == 0) {
                    etQuantity.setError("Invalid Quantity");
                    etQuantity.requestFocus();
                } else if (etPrice.getText().toString().trim().length() == 0) {
                    etPrice.setError("Invalid Price");
                    etPrice.requestFocus();
                } else {
                    addStockAPI(etQuantity.getText().toString(), etPrice.getText().toString());
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showData() {
        String sd = getIntent().getStringExtra("data");
        try {
            JSONObject mainObject = new JSONObject(sd);
            Timber.d("onVelocitySuccess: " + mainObject);
            if (!mainObject.getBoolean("error")) {
                JSONArray data = mainObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object = data.getJSONObject(i);
                    tvBrand.setText(object.getString("brand_name"));
                    tvSize.setText(object.getString("battery_size"));
                    tvQuantity.setText(object.getString("quantity"));
                    tvCost.setText(object.getString("cost_price"));
                }

            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addStockAPI(String quantity, String price) {
        loader.setVisibility(View.VISIBLE);
        Velocity.post(UtilityConstraints.UrlLocation.SELL_SCRAP)
                .withFormData("sold_by", sessionManager.getWAREHOUSE_ID())
                .withFormData("stock_id", getIntent().getStringExtra("id"))
                .withFormData("quantity", quantity)
                .withFormData("sold_price", price)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                setResult(Activity.RESULT_OK, new Intent());
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
