package com.shuo.ba.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shuo.ba.R;
import com.shuo.ba.beans.MyChatBean;
import com.shuo.ba.databinding.ItemChatViewBinding;
import com.shuo.ba.databinding.LoadingMoreBinding;
import com.shuo.chatmodule.beans.ChatBean;
import com.shuo.chatmodule.constant.NetworkConstant;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyChatBean> datas;

    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_BOTTOM = 1;
    private static final int ITEM_TYPE_TOP = 2;

    private boolean hasMore = true;
    private LoadingMoreBinding loadingMoreBinding;

    private Activity activity;

    public ChatListAdapter(List<MyChatBean> datas, Activity activity) {
        this.datas = datas;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_NORMAL) {
            ItemChatViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_view, parent, false);
            return new ChatItemViewHolder(binding);
        } else if (viewType == ITEM_TYPE_TOP) {
            return new EmptyViewHolder(new View(parent.getContext()));
        } else {
            LoadingMoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.loading_more, parent, false);
            this.loadingMoreBinding = binding;
            return new BottomViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != getItemCount() - 1 && position != 0) {
            ((ChatItemViewHolder) holder).binding.setChatBean(datas.get(position - 1));
            ((ChatItemViewHolder) holder).setData(datas.get(position - 1));
            ((ChatItemViewHolder) holder).binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        if (datas == null) {
            return 2;
        }
        return datas.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_TOP;
        }
        if (position == getItemCount() - 1) {
            return ITEM_TYPE_BOTTOM;
        }
        return ITEM_TYPE_NORMAL;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        if (!hasMore) {
            loadingMoreBinding.progressBar.setVisibility(View.GONE);
            loadingMoreBinding.tvLoadingMore.setText("没有更多了");
        }
    }


    class ChatItemViewHolder extends RecyclerView.ViewHolder {
        private ItemChatViewBinding binding;

        ChatItemViewHolder(@NonNull ItemChatViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }


        void setData(ChatBean chatBean) {
            binding.itemChatIvHeader.setImageResource(R.mipmap.header_1);
            if (activity != null) {
                RequestBuilder<Drawable> builder = Glide.with(activity).load("http://" + NetworkConstant.SERVER_IP + ":" + NetworkConstant.SERVER_PORT + "/headshot/" + chatBean.getBelongUser().getId()).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        activity.runOnUiThread(()->{
                            binding.itemChatIvHeader.setImageDrawable(resource);
                        });
                        return false;
                    }
                });
                Target<Drawable> target = builder.preload();
            }
        }
    }

    class BottomViewHolder extends RecyclerView.ViewHolder {
        private LoadingMoreBinding binding;

        BottomViewHolder(@NonNull LoadingMoreBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
