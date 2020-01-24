package com.Emergency.sos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class message extends AppCompatActivity {
    AppCompatEditText editText;
    Button save;
    String Message2;
   static String m="I need your help,it is a case of Emergency.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue_600));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.grey_100_));



        BottomNavigationView navigationView=findViewById(R.id.navbar);
        Menu menu=navigationView.getMenu();
        MenuItem menuItem=menu.getItem(2);
        menuItem.setChecked(true);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.contactpage:{
                        Intent intent=new Intent(getApplicationContext(),Contacts.class);
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

        editText=findViewById(R.id.sosmessage);
        editText.setText(SOS.sharedPreferences.getString("Message",m));
        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SOS.sharedPreferences.edit().putString("Message",editText.getText().toString()).commit();
                SOS.message=SOS.sharedPreferences.getString("Message",m);
                Toast.makeText(message.this, "Message Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
