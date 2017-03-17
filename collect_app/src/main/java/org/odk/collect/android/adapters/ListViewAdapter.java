package org.odk.collect.android.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.odk.collect.android.R;

import java.util.ArrayList;

/**
 * Created by kunalsingh on 01/03/17.
 */

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private ArrayList<String> text1;
    private ArrayList<String> text2;
    private ArrayList<String> text3;
    public static final String TAG = "ListViewAdapter";

    public ListViewAdapter(Context mContext, ArrayList<String> text1, ArrayList<String> text2, ArrayList<String> text3) {
        this.mContext = mContext;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listview_item,null);
        if(view==null)
            Log.d(TAG,"view ids nu;;");
        else
        Log.d(TAG,"vitjtj");
        SwipeLayout swipeLayout = (SwipeLayout)view.findViewById(R.id.swipe);

        swipeLayout.addSwipeListener(new SimpleSwipeListener(){
            @Override
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
            }
        });

        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {

        TextView texts1,texts2,texts3;

        texts1 = (TextView)convertView.findViewById(R.id.text1);
        texts2  = (TextView)convertView.findViewById(R.id.text2);
        texts3 = (TextView)convertView.findViewById(R.id.text3);

        texts1.setText(text1.get(position));
        texts2.setText(text2.get(position));
        texts3.setText(text3.get(position));
    }

    @Override
    public int getCount() {
        return text1.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
