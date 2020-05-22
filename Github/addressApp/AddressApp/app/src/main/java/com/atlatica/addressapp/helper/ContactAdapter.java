package com.atlatica.addressapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atlatica.addressapp.EditActivity;
import com.atlatica.addressapp.R;
import com.atlatica.addressapp.controller.AppConfig;
import com.atlatica.addressapp.controller.AppController;
import com.atlatica.addressapp.model.Contact;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements Filterable {

    //the context in which the list will be generated
    private Context context;

    //we will use a list to store our contacts here
    private List<Contact> contactList;
    private List<Contact> fContactList;

    //getting the contact and context here
    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.fContactList = contactList;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key = charSequence.toString();
                if(Key.isEmpty()) {
                    fContactList = contactList;
                } else {
                    List<Contact> fList = new ArrayList<>();
                    for(Contact element: contactList) {
                        if(element.getFirstName().toLowerCase().contains(Key.toLowerCase())){
                            fList.add(element);
                        }
                        if(element.getLastName().toLowerCase().contains(Key.toLowerCase())){
                            fList.add(element);
                        }
                    }
                    fContactList = fList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = fContactList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                contactList = (List<Contact>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //we inflate our view here
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_row, parent, false);
        //LayoutInflater inflater = LayoutInflater.from(context);
       // View view = inflater.inflate(R.layout.contact_list_row,null);

        return new ContactAdapter.ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {

        //getting contact at the specified position
        final Contact contact = contactList.get(position);

        //we bind each data to its corresponding holder;
        //withing the try and catch statement we try to fix the image in the holder and catch
        //any potential errors we might face;
//        try {
//            Glide.with(context).load(contact.getImage())
//                    .placeholder(R.drawable.ic_account)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.contactImage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        holder.firstName.setText(contact.getFirstName());
        holder.lastName.setText(contact.getLastName());
        holder.email.setText(contact.getEmail());
        holder.phone.setText(contact.getPhone());

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteContact(context, contact.getId()).deleteContact(contact.getId());
            }
        }); //end delete onclick listener

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this function moves as to the contact detail (to help us edit or just view details
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("id", contact.getId());
                //intent.putExtra("image", contact.getImage());
                intent.putExtra("firstName", contact.getFirstName());
                intent.putExtra("lastName", contact.getLastName());
                intent.putExtra("email", contact.getEmail());
                intent.putExtra("phone", contact.getPhone());

                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }



    public class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView firstName;
        TextView lastName;
        TextView email;
        TextView phone;
        ImageView deleteImg;
      //  AppCompatImageView contactImage;

        public ContactViewHolder(View itemView) {
            super(itemView);

         //   contactImage = itemView.findViewById(R.id.list_logo);
            firstName = itemView.findViewById(R.id.list_first_name);
            lastName = itemView.findViewById(R.id.list_last_name);
            email = itemView.findViewById(R.id.list_email);
            phone = itemView.findViewById(R.id.list_number);
            deleteImg = itemView.findViewById(R.id.delete_logo);
        }
    }


    private class DeleteContact  {
        private ProgressDialog progressDialog;
        android.app.AlertDialog.Builder builder;
        Context mContext;
        int id = 0;

        public DeleteContact(Context mContext, int id) {
            progressDialog = new ProgressDialog(mContext);
            builder = new android.app.AlertDialog.Builder(mContext, R.style.DialogTheme);
            this.mContext = mContext;
            this.id = id;
        }



        //volley implementation of deleting the contacts
        private void deleteContact(int id) {

            // Tag used to cancel the request
            String tag_string_req = "req_login";

            progressDialog.setMessage("Deleting.....");
            showDialog();
            //making of request takes place here
            StringRequest strReq = new StringRequest(Request.Method.DELETE,
                    AppConfig.BASE_URL+"/"+id, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("success");

                        Log.d("Response", ""+response);


                        // Check for error node in json
                        if (!error) {




                            String Msg = jObj.getString("message");

                            Toast.makeText(mContext,
                                    Msg, Toast.LENGTH_LONG).show();




                        } else {

                            String Msg = jObj.getString("message");

                            Toast.makeText(mContext,
                                    Msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    Toast.makeText(mContext,
                            "No connectivity....please check and try again", Toast.LENGTH_LONG).show();
                    hideDialog();
                    //progress.hideProgress();

                }
            }) {

                //we add our header elements here
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<String, String>();
                  //  map.put("X-Device-Info","Android FOO BAR");

                    map.put("Accept", "application/json");

                    return map;
                }



                //we dont need this function now
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();


                    return params;
                }

            };

            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    6000,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }



        private void showDialog() {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        private void hideDialog() {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }

    }
}
