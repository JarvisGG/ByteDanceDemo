package me.drakeet.multitype.tool;

import java.util.ArrayList;
import java.util.List;

public class MultiListImpl implements MultiList{

    /**
     * 维护两个list
     * 一个是数据list  一个是策略list
     * 保证两个list的大小相等
     */
    private List<List<?>> mDataList = new ArrayList<>();
    private List<IListDisplayStrategy> mStrategyList = new ArrayList<>();

    @Override
    public int size() {
        int size = 0;
        for (int i = 0; i < mDataList.size(); i ++) {
            List<?> list = mDataList.get(i);
            IListDisplayStrategy strategy = mStrategyList.get(i);
            if (strategy != null && strategy.isShow() && list != null) {
                size += list.size();
            }
        }

        return size;
    }

    @Override
    public Object get(int position) {
        for (int i = 0; i < mDataList.size(); i ++) {
            List<?> list = mDataList.get(i);
            IListDisplayStrategy strategy = mStrategyList.get(i);
            if (strategy != null && strategy.isShow() && list != null) {
                if (position < list.size()) {
                    return list.get(position);
                } else {
                    position -= list.size();
                }
            }
        }
        return null;
    }

    public void setList(List<?> list) {
        if (list != null) {
            mDataList.clear();
            mStrategyList.clear();
            mDataList.add(list);
            mStrategyList.add(new IListDisplayStrategy() {
                @Override
                public boolean isShow() {
                    return true;
                }
            });
        }
    }

    public void addList(List<?> list) {
        addList(list, new IListDisplayStrategy() {
            @Override
            public boolean isShow() {
                return true;
            }
        });
    }

    public void addList(List<?> list, IListDisplayStrategy strategy) {
        if (list != null && strategy != null) {
            mDataList.add(list);
            mStrategyList.add(strategy);
        }
    }

    public interface IListDisplayStrategy {
        boolean isShow();
    }
}
