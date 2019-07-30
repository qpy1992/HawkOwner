package com.bt.smart.cargo_owner.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.AddGoodsBean;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2019/1/9 8:59
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LvAddGoodsAdapter extends BaseAdapter {
    private Context mContext;
    private List<AddGoodsBean> mList;

    public LvAddGoodsAdapter(Context context, List<AddGoodsBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final MyViewHolder viewholder;
        if (null == view) {
            viewholder = new MyViewHolder();
            view = View.inflate(mContext, R.layout.adapter_lv_goods, null);
            viewholder.tv_goodsname = view.findViewById(R.id.tv_goodsname);
            viewholder.tv_goodswei = view.findViewById(R.id.tv_goodswei);
            viewholder.tv_goodsname.setText(Html.fromHtml(mContext.getString(R.string.goodsname)));
            viewholder.tv_goodswei.setText(Html.fromHtml(mContext.getString(R.string.goodswei)));
            viewholder.et_name = view.findViewById(R.id.et_name);
            viewholder.img_del = view.findViewById(R.id.img_del);
            viewholder.et_area = view.findViewById(R.id.et_area);
            viewholder.et_weight = view.findViewById(R.id.et_weight);
            view.setTag(viewholder);
        } else {
            viewholder = (MyViewHolder) view.getTag();
        }

        if (viewholder.et_name.getTag() instanceof TextWatcher) {
            viewholder.et_name.removeTextChangedListener((TextWatcher) viewholder.et_name.getTag());
        }
        if (viewholder.et_area.getTag() instanceof TextWatcher) {
            viewholder.et_area.removeTextChangedListener((TextWatcher) viewholder.et_area.getTag());
        }
        if (viewholder.et_weight.getTag() instanceof TextWatcher) {
            viewholder.et_weight.removeTextChangedListener((TextWatcher) viewholder.et_weight.getTag());
        }
        viewholder.et_name.setText(null == mList.get(i).getName() ? "" : mList.get(i).getName());
        viewholder.et_area.setText(null == mList.get(i).getArea() ? "" : mList.get(i).getArea());
        viewholder.et_weight.setText(null == mList.get(i).getWeight() ? "" : mList.get(i).getWeight());
        initCjWatcher(i, viewholder.et_name, 0);
        initCjWatcher(i, viewholder.et_area, 1);
        initCjWatcher(i, viewholder.et_weight, 2);

        viewholder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private class MyViewHolder {
        TextView tv_goodsname,tv_goodswei;
        ImageView img_del;
        EditText et_name, et_area, et_weight;
    }

    private void initCjWatcher(final int i, TextView et, final int type) {
        TextWatcher sim_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (0 == type) {
                    mList.get(i).setName(editable.toString());
                } else if (1 == type) {
                    mList.get(i).setArea(editable.toString());
                } else if (2 == type) {
                    mList.get(i).setWeight(editable.toString());
                }
            }
        };
        et.addTextChangedListener(sim_watcher);
        et.setTag(sim_watcher);
    }
}
