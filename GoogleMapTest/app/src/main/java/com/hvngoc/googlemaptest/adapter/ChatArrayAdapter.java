package com.hvngoc.googlemaptest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.model.ChatMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 12125_000 on 5/11/2016.
 */
public class ChatArrayAdapter extends ArrayAdapter {
    private List chatMessageList = new ArrayList();
    @Override
    public void add(Object object) {
        super.add(object);
        chatMessageList.add(object);
        notifyDataSetChanged();
    }

    public void addListMessage(List<ChatMessage> listMessage) {
        if(listMessage != null) {
            Collections.reverse(listMessage);
            chatMessageList.addAll(listMessage);
            notifyDataSetChanged();
        }
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return (ChatMessage)this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.card_item_chat_singlemessage, parent, false);
        }
        CircleImageView avatar = (CircleImageView) row.findViewById(R.id.img_friendAvatar);
        Picasso.with(GLOBAL.CurrentContext).load(getItem(position).getSenderAvatar()).error(R.drawable.icon_profile).into(avatar);
        RelativeLayout singleMessageContainer = (RelativeLayout) row.findViewById(R.id.singleMessageContainer);
        ChatMessage chatMessageObj = getItem(position);
        TextView chatText = (TextView) row.findViewById(R.id.singleMessage);
        chatText.setText(chatMessageObj.getMessage());
        chatText.setBackgroundResource(chatMessageObj.isLeft() ? R.drawable.bubble_b : R.drawable.bubble_a);
        singleMessageContainer.setGravity(chatMessageObj.isLeft() ? Gravity.LEFT : Gravity.RIGHT);
        avatar.setVisibility(chatMessageObj.isLeft() ? View.VISIBLE : View.INVISIBLE);
        return row;
    }
}
