package me.drakeet.multitype.footer;

/**
 * 文章详情页，底部加载更多，加载中等状态的 Bean
 * 以UI 为前缀，代表此对象只是为了UI渲染使用
 * Created by sunhuichuan on 2018/7/27.
 */
public class UIFooter {
    //加载中
    public static final int STATE_LOADING = 1;

    //加载完成
    public static final int STATE_COMPLETE = 2;

    //没有更多内容
    public static final int STATE_NO_MORE = 3;

    protected int mState;


    public UIFooter() {
        setState(STATE_LOADING);
    }


    /**
     * 设置状态
     *
     * @param mState
     */
    public void setState(int mState) {
        this.mState = mState;

    }

    public int getState() {
        return mState;
    }

    public String getStateText() {
        if (mState == STATE_NO_MORE){
            return "没有更多了";
        }else {
            return "加载中...";
        }
    }
}
