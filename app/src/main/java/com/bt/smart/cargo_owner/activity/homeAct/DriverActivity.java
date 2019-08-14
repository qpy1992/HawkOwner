package com.bt.smart.cargo_owner.activity.homeAct;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.fragment.home.DriverFragment;

public class DriverActivity extends AppCompatActivity {
    private DriverFragment driverFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_frame);
        initView();
    }

    private void initView() {
        driverFragment = new DriverFragment();
        FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
        ftt.add(R.id.frame, driverFragment, "driverFragment");
        ftt.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        driverFragment.onActivityResult(requestCode, resultCode, data);
    }
}
