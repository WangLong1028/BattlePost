package com.shuo.ba.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.shuo.ba.R;
import com.shuo.ba.databinding.BottomItemViewBinding;


public class BottomItemView extends RelativeLayout {

    private Drawable icon;
    private String title;

    private BottomItemViewBinding binding;

    public BottomItemView(Context context) {
        this(context, null);
    }

    public BottomItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_item_view, this, true);

        parseAttr(attrs);
    }

    private void parseAttr(AttributeSet attrs) {
        if (attrs == null){
            return;
        }
        @SuppressLint("Recycle") TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BottomItemView);
        icon = typedArray.getDrawable(R.styleable.BottomItemView_icon);
        title = typedArray.getString(R.styleable.BottomItemView_item_title);

        binding.ivIcon.setImageDrawable(icon);
        binding.tvTitle.setText(title);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        binding.cardView.setOnClickListener((View view)-> onItemClickListener.onClick());
    }

    public interface OnItemClickListener{
        void onClick();
    }
}
