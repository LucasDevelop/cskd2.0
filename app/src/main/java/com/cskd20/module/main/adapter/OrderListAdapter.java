package com.cskd20.module.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cskd20.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 lucas
 * @创建时间 2017/6/14 0014 17:49
 * @描述 TODO
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListHolder> {

    public static final int statu_loading = 1;
    public static final int statu_normal  = 0;
    public static       int currentStatus = statu_normal;

    static final int item_load_more = 1;
    static final int item_normal    = 0;

    List<String> datas = new ArrayList<>();

    @Override
    public OrderListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OrderListHolder holder = null;
        if (viewType == item_normal) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);
            holder = new OrderListHolder(inflate);
        }
        if (viewType == item_load_more) {
            TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer,parent,false);
            holder =new OrderListHolder(textView);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size()+1;
    }

    public void setDatas(List<String> data) {
        datas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == datas.size() - 1 ? item_load_more : item_normal;
    }

    public class OrderListHolder extends RecyclerView.ViewHolder {
        private View mItemView;

        public OrderListHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }
    }
}
