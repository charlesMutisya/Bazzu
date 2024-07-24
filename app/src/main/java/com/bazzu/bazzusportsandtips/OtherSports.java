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


public class OtherSports extends Fragment {
    private LinearLayoutManager mlayoutmanager;
    private RecyclerView mRecyclerView;
    private  HolderAdapterBazu mAdapter;
    private View view;
    public OtherSports() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_other_sports, container, false);
        ArrayList<ModelBazu> exampleList = new ArrayList<>();
        exampleList.add(new ModelBazu(R.drawable.tabletennis,"Table Tennis"));
        exampleList.add(new ModelBazu(R.drawable.handball,"Handball"));
        exampleList.add(new ModelBazu(R.drawable.tt,"Tennis"));
        exampleList.add(new ModelBazu(R.drawable.basketball,"Basketball"));
        exampleList.add(new ModelBazu(R.drawable.icehockey,"Ice Hockey"));

        mRecyclerView = view.findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        mlayoutmanager = new GridLayoutManager(getContext(),2);
        mAdapter = new HolderAdapterBazu(exampleList);
        mRecyclerView.setLayoutManager(mlayoutmanager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new HolderAdapterBazu.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Table Tennis Picks");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","table tennis");
                    startActivity(intent);
                }
                else if (position == 1){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Handball Tips");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","handball tips");
                    startActivity(intent);
                }
                else if (position == 2){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Tennis Tips");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","tennis tips");
                    startActivity(intent);
                }
                else if (position == 3){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Basketball Tips");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","basketball tips");
                    startActivity(intent);
                }
                else if (position == 4){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Ice Hockey Tips");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","ice hockey tips");
                    startActivity(intent);
                }
            }
        });



        return view;
    }
}