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
import org.odk.collect.android.provider.InstanceProvider;
import org.odk.collect.android.provider.InstanceProviderAPI;

import java.util.ArrayList;

/**
 * Created by kunalsingh on 13/03/17.
 */

public class FinalisedFragment extends Fragment {

    public static final String TAG = "Finalised";
    private ArrayList<FormInfo> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;

    public FinalisedFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.finalised_frag,container,false);

        setHasOptionsMenu(true);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_finalised);

        String selection = InstanceProviderAPI.InstanceColumns.STATUS + "=? or "
                + InstanceProviderAPI.InstanceColumns.STATUS + "=?";
        String selectionArgs[] = {InstanceProviderAPI.STATUS_COMPLETE,
                InstanceProviderAPI.STATUS_SUBMISSION_FAILED};
        String sortOrder = InstanceProviderAPI.InstanceColumns.DISPLAY_NAME + " ASC";
        Cursor c = getActivity().managedQuery(InstanceProviderAPI.InstanceColumns.CONTENT_URI, null, selection,
                selectionArgs, sortOrder);

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

        inflater.inflate(R.menu.finalisedfrag_menu,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
