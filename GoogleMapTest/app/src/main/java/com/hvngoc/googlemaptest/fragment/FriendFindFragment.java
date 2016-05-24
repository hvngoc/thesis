package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.FriendHelpersAsyncTask;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Friend;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendFindFragment extends Fragment {

    CardView cardFindFriend, cardFindNothing;
    CircleImageView img_find_avatar;
    TextView txt_find_name;
    TextView txt_friendNum, txt_friendMutual;
    TextView txt_addFriend, txt_addMoreDetail;
    ImageView img_add_Friend;
    ImageView btnFriendSearch;
    EditText editFriendSearch;

    private Friend friend;
    ProgressDialog progressDialog = null;

    public FriendFindFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_find, container, false);

        cardFindFriend = (CardView) view.findViewById(R.id.cardFindFriend);
        cardFindNothing = (CardView) view.findViewById(R.id.cardFindNothing);
        img_find_avatar = (CircleImageView) view.findViewById(R.id.img_find_avatar);
        txt_find_name = (TextView) view.findViewById(R.id.txt_find_name);
        txt_friendNum = (TextView) view.findViewById(R.id.txt_friendNum);
        txt_friendMutual = (TextView) view.findViewById(R.id.txt_friendMutual);
        txt_addFriend = (TextView) view.findViewById(R.id.txt_addFriend);
        txt_addMoreDetail = (TextView) view.findViewById(R.id.txt_addMoreDetail);
        img_add_Friend = (ImageView) view.findViewById(R.id.img_add_Friend);
        editFriendSearch = (EditText) view.findViewById(R.id.editFriendSearch);
        btnFriendSearch = (ImageView) view.findViewById(R.id.btnFriendSearch);

        txt_addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FriendHelpersAsyncTask friendHelpersAsyncTask = new FriendHelpersAsyncTask(friend.getId());
                if (friend.getIsFriend() == 0) {
                    friendHelpersAsyncTask.runAddFriendAsyncTask();
                    friendHelpersAsyncTask.setDelegation(new DelegationHelper() {
                        @Override
                        public void doSomeThing() {
                            Toast.makeText(getContext(), getString(R.string.send_request_ok),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    
                    friendHelpersAsyncTask.runDeleteFriendAsyncTask();
                    friendHelpersAsyncTask.setDelegation(new DelegationHelper() {
                        @Override
                        public void doSomeThing() {
                            friend.setIsFriend(0);
                            ChangeLayoutUnFriend();
                        }
                    });
                }
            }
        });
        txt_addMoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = CONSTANT.TYPE_FRIEND;
                if (friend.getIsFriend() == 0) {
                    type = CONSTANT.TYPE_SUGGEST;
                }
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_body, ProfileFragment.getInstance(friend.getId(), type));
                fragmentTransaction.commit();
            }
        });
        btnFriendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editFriendSearch.getText().toString();
                if (text.length() == 0)
                    Toast.makeText(getContext(), getString(R.string.input_email), Toast.LENGTH_SHORT).show();
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches())
                    Toast.makeText(getContext(), getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                else {
                    Log.i("onclick find", "click find friend OKKKK");
                    progressDialog = new ProgressDialog(getActivity(),
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getString(R.string.searching));
                    progressDialog.show();
                    new SearchFriendByEmailAsyncTask(text).execute();
                }
            }
        });

        setLayoutContent(View.INVISIBLE, View.VISIBLE);
        return view;
    }


    private void setDataFriendFounded(){
        Picasso.with(GLOBAL.CurrentContext).load(friend.getAvatar()).error(R.drawable.icon_profile).into(img_find_avatar);
        txt_find_name.setText(friend.getName());
        txt_friendNum.setText(friend.getNumFriend() + "");
        txt_friendMutual.setText(friend.getMutualFriend() + "");
        ChangeLayoutUnFriend();
    }

    private void ChangeLayoutUnFriend(){
        if (friend.getIsFriend() == 0){
            txt_addFriend.setText(getString(R.string.hint_add_friend));
            txt_addFriend.setTextColor(Color.parseColor("#030fff"));
            img_add_Friend.setImageResource(R.drawable.ic_friend_add_black);
        }
        else {
            txt_addFriend.setText(getString(R.string.un_friend));
            txt_addFriend.setTextColor(Color.RED);
            img_add_Friend.setImageResource(R.drawable.ic_friend_delete);
        }
    }

    private void setLayoutContent(int visibleCardFriend, int visibleNothing){
        cardFindFriend.setVisibility(visibleCardFriend);
        cardFindNothing.setVisibility(visibleNothing);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class SearchFriendByEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String text;
        public SearchFriendByEmailAsyncTask(String text) {
            this.text = text;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "searchFriendByEmail";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("email", text);
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
            if (result){
                String res = helper.getResponse();
                Gson gson = new Gson();
                friend = gson.fromJson(res, Friend.class);
                setLayoutContent(View.VISIBLE, View.INVISIBLE);
                setDataFriendFounded();
            }
            else{
                setLayoutContent(View.INVISIBLE, View.VISIBLE);
            }
            progressDialog.dismiss();
        }
    }
}