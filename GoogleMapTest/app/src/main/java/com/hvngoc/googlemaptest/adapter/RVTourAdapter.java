package com.hvngoc.googlemaptest.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.activity.TourDetailActivity;
import com.hvngoc.googlemaptest.custom.ConfirmDialog;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.model.Tour;
import com.hvngoc.googlemaptest.services.TourCreationService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hoang Van Ngoc on 21/06/2016.
 */
public class RVTourAdapter extends RecyclerView.Adapter<RVTourAdapter.NewsItemViewHolder> {

    private ArrayList<Tour> listTour;
    private String userID;

    public RVTourAdapter(ArrayList<Tour> listTour, String userID) {
        this.listTour = listTour;
        this.userID = userID;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_tour, viewGroup, false);
        return new NewsItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsItemViewHolder newsViewHolder, int i) {
        Tour tour = listTour.get(i);
        Picasso.with(GLOBAL.CurrentContext).load(tour.getUserAvatar()).error(R.drawable.icon_profile).into(newsViewHolder.imgAvatar);
        newsViewHolder.txtUserName.setText(tour.getUserName());
        if (tour.getStatus() == 1){
            newsViewHolder.imgTourLive.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            newsViewHolder.txtTourLive.setText(GLOBAL.CurrentContext.getString(R.string.tour_status_live));
            newsViewHolder.txtTourLive.setTextColor(Color.RED);
        }
        else {
            newsViewHolder.imgTourLive.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            newsViewHolder.txtTourLive.setText(GLOBAL.CurrentContext.getString(R.string.tour_status_stop));
            newsViewHolder.txtTourLive.setTextColor(Color.GRAY);
        }
        newsViewHolder.txtStartDate.setText(ParseDateTimeHelper.parse(tour.getStartDate()));
        newsViewHolder.txtAddressStart.setText(tour.getAddressStart());
        newsViewHolder.txtNumLikeStart.setText(tour.getStartNumLike() + "");
        newsViewHolder.txtNumCommentStart.setText(tour.getStartNumComment() + "");
        newsViewHolder.txtNumSharedStart.setText(tour.getStartNumShare() + "");

        newsViewHolder.txtTourNumber.setText(tour.getNumPlaces() + "");
        newsViewHolder.txtDistanceNumber.setText(tour.getDistanceNumber() + " km");

        newsViewHolder.txtStopDate.setText(ParseDateTimeHelper.parse(tour.getStopDate()));
        newsViewHolder.txtAddressStop.setText(tour.getAddressTop());
        newsViewHolder.txtNumLikeStop.setText(tour.getStopNumLike() + "");
        newsViewHolder.txtNumCommentStop.setText(tour.getStopNumComment() + "");
        newsViewHolder.txtNumSharedStop.setText(tour.getStopNumShare() + "");
    }

    @Override
    public int getItemCount() {
        return listTour.size();
    }

    class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CircleImageView imgAvatar;
        public TextView txtUserName;
        public ImageView imgTourLive;
        public TextView txtTourLive;

        public TextView txtStartDate, txtAddressStart;
        public TextView txtNumLikeStart, txtNumCommentStart, txtNumSharedStart;

        public TextView txtDistanceNumber, txtTourNumber;

        public TextView txtAddressStop, txtStopDate;
        public TextView txtNumLikeStop, txtNumCommentStop, txtNumSharedStop;
        public LinearLayout layout_tour_live;

        public NewsItemViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            imgAvatar = (CircleImageView) itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            imgTourLive = (ImageView) itemView.findViewById(R.id.imgTourLive);
            txtTourLive = (TextView) itemView.findViewById(R.id.txtTourLive);
            txtStartDate = (TextView) itemView.findViewById(R.id.txtStartDate);
            txtAddressStart = (TextView) itemView.findViewById(R.id.txtAddressStart);
            txtNumLikeStart = (TextView) itemView.findViewById(R.id.txtNumLikeStart);
            txtNumCommentStart = (TextView) itemView.findViewById(R.id.txtNumCommentStart);
            txtNumSharedStart = (TextView) itemView.findViewById(R.id.txtNumSharedStart);
            txtDistanceNumber = (TextView) itemView.findViewById(R.id.txtDistanceNumber);
            txtTourNumber = (TextView) itemView.findViewById(R.id.txtTourNumber);
            txtAddressStop = (TextView) itemView.findViewById(R.id.txtAddressStop);
            txtStopDate = (TextView) itemView.findViewById(R.id.txtStopDate);
            txtNumLikeStop = (TextView) itemView.findViewById(R.id.txtNumLikeStop);
            txtNumCommentStop = (TextView) itemView.findViewById(R.id.txtNumCommentStop);
            txtNumSharedStop = (TextView) itemView.findViewById(R.id.txtNumSharedStop);
            layout_tour_live = (LinearLayout) itemView.findViewById(R.id.layout_tour_live);
            layout_tour_live.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userID.equals(GLOBAL.CurrentUser.getId())){
                        if (listTour.get(getAdapterPosition()).getStatus() == 1){
                            final ConfirmDialog confirmDialog = new ConfirmDialog(GLOBAL.CurrentContext,
                                    GLOBAL.CurrentContext.getString(R.string.stop_current_post));
                            confirmDialog.setOnButtonOKClick(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new StopTourAsyncTask(GLOBAL.TOUR_ON_STARTING, getAdapterPosition()).execute();
                                    GLOBAL.CurrentContext.stopService(new Intent(GLOBAL.CurrentContext.getApplicationContext(),
                                            TourCreationService.class));
                                    confirmDialog.dismiss();
                                }
                            });
                            confirmDialog.show();
                        }
//                        else{
//                            if (GLOBAL.TOUR_ON_STARTING != null){
//                                ConfirmDialog confirmDialog = new ConfirmDialog(GLOBAL.CurrentContext,
//                                        GLOBAL.CurrentContext.getString(R.string.tour_confirm));
//                                confirmDialog.setOnButtonOKClick(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        new StopTourAsyncTask(GLOBAL.TOUR_ON_STARTING).execute();
//                                        GLOBAL.CurrentContext.stopService(new Intent(GLOBAL.CurrentContext.getApplicationContext(),
//                                                TourCreationService.class));
//
//                                        GLOBAL.TOUR_ON_STARTING = listTour.get(getAdapterPosition()).getId();
//                                    }
//                                });
//                                confirmDialog.show();
//                            }
//                            else {
//
//                            }
//                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            String tourID = listTour.get(getAdapterPosition()).getId();
            Intent intent = new Intent(GLOBAL.CurrentContext, TourDetailActivity.class);
            intent.putExtra("tourID", tourID);
            intent.putExtra("userID", userID);
            intent.putExtra("status", listTour.get(getAdapterPosition()).getStatus());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            GLOBAL.CurrentContext.startActivity(intent);
        }
    }

//    ---------------------------------------------------------------------------------------------------------------
    private class StopTourAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String data;
        private int position;
        public StopTourAsyncTask(String data, int position){
            this.data = data;
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "stopTourLive";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("tourID", data);
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
                listTour.get(position).setStatus(0);
                notifyDataSetChanged();
                GLOBAL.TOUR_ON_STARTING = null;
            }
        }
    }
}
