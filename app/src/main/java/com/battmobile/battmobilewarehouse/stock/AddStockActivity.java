package com.battmobile.battmobilewarehouse.stock;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.common.SelectBrandActivity;
import com.battmobile.battmobilewarehouse.utility.UtilityConstraints;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.rw.velocity.RequestBuilder;
import com.rw.velocity.Velocity;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class AddStockActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvSourceDetail, tvBatteryDetail;
    LinearLayout rlSource, rlBattery;
    RadioButton rbSupplier, rbTechnician, rbLower, rbRetail, rbHigher;
    RadioGroup rgSource;
    RelativeLayout rlSelectSource, rlSelectBattery;
    TextView tvSourceTag, tvSource, tvBattery, tvAdd, tvCancel;
    EditText etContact, etAddress, etSize, etQuantity, etCost, etLower, etRetail, etHigher;
    final int SUPPLIER_REQUEST_CODE = 101, TECHNICIAN_REQUEST_CODE = 102, BATTERY_REQUEST_CODE = 103;
    String source_id = "", battery_id = "";
    RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        if (getIntent().getStringExtra("battery_type").equals("New")) {
            Utils.setActionBarScreen("Add Stock", getSupportActionBar());
        } else {
            Utils.setActionBarScreen("Add Scrap Battery", getSupportActionBar());
        }
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        tvSourceDetail = findViewById(R.id.tv_source_detail);
        tvBatteryDetail = findViewById(R.id.tv_battery_detail);
        rlSource = findViewById(R.id.rl_source);
        rlBattery = findViewById(R.id.rl_battery);
        rbSupplier = findViewById(R.id.rb_supplier);
        rbTechnician = findViewById(R.id.rb_technician);
        rgSource = findViewById(R.id.rg_source);
        rlSelectSource = findViewById(R.id.rl_select_source);
        tvSourceTag = findViewById(R.id.tv_source_tag);
        tvSource = findViewById(R.id.tv_source);
        etContact = findViewById(R.id.et_contact);
        etAddress = findViewById(R.id.et_address);
        rlSelectBattery = findViewById(R.id.rl_select_battery);
        tvBattery = findViewById(R.id.tv_battery);
        etSize = findViewById(R.id.et_size);
        etQuantity = findViewById(R.id.et_quantity);
        etCost = findViewById(R.id.et_cost);
        etLower = findViewById(R.id.et_lower);
        etRetail = findViewById(R.id.et_retail);
        etHigher = findViewById(R.id.et_higher);
        tvAdd = findViewById(R.id.tv_add);
        tvCancel = findViewById(R.id.tv_cancel);
        rbLower = findViewById(R.id.rb_lower);
        rbRetail = findViewById(R.id.rb_retail);
        rbHigher = findViewById(R.id.rb_higher);
        tvSourceDetail.setOnClickListener(this);
        tvBatteryDetail.setOnClickListener(this);
        rlSelectSource.setOnClickListener(this);
        rlSelectBattery.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        rgSource.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_supplier) {
                    tvSourceTag.setText("Choose Supplier");
                    tvSource.setText("Select Supplier");
                    etContact.setVisibility(View.VISIBLE);
                    etAddress.setVisibility(View.VISIBLE);
                    source_id = "";
                    etContact.setText("");
                    etAddress.setText("");
                } else {
                    tvSourceTag.setText("Choose Technician");
                    tvSource.setText("Select Technician");
                    etContact.setVisibility(View.GONE);
                    etAddress.setVisibility(View.GONE);
                    source_id = "";
                    etContact.setText("");
                    etAddress.setText("");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_source_detail:
                rlSource.setVisibility(View.VISIBLE);
                rlBattery.setVisibility(View.GONE);
                tvSourceDetail.setBackground(getResources().getDrawable(R.drawable.filled_round_corner_rect_color_primary));
                tvSourceDetail.setTextColor(getResources().getColor(R.color.white));
                tvBatteryDetail.setBackground(getResources().getDrawable(R.drawable.filled_round_corner_rect_grey));
                tvBatteryDetail.setTextColor(getResources().getColor(R.color.black));

                break;
            case R.id.tv_battery_detail:
                rlSource.setVisibility(View.GONE);
                rlBattery.setVisibility(View.VISIBLE);
                tvSourceDetail.setBackground(getResources().getDrawable(R.drawable.filled_round_corner_rect_grey));
                tvSourceDetail.setTextColor(getResources().getColor(R.color.black));
                tvBatteryDetail.setBackground(getResources().getDrawable(R.drawable.filled_round_corner_rect_color_primary));
                tvBatteryDetail.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.rl_select_source:
                if (rbSupplier.isChecked())
                    startActivityForResult(new Intent(AddStockActivity.this, SelectBrandActivity.class)
                                    .putExtra("for", "supplier"),
                            SUPPLIER_REQUEST_CODE);
                else
                    startActivityForResult(new Intent(AddStockActivity.this, SelectBrandActivity.class)
                                    .putExtra("for", "technician"),
                            TECHNICIAN_REQUEST_CODE);
                break;
            case R.id.rl_select_battery:
                startActivityForResult(new Intent(AddStockActivity.this, SelectBrandActivity.class)
                                .putExtra("for", "battery"),
                        BATTERY_REQUEST_CODE);
                break;
            case R.id.tv_add:
                if (source_id.trim().length() == 0) {
                    if (rbSupplier.isChecked())
                        Toast.makeText(AddStockActivity.this, "Select Supplier", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(AddStockActivity.this, "Select Technician", Toast.LENGTH_SHORT).show();
                } else if (battery_id.trim().length() == 0) {
                    Toast.makeText(AddStockActivity.this, "Select Battery", Toast.LENGTH_SHORT).show();
                } else if (etQuantity.getText().toString().trim().length() == 0) {
                    etQuantity.setError("Invalid Quantity");
                    etQuantity.requestFocus();
                } else if (etCost.getText().toString().trim().length() == 0) {
                    etCost.setError("Invalid Cost Price");
                    etCost.requestFocus();
                } else if (etLower.getText().toString().trim().length() == 0) {
                    etLower.setError("Invalid Lower Price");
                    etLower.requestFocus();
                } else if (etRetail.getText().toString().trim().length() == 0) {
                    etRetail.setError("Invalid Retail Price");
                    etRetail.requestFocus();
                } else if (etHigher.getText().toString().trim().length() == 0) {
                    etHigher.setError("Invalid Higher Price");
                    etHigher.requestFocus();
                } else
                    addStockAPI(etQuantity.getText().toString(),
                            etCost.getText().toString(),
                            etLower.getText().toString(),
                            etRetail.getText().toString(),
                            etHigher.getText().toString(),
                            (rbLower.isChecked()) ? "Lower Retail" : (rbRetail.isChecked()) ? "Retail" : (rbHigher.isChecked()) ? "Higher Retail" : "");
                break;
            case R.id.tv_cancel:
                finish();
                break;
            default:
                break;
        }
    }


    private void addStockAPI(String quantity, String cost_price, String lower_retail_sale_price, String retail_sale_price,
                             String higher_retail_sale_price, String sale_price_type) {
        loader.setVisibility(View.VISIBLE);
        RequestBuilder requestBuilder = Velocity.post(UtilityConstraints.UrlLocation.ADD_STOCK);
        if (rbSupplier.isChecked()) {
            requestBuilder.withFormData("supplier_id", source_id);
            requestBuilder.withFormData("battery_source", "Supplier");
        } else {
            requestBuilder.withFormData("technician_id", source_id);
            requestBuilder.withFormData("battery_source", "Technician");
        }
        requestBuilder.withFormData("battery_type", getIntent().getStringExtra("battery_type"))
                .withFormData("product_id", battery_id)
                .withFormData("quantity", quantity)
                .withFormData("cost_price", cost_price)
                .withFormData("lower_retail_sale_price", lower_retail_sale_price)
                .withFormData("retail_sale_price", retail_sale_price)
                .withFormData("higher_retail_sale_price", higher_retail_sale_price)
                .withFormData("sale_price_type", sale_price_type)
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
                case SUPPLIER_REQUEST_CODE:
                    source_id = data.getStringExtra("id");
                    tvSource.setText(data.getStringExtra("name"));
                    etContact.setText(data.getStringExtra("contact"));
                    etAddress.setText(data.getStringExtra("address"));
                    etContact.setVisibility(View.VISIBLE);
                    etAddress.setVisibility(View.VISIBLE);
                    break;
                case TECHNICIAN_REQUEST_CODE:
                    source_id = data.getStringExtra("id");
                    tvSource.setText(data.getStringExtra("name"));
                    etContact.setText(data.getStringExtra("contact"));
                    etAddress.setText(data.getStringExtra("address"));
                    etContact.setVisibility(View.GONE);
                    etAddress.setVisibility(View.GONE);
                    break;
                case BATTERY_REQUEST_CODE:
                    battery_id = data.getStringExtra("id");
                    tvBattery.setText(data.getStringExtra("name") + " - " + data.getStringExtra("contact"));
                    etSize.setText(data.getStringExtra("address"));
                    break;
            }
        }
    }
}