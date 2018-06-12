package com.example.laza.afinal.Activities.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laza.afinal.Activities.AuthActivity.AuthActivity;
import com.example.laza.afinal.Activities.UserAccountActivity.UserAccountActivity;
import com.example.laza.afinal.Classes.AdapterClasses.SettingsListViewAdapter;
import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.ModelClasses.RouteHolder;
import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.AdapterClasses.MainPageAdapter;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.R;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        RoutesFragment.OnFragmentInteractionMainListener,
        GoogleMapFragment.OnFragmentInteractionMainListener,
        MyPlacesFragment.OnFragmentInteractionMainListener,
        IVolleyResponse{

    ViewPager viewPager;
    int prevGroup = -1;

    String username;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setBackgroundColor(R.drawable.background);

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.map_pager);
        viewPager.setOffscreenPageLimit(getResources().getInteger(R.integer.tab_nums));
        viewPager.setAdapter(new MainPageAdapter(getSupportFragmentManager()));
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        SettingsListViewAdapter adapter = new SettingsListViewAdapter(this,
                Arrays.asList(getResources().getStringArray(R.array.marks_include)));
        ListView settings = findViewById(R.id.route_settings);
        settings.setAdapter(adapter);
        username = SharedPreferencesHelper.getAccount().getUsername(this);

        setProfileData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplicationContext.setContext(getBaseContext());
        SharedPreferencesHelper.getAccount().putToken(getBaseContext(), FirebaseInstanceId.getInstance().getToken());
        SharedPreferencesHelper.getAccount().putOnDestroyed(getBaseContext(), false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferencesHelper.getAccount().getToken(getBaseContext());
        SharedPreferencesHelper.getAccount().putOnDestroyed(getBaseContext(), true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NotificationClick(intent);
    }

    private void NotificationClick(Intent intent){
        MyPlace myPlace = intent.getParcelableExtra("myPlace");
        onFragmentInteractionMain(myPlace);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        String name = fragment.getClass().getSimpleName();
        Intent intent = this.getIntent();
        if (name.equals("GoogleMapFragment") && intent.hasExtra("myPlace"))
        {
            MyPlace myPlace = intent.getParcelableExtra("myPlace");
            ((GoogleMapFragment) fragment).setMyPlace(myPlace);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    viewPager.setCurrentItem(getResources().getInteger(R.integer.google_map));
                    break;
                }
                case R.id.navigation_dashboard: {
                    viewPager.setCurrentItem(getResources().getInteger(R.integer.routeHolder));
                    break;
                }
                case R.id.navigation_notifications: {
                    viewPager.setCurrentItem(getResources().getInteger(R.integer.event_place_fragment));
                    break;
                }
            }
            return true;
        }
    };

    private void setProfileData() {
        TextView textView = findViewById(R.id.textViewUsername);
        textView.setText(SharedPreferencesHelper.getAccount().getUsername(this));

        ImageView imageView = findViewById(R.id.userImageView);
        imageView.setImageBitmap(SharedPreferencesHelper.getAccount()
                .loadImageFromStorage(SharedPreferencesHelper.getAccount().getUserPic(this)));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();

            }
        });

    }

    private List<String> getHeaders() {
        List<String> headers = new ArrayList<String>();
        headers.add(getResources().getString(R.string.mark));
        headers.add(getResources().getString(R.string.include));
        headers.add(getResources().getString(R.string.subscribe));

        return headers;
    }

    private HashMap<String, List<String>> getData(List<String> headers) {
        HashMap<String, List<String>> data = new HashMap<String, List<String>>();

        data.put(headers.get(0), Arrays.asList(getResources().getStringArray(R.array.place_mark)));
        data.put(headers.get(1), Arrays.asList(getResources().getStringArray(R.array.place_include)));
        data.put(headers.get(2), Arrays.asList(getResources().getStringArray(R.array.marks_include)));

        return data;
    }

    private void logoutDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.logout_title));
        builder.setMessage(getResources().getString(R.string.logout_messagge));

        builder.setPositiveButton(getResources().getString(R.string.logout_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                        startActivity(intent);
                        SharedPreferencesHelper.getAccount().removeUser(MainActivity.this);
                        finish();
                    }
                });

        builder.setNegativeButton(getResources().getString(R.string.setup_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivityForResult(new Intent(MainActivity.this, UserAccountActivity.class), 100);
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (viewPager.getCurrentItem() == getResources().getInteger(R.integer.google_map))
                setAlertDialog();
            else {
                viewPager.setCurrentItem(getResources().getInteger(R.integer.google_map));
                bottomNavigationView.getMenu().getItem(getResources().getInteger(R.integer.google_fragment)).setChecked(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == getResources().getInteger(R.integer.login))
        {
            Intent viewPager = new Intent(this, AuthActivity.class);
            startActivity(viewPager);
        }
        else if (requestCode == getResources().getInteger(R.integer.account_updated)){
            if (resultCode == Activity.RESULT_OK) {
                ImageView imageView = findViewById(R.id.userImageView);
                imageView.setImageBitmap(SharedPreferencesHelper.getAccount()
                        .loadImageFromStorage(SharedPreferencesHelper.getAccount().getUserPic(this)));
                TextView textView = findViewById(R.id.textViewUsername);
                textView.setText(SharedPreferencesHelper.getAccount().getUsername(this));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        return true;
    }

    private void setAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.title_close));
        builder.setMessage(getResources().getString(R.string.messagge_close));

        builder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        finish();
                    }
                });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    @Override
    public void onFragmentInteractionMain(RouteHolder routeHolder) {
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        if (fragment instanceof GoogleMapFragment){
            ((GoogleMapFragment) fragment).setNewRoute(routeHolder);
            viewPager.setCurrentItem(getResources().getInteger(R.integer.google_fragment));
            bottomNavigationView.getMenu().getItem(getResources().getInteger(R.integer.google_fragment)).setChecked(true);
        }
    }

    @Override
    public void onFragmentInteractionMain(MyPlace myPlace) {
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        if (fragment instanceof GoogleMapFragment){
            ((GoogleMapFragment) fragment).setNewPlace(myPlace);
            viewPager.setCurrentItem(getResources().getInteger(R.integer.google_fragment));
            bottomNavigationView.getMenu().getItem(getResources().getInteger(R.integer.google_fragment)).setChecked(true);
        }
    }

    @Override
    public void onFragmentInteractionDelete(MyPlace myPlace) {
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        if (fragment instanceof GoogleMapFragment){
            ((GoogleMapFragment) fragment).deleteEventPlace(myPlace);
        }
    }

    @Override
    public void OnResponse(Object response) {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnIDResponse(Object response) {

    }

    @Override
    public void NotifyResponse(Object response){

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void OnError(String error) {

    }

    @Override
    public Context GetContext() {
        return this;
    }

    @Override
    public void onFragmentInteractionMain(int i) {

    }

}
