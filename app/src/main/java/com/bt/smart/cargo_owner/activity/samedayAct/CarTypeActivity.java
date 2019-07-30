package com.bt.smart.cargo_owner.activity.samedayAct;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.fragment.user.SelectModelLengthFragment;

public class CarTypeActivity extends AppCompatActivity {
    private SelectModelLengthFragment selectModelLengthFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_frame);
        setView();
    }

    protected void setView(){
        selectModelLengthFragment = new SelectModelLengthFragment();
        FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
        ftt.add(R.id.frame, selectModelLengthFragment, "selectModelLengthFragment");
        ftt.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectModelLengthFragment.onActivityResult(requestCode, resultCode, data);
    }
}
