package com.atlatica.addressapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atlatica.addressapp.controller.AppConfig;
import com.atlatica.addressapp.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    EditText edFirstName, edLastName, edPhoneNumber, edEmail;
    Button btnUpdate;
    String firstName, lastName, phoneNumber, email;
    Toolbar tool;
    int id = 0;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        tool = (Toolbar) findViewById(R.id.edit_contact_my_toolbar);
        setSupportActionBar(tool);

        pDialog = new ProgressDialog(this);

        pDialog.setCancelable(false);

        edFirstName = findViewById(R.id.edit_contact_first_name);
        edLastName = findViewById(R.id.edit_contact_last_name);
        edEmail = findViewById(R.id.edit_contact_email);
        edPhoneNumber = findViewById(R.id.edit_contact_contact_number);

        btnUpdate = findViewById(R.id.button_update_contact);

        firstName = edFirstName.getText().toString().trim();
        lastName = edLastName.getText().toString().trim();
        email = edEmail.getText().toString().trim();
        phoneNumber = edPhoneNumber.getText().toString().trim();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getExtraIntent();
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    //getting extra info from previous activity
    public void getExtraIntent(){
        if(getIntent().hasExtra("id")) {
            id = getIntent().getIntExtra("id", 0);
            firstName = getIntent().getStringExtra("firstName");
            lastName = getIntent().getStringExtra("lastName");
            email = getIntent().getStringExtra("email");
            phoneNumber = getIntent().getStringExtra("contactNumber");

            if(id!=0){
                edFirstName.setText(firstName);
                edLastName.setText(lastName);
                edEmail.setText(email);
                edPhoneNumber.setText(phoneNumber);
            }
        }else{
            btnUpdate.setText("Add");
        }
    }


    //function to update contact
    public void updateContact(View view) {

        firstName = edFirstName.getText().toString().trim();
        lastName = edLastName.getText().toString().trim();
        email = edEmail.getText().toString().trim();
        phoneNumber = edPhoneNumber.getText().toString().trim();

        if(firstName.equalsIgnoreCase("")){
            edFirstName.setError("Please enter first name");
            edFirstName.requestFocus();
            return;
        }
        if(lastName.equalsIgnoreCase("")){
            edLastName.setError("Please enter last name");
            edLastName.requestFocus();
            return;
        }
        if(email.equalsIgnoreCase("") || !email.contains("@")){
            edEmail.setError("Please enter valid email id");
            edEmail.requestFocus();
            return;
        }
        if(phoneNumber.length()!=10){
            edPhoneNumber.setError("Please enter 10 digit mobile number");
            edPhoneNumber.requestFocus();
            return;
        }


        //Toast.makeText(getApplicationContext(), "moved on", Toast.LENGTH_SHORT).show();

        addUpdateContact(firstName,lastName,email,phoneNumber);
       // new addUpdateContact(EditContact.this, EditContact.this).execute();


    }
    private void addUpdateContact(final String firstname, final String lastname, final String email, final String phone) {


        // Tag used to cancel the request
        String tag_string_req = "req_store";

        pDialog.setMessage("Loading ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.BASE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj ;
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (!error) {



                        String success = jObj.getString("message");
                        // Launch login activity
                        Toast toast = Toast.makeText(getApplicationContext(),"Contact Saved successfully...",Toast.LENGTH_LONG);
                        Intent intent = new Intent(
                                EditActivity.this,
                                MainActivity.class);

                        startActivity(intent);

                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                         String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                    //we hide progress dialog here
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "No internet connection...please check and try again", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                //  map.put("X-Device-Info","Android FOO BAR");

                map.put("Accept", "application/json");

                return map;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("email", email);
                params.put("phone", phone);


                return params;
            }

        };

        //retry policy to avoid double insertions
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }


    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
