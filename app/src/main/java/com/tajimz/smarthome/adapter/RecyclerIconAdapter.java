package com.tajimz.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tajimz.smarthome.R;
import com.tajimz.smarthome.databinding.ItemIconBinding;
import com.tajimz.smarthome.model.IconModel;

import java.util.List;

public class RecyclerIconAdapter extends RecyclerView.Adapter<RecyclerIconAdapter.IconViewHolder> {
    List<IconModel> list;
    Context context;

    int selected = 0;
    int lastSelected;
    public RecyclerIconAdapter(Context context, List<IconModel> list){
        this.list = list;
        this.context = context;

    }



    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IconViewHolder(ItemIconBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        IconModel iconModel = list.get(position);
        holder.binding.imgIcon.setImageResource(iconModel.getPath());
        if (selected == position){
            holder.binding.parentLay.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg));
            holder.binding.imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.white));

        }else {
            holder.binding.parentLay.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.binding.imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.black));
        }
        holder.binding.parentLay.setOnClickListener(v->{

            lastSelected = selected;
            selected = position;
            notifyItemChanged(lastSelected);
            notifyItemChanged(selected);

        });
        

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class IconViewHolder extends RecyclerView.ViewHolder{

        ItemIconBinding binding;
        public IconViewHolder(ItemIconBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public String getSelected(){
        return String.valueOf(list.get(selected).getPath());
    }


}
