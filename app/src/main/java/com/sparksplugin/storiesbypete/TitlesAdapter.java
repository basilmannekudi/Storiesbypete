package com.sparksplugin.storiesbypete;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesAdapter.ViewHolder> {

    private Context mContext;
    private List<Titles> mTitles;
    public TitlesAdapter(Context mContext, List<Titles> mTitles){
        this.mTitles = mTitles;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.title_item_2, parent, false);
        return new TitlesAdapter.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Titles titles = mTitles.get(position);
        holder.title_name.setText(titles.getName());
        holder.date.setText(titles.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Loading", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,StoryActivity.class);
                intent.putExtra("name", titles.getName());
                intent.putExtra("epi", "1");
                intent.putExtra("epn",titles.getEpn());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return mTitles.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView date;
        public TextView title_name;
        public ImageView title_img;
        CardView bgLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.title_date);
            title_img = itemView.findViewById(R.id.im_title);
            bgLayout = itemView.findViewById(R.id.token_item_bg);
            title_name = itemView.findViewById(R.id.title_name);


        }
    }
}
