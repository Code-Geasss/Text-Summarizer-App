package com.example.loginapp;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Member> members;

    public MyAdapter(ArrayList<Member> m)
    {
        members = m;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.input.setText(members.get(position).getInput());
        holder.output.setText(members.get(position).getOutput());
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder
    {
        View view;
        TextView input,output;
        public MyViewHolder (@NonNull View itemView)
        {
            super(itemView);
            input = itemView.findViewById(R.id.input);
            input.setMovementMethod(new ScrollingMovementMethod());
            output = itemView.findViewById(R.id.output);
            output.setMovementMethod(new ScrollingMovementMethod());
            view= itemView;
        }

    }
}
