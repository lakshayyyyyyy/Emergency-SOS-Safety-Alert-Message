package com.Emergency.sos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {
    ArrayList<String> contacts;
    Button add,pri;
    String contact="";
    int CONTACT_PICK_REQUEST=1000;
    private View parent_view;
    ArrayList<Contact> selectedContacts=new ArrayList<Contact>();
   static ArrayList<Contact> allinfo;
    private RecyclerView recyclerView;
    static AdapterListSwipe mAdapter;
    private ItemTouchHelper mItemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        allinfo= (ArrayList<Contact>) SOS.arraylist.getListObject("getinfo",Contact.class);

        getWindow().setStatusBarColor(getResources().getColor(R.color.blue_600));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.grey_100_));
        contacts=new ArrayList<String>();
        add=findViewById(R.id.addcon);
        initcomponent();

        BottomNavigationView navigationView=findViewById(R.id.navbar);
        Menu menu=navigationView.getMenu();
        MenuItem menuItem=menu.getItem(1);
        menuItem.setChecked(true);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.messagepage:
                    {
                        Intent intent=new Intent(getApplicationContext(),message.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.sospage:
                    {
                        Intent intent=new Intent(getApplicationContext(),SOS.class);
                        startActivity(intent);
                        return true;
                    }

                }
                return false;
            }
        });


    }
    public void initcomponent()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentContactPick = new Intent(Contacts.this,ContactsPickerActivity.class);
                startActivityForResult(intentContactPick,CONTACT_PICK_REQUEST);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //List<Social> items = DataGenerator.getSocialData(this);

        //set data and list adapter
        mAdapter = new AdapterListSwipe(this,allinfo );

        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSwipe.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Contact obj, int position) {
                Snackbar.make(view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();

            }
        });

        ItemTouchHelper.Callback callback = new SwipeItemTouchHelper(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_OK) {

             selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");
            for (int i = 0; i < selectedContacts.size(); i++) {
                if(SOS.numbers.indexOf(selectedContacts.get(i).phone)!=-1)
                {
                    Toast.makeText(this, "Contact is Already Added", Toast.LENGTH_SHORT).show();
                }
                else {
                    SOS.numbers.add(selectedContacts.get(i).phone);
                    allinfo.add(selectedContacts.get(i));
                    Toast.makeText(this, "Added Sucessfully", Toast.LENGTH_SHORT).show();
                }
            }
            SOS.arraylist.putListObject("getinfo",allinfo);
            initcomponent();
        }

    }
}