package com.Emergency.sos;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SOS extends AppCompatActivity {
    CircleImageView sos;
    int trash=0;
    String lon="",lat="";
    static String message="";
    static TinyDB arraylist;
    static SharedPreferences sharedPreferences;
    BottomNavigationView navigation;
    MarshMallowPermission marshMallowPermission;
    static ArrayList<String> numbers=new ArrayList<String>();

    public static String[] permissionsRequired = new String[]{Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.SEND_SMS
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        marshMallowPermission=new MarshMallowPermission(SOS.this);
        arraylist=new TinyDB(getApplicationContext());
        Contacts.allinfo= (ArrayList<Contact>) SOS.arraylist.getListObject("getinfo",Contact.class);
        for (int i=0;i<Contacts.allinfo.size();i++)
        {
            numbers.add(Contacts.allinfo.get(i).phone);
        }
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        message=sharedPreferences.getString("Message", com.Emergency.sos.message.m);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue_600));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.grey_100_));

        if (Build.VERSION.SDK_INT >= 23) {
            if (marshMallowPermission.checkPermissionForReadContacts() && marshMallowPermission.checkPermissionForReadPhoneState()
                && marshMallowPermission.checkPermissionForSMS() &&
                     marshMallowPermission.checkPermissionForAccessFineLocation(SOS.this)) {
                init();
            } else {
                chkpermission();
            }
        }


    init();


    }
    private  void init()
    {

        final FusedLocationProviderClient fusedLocationProviderClient=new FusedLocationProviderClient(getApplicationContext());
        if(trash==1){
        if (ActivityCompat.checkSelfPermission(SOS.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SOS.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }}
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    Double a=location.getLongitude();
                    Double b=location.getLatitude();
                    lon=a.toString();
                    lat=b.toString();
                }
            }
        });

         sos=findViewById(R.id.Sosbutton);
         sos.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(numbers.isEmpty())
                 {
                     Toast.makeText(SOS.this, "Add Atleast 1 Contact before sending message", Toast.LENGTH_LONG).show();
                 }
                 else if(message.isEmpty())
                 {
                     Toast.makeText(SOS.this, "You have an empty message", Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                     Toast.makeText(SOS.this, "Sent Sucessfully", Toast.LENGTH_SHORT).show();
                 }

//location



// message
                 sendsms(numbers,lon,lat);
             }
         });
        BottomNavigationView navigationView=findViewById(R.id.navbar);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.contactpage:{
                        Intent intent=new Intent(getApplicationContext(),Contacts.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.messagepage:
                    {
                        Intent intent=new Intent(getApplicationContext(),message.class);
                        startActivity(intent);
                        return true;
                    }


                }
                return false;
            }
        });


    }
    public void sendsms(ArrayList<String> phone,String lon,String lat)
    {
        SmsManager smsManager=SmsManager.getDefault();
        for (int i=0;i<phone.size();i++)
        {
            if(lat.equals("") || lon.equals(""))
            {
                smsManager.sendTextMessage(phone.get(i), null, message, null, null);
            }
            else
            smsManager.sendTextMessage(phone.get(i), null, message+" My Longitude is" +lon+" and latitude is "+lat, null, null);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void chkpermission() {

        requestPermissions(permissionsRequired, 101);

    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }
}
