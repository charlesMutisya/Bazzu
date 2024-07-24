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

public class JackPotsBazenga extends Fragment {
    private LinearLayoutManager mlayoutmanager;
    private RecyclerView mRecyclerView;
    private  HolderAdapterBazu mAdapter;
    private View view;

    public JackPotsBazenga() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_jack_pots, container, false);
        ArrayList<ModelBazu> exampleList = new ArrayList<>();
        exampleList.add(new ModelBazu(R.drawable.tikamidweek,"Betika Midweek Jp"));
        exampleList.add(new ModelBazu(R.drawable.tika,"Betika Grand Jp"));
        exampleList.add(new ModelBazu(R.drawable.spmidweek,"Sportpesa Midweek"));
        exampleList.add(new ModelBazu(R.drawable.sppro,"Sportpesa Mega"));
        exampleList.add(new ModelBazu(R.drawable.otherjp,"Other Jackpots"));
        exampleList.add(new ModelBazu(R.drawable.mztweekly,"Mozzartbet Weekly"));
        mRecyclerView = view.findViewById(R.id.recycler_view3);
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
                    intentt.putExtra("title", "Betika Midweek Jackpot");
                    intentt.putExtra("db", "BazuJackpots");
                    intentt.putExtra("selected", "betika midweek");
                    startActivity(intentt);
                }
                else if (position == 1){
                    Intent intent0 = new Intent(getContext(), BazengafbGames.class);
                    intent0.putExtra("title", "Betika Grand Jackpot");
                    intent0.putExtra("db", "BazuJackpots");
                    intent0.putExtra("selected","betika grand");
                    startActivity(intent0);
                }
                else if (position == 2){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Sportpesa Midweek Jackpot");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","sportpesa midweek");
                    startActivity(intent);
                }
                else if (position == 3){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Sportpesa Mega Jackpot");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","sportpesa mega");
                    startActivity(intent);
                }
                else if (position == 4){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Other Jackpots");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","betpawa weekend");
                    startActivity(intent);
                }
                else if (position == 5){
                    Intent intent = new Intent(getContext(), BazengafbGames.class);
                    intent.putExtra("title", "Mozzart Weekly Jackpot");
                    intent.putExtra("db", "BazuJackpots");
                    intent.putExtra("selected","mozzart weekly");
                    startActivity(intent);
                }
            }
        });


        return view;
    }
}