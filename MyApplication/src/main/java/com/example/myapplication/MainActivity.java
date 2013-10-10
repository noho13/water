package com.example.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements ActionBar.TabListener {

    private static final String SAVE_TAB_INDEX = "save_tab_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.addTab(bar.newTab().setText("Tab 1").setTabListener(this));
        bar.addTab(bar.newTab().setText("Tab 2").setTabListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state.containsKey(SAVE_TAB_INDEX)) {
            getActionBar().setSelectedNavigationItem(state.getInt(SAVE_TAB_INDEX));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_TAB_INDEX, getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (tab.getPosition() == 1) {
            SomeFragment frag = new SomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString(SomeFragment.FRAGMENT_CONTENT, "I am some fragment");
            frag.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(android.R.id.content, frag).commit();
        } else {
            MyListFragment listFragment = new MyListFragment();
            getFragmentManager().beginTransaction().replace(android.R.id.content, listFragment).commit();
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public static class SomeFragment extends Fragment {

        public static final String FRAGMENT_CONTENT = "fragment_content";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            TextView text = new TextView(getActivity());
            text.setGravity(Gravity.CENTER);
            text.setText(getArguments().getString(FRAGMENT_CONTENT));
            return text;
        }
    }

    public static class MyListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final String TAG = MyListFragment.class.getSimpleName();
        private static final Uri URI = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        private String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
        private CursorAdapter mAdapter;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
//            mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, null, new String[]{MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            mAdapter = new MyAdapter(getActivity(), null, 0);
            setListAdapter(mAdapter);

            JSONObject outer = new JSONObject();
            JSONObject product = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject arrayObjOne = new JSONObject();
            JSONObject arrayObjTwo = new JSONObject();

            try {
                product.put("first_name", "joe").put("second_name", "strong").put("married", false);
//                arrayObjOne.put("some_boolean", false);
//                arrayObjTwo.put("some_boolean", true);
//                array.put(arrayObjOne);
//                array.put(arrayObjTwo);
                array.put(0, "string 0").put(1, "string 1").put(2, "string 2");
                product.put("string_array", array);
                outer.put("product", product);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String jsonAsString = outer.toString();
//            Log.d(TAG, jsonAsString);

            try {
                String example = outer.getJSONObject("product").getJSONArray("string_array").getString(1);
                Log.d(TAG, "example: " + example);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getLoaderManager().initLoader(0, null, this);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(getActivity(), URI, projection, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            mAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mAdapter.swapCursor(null);
        }
    }
}
