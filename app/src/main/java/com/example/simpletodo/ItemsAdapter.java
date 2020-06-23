package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


//Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    List <String> items;

    public ItemsAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Use layout inflator to inflate  a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);

        // wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }


    //responsible for binding data to a particular View Holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab item at the position
        String item = items.get(position);

        //Bind the item into the specified view holder
        holder.bind(item);

    }

    //Tells the RecyclerView how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to the views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //Update the view inside the ViewHolder with this data
        public void bind(String item) {
            tvItem.setText(item);
        }
    }
}