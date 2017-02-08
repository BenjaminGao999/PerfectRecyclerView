package com.adnonstop.library.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.adnonstop.library.BaseViewHolder;
import com.adnonstop.library.interfaces.OnItemChildClickListener;
import com.adnonstop.library.interfaces.OnItemClickListener;
import com.adnonstop.library.interfaces.OnSwipeMenuClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Othershe
 * Time: 2016/9/9 15:52
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter<T> {
    protected OnItemClickListener<T> mItemClickListener;


    protected ArrayList<Integer> mViewId = new ArrayList<>();
    protected ArrayList<OnSwipeMenuClickListener<T>> mListener = new ArrayList<>();

    private ArrayList<Integer> mItemChildIds = new ArrayList<>();
    protected ArrayList<OnItemChildClickListener<T>> mItemChildListeners = new ArrayList<>();

    public CommonBaseAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    /**
     * item赋值；
     * 为item整体布局里的某一控件设置点击事件。
     *
     * @param holder
     * @param data
     */
    protected abstract void convert(BaseViewHolder holder, T data);

    protected abstract int getItemLayoutId();

    /**
     * 扩展一个getViewType用来实现真实数据条目的定制：
     * 如果真实数据条目只有一种类型，则默认即可；否则，根据data判断该返回啥类型。
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isCommonItemView(viewType)) {
            return BaseViewHolder.create(mContext, getItemLayoutId(), parent);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    /**
     * 为真实的item绑定数据
     * 添加监听
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (isCommonItemView(viewType)) {
            bindCommonItem(holder, position);
        }
    }

    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position) {
        final BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        convert(baseViewHolder, mDatas.get(position));

        /**
         * 将item监听转移到这里
         * 为item整体布局添加点击事件
         */
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(baseViewHolder, mDatas.get(position), position);
                }
            }
        });

        /**
         * 为item整体布局里的控件添加监听
         */
        for (int i = 0; i < mItemChildIds.size(); i++) {
            final int tempI = i;
            if (baseViewHolder.getConvertView().findViewById(mItemChildIds.get(i)) != null) {
                baseViewHolder.getConvertView().findViewById(mItemChildIds.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemChildListeners.get(tempI).onItemChildClick(baseViewHolder, mDatas.get(position), position);
                    }
                });
            }
        }

        /**
         * 不知道这是干嘛的
         */
        if (mViewId.size() > 0 && mListener.size() > 0 && baseViewHolder.getSwipeView() != null) {
            ViewGroup swipeView = (ViewGroup) baseViewHolder.getSwipeView();

            for (int i = 0; i < mViewId.size(); i++) {
                final int tempI = i;
                swipeView.findViewById(mViewId.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.get(tempI).onSwipMenuClick(baseViewHolder, mDatas.get(position), position);
                    }
                });
            }
        }
    }

    @Override
    protected int getViewType(int position, T data) {
        return TYPE_COMMON_VIEW;
    }

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnSwipMenuClickListener(int viewId, OnSwipeMenuClickListener<T> swipeMenuClickListener) {
        mViewId.add(viewId);
        mListener.add(swipeMenuClickListener);
    }

    /**
     * 为item里指定id的控件添加监听
     *
     * @param viewId
     * @param itemChildClickListener T data类型
     */
    public void setOnItemChildClickListener(int viewId, OnItemChildClickListener<T> itemChildClickListener) {
        mItemChildIds.add(viewId);
        mItemChildListeners.add(itemChildClickListener);
    }

}
