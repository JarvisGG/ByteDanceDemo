package me.drakeet.multitype.footer;

import androidx.annotation.NonNull;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 带 footer 的 MultiTypeAdapter ，建议和 {@link LoadMoreDelegate }配套使用
 *
 * Created by sunhuichuan on 2020-05-04
 */
public class MultiTypeFooterAdapter extends MultiTypeAdapter {

    /**
     * UIFooter
     */
    private UIFooter mFooter;

    public MultiTypeFooterAdapter() {
        this(new UIFooter());
    }

    public MultiTypeFooterAdapter(@NonNull UIFooter uiFooter) {
        super();
        this.mFooter = uiFooter;
        register(UIFooter.class, new FooterViewBinder());
    }


    /**
     * 是否是footer
     * @param position
     * @return
     */
    private boolean isFooterPosition(int position) {
        return position == (getItemCount() - 1);
    }

    @Override
    protected Object getItemObject(int position) {
        if (isFooterPosition(position)) {
            return mFooter;
        }
        //原有逻辑
        return super.getItemObject(position);
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if (itemCount > 0) {
            return itemCount + 1;
        }
        return 0;
    }


    /**
     * 没有更多数据了
     */
    public void setFooterNoMoreData() {
        setFooterMoreData(false);
    }

    /**
     * 设置是否有更多
     * @param hasMore
     */
    public void setFooterMoreData(boolean hasMore) {
        mFooter.setState(hasMore ? UIFooter.STATE_LOADING : UIFooter.STATE_NO_MORE);
    }

}
