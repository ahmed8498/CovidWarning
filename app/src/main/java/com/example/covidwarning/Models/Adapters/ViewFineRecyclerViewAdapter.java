package com.example.covidwarning.Models.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidwarning.Models.Fine;
import com.example.covidwarning.R;

import java.util.ArrayList;

public class ViewFineRecyclerViewAdapter extends RecyclerView.Adapter<ViewFineRecyclerViewAdapter.ViewHolder> {


    private ArrayList<Fine> finesArrayList;

    public ViewFineRecyclerViewAdapter(ArrayList<Fine> finesArrayList) {
        this.finesArrayList  = finesArrayList;
    }

    @NonNull
    @Override
    public ViewFineRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View fineIntervalView = inflater.inflate(R.layout.fine_custom_row, parent, false);

        // Return a new holder instance
        ViewFineRecyclerViewAdapter.ViewHolder viewHolder = new ViewFineRecyclerViewAdapter.ViewHolder(fineIntervalView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewFineRecyclerViewAdapter.ViewHolder holder, int position) {
        Fine fine = finesArrayList.get(position);
        holder.fineLocationLabel.setText("Location: " + fine.getFineLocation());
        holder.fineAmountLabel.setText("Amount: " + fine.getFineAmount());
        holder.fineTypeLabel.setText("Type: " +fine.getFineType());
        holder.fineDateLabel.setText("Date & Time: " +fine.getFineDate() + " - "+fine.getFineTime());

    }

    @Override
    public int getItemCount() {
        return finesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView fineDateLabel;
        public TextView fineTypeLabel;
        public TextView fineAmountLabel;
        public TextView fineLocationLabel;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            fineDateLabel = (TextView) itemView.findViewById(R.id.viewFineDateLabel);
            fineTypeLabel = (TextView) itemView.findViewById(R.id.viewFineTypeLabel);
            fineAmountLabel = (TextView) itemView.findViewById(R.id.viewFineAmountLabel);
            fineLocationLabel = (TextView) itemView.findViewById(R.id.viewFineLocationLabel);
        }
    }
}
