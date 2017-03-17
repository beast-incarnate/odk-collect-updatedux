package org.odk.collect.android.adapters;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.odk.collect.android.R;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.pojo.FormInfo;
import org.odk.collect.android.provider.InstanceProviderAPI;
import org.odk.collect.android.utilities.ApplicationConstants;

import java.util.ArrayList;

/**
 * Created by kunalsingh on 14/03/17.
 */


class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView tv1,tv2,tv3,tv4;

    ArrayList<FormInfo> arrayList = new ArrayList<>();

    Activity act;

    public static final String TAG = "RecyclerViewHolder";

    public RecyclerViewHolder(View itemView , ArrayList<FormInfo> arrayList , Activity act) {
        super(itemView);

        this.arrayList = arrayList;
        this.act = act;

        itemView.setOnClickListener(this);

        tv1 = (TextView)itemView.findViewById(R.id.text1);
        tv2 = (TextView)itemView.findViewById(R.id.text2);
        tv3 = (TextView)itemView.findViewById(R.id.text3);
        tv4 = (TextView)itemView.findViewById(R.id.form_status);


    }

    @Override
    public void onClick(View v) {

        int pos = getPosition();

        FormInfo formInfo = arrayList.get(pos);

        String selection;
        String[] selectionArgs = new String[]{String.valueOf(formInfo.getId())};

        selection = InstanceProviderAPI.InstanceColumns._ID + " = ? ";

        Cursor c = act.managedQuery(InstanceProviderAPI.InstanceColumns.CONTENT_URI, null, selection, selectionArgs,null );
        c.moveToFirst();

       if(c.getCount()!=0)
           editForm(c,act);

    }


    public void editForm(Cursor c , Activity act){

        Uri instanceUri =
                ContentUris.withAppendedId(InstanceProviderAPI.InstanceColumns.CONTENT_URI,
                        c.getLong(c.getColumnIndex(InstanceProviderAPI.InstanceColumns._ID)));

        Collect.getInstance().getActivityLogger().logAction(this, "onListItemClick",
                instanceUri.toString());



                Intent intent = new Intent(Intent.ACTION_EDIT, instanceUri);

                    intent.putExtra(ApplicationConstants.BundleKeys.FORM_MODE, ApplicationConstants.FormModes.EDIT_SAVED);

                act.startActivity(intent);
            }

    }


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ArrayList<FormInfo> arrayList = new ArrayList<>();

    public static final String TAG = "RVH";

    private Context mContext;

    private Activity activity;

    public RecyclerViewAdapter(ArrayList<FormInfo> arrayList, Context mContext , Activity activity) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.activity = activity;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_view,null);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view , arrayList ,activity );

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {


        holder.tv1.setText(arrayList.get(position).getName());
        holder.tv2.setText(arrayList.get(position).getDate());
        holder.tv3.setText(arrayList.get(position).getSubtext());
        holder.tv4.setText(arrayList.get(position).getStatus());



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}


