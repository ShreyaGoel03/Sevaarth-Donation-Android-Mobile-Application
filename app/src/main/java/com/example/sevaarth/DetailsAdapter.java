package com.example.sevaarth;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.Detailsviewholder>{

    private ArrayList<Donation_Data_Org> donationholder;
    private int flag;

    public DetailsAdapter(ArrayList<Donation_Data_Org> donationholder, int flag){
        this.donationholder = donationholder;
        this.flag = flag;
    }
    @NonNull
    @Override
    public Detailsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donationsrvfrag, parent, false);
        Detailsviewholder detailsviewholder = new Detailsviewholder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Donation_Data_Org org = donationholder.get(detailsviewholder.getAdapterPosition());
                String key = org.getKey();
                Intent intent = new Intent(view.getContext(), Donation_Details_Org.class);
                intent.putExtra("Key", key);
                view.getContext().startActivity(intent);
            }
        });

        return detailsviewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull Detailsviewholder holder, int position) {

        holder.name.setText(donationholder.get(position).getName());
        holder.email.setText((donationholder.get(position).getEmail()).replace(",", "."));
        if (flag == 1){
            holder.date.setText("Donated on ");
            holder.date_val.setText(donationholder.get(position).getDate_donated());
        }
        else{
            holder.date.setText("Received on ");
            holder.date_val.setText(donationholder.get(position).getDate_received());
        }
        Donation_Data_Org item = donationholder.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return donationholder.size();
    }

    public class Detailsviewholder extends RecyclerView.ViewHolder{

        private Donation_Data_Org mitem;
        TextView name, email, date, date_val;
        public Detailsviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            date = itemView.findViewById(R.id.date);
            date_val = itemView.findViewById(R.id.date_val);
        }

        public void bind(Donation_Data_Org item){
            mitem = item;
        }
    }

}
