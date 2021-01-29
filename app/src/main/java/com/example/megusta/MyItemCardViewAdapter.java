package com.example.megusta;
/**
 * Assignment: Concluding assignment
 * Campus: Ashdod
 * Author1: Ilan Kroter, ID: 323294843
 * Author2: Noy Nir, ID: 207993940
 **/

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for the card views of the items.
 */
public class MyItemCardViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Item> mValues;

    private OnItemLongClickListener click;

    public MyItemCardViewAdapter() {
    }

    public void setValues(List<Item> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder,int position) {
        holder.bind(mValues.get(position));
        holder.itemView.setOnLongClickListener(v -> {
            if (click!=null) {
                click.onLongClick(mValues.get(position));
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : 0;
    }


    interface OnItemLongClickListener {
        public void onLongClick(Item item);
    }

    public void setClick(OnItemLongClickListener click) {
        this.click = click;
    }


}
