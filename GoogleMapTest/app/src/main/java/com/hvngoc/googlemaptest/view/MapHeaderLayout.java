package com.hvngoc.googlemaptest.view;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.custom.IconizedMenu;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Hoang Van Ngoc on 09/05/2016.
 */
public class MapHeaderLayout extends RelativeLayout {

    private ImageView img_header_action;
    private AutoCompleteTextView search_text_header;
    private ImageView img_header_search;
    private PlaceAutocompleteFragment autocompleteFragment;
    private IconizedMenu iconizedMenu;

    public MapHeaderLayout(Context context) {
        super(context);
        init();
    }

    private ArrayList<Post> currentListPost = new ArrayList<>();
    public ArrayList<Post> getCurrentListPost(){
        return this.currentListPost;
    }

    private PlaceSelectionListener placeSelectionListener;
    public void setOnPlaceSelectionListener(PlaceSelectionListener listener){
        this.placeSelectionListener = listener;
    }

    private DelegationHelper delegationHelper;
    public void setDelegationHelper(DelegationHelper helper){
        this.delegationHelper = helper;
    }

    public void setTextPlaceFragment(Place place){
        autocompleteFragment.setText(place.getAddress().toString());
    }

    private void setEnableView(int viewPlace, int viewSearch){
        findViewById(R.id.place_autocomplete_fragment).setVisibility(viewPlace);
        search_text_header.setVisibility(viewSearch);
        img_header_search.setVisibility(viewSearch);
    }
//    *************************************************************************************
    private void init(){
        inflate(getContext(), R.layout.layout_map_header, this);
        autocompleteFragment = (PlaceAutocompleteFragment)((Activity)GLOBAL.CurentContext).getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("tap here for searching");
        autocompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);

        search_text_header = (AutoCompleteTextView) findViewById(R.id.search_text_header);
        search_text_header.setDropDownBackgroundResource(R.color.white);
        search_text_header.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search_text_header.showDropDown();
                return false;
            }
        });

        img_header_search = (ImageView) findViewById(R.id.img_header_search);
        img_header_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        img_header_action = (ImageView) findViewById(R.id.img_header_action);
        img_header_action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iconizedMenu.show();
            }
        });

        InitIconActionMenu();
        RunSearchOption(R.id.map_search_name);
        setEnableView(View.INVISIBLE, View.VISIBLE);
    }

    private void InitIconActionMenu(){
        iconizedMenu = new IconizedMenu(GLOBAL.CurentContext, img_header_action);
        iconizedMenu.getMenuInflater().inflate(R.menu.menu_map_search_engine, iconizedMenu.getMenu());
        iconizedMenu.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                RunSearchOption(item.getItemId());
                iconizedMenu.dismiss();
                return false;
            }
        });
    }

    private void RunSearchOption(int id){
        switch (id){
            case R.id.map_search_name:
                new GetListFriendNameAsyncTask().execute();
                search_text_header.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String text = search_text_header.getAdapter().getItem(position).toString();
                        search_text_header.setText(text);
                        currentListPost.clear();
                        new SearchPostByNameAsyncTask(text).execute();
                    }
                });
                setEnableView(View.INVISIBLE, View.VISIBLE);
                break;
            case R.id.map_search_tag:
                search_text_header.setAdapter(new ArrayAdapter<>(GLOBAL.CurentContext, android.R.layout.simple_dropdown_item_1line, GLOBAL.listTag));
                search_text_header.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String text = search_text_header.getAdapter().getItem(position).toString();
                        search_text_header.setText(text);
                        currentListPost.clear();
                        new SearchPostByTagAsyncTask(text).execute();
                    }
                });
                setEnableView(View.INVISIBLE, View.VISIBLE);
                break;
            case R.id.map_search_place:
                setEnableView(View.VISIBLE, View.INVISIBLE);
                break;
        }
    }

//    ****************************************************************************
    private class GetListFriendNameAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "GetListFriendName";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<String>>(){}.getType();
                ArrayList<String> listName = gson.fromJson(res, listType);
                search_text_header.setAdapter(new ArrayAdapter<>(GLOBAL.CurentContext, android.R.layout.simple_dropdown_item_1line, listName));
            }
        }
    }
    private class SearchPostByNameAsyncTask extends AsyncTask<Void, Void, Boolean> {
        String name;
        private HTTPPostHelper helper;

        public SearchPostByNameAsyncTask(String name){
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "SearchPostByName";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("name", name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
                currentListPost = gson.fromJson(res, listType);
                delegationHelper.doSomeThing();
            }
        }
    }

    private class SearchPostByTagAsyncTask extends AsyncTask<Void, Void, Boolean> {
        String tag;
        private HTTPPostHelper helper;

        public SearchPostByTagAsyncTask(String tag){
            this.tag = tag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "SearchPostByTag";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("tag", tag);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
                currentListPost = gson.fromJson(res, listType);
                delegationHelper.doSomeThing();
            }
        }
    }
}
