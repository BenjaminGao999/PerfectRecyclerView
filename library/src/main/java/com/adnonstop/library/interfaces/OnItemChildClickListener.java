package com.adnonstop.library.interfaces;


import com.adnonstop.library.BaseViewHolder;

/**
 * Author: Othershe
 * Time: 2016/8/29 10:48
 */
public interface OnItemChildClickListener<T> {
    void onItemChildClick(BaseViewHolder baseViewHolder, T data, int position);
}
