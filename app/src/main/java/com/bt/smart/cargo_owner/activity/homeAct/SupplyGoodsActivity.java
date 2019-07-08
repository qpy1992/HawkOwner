package com.bt.smart.cargo_owner.activity.homeAct;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.bt.smart.cargo_owner.BaseActivity;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.fragment.home.SupplyGoodsFragment;

public class SupplyGoodsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_frame);
        initView();
    }

    private void initView() {
        SupplyGoodsFragment supplyGoodsFt = new SupplyGoodsFragment();
        FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
        ftt.add(R.id.frame, supplyGoodsFt, "supplyGoodsFt");
        ftt.commit();
    }
}
