package org.odk.collect.android.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.RecyclerViewAdapter;
import org.odk.collect.android.pojo.FormInfo;
import org.odk.collect.android.provider.InstanceProviderAPI;

import java.util.ArrayList;

/**
 * Created by kunalsingh on 13/03/17.
 */

public class SentFragment extends Fragment {


    private RecyclerView recyclerView;

    public static final String TAG = "SentFragment";

    private ArrayList<FormInfo> arrayList = new ArrayList<>();;

    public SentFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Log.d(TAG,"sent frag made");

        View view = inflater.inflate(R.layout.sent_frag,container,false);

        setHasOptionsMenu(true);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_sent);


        String selection;
        String[] selectionArgs = new String[]{InstanceProviderAPI.STATUS_SUBMITTED};
        String sortOrder = InstanceProviderAPI.InstanceColumns.STATUS + " DESC, " + InstanceProviderAPI.InstanceColumns.DISPLAY_NAME + " ASC";

        selection = InstanceProviderAPI.InstanceColumns.STATUS + " = ? ";

        Cursor c = getActivity().managedQuery(InstanceProviderAPI.InstanceColumns.CONTENT_URI, null, selection, selectionArgs, sortOrder);
        c.moveToFirst();
        while(c.moveToNext()){
            arrayList.add(new FormInfo(c.getString(c.getColumnIndex(InstanceProviderAPI.InstanceColumns.DISPLAY_NAME)),
                    c.getString(c.getColumnIndex(InstanceProviderAPI.InstanceColumns.DISPLAY_SUBTEXT)),
                    c.getString(c.getColumnIndex(InstanceProviderAPI.InstanceColumns.DELETED_DATE)),
                    c.getString(c.getColumnIndex(InstanceProviderAPI.InstanceColumns.STATUS)),c.getLong(c.getColumnIndex(InstanceProviderAPI.InstanceColumns._ID))));
        }

        Log.d(TAG,"count"+" " +arrayList.size());

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

        inflater.inflate(R.menu.sentfrag_menu,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
