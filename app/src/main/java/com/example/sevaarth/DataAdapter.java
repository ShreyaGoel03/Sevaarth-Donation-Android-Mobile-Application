package com.example.sevaarth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Data> listdata;
    private Context mContext;
    private String name;
    private String email;


    public DataAdapter(List<Data> listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.data,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data d=listdata.get(viewHolder.getAdapterPosition());
                name=d.getFullName();
                email=d.getEmail();
                Intent intent=new Intent(view.getContext(), DisplayDetails.class);
                intent.putExtra("NGO Name",name);
                intent.putExtra("Email id",email);
                view.getContext().startActivity(intent);
            }
        });

        //return new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data ld=listdata.get(position);
        holder.fullname.setText(ld.getFullName());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView fullname;
        public ViewHolder(View itemView) {
            super(itemView);
            fullname=(TextView)itemView.findViewById(R.id.fullname);
        }



    }


}

