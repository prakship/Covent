package com.example.inevent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseRoot;
    DatabaseReference databaseGroup;
    DatabaseReference databaseConnection;

    EditText etSearch;

    TextView tvAdd;

    private ProgressBar spinner;

    ListView listViewGroup;

    List<Group> groupList;

    GroupAdapter adapter;

    TextView emptyTextView;

    int groupCount =0;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public HashMap<String , Integer> mapForDate = new HashMap<String , Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        currentUser  = mAuth.getCurrentUser();

        spinner = findViewById(R.id.progressBar1);

        emptyTextView = findViewById(R.id.empty_view);

        etSearch = findViewById(R.id.etSearch);

        tvAdd = findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,GroupCreationActivity.class);
                intent.putExtra("map",mapForDate);
                startActivity(intent);
            }
        });

        groupList = new ArrayList<>();

        databaseRoot = FirebaseDatabase.getInstance().getReference();
        databaseGroup = databaseRoot.child("groups");
        databaseConnection = databaseRoot.child("connections");

        listViewGroup = findViewById(R.id.list);


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                List<Group> newList = new ArrayList<Group>();
                String filter = charSequence.toString();
                for (int i=0;i<groupList.size();i++)
                {
                    if (groupList.get(i).getgName().toLowerCase().contains(filter.toLowerCase()))
                    {
                        newList.add(groupList.get(i));
                    }
                }

                if(newList.isEmpty()){
                    emptyTextView.setText("No groups found!");
                }
                adapter = new GroupAdapter(MainActivity.this,newList);

                listViewGroup.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();



        spinner.setVisibility(View.VISIBLE);
        databaseGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("groupList length: ", groupList.size() + "");

                if(!groupList.isEmpty()) {
                    adapter = new GroupAdapter(MainActivity.this, groupList);
                    listViewGroup.setAdapter(adapter);
                }
                groupList.clear();



                for(final DataSnapshot groupSnapshot : dataSnapshot.getChildren()){

                    final Group group = groupSnapshot.getValue(Group.class);

                    Log.i("Group: ", group + "");

                    databaseConnection.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            for(DataSnapshot connectionSnapshot : dataSnapshot1.getChildren()){

                                Connection con = connectionSnapshot.getValue(Connection.class);

                                if(con.email.equals(currentUser.getEmail()) && con.gId.equals(groupSnapshot.getKey())){
                                    groupList.add(group);
                                }


                            }
                            adapter = new GroupAdapter(MainActivity.this, groupList);
                            listViewGroup.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


                adapter = new GroupAdapter(MainActivity.this,groupList);

                listViewGroup.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.covidMap: {
                break;
            }

            case R.id.userAuth: {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,LogInActivity.class));
                break;
            }

            case R.id.alarm: {
                triggerAlarm();
                break;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
    }


    public void triggerAlarm(){
        databaseConnection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot connectionSnapshot : dataSnapshot.getChildren()){
                    final Connection con = connectionSnapshot.getValue(Connection.class);

                    if(con.getEmail().equals(currentUser.getEmail())) {

                        databaseGroup.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                for (final DataSnapshot groupSnapshot : dataSnapshot1.getChildren()) {
                                    Group group = groupSnapshot.getValue(Group.class);

                                    if (groupSnapshot.getKey().equals(con.gId)) {
                                        int temp = group.gAffectedCount;
                                        Log.i("temp: ", temp + "");
                                        groupSnapshot.getRef().child("gAffectedCount").setValue(temp + 1);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
