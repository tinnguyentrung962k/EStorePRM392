package com.prm392.estoreprm392;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.List;

public abstract class BaseAdapter<T, VH extends BaseAdapter.BaseItemViewHolder<T, ?>> extends RecyclerView.Adapter<VH> {

    private OnItemClickListener<T> onItemClickListener;
    private SetItemOrderBy<T> setItemOrderBy;

    protected abstract DiffUtil.ItemCallback<T> differCallBack();

    protected final AsyncListDiffer<T> differ = new AsyncListDiffer<>(this, differCallBack());

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T item = differ.getCurrentList().get(position);
        holder.bind(item);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(item));
        }
    }

    public void submitList(List<T> list) {
        if (setItemOrderBy != null) {
            setItemOrderBy.setOrder(list);
        }
        differ.submitList(list);
        notifyDataSetChanged();
    }

    public void setItemOrderBy(SetItemOrderBy<T> listener) {
        this.setItemOrderBy = listener;
    }

    public void setItemOnClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    public abstract static class BaseItemViewHolder<T, VB extends ViewBinding> extends RecyclerView.ViewHolder {

        protected final VB binding;
        protected final Context context;

        public BaseItemViewHolder(@NonNull VB binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = binding.getRoot().getContext();
        }

        public abstract void bind(T item);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public interface SetItemOrderBy<T> {
        void setOrder(List<T> items);
    }
}