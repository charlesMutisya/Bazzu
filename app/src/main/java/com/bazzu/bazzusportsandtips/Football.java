package com.bazzu.bazzusportsandtips;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class Football extends Fragment {

    LinearLayoutManager mlayoutmanager;
    RecyclerView mRecyclerView;
    private  HolderAdapterBazu mAdapter;
    private View view;

    public Football() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_football, container, false);
        ArrayList<ModelBazu> exampleList = new ArrayList<>();
        exampleList.add(new ModelBazu(R.drawable.dailyfootball,"Daily Tips"));
        exampleList.add(new ModelBazu(R.drawable.supersingles,"Super Singles"));
        exampleList.add(new ModelBazu(R.drawable.daily2plus,"Daily 2+"));
        exampleList.add(new ModelBazu(R.drawable.goalscorer,"Goal Scorer"));
        exampleList.add(new ModelBazu(R.drawable.corner,"Corner Tips"));
        exampleList.add(new ModelBazu(R.drawable.goalscorer,"Card Tips"));
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mlayoutmanager = new GridLayoutManager(getContext(),2);
        mAdapter = new HolderAdapterBazu(exampleList);
        mRecyclerView.setLayoutManager(mlayoutmanager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new HolderAdapterBazu.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position==0) {
                    Intent intentt = new Intent(getContext(), BazengafbGames.class);
                    intentt.putExtra("title", "Daily Tips");
                    intentt.putExtra("db", "BazuJackpots");
                    intentt.putExtra("selected", "daily tips");
                    startActivity(intentt);
                }
                else if (position == 1){
                    Intent intent0 = new Intent(getContext(), BazengafbGames.class);
                    intent0.putExtra("title", "Super Singles");
                    intent0.putExtra("db", "BazuJackpots");
                    intent0.putExtra("selected","super singles");
                    startActivity(intent0);
                }
                else if (position == 2){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Daily 2+ Odds");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","daily 2+ odds");
                    startActivity(intent);
                }
                else if (position == 3){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Goal Scorer");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","goal scorer");
                    startActivity(intent);
                }
                else if (position == 4){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Corner Tips");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","corner tips");
                    startActivity(intent);
                }
                else if (position == 5){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Card Tips");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","card tips");
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}