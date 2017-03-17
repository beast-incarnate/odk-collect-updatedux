package org.odk.collect.android.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.odk.collect.android.R;
import org.odk.collect.android.activities.FormChooserList;
import org.odk.collect.android.adapters.RecyclerViewAdapter;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.pojo.FormInfo;
import org.odk.collect.android.provider.InstanceProviderAPI;

import java.util.ArrayList;

/**
 * Created by kunalsingh on 12/03/17.
 */

public class AllFragment  extends android.support.v4.app.Fragment {


    public static final String TAG = "AllFragment";
    RecyclerView recyclerView;
    public AllFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.all_frag,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        ArrayList<FormInfo> arrayList = new ArrayList<>();

        setHasOptionsMenu(true);

        String selection;
        String[] selectionArgs = new String[]{InstanceProviderAPI.STATUS_SUBMITTED};
        String sortOrder = InstanceProviderAPI.InstanceColumns.STATUS + " DESC, " + InstanceProviderAPI.InstanceColumns.DISPLAY_NAME + " ASC";

        selection = InstanceProviderAPI.InstanceColumns.STATUS + " != ? ";

        Cursor c = getActivity().managedQuery(InstanceProviderAPI.InstanceColumns.CONTENT_URI, null, selection, selectionArgs, sortOrder);
        c.moveToFirst();
        while(c.moveToNext()){
            arrayList.add(new FormInfo(c.getString(c.getColumnIndex(InstanceProviderAPI.InstanceColumns.DISPLAY_NAME)),
                    c.getString(c.getColumnIndex(InstanceProviderAPI.InstanceColumns.DISPLAY_SUBTEXT)),
                    c.getString(c.getColumnIndex(InstanceProviderAPI.InstanceColumns.DELETED_DATE)),
                    c.getString(c.getColumnIndex(InstanceProviderAPI.InstanceColumns.STATUS)),c.getLong(c.getColumnIndex(InstanceProviderAPI.InstanceColumns._ID))));
        }

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(arrayList,getContext(),getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);


        recyclerViewAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        menu.clear();

        inflater.inflate(R.menu.allfrag_menu,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int itemId = item.getItemId();

        switch (itemId){

            case R.id.all_frag_addnew :Collect.getInstance().getActivityLogger()
                       .logAction(this, "fillBlankForm", "click");
                Intent i = new Intent(getContext(),
                        FormChooserList.class);
                startActivity(i);
                break;



        }


        return super.onOptionsItemSelected(item);
    }


//    public static void setListViewHeightBasedOnChildren(ListView listView){
//
//        ListAdapter listAdapter = listView.getAdapter();
//
//        if(listAdapter==null)
//            return ;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for(int i=0;i<listAdapter.getCount();i++){
//            view = listAdapter.getView(i,view,listView);
//            if(i==0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight()*
//                (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//
//    }

}
