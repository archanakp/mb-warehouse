package com.battmobile.battmobilewarehouse.brands;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.utility.UtilityConstraints;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONException;
import org.json.JSONObject;

public class AddBrandsActivity extends AppCompatActivity {
    Menu menu;
    String TAG = AddBrandsActivity.class.getSimpleName();
    EditText etName;
    RelativeLayout loader;
    RadioButton rbActive, rbInActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brands);
        Utils.setActionBarScreen("Add Brands", getSupportActionBar());
        loader = findViewById(R.id.loader);
        Velocity.initialize(3);
        etName = findViewById(R.id.et_name);
        rbActive = findViewById(R.id.rb_active);
        rbInActive = findViewById(R.id.rb_inactive);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_tick, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_tick) {
            if (Utils.isNetworkConnected(AddBrandsActivity.this)) {
                if (etName.getText().toString().trim().length() == 0) {
                    etName.setError("Invalid Name");
                    etName.requestFocus();
                } else {
                    loader.setVisibility(View.VISIBLE);
                    addProductAPI();
                }

            } else
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addProductAPI() {
        Velocity.post(UtilityConstraints.UrlLocation.ADD_BRAND)
                .withFormData("brand_name", etName.getText().toString())
                .withFormData("status", rbActive.isChecked() ? "1" : "0")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {
                                setResult(Activity.RESULT_OK, new Intent());
                                finish();
                            }
                            Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        loader.setVisibility(View.GONE);
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
