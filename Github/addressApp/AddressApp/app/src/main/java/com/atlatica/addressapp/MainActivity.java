package com.atlatica.addressapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atlatica.addressapp.controller.AppConfig;
import com.atlatica.addressapp.controller.AppController;
import com.atlatica.addressapp.helper.ContactAdapter;
import com.atlatica.addressapp.model.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactAdapter adapter;

    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tool = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(tool);

        //we get the intance of the recycler view from the xml here
        recyclerView = (RecyclerView) findViewById(R.id.contact_list_recyclerView);






        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.contact_list_refresh_layout);

        contactList = new ArrayList<>();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {


                fetchContacts();


            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                fetchContacts();
            }
        });



        fetchContacts();

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        MenuItem addContact = menu.findItem(R.id.add_contact);
        MenuItem firstNameAscendingSort = menu.findItem(R.id.action_sort_ascending_fn);
        MenuItem firstNameDescendingSort = menu.findItem(R.id.action_sort_descending_fn);
        MenuItem lastNameAscendingSort = menu.findItem(R.id.action_sort_ascending_ln);
        MenuItem lastNameDescendingSort = menu.findItem(R.id.action_sort_descending_ln);
        MenuItem contactNumberAscendingSort = menu.findItem(R.id.action_sort_ascending_cn);
        MenuItem contactNumberDescendingSort = menu.findItem(R.id.action_sort_descending_cn);

        //sorting code start
        firstNameAscendingSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Collections.sort(contactList, Contact.BY_FIRSTNAME_DESCENDING);
                Collections.sort(contactList, (Contact p1, Contact p2) -> p1.getFirstName().compareTo(p2.getFirstName()));
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        firstNameDescendingSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(contactList, Contact.BY_FIRSTNAME_ASCENDING);
                Collections.sort(contactList, (Contact p1, Contact p2) -> p2.getFirstName().compareTo(p1.getFirstName()));
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        lastNameAscendingSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(contactList, (Contact p1, Contact p2) -> p1.getLastName().compareTo(p2.getLastName()));
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        lastNameDescendingSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(contactList, (Contact p1, Contact p2) -> p2.getLastName().compareTo(p1.getLastName()));
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        contactNumberAscendingSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(contactList, (Contact p1, Contact p2) -> p1.getPhone().compareTo(p2.getPhone()));
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        contactNumberDescendingSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(contactList, (Contact p1, Contact p2) -> p2.getPhone().compareTo(p1.getPhone()));
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        //sorting code end
        addContact.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
                return false;
            }
        });

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.equalsIgnoreCase("")) {
                    adapter.getFilter().filter(query);
                }else{
                   fetchContacts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equalsIgnoreCase("")) {
                    adapter.getFilter().filter(newText);
                }else{
                    fetchContacts();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }





    //we fetch all the contact list using this function
    private void fetchContacts() {
//

        // Tag used to cancel the request
        String tag_string_req = "req_fetch contacts";

        pDialog.setMessage("Loading ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.BASE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                Contact contact = null;

                try {
                    JSONObject jObj ;
                    JSONArray cLists = null;
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (!error) {


                        cLists = jObj.getJSONArray("data");

                       // System.out.println(cLists.toString());


                        for(int i = 0; i < cLists.length(); i++) {

                            JSONObject jo = cLists.getJSONObject(i);

                            int id = jo.getInt("id");
                            String firstname = jo.getString("firstname");
                            String lastname = jo.getString("lastname");
                            String email = jo.getString("email");
                            String mobile = jo.getString("phone");


                             contact = new Contact(id,firstname,lastname,email,mobile);
                            contactList.add(contact);
                        }



                       adapter = new ContactAdapter(MainActivity.this, contactList);
                         recyclerView.setAdapter(adapter);
                          recyclerView.setHasFixedSize(true);
                         recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));



                        // System.out.println("response: "+response.toString());




                        adapter.notifyDataSetChanged();


                        // swipeRefreshLayout.setRefreshing(false);



                    } else {

                        String message  = jObj.getString("message");

                        Toast.makeText(getApplicationContext(),
                                message, Toast.LENGTH_LONG).show();
                    }
                    //we hide progress dialog here
                } catch (JSONException e) {
                    e.printStackTrace();

                }


                swipeRefreshLayout.setRefreshing(false);



            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG).show();
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
