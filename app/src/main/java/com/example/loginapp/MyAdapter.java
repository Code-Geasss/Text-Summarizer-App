package com.example.loginapp;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  implements Filterable {
    Context context;
    ArrayList<Member> members;
    List<Member> mDataFiltered ;
    boolean isDark = false;

    public MyAdapter(Context mContext,ArrayList<Member> m,boolean isDark)
    {
        this.context = mContext;
        this.members = m;
        this.isDark = isDark;
        this.mDataFiltered = members;
    }
    public MyAdapter(Context mContext,ArrayList<Member> m)
    {
        this.context = mContext;
        this.members = m;
        this.mDataFiltered = members;
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
        holder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale));

        holder.input.setText(members.get(position).getInput());
        holder.output.setText(members.get(position).getOutput());
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter()
    {
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();
                if(key.isEmpty())
                {
                    mDataFiltered = members;
                }
                else{
                    List<Member> lstFiltered = new ArrayList<>();
                    for (Member row : members) {

                        if (row.getInput().toLowerCase().contains(key.toLowerCase())) {
                            lstFiltered.add(row);
                        }
                    }
                    mDataFiltered = lstFiltered;
                }
                FilterResults filterResults =new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (List<Member>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder
    {
        View view;
        TextView input,output;
        RelativeLayout container;
        public MyViewHolder (@NonNull View itemView)
        {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            input = itemView.findViewById(R.id.input);
            input.setMovementMethod(new ScrollingMovementMethod());
            output = itemView.findViewById(R.id.output);
            output.setMovementMethod(new ScrollingMovementMethod());

            if (isDark) {
                setDarkTheme();
            }
            view = itemView;
        }

        private void setDarkTheme()
        {
            container.setBackgroundResource(R.drawable.card_bg_dark);
        }

    }
}
