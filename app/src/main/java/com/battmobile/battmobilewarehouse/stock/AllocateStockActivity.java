package com.battmobile.battmobilewarehouse.stock;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.common.SelectBrandActivity;
import com.battmobile.battmobilewarehouse.utility.SessionManager;
import com.battmobile.battmobilewarehouse.utility.UtilityConstraints;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.rw.velocity.RequestBuilder;
import com.rw.velocity.Velocity;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class AllocateStockActivity extends AppCompatActivity {
    RelativeLayout rlSelectSource;
    TextView tvSourceTag, tvSource, tvAdd, tvCancel;
    EditText etQuantity;
    final int TECHNICIAN_REQUEST_CODE = 101;
    RelativeLayout loader;
    SessionManager sessionManager;
    String source_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_stock);
        Utils.setActionBarScreen("Stock Allocate", getSupportActionBar());
        rlSelectSource = findViewById(R.id.rl_select_source);
        tvSourceTag = findViewById(R.id.tv_source_tag);
        tvSource = findViewById(R.id.tv_source);
        etQuantity = findViewById(R.id.et_quantity);
        sessionManager = new SessionManager(AllocateStockActivity.this);
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        tvAdd = findViewById(R.id.tv_add);
        tvCancel = findViewById(R.id.tv_cancel);
        rlSelectSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllocateStockActivity.this, SelectBrandActivity.class)
                                .putExtra("for", "technician"),
                        TECHNICIAN_REQUEST_CODE);
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (source_id.trim().length() == 0) {
                    if (source_id.trim().length() == 0)
                        Toast.makeText(AllocateStockActivity.this, "Select Technician", Toast.LENGTH_SHORT).show();
                } else if (etQuantity.getText().toString().trim().length() == 0) {
                    etQuantity.setError("Invalid Quantity");
                    etQuantity.requestFocus();
                } else {
                    addStockAPI(etQuantity.getText().toString());
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

    private void addStockAPI(String quantity) {
        loader.setVisibility(View.VISIBLE);
        Velocity.post(UtilityConstraints.UrlLocation.ALLOCATE_STOCK)
                .withFormData("stock_id", getIntent().getStringExtra("id"))
                .withFormData("technician_id", source_id)
                .withFormData("assigned_quantity", quantity)
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {

                case TECHNICIAN_REQUEST_CODE:
                    source_id = data.getStringExtra("id");
                    tvSource.setText(data.getStringExtra("name"));
                    break;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
