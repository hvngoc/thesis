package com.hvngoc.googlemaptest.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.Global;
import com.hvngoc.googlemaptest.adapter.RVAdapter;
import com.hvngoc.googlemaptest.model.NewsItem;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private List<NewsItem> news;
    private RecyclerView listnews;

    public HomeFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        loadComponents(rootView);
        initData();
        initListNewsAdapter();
        return rootView;
    }

    private void loadComponents(View rootView) {
        listnews = (RecyclerView) rootView.findViewById(R.id.list_news);
        LinearLayoutManager llm = new LinearLayoutManager(Global.CurentContext);
        listnews.setLayoutManager(llm);
        listnews.setHasFixedSize(true);
    }

    private void initData(){
        news = new ArrayList<NewsItem>();
        news.add(new NewsItem("Nguyễn Văn Tường", "The most beatiful place in the world, The most beatiful place in the world, The most beatiful place in the world", R.drawable.image1));
        news.add(new NewsItem("Ngô Thị Liên", "Flowers of Dalat. Go, go, go, ...", R.drawable.image1));
        news.add(new NewsItem("Nguyễn Minh Nhân", "Special feeling!", R.drawable.image2));
        news.add(new NewsItem("Trương Thanh Sang", "Yesterday!", R.drawable.image4));
        news.add(new NewsItem("Trần Ngọc Như", "Come here with me!", R.drawable.image5));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image6));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image1));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image2));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image1));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image4));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image5));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image6));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image1));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image2));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image4));

    }

    private void initListNewsAdapter(){
        RVAdapter adapter = new RVAdapter(news);
        listnews.setAdapter(adapter);
        /*
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        listnews.setLayoutManager(gridLayoutManager);
        */
       // listnews.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}