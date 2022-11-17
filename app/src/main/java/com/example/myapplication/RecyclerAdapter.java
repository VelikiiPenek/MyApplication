package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<String> orderNameList;
    List<String> orderList;
    List<String> idList;

    private final RecyclerAdapterInterface recyclerAdapterInterface;

    public RecyclerAdapter(List<String> orderNameList, List<String> orderList, List<String> idList, RecyclerAdapterInterface recyclerAdapterInterface) {

        this.orderNameList = orderNameList;
        this.orderList = orderList;
        this.idList = idList;
        this.recyclerAdapterInterface = recyclerAdapterInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView1.setText(orderNameList.get(position));
        holder.textView2.setText(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.OrderName);
            textView2 = itemView.findViewById(R.id.Order);
            constraintLayout = itemView.findViewById(R.id.check);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerAdapterInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
