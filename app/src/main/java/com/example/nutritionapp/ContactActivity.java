package com.example.nutritionapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutritionapp.model.App;
import com.example.nutritionapp.model.Contact;
import com.example.nutritionapp.model.MyFireStoreHandler;

import java.util.List;
import java.util.stream.Collectors;

import static android.Manifest.permission.CALL_PHONE;

public class ContactActivity extends AppCompatActivity {

    private static String TAG = "ContactActivity";

    // Create recycle view
    private TextView tvTitle;
    private RecyclerView rvContacts;

    private String email;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Intent intent = getIntent();
        String title =  intent.getStringExtra("title");

        if (title == null) {
            title = "";
        }

        tvTitle = findViewById(R.id.tvContactsTitle);
        tvTitle.setText(title);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        String fullName = sharedPreferences.getString("fullName", "guest");

        rvContacts = findViewById(R.id.rvContacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        MyFireStoreHandler.getAllContacts(
                contactList -> {
                            if (contactList==null) {
                                Toast.makeText(ContactActivity.this, "Failed to get contacts", Toast.LENGTH_LONG).show();
                                return;
                            }
                            contactList =
                                contactList.stream().filter(
                                        contact->!contact.getEmail().equalsIgnoreCase(email)).
                                            collect(Collectors.toList());

                            boolean isAdmin = App.isAdmin(email);

                            RecyclerView.Adapter adapter = new ContactsRecyclerViewvAdapter(contactList, isAdmin,
                                    new MyOnRowControlClickListener() {
                                @Override
                                public void onRowControlClick(Contact contact,
                                                              boolean emailClicked, boolean phoneClicked,
                                                              boolean btnChatClicked,
                                                              boolean chkboxPaidClicked, boolean chkboxPaidState, boolean btnProgressClicked) {
                                    Toast.makeText(ContactActivity.this, "Selected: " + contact.getEmail(), Toast.LENGTH_SHORT).show();
                                    if (emailClicked) {
                                        Intent i = new Intent(Intent.ACTION_SEND);
                                        i.setType("message/rfc822");
                                        i.putExtra(intent.EXTRA_EMAIL, new String[]{contact.getEmail()});
                                        i.putExtra(Intent.EXTRA_SUBJECT, "היי");
                                        i.putExtra(Intent.EXTRA_TEXT, "מה שלומך?");
                                        try {
                                            startActivity(Intent.createChooser(i, "Send mail..."));
                                        }
                                        catch(android.content.ActivityNotFoundException ex) {
                                            Toast.makeText(ContactActivity.this, "אין אפליקציה של מייל", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else if (phoneClicked) {
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:" + contact.getPhone()));
                                        if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) ==
                                                PackageManager.PERMISSION_GRANTED) {
                                            ContactActivity.this.startActivity(intent);
                                        }
                                        else {
                                            requestPermissions(new String[]{CALL_PHONE}, 1);
                                        }

                                    }
                                    else if (btnChatClicked) {
                                        Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
                                        intent.putExtra("email of chat owner", contact.getEmail());
                                        intent.putExtra("fullname of chat owner", contact.getFullName());
                                        startActivity(intent);
                                    }
                                    else if (chkboxPaidClicked) {
                                        MyFireStoreHandler.updateUserPaidStatus(contact.getEmail(), chkboxPaidState, new MyFireStoreHandler.RequestCallback() {
                                            @Override
                                            public void OnResponse(Boolean success, String message) {
                                                if (success) {
                                                    Toast.makeText(ContactActivity.this, contact.getEmail() + " paid state updated", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(ContactActivity.this, "Failed updating " + contact.getEmail() + " paid state", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else if (btnProgressClicked) {
                                        Intent intent = new Intent(ContactActivity.this, StatusActivity.class);
                                        intent.putExtra("email of progress owner", contact.getEmail());
                                        intent.putExtra("fullname of progress owner", contact.getFullName());
                                        startActivity(intent);
                                    }

                                }
                            });
                            rvContacts.setAdapter(adapter);
                    });
    }

    interface MyOnRowControlClickListener {
        void onRowControlClick(Contact contact,
                               boolean emailClicked, boolean phoneClicked,
                               boolean btnChatClicked, boolean chkboxPaidClicked, boolean chkboxPaidState,
                               boolean btnProgressClicked);
    }

    static class ContactsRecyclerViewvAdapter extends RecyclerView.Adapter<ContactsRecyclerViewvAdapter.ContactsViewHolder>  {

        private List<Contact> contacts;
        boolean isAdmin;
        private MyOnRowControlClickListener myOnRowControlClickListener;

        public ContactsRecyclerViewvAdapter(List<Contact> contacts, boolean isAdmin, MyOnRowControlClickListener myOnItemClickListener) {
            this.contacts = contacts;
            this.isAdmin = isAdmin;
            this.myOnRowControlClickListener = myOnItemClickListener;
        }
        @NonNull
        @Override
        public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
            Contact contact = contacts.get(position);
            holder.tvFullName.setText(contact.getFullName());
            holder.tvEmail.setText(Html.fromHtml("<a href=\"mailto:" + contact.getEmail() + "\">" + contact.getEmail() + "</a>"));
            holder.tvPhone.setText(Html.fromHtml("<a href=\"tel:" + contact.getPhone() + "\">" + contact.getPhone() + "</a>"));
            // Change color of a row based on position
            if(position%2==0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#99CCFF"));
            }

            if (!isAdmin) {
                holder.layoutContactControls.setVisibility(View.GONE);
            }

            holder.chkboxContactPaid.setChecked(contact.isPaid());

            holder.tvEmail.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          myOnRowControlClickListener.onRowControlClick(contact,
                                  true, false,
                                  false, false, false, false);
                      }
            });

            holder.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOnRowControlClickListener.onRowControlClick(contact,
                            false, true,
                            false, false, false, false);
                }
            });

            holder.btnContactChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOnRowControlClickListener.onRowControlClick(contact,
                            false, false,
                            true, false, false, false);
                }
            });

            holder.chkboxContactPaid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOnRowControlClickListener.onRowControlClick(contact,
                            false, false,
                            false, true, holder.chkboxContactPaid.isChecked(), false);
                }
            });

            holder.btnContactProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOnRowControlClickListener.onRowControlClick(contact,
                            false, false,
                            false, false, holder.chkboxContactPaid.isChecked(), true);
                }
            });
        }

        @Override
        public int getItemCount() {

            return contacts.size();
        }

        class ContactsViewHolder extends RecyclerView.ViewHolder {

            View itemView;
            TextView tvFullName,tvEmail,tvPhone;
            LinearLayout layoutContactControls;
            Button btnContactChat;
            CheckBox chkboxContactPaid;
            Button btnContactProgress;

            public ContactsViewHolder(@NonNull View itemView) {
                super(itemView);
                this.itemView = itemView;
                tvFullName = itemView.findViewById(R.id.tvContactFullName);
                tvEmail = itemView.findViewById(R.id.tvContactEmail);
                tvPhone = itemView.findViewById(R.id.tvContactPhone);
                layoutContactControls = itemView.findViewById(R.id.layoutContactControls);
                btnContactChat = itemView.findViewById(R.id.btnContactChat);
                chkboxContactPaid = itemView.findViewById(R.id.chkboxContactPaid);
                btnContactProgress = itemView.findViewById(R.id.btnContactProgress);
            }
        }
    }
}
