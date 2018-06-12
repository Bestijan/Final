package com.example.laza.afinal.Activities.AuthActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.example.laza.afinal.Classes.AdapterClasses.SignInRegisterPageAdapter;
import com.example.laza.afinal.R;


public class AuthActivity extends AppCompatActivity implements SignInFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener
{
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new SignInRegisterPageAdapter(getSupportFragmentManager()));
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == getResources().getInteger(R.integer.register))
            viewPager.setCurrentItem(getResources().getInteger(R.integer.sing_up));
        else setAlertDialog();
    }

    @Override
    public void onFragmentInteraction(int i) {
        viewPager.setCurrentItem(i);
    }

    @Override
    public void Finish() {
        finish();
    }

    private void setAlertDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.title_close));
        builder.setMessage(R.string.messagge_close);

        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        finish();
                    }
                });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
         builder.show();
    }
}