package me.drakeet.multitype.footer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.R;


/**
 * Created by sunhuichuan on 2018/7/27.
 */
public class FooterViewBinder extends ItemViewBinder<UIFooter,FooterViewBinder.FooterViewHolder> {


    @NonNull
    @Override
    protected FooterViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        FooterViewHolder viewHolder = new FooterViewHolder(parent);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull FooterViewHolder holder, @NonNull UIFooter item, int position, int itemSize) {
        holder.fill(item);
    }

    /**
     * 底部最后一个footer的ViewHolder
     */
    public class FooterViewHolder extends RecyclerView.ViewHolder {

        private TextView mLoadTextView;
        private ImageView mLoadIcon;
        private UIFooter mData;
        private Animation mIconAnim;


        public FooterViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false));
            findView();
            initAnimation(parent.getContext());
        }

        private void findView() {
            mLoadTextView = itemView.findViewById(R.id.text_load_text);
            mLoadIcon = itemView.findViewById(R.id.iv_load_icon);
        }

        private void initAnimation(Context context) {
            mIconAnim = new RotateAnimation(0, 360,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5F,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5F);
            mIconAnim.setDuration(1000);
            mIconAnim.setInterpolator(context, android.R.anim.linear_interpolator);
        }


        public void fill(UIFooter data) {
            mData = data;
            if (data.getState() == UIFooter.STATE_LOADING) {
                mLoadIcon.startAnimation(mIconAnim);
                mLoadIcon.setVisibility(View.VISIBLE);
                mLoadTextView.setVisibility(View.INVISIBLE);
            } else {
                mLoadIcon.clearAnimation();
                mLoadIcon.setVisibility(View.INVISIBLE);
                mLoadTextView.setText(data.getStateText());
                mLoadTextView.setVisibility(View.VISIBLE);
            }
        }

    }

}
