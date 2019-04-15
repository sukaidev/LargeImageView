package com.sukaidev.largeimageview.gesture;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by sukaidev on 2019/04/15.
 * 自定义手势操作基类.
 */
public abstract class BaseGestureDetector {

    // 手势状态
    protected boolean mGestureInProgress;

    // 记录前一个触摸点
    protected MotionEvent mPreMotionEvent;
    // 当前触摸点
    protected MotionEvent mCurrentMotionEvent;

    private Context mContext;

    BaseGestureDetector(Context context) {
        mContext = context;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!mGestureInProgress) {
            handleStartProgressEvent(event);
        } else {
            handleInProgressEvent(event);
        }
        return true;
    }

    /**
     * 滑动时需要进行的操作.
     * @param event
     */
    protected abstract void handleInProgressEvent(MotionEvent event);

    /**
     * 开始滑动时需要进行的操作.
     * @param event
     */
    protected abstract void handleStartProgressEvent(MotionEvent event);

    /**
     * 更新滑动状态.
     * @param event
     */
    protected abstract void updateStateByEvent(MotionEvent event);

    /**
     * 重置滑动状态.
     */
    void resetState() {
        if (mPreMotionEvent != null) {
            mPreMotionEvent.recycle();
            mPreMotionEvent = null;
        }
        if (mCurrentMotionEvent != null) {
            mCurrentMotionEvent.recycle();
            mCurrentMotionEvent = null;
        }
        mGestureInProgress = false;
    }

}
