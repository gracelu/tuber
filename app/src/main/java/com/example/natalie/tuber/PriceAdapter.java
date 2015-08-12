package com.example.natalie.tuber;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jinglingli on 8/11/15.
 */
public class PriceAdapter extends CursorAdapter {

    public static class ViewHolder {

        public final TextView serviceName;
        public final TextView priceTag;

        public ViewHolder(View view) {
            serviceName = (TextView) view.findViewById(R.id.serviceName);
            priceTag = (TextView) view.findViewById(R.id.priceTag);
        }
    }

    public PriceAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.taxi_list_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.serviceName.setText(cursor.getString(QueryFragment.COL_PRICE_SERVICENAME));
        viewHolder.priceTag.setText(cursor.getString(QueryFragment.COL_PRICE_PRICE));

    }
}
