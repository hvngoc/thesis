package com.hvngoc.googlemaptest.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.fragment.ProfileFragment;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.FriendHelpersAsyncTask;
import com.hvngoc.googlemaptest.model.Friend;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hoang Van Ngoc on 24/03/2016.
 */
public class RVFriendAdapter extends RecyclerView.Adapter<RVFriendAdapter.ViewHolder>{
    private List<Friend> mItems;
    private int visibilityAdd;
    private int visibilityDelete;
    private int visibilityAddRequest;
    private FragmentManager fragmentManager;
    private int type;
    public RVFriendAdapter(List<Friend> listItems, int visibilityAdd, int visibilityDelete, int visibilityAddRequest, FragmentManager manager, int type) {
        super();
        mItems = listItems;
        this.visibilityAdd = visibilityAdd;
        this.visibilityAddRequest = visibilityAddRequest;
        this.visibilityDelete = visibilityDelete;
        this.fragmentManager = manager;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_friend, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.btnFriendAdd.setVisibility(visibilityAdd);
        viewHolder.btnFriendAddRequest.setVisibility(visibilityAddRequest);
        viewHolder.btnFriendDelete.setVisibility(visibilityDelete);

        Friend item = mItems.get(i);
        viewHolder.txt_friendUserName.setText(item.getName());
        viewHolder.txt_friendNum.setText(item.getNumFriend() + "");
        viewHolder.txt_friendMutual.setText(item.getMutualFriend() + "");
        Picasso.with(GLOBAL.CurrentContext).load(item.getAvatar()).error(R.drawable.icon_profile).into(viewHolder.img_friendAvatar);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView img_friendAvatar;
        public TextView txt_friendUserName;
        public TextView txt_friendNum;
        public TextView txt_friendMutual;
        public ImageView btnFriendAdd, btnFriendDelete, btnFriendAddRequest;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            img_friendAvatar = (ImageView)itemView.findViewById(R.id.img_friendAvatar);
            txt_friendUserName = (TextView)itemView.findViewById(R.id.txt_friendUserName);
            txt_friendNum = (TextView)itemView.findViewById(R.id.txt_friendNum);
            txt_friendMutual = (TextView) itemView.findViewById(R.id.txt_friendMutual);
            btnFriendAdd = (ImageView) itemView.findViewById(R.id.btnFriendAdd);
            btnFriendDelete = (ImageView) itemView.findViewById(R.id.btnFriendDelete);
            btnFriendAddRequest = (ImageView) itemView.findViewById(R.id.btnFriendAddRequest);

            btnFriendAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    String id = mItems.get(position).getId();
                    FriendHelpersAsyncTask friendHelpersAsyncTask = new FriendHelpersAsyncTask(id);
                    friendHelpersAsyncTask.setDelegation(new DelegationHelper() {
                        @Override
                        public void doSomeThing() {
                            mItems.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    friendHelpersAsyncTask.runAddFriendAsyncTask();
                }
            });

            btnFriendAddRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    String id = mItems.get(position).getId();
                    FriendHelpersAsyncTask friendHelpersAsyncTask = new FriendHelpersAsyncTask(id);
                    friendHelpersAsyncTask.setDelegation(new DelegationHelper() {
                        @Override
                        public void doSomeThing() {
                            mItems.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    friendHelpersAsyncTask.runConfirmRequestAsyncTask();
                }
            });

            btnFriendDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    String id = mItems.get(position).getId();
                    FriendHelpersAsyncTask friendHelpersAsyncTask = new FriendHelpersAsyncTask(id);
                    friendHelpersAsyncTask.setDelegation(new DelegationHelper() {
                        @Override
                        public void doSomeThing() {
                            mItems.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    if (visibilityAddRequest == View.INVISIBLE)
                        friendHelpersAsyncTask.runDeleteFriendAsyncTask();
                    else
                        friendHelpersAsyncTask.runDeleteRequestAsyncTask();
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String id = mItems.get(position).getId();
            Log.i("ONCLICK", "ONCLICK");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, ProfileFragment.getInstance(id, type));
            fragmentTransaction.commit();
        }
    }
}
