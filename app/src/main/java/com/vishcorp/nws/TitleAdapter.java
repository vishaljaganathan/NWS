package com.vishcorp.nws;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder> {

    // private  static  final  String Tag="recyclerView";
    private FragmentActivity context;
    private ArrayList<Title> titleArrayList;
Itemsearch isearch;


    public TitleAdapter(FragmentActivity context, ArrayList<Title> titleArrayList,Itemsearch isearch) {
        this.context = context;
        this.titleArrayList = titleArrayList;
        this.isearch = isearch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horiviewmodel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Title title = titleArrayList.get(position);
        holder.tname.setText(titleArrayList.get(position).getName());
        Glide.with(this.context).load(titleArrayList.get(position).getTurl()).into(holder.timage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, title.getName(), Toast.LENGTH_SHORT).show();
                isearch.getdata(title.getName());

            }
        });
    }


    @Override
    public int getItemCount() {
        return titleArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView timage;
        TextView tname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tname = itemView.findViewById(R.id.titleid);
            timage = itemView.findViewById(R.id.titleimg);
        }
    }
}
