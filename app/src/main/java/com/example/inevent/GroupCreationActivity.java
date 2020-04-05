package com.example.inevent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupCreationActivity extends AppCompatActivity {

    EditText mEventName;
    EditText mEventPlace;
    EditText mEventDate;

    Button mAddPeople;
    Button mCreateEventGroup;

    ArrayList<String> groupList;

    DatabaseReference databaseGroup;
    DatabaseReference databaseConnection;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

        mAuth = FirebaseAuth.getInstance();


        databaseGroup = FirebaseDatabase.getInstance().getReference("groups");
        databaseConnection = FirebaseDatabase.getInstance().getReference("connections");

        mEventName = findViewById(R.id.etGroupName);
        mEventPlace = findViewById(R.id.etGroupLocation);
        mEventDate = findViewById(R.id.etGroupDate);

        mAddPeople = findViewById(R.id.btAddPeople);
        mAddPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


        mCreateEventGroup = findViewById(R.id.btSubmitGroup);
        mCreateEventGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (groupList.size() > 5) {
                        createEventGroup();
                        Toast.makeText(getApplicationContext(), "Event group created!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                "Minimum 5 people needed to create a group", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        groupList = new ArrayList<String>();
    }


    public void openDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(GroupCreationActivity.this);
        alert.setMessage("Enter the Email:");
        alert.setTitle("Add Person");

        alert.setView(edittext);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                //Editable YouEditTextValue = edittext.getText();
                //OR
                String newEmail = edittext.getText().toString();
                groupList.add(newEmail);
                Toast.makeText(getApplicationContext(), "Member added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }


    public void createEventGroup() throws ParseException {
        String groupName = mEventName.getText().toString();
        String groupLocation = mEventPlace.getText().toString();
        String groupDate = mEventDate.getText().toString();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject = formatter.parse(groupDate);

        final FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();

        String gid = databaseGroup.push().getKey();
        Group group = new Group(groupName, groupDate, email, "safe", groupList.size(), groupLocation,0);
        databaseGroup.child(gid).setValue(group);

        for (int i = 0; i < groupList.size(); i++) {
            String temp = groupList.get(i);

            String cid = databaseConnection.push().getKey();
            Connection con = new Connection(temp,gid);
            databaseConnection.child(cid).setValue(con);
        }


        Intent intent = new Intent(GroupCreationActivity.this, MainActivity.class);
        startActivity(intent);


    }

}
