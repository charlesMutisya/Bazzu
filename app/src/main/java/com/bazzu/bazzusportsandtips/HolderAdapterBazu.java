package com.bazzu.bazzusportsandtips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HolderAdapterBazu extends RecyclerView.Adapter<HolderAdapterBazu.ExampleViewHolder> {
    public View itemView;
    private ArrayList<ModelBazu> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static  class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView1;
        public ImageView mImageView;
        public ExampleViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }

                    }
                }
            });
        }
    }
    public HolderAdapterBazu(ArrayList<ModelBazu> exampleList){
        mExampleList = exampleList;
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bazenga_football_model, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ModelBazu currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
     }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


}
