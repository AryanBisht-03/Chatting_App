package com.example.tryingwhatsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tryingwhatsapp.Models.StatusModel;
import com.example.tryingwhatsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    ArrayList<StatusModel> list;
    Context context;

    public StatusAdapter(ArrayList<StatusModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_status,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StatusModel status = list.get(position);

        Picasso.get().load(status.getProfilpic()).placeholder(R.drawable.ic_man).into(holder.image);

        holder.status.setText(status.getStatus());

        holder.name.setText(status.getUserName());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView status,name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profilpicStatus);
            status = itemView.findViewById(R.id.statusMessage);
            name = itemView.findViewById(R.id.userNameStatus);

        }

    }
}
