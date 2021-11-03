package com.example.covidwarning.Models.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidwarning.R;

public class ViewFineIntervalReyclerViewAdapter  extends RecyclerView.Adapter<ViewFineIntervalReyclerViewAdapter.ViewHolder> {


    private int[] intervalsCount;

    public ViewFineIntervalReyclerViewAdapter(int[] intervalsCount) {
        this.intervalsCount = intervalsCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View fineIntervalView = inflater.inflate(R.layout.fine_interval_custom_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(fineIntervalView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            int count = intervalsCount[position];

            holder.fineCountLabel.setText("Number of violations: " + String.valueOf(count));

            if(position == 0){
                holder.fineIntervalLabel.setText("Violatiom time: 8:00 - 9:00");
            }
            else if (position == 1){
                holder.fineIntervalLabel.setText("Violatiom time: 9:00 - 10:00");
            }
            else if (position == 2){
                holder.fineIntervalLabel.setText("Violatiom time: 10:00 - 11:00");
            }
            else if (position == 3){
                holder.fineIntervalLabel.setText("Violatiom time: 11:00 - 12:00");
            }
            else if (position == 4){
                holder.fineIntervalLabel.setText("Violatiom time: 12:00 - 13:00");
            }
            else if (position == 5){
                holder.fineIntervalLabel.setText("Violatiom time: 13:00 - 14:00");
            }
            else if (position == 6){
                holder.fineIntervalLabel.setText("Violatiom time: 14:00 - 15:00");
            }
            else if (position == 7){
                holder.fineIntervalLabel.setText("Violatiom time: 15:00 - 16:00");
            }
            else if (position == 8){
                holder.fineIntervalLabel.setText("Violatiom time: 16:00 - 17:00");
            }else if (position == 9){
                holder.fineIntervalLabel.setText("Violatiom time: 17:00 - 00:00");
            }

//            int totalCount = 0;
//            for(int i = 0 ; i < intervalsCount.length;i++){
//                totalCount += intervalsCount[i];
//            }


    }

    @Override
    public int getItemCount() {
        return intervalsCount.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    public TextView fineIntervalLabel;
    public TextView fineCountLabel;

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public ViewHolder(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);

        fineIntervalLabel = (TextView) itemView.findViewById(R.id.fineIntervalLabel);
        fineCountLabel = (TextView) itemView.findViewById(R.id.fineCountLabel);
    }
}
}
