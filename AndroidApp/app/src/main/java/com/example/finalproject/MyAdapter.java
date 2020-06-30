package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;



public class MyAdapter extends RecyclerView.Adapter {
    private ArrayList<Request> mDataset;
    private Context context;
    private OnRequestListener mOnRequestListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        private TextView textView;
        private ImageView image;
        private OnRequestListener onRequestListener;

        public MyViewHolder(View itemView, OnRequestListener onRequestListener) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            this.image = itemView.findViewById(R.id.image);
            this.onRequestListener = onRequestListener;
            itemView.setOnClickListener(this);
        }
        public TextView getText() {
            return this.textView;
        }
        public ImageView getImage() {
            return this.image;
        }

        @Override
        public void onClick(View view) {
            onRequestListener.OnRequestClick(getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, ArrayList<Request> myDataset, OnRequestListener onRequestListener) {
        this.context = context;
        this.mDataset = myDataset;
        this.mOnRequestListener = onRequestListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_cell_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, mOnRequestListener);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Request req = mDataset.get(position);
        String text = "Request from " + req.getName(); 
        ((MyViewHolder)holder).getText().setText(text); 

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnRequestListener {
        void OnRequestClick(int position);
    }
}