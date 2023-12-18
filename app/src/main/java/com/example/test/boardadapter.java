package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class boardadapter extends RecyclerView.Adapter<boardadapter.ViewHolder>{
    Context context;
    ArrayList<Note> arrayList;
    OnItemClickListener onItemClickListener;

    public boardadapter(Context context, ArrayList<Note> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.board_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String textofmaterial = String.format("%-7s",arrayList.get(position).getMaterial());
        if (textofmaterial.length() > 7){
            textofmaterial = textofmaterial.substring(0, 7);
        }
        textofmaterial = " " + textofmaterial + " ";
        holder.material.setText(textofmaterial);

        String textoftitle = String.format("%-18s", arrayList.get(position).getTitle());
        if(textoftitle.length() > 18){
            textoftitle = textoftitle.substring(0, 18);
        }
        textoftitle = " " + textoftitle + " ";
        holder.title.setText(textoftitle);

        String textofwriterid = String.format("%-12s",arrayList.get(position).getWriterID());
        if (textofwriterid.length() > 12){
            textofwriterid = textofwriterid.substring(0, 12);
        }
        textofwriterid = " " + textofwriterid + "  ";
        holder.writerid.setText(textofwriterid);

        holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView material, title, writerid;
        public ViewHolder (@NonNull View itemView){
            super(itemView);

            material = itemView.findViewById(R.id.list_item_material);

            title = itemView.findViewById(R.id.list_item_title);

            writerid = itemView.findViewById(R.id.list_item_writerid);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Note note);
    }
}
