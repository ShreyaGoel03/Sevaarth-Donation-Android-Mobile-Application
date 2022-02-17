package com.example.sevaarth;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventsAdapter extends Adapter<EventsAdapter.ViewHolder> {

    private List<Events> listdata;
    private Context mContext;
    private String date;
    private String time,state,city,type,fulldetail;

    public EventsAdapter(List<Events> listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_events,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Events d=listdata.get(viewHolder.getAdapterPosition());
                 date =d.getDate();
                 time=d.getTime();
                 type=d.getType();
                 state=d.getState();
                 city=d.getCity();
                 fulldetail=d.getEvent_Details();
                 String location="city"+","+"state";
                 String daystart=date+time;




                // fulldetail=d.getEvent_Details();
                // Intent intent=new Intent(view.getContext(),DonorActivity2.class);
                long startTime = 0;
                //String startDate = "2011-09-01";
                try {
                    Date startdate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                    startTime = startdate.getTime();
                }
                catch(Exception e){ }

                Calendar calendarEvent = Calendar.getInstance();
                Intent i = new Intent(Intent.ACTION_EDIT);
                i.setType("vnd.android.cursor.item/event");
                //i.putExtra("beginTime", calendarEvent.getTimeInMillis());
                i.putExtra("beginTime",startTime);
                i.putExtra("allDay", true);
                i.putExtra("rule", "FREQ=YEARLY");
                i.putExtra("description",fulldetail);
                i.putExtra("hasalarm","true");
                i.putExtra("location",city+","+state);

              //  i.putExtra("endTime", "");

                i.putExtra("title", "Event for "+ type + " " + "Donation");

                view.getContext().startActivity(i);

                // view.getContext().startActivity(intent);

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Events ld=listdata.get(position);
        holder.txtdet.setText(ld.getEvent_Details());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtdet;
        public ViewHolder(View itemView) {
            super(itemView);
            txtdet=(TextView)itemView.findViewById(R.id.eventdet);
        }
    }
}
