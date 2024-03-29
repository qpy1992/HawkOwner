package com.bt.smart.cargo_owner.fragment.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bt.smart.cargo_owner.R;

public class MDeFragment extends Fragment {
    private View view;
    private TextView mde_jine,mde_type,mde_time,mde_orderno,mde_yue,mde_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mde, container, false);
        setViews();
        return view;
    }

    protected void setViews(){
        mde_jine = view.findViewById(R.id.mde_jine);
        mde_type = view.findViewById(R.id.mde_type);
        mde_time = view.findViewById(R.id.mde_time);
        mde_orderno = view.findViewById(R.id.mde_orderno);
        mde_yue = view.findViewById(R.id.mde_yue);
        mde_title = view.findViewById(R.id.mde_title);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mde_jine.setText(bundle.getString("fmoney"));
            String type = bundle.getString("ftype");
            if(type.equals("0")){
                mde_type.setText("收入");
            }else{
                mde_type.setText("支出");
            }
            mde_time.setText(bundle.getString("addtime"));
            mde_orderno.setText(bundle.getString("tradeno"));
            mde_yue.setText(bundle.getString("fremain"));
            mde_title.setText(bundle.getString("fnote"));
        }
    }

}
