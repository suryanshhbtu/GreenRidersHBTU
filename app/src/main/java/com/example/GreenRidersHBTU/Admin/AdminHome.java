package com.example.GreenRidersHBTU.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GreenRidersHBTU.MainActivity;
import com.example.GreenRidersHBTU.R;
import com.example.GreenRidersHBTU.Util.AppInfo;
import com.example.GreenRidersHBTU.Util.ChangePassword;
import com.example.GreenRidersHBTU.Util.ShowRealtimeCycles;

public class AdminHome extends AppCompatActivity {
    public static String _id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Intent intent = getIntent();
        _id = intent.getStringExtra("_id");
        String email = intent.getStringExtra("email");

        String name = intent.getStringExtra("name");

        TextView emailTV = (TextView) findViewById(R.id.adminEmailTV);
        emailTV.setText(email);
        TextView nameTV = (TextView) findViewById(R.id.adminNameTV);
        nameTV.setText(name);



       findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, AdminDeleteUserAndGuard.class));
            }
        });

        findViewById(R.id.deleteCycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, AdminDeleteCycle.class));
            }
        });
        findViewById(R.id.addCycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, AdminAddCycle.class));
            }
        });
        findViewById(R.id.addUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, AdminAddUser.class));
            }
        });
        findViewById(R.id.addGuard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, AdminAddGuard.class));
            }
        });
        findViewById(R.id.realtime_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, ShowRealtimeCycles.class));
            }
        });


    }
    @Override
    public void onBackPressed() {
//            super.onBackPressed();
        finishAffinity();
        finish();
    }
    // Adding Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mailIt:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Write Subject Here");
                intent.putExtra(Intent.EXTRA_TEXT, "Write Your Feedback Here");

                startActivity(Intent.createChooser(intent, "Send Email"));
                Toast.makeText(this,"Generating Mail",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                MainActivity.AUTH_TOKEN = "";
                startActivity(new Intent(AdminHome.this, MainActivity.class));
                SharedPreferences preferences =getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Toast.makeText(this,"Logout Successfully",Toast.LENGTH_SHORT).show();

                Toast.makeText(this,"Logout Successfully",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.appinfo:
                startActivity(new Intent(AdminHome.this, AppInfo.class));
                Toast.makeText(this,"App Info",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.changePasswordMenu:
                startActivity(new Intent(AdminHome.this, ChangePassword.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}