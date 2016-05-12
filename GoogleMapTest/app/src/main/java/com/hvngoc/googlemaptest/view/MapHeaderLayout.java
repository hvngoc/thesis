package com.hvngoc.googlemaptest.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
import com.hvngoc.googlemaptest.adapter.SearchPostAdapter;
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
        autocompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);
        search_text_header.setVisibility(viewSearch);
        img_header_search.setVisibility(viewSearch);
    }
//    *************************************************************************************
    private void init(){
        inflate(getContext(), R.layout.layout_map_header, this);
        autocompleteFragment = (PlaceAutocompleteFragment)((Activity)GLOBAL.CurentContext).getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("tap here for searching");

        search_text_header = (AutoCompleteTextView) findViewById(R.id.search_text_header);
        search_text_header.setDropDownBackgroundResource(R.color.white);
        search_text_header.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search_text_header.showDropDown();
                return false;
            }
        });
        search_text_header.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = (Post) search_text_header.getAdapter().getItem(position);
                currentListPost.clear();
                currentListPost.add(post);
                delegationHelper.doSomeThing();
                search_text_header.setText("");
            }
        });
        search_text_header.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 30)
                    return;
                if (text.length() > 0)
                    new SearchPostAsyncTask(text).execute();
            }
        });

        img_header_search = (ImageView) findViewById(R.id.img_header_search);
        img_header_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delegationHelper.doSomeThing();
            }
        });

        img_header_action = (ImageView) findViewById(R.id.img_header_action);
        img_header_action.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        img_header_action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iconizedMenu.show();
            }
        });

        InitIconActionMenu();
        RunSearchOption(R.id.map_search_post);
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
            case R.id.map_search_post:
                setEnableView(View.INVISIBLE, View.VISIBLE);
                break;
            case R.id.map_search_place:
                setEnableView(View.VISIBLE, View.INVISIBLE);
                break;
        }
    }

//    ****************************************************************************
    private class SearchPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String text;
        public SearchPostAsyncTask(String text){
            this.text = text;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "SearchPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("params", text);
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
                search_text_header.setAdapter(new SearchPostAdapter(GLOBAL.CurentContext, android.R.layout.simple_list_item_1, currentListPost));
                search_text_header.showDropDown();
            }
        }
    }
}
