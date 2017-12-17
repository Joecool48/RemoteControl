package com.example.joeyb.remotecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyProjects extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView mProjects;
    private static final String TAG = "MyProjects";
    //public ArrayList<Project> projects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects);
        Log.d(TAG, "onCreate: onCreate started");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mProjects = (ListView) findViewById(R.id.projects);
        ArrayList<Project> projects = new ArrayList<>();
        Project LED_Sign = new Project("Joey\'s LED Project", "Joeys LED Sign", "98d3:32:107591", "Bluetooth");
        projects.add(LED_Sign);
        ProjectsAdapter adapter = new ProjectsAdapter(this, R.layout.list_item, projects);
        mProjects.setAdapter(adapter);
        mProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent(getApplicationContext(), LED_Project.class);
                    startActivityForResult(intent, ActivityNumbers.LED_Project);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d(TAG, "onCreate: onCreate Finished");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
     @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_my_projects) {

        } else if (id == R.id.nav_bluetooth) {
            Intent intent = new Intent(getApplicationContext(), Bluetooth.class);
            startActivityForResult(intent, ActivityNumbers.Bluetooth);
        } else if (id == R.id.nav_wifi) {
            Intent intent = new Intent(getApplicationContext(), WiFi.class);
            startActivityForResult(intent, ActivityNumbers.WiFi);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivityForResult(intent, ActivityNumbers.Settings);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(), About.class);
            startActivityForResult(intent, ActivityNumbers.About);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
