package com.bt.smart.cargo_owner.activity.userAct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.bt.smart.cargo_owner.BaseActivity;
import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.fragment.user.PersonalInfoFragment;
import com.bt.smart.cargo_owner.fragment.user.SubmitIDCardFragment;

/**
 * @创建者 AndyYan
 * @创建时间 2019/1/8 9:38
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class AuthenticationActivity extends BaseActivity {
    private SubmitIDCardFragment mIDCardFt;
    private PersonalInfoFragment personalFt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_frame);
        setView();
        setData();
    }

    private void setView() {
        if (null == MyApplication.userFccountid || "".equals(MyApplication.userFccountid)) {
            mIDCardFt = new SubmitIDCardFragment();
            FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
            ftt.add(R.id.frame, mIDCardFt, "mIDCardFt");
            ftt.commit();
        } else {
            personalFt = new PersonalInfoFragment();
            FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
            ftt.add(R.id.frame, personalFt, "personalFt");
            ftt.commit();
        }
    }

    private void setData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        personalFt.onActivityResult(requestCode, resultCode, data);
        mIDCardFt.onActivityResult(requestCode, resultCode, data);
    }
}
