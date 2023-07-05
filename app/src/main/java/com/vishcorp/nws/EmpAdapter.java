package com.vishcorp.nws;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EmpAdapter extends RecyclerView.Adapter < EmpAdapter.ViewHolder > {

    private final FragmentActivity mcontext;

    private ArrayList < worker > workers;

    public EmpAdapter(FragmentActivity mcontext, ArrayList < worker > workers) {
        this.mcontext = mcontext;
        this.workers = workers;
    }

    public void setFilteredList(ArrayList < worker > filteredList) {
        this.workers = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmpAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verticalviewmodel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final worker worker = workers.get(position);

        holder.Ename.setText(worker.getName());
        holder.Ejob.setText(worker.getJobname());
        Glide.with(this.mcontext).load(worker.getPimage()).into(holder.Eimage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout, new workerFragment(worker.getName(), worker.getJobname(), worker.getPimage(), worker.getAddress(), worker.getAbout(), worker.getPhoneNo(), worker.getEmail(), worker.getLongitude(), worker.getLatitude()))
                        .addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return workers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Ename, Ejob;
        ImageView Eimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Ename = itemView.findViewById(R.id.Ename);
            Ejob = itemView.findViewById(R.id.Ejob);
            Eimage = itemView.findViewById(R.id.Eimage);
        }

    }
}