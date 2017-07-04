package com.example.da08.firebasebbs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.da08.firebasebbs.domain.Bbs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Da08 on 2017. 7. 3..
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {
    List<Bbs>  data = new ArrayList<>();
    private LayoutInflater inflater;

    public RecyclerAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Bbs> data){
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Bbs bbs = data.get(position);
        holder.txtId.setText(bbs.id+"");
        holder.txtTitle.setText(bbs.title);
        holder.txtAuthor.setText(bbs.author);
        holder.setCount(bbs.count);
        holder.setDate(convertLongToString(bbs.date));
        holder.setPosition(position);

    }
    private String convertLongToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return sdf.format(date);
    }

    class Holder extends RecyclerView.ViewHolder{
        private int position;
       private TextView txtId, txtTitle, txtAuthor, txtCount, txtDate;

        public Holder(final View itemView){
            super(itemView);
            txtId = (TextView)itemView.findViewById(R.id.txtId);
            txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            txtAuthor = (TextView)itemView.findViewById(R.id.txtAuthor);
            txtCount = (TextView)itemView.findViewById(R.id.txtCount);
            txtDate = (TextView)itemView.findViewById(R.id.txtDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ReadActivity.class);
                    intent.putExtra("LIST_POSITION", position);
                    itemView.getContext().startActivity(intent);
                }
            });
        }



        public void setPosition(int position){
            this.position = position;
        }

        public void setCount (long count){
            txtCount.setText(count + "");
        }

        public void setDate(String date){
            txtDate.setText(date + "");
        }
    }
}
