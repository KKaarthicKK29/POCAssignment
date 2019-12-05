package com.example.karthickmadasamy.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karthickmadasamy.myapplication.R;
import com.example.karthickmadasamy.myapplication.models.Rows;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.karthickmadasamy.myapplication.utils.SquareImageView;

import java.util.List;

/**
 * Created by Karthick.Madasamy on 12/4/2019.
 */

public class FeederAdapter extends RecyclerView.Adapter<FeederAdapter.FeederHolder> {
    private String TAG=FeederAdapter.this.getClass().getName();
    private List<Rows> rowsList;
    private Context context;
    OnItemClickListener onItemClickListener;
    public FeederAdapter(List<Rows> rowsList, Context context, OnItemClickListener onItemClickListener) {
        this.rowsList = rowsList;
        this.context = context;
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public FeederHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feeder_rows,parent,false);
        FeederHolder FeederHolder= new FeederHolder(v);
        return FeederHolder;
    }

    @Override
    public void onBindViewHolder(FeederHolder holder, int position) {
        holder.tvTitle.setText(rowsList.get(position).getTitle());
        holder.tvDescription.setText(rowsList.get(position).getDescription());
        if(rowsList.get(position).getDescription()!=null && !rowsList.get(position).getDescription().equalsIgnoreCase("")) // set description values if values is not null or not empty
            holder.tvDescription.setText(rowsList.get(position).getDescription());
        else
        holder.tvDescription.setText(context.getResources().getString(R.string.description_not_available));
        holder.click(rowsList.get(position),onItemClickListener);
        Glide.with(context).load(rowsList.get(position).getImageHref()).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.lazyimage).into(holder.imageViewFeeder);
    }

    @Override
    public int getItemCount() {
        return rowsList.size();
    }

    public interface OnItemClickListener {
        void onClick(Rows rows);
    }

    public class FeederHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDescription;
        private SquareImageView imageViewFeeder;
        public FeederHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_title);
            tvDescription = v.findViewById(R.id.tv_description);
            imageViewFeeder = v.findViewById(R.id.imageView_Feeder);
        }
        public void click(final Rows rows, final OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(rows);
                }
            });
        }
    }
}

