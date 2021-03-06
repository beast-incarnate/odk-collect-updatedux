/*
 * Copyright (C) 2009 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.odk.collect.android.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.ListViewAdapter;
import org.odk.collect.android.adapters.ViewSentListAdapter;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.provider.InstanceProviderAPI;
import org.odk.collect.android.provider.InstanceProviderAPI.InstanceColumns;
import org.odk.collect.android.utilities.ApplicationConstants;

import java.util.ArrayList;

/**
 * Responsible for displaying all the valid instances in the instance directory.
 *
 * @author Yaw Anokwa (yanokwa@gmail.com)
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public class InstanceChooserList extends ListActivity {

    private static final boolean EXIT = true;
    private static final boolean DO_NOT_EXIT = false;
    private AlertDialog mAlertDialog;
    private Button mNewFormButton;
    private Button mDelSendButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // must be at the beginning of any activity that can be called from an external intent
        try {
            Collect.createODKDirs();
        } catch (RuntimeException e) {
            createErrorDialog(e.getMessage(), EXIT);
            return;
        }

        setContentView(R.layout.chooser_list_layout);
        mDelSendButton = (Button)findViewById(R.id.del_send_form_btn);
        TextView tv = (TextView) findViewById(R.id.status_text);
        tv.setVisibility(View.GONE);
        String selection;
        String[] selectionArgs = new String[]{InstanceProviderAPI.STATUS_SUBMITTED};
        String sortOrder = InstanceColumns.STATUS + " DESC, " + InstanceColumns.DISPLAY_NAME + " ASC";

        if (getIntent().getStringExtra(ApplicationConstants.BundleKeys.FORM_MODE).equalsIgnoreCase(ApplicationConstants.FormModes.EDIT_SAVED)) {
            mDelSendButton.setVisibility(View.GONE);
            setTitle(getString(R.string.review_data));
            selection = InstanceColumns.STATUS + " != ? ";
        } else {
            setTitle(getString(R.string.view_sent_forms));
            selection = InstanceColumns.STATUS + " = ? ";
        }

        Cursor c = managedQuery(InstanceColumns.CONTENT_URI, null, selection, selectionArgs, sortOrder);

        String[] data = new String[]{
                InstanceColumns.DISPLAY_NAME, InstanceColumns.DISPLAY_SUBTEXT, InstanceColumns.DELETED_DATE
        };
        int[] view = new int[]{
                R.id.text1, R.id.text2, R.id.text4
        };

        // render total instance view
        SimpleCursorAdapter instances;
        if (getIntent().getStringExtra(ApplicationConstants.BundleKeys.FORM_MODE).equalsIgnoreCase(ApplicationConstants.FormModes.EDIT_SAVED)) {
            instances = new SimpleCursorAdapter(this, R.layout.two_item, c, data, view);
        } else {
            ((TextView) findViewById(android.R.id.empty)).setText(R.string.no_items_display_sent_forms);
            instances = new ViewSentListAdapter(this, R.layout.two_item, c, data, view);
        }

        setListAdapter(instances);

         c.moveToFirst();
        ArrayList<String> text1=new ArrayList<>();
        ArrayList<String> text2=new ArrayList<>();
        ArrayList<String> text3=new ArrayList<>();

        while(c.moveToNext()){

            text1.add(c.getString(c.getColumnIndex(InstanceColumns.DISPLAY_NAME)));
            text2.add(c.getString(c.getColumnIndex(InstanceColumns.DISPLAY_SUBTEXT)));
            text3.add(c.getString(c.getColumnIndex(InstanceColumns.DELETED_DATE)));
        }

        ListView listView = getListView();

        ListViewAdapter mAdapter = new ListViewAdapter(this,text1,text2,text3);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) getListAdapter().getItem(position);
                startManagingCursor(c);
                Uri instanceUri =
                        ContentUris.withAppendedId(InstanceColumns.CONTENT_URI,
                                c.getLong(c.getColumnIndex(InstanceColumns._ID)));

                Collect.getInstance().getActivityLogger().logAction(this, "onListItemClick",
                        instanceUri.toString());


                    String action = getIntent().getAction();
                    if (Intent.ACTION_PICK.equals(action)) {
                        // caller is waiting on a picked form
                        setResult(RESULT_OK, new Intent().setData(instanceUri));
                    } else {
                        // the form can be edited if it is incomplete or if, when it was
                        // marked as complete, it was determined that it could be edited
                        // later.
                        String status = c.getString(c.getColumnIndex(InstanceColumns.STATUS));
                        String strCanEditWhenComplete =
                                c.getString(c.getColumnIndex(InstanceColumns.CAN_EDIT_WHEN_COMPLETE));

                        boolean canEdit = status.equals(InstanceProviderAPI.STATUS_INCOMPLETE)
                                || Boolean.parseBoolean(strCanEditWhenComplete);
                        if (!canEdit) {
                            createErrorDialog(getString(R.string.cannot_edit_completed_form),
                                    DO_NOT_EXIT);
                            return;
                        }
                        // caller wants to view/edit a form, so launch formentryactivity
                   //     Intent parentIntent = new InstanceChooserList().getIntent();
                        Intent intent = new Intent(Intent.ACTION_EDIT, instanceUri);
//                        if (parentIntent.getStringExtra(ApplicationConstants.BundleKeys.FORM_MODE).equalsIgnoreCase(ApplicationConstants.FormModes.EDIT_SAVED)) {
//                            intent.putExtra(ApplicationConstants.BundleKeys.FORM_MODE, ApplicationConstants.FormModes.EDIT_SAVED);
//                        } else {
                            intent.putExtra(ApplicationConstants.BundleKeys.FORM_MODE, ApplicationConstants.FormModes.EDIT_SAVED);

                        startActivity(intent);
                    }

                }

        });

        mNewFormButton = (Button)findViewById(R.id.fill_new_form_btn);
        mNewFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                Collect.getInstance().getActivityLogger()
                        .logAction(this, "fillBlankForm", "click");
                Intent i = new Intent(getApplicationContext(),
                        FormChooserList.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }


    /**
     * Stores the path of selected instance in the parent class and finishes.
     */
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Cursor c = (Cursor) getListAdapter().getItem(position);
        startManagingCursor(c);
        Uri instanceUri =
                ContentUris.withAppendedId(InstanceColumns.CONTENT_URI,
                        c.getLong(c.getColumnIndex(InstanceColumns._ID)));

        Collect.getInstance().getActivityLogger().logAction(this, "onListItemClick",
                instanceUri.toString());

        if (view.findViewById(R.id.visible_off).getVisibility() != View.VISIBLE) {
            String action = getIntent().getAction();
            if (Intent.ACTION_PICK.equals(action)) {
                // caller is waiting on a picked form
                setResult(RESULT_OK, new Intent().setData(instanceUri));
            } else {
                // the form can be edited if it is incomplete or if, when it was
                // marked as complete, it was determined that it could be edited
                // later.
                String status = c.getString(c.getColumnIndex(InstanceColumns.STATUS));
                String strCanEditWhenComplete =
                        c.getString(c.getColumnIndex(InstanceColumns.CAN_EDIT_WHEN_COMPLETE));

                boolean canEdit = status.equals(InstanceProviderAPI.STATUS_INCOMPLETE)
                        || Boolean.parseBoolean(strCanEditWhenComplete);
                if (!canEdit) {
                    createErrorDialog(getString(R.string.cannot_edit_completed_form),
                            DO_NOT_EXIT);
                    return;
                }
                // caller wants to view/edit a form, so launch formentryactivity
                Intent parentIntent = this.getIntent();
                Intent intent = new Intent(Intent.ACTION_EDIT, instanceUri);
                if (parentIntent.getStringExtra(ApplicationConstants.BundleKeys.FORM_MODE).equalsIgnoreCase(ApplicationConstants.FormModes.EDIT_SAVED)) {
                    intent.putExtra(ApplicationConstants.BundleKeys.FORM_MODE, ApplicationConstants.FormModes.EDIT_SAVED);
                } else {
                    intent.putExtra(ApplicationConstants.BundleKeys.FORM_MODE, ApplicationConstants.FormModes.VIEW_SENT);
                }
                startActivity(intent);
            }
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Collect.getInstance().getActivityLogger().logOnStart(this);
    }

    @Override
    protected void onStop() {
        Collect.getInstance().getActivityLogger().logOnStop(this);
        super.onStop();
    }

    private void createErrorDialog(String errorMsg, final boolean shouldExit) {
        Collect.getInstance().getActivityLogger().logAction(this, "createErrorDialog", "show");

        mAlertDialog = new AlertDialog.Builder(this).create();
        mAlertDialog.setIcon(android.R.drawable.ic_dialog_info);
        mAlertDialog.setMessage(errorMsg);
        DialogInterface.OnClickListener errorListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Collect.getInstance().getActivityLogger().logAction(this,
                                "createErrorDialog",
                                shouldExit ? "exitApplication" : "OK");
                        if (shouldExit) {
                            finish();
                        }
                        break;
                }
            }
        };
        mAlertDialog.setCancelable(false);
        mAlertDialog.setButton(getString(R.string.ok), errorListener);
        mAlertDialog.show();
    }


}
