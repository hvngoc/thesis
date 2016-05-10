package com.hvngoc.googlemaptest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hoang Van Ngoc on 10/05/2016.
 */
public class SearchPostAdapter extends ArrayAdapter<Post> {

    private LayoutInflater layoutInflater;
    private List<Post> postList;

    public SearchPostAdapter(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        postList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.card_item_search_post, null);

        Post item = postList.get(position);
        ImageView img_search_avatar = (ImageView) convertView.findViewById(R.id.img_search_avatar);
        Picasso.with(GLOBAL.CurentContext).load(item.getUserAvatar()).error(R.drawable.icon_profile).into(img_search_avatar);

        TextView txt_search_username = (TextView) convertView.findViewById(R.id.txt_search_username);
        txt_search_username.setText(item.userName);

        TextView txt_search_posted = (TextView) convertView.findViewById(R.id.txt_search_posted);
        txt_search_posted.setText(item.relationShip + " on tag: ");

        TextView txt_search_list_tag = (TextView) convertView.findViewById(R.id.txt_search_list_tag);
        txt_search_list_tag.setText(item.tag);

        TextView txt_search_content = (TextView) convertView.findViewById(R.id.txt_search_content);
        txt_search_content.setText(item.getContent());

        return convertView;
    }
}
