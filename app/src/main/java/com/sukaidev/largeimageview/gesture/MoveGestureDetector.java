package com.sukaidev.largeimageview.gesture;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by sukaidev on 2019/04/15.
 * 自定义手势处理.
 */
public class MoveGestureDetector extends BaseGestureDetector {

    private PointF mCurrentPointer;

    private PointF mPrePointer;

/*
    // 仅仅为了减少创建内存
    private PointF mDeltaPointer = new PointF();
*/

    // 用于记录移动距离，并返回
    private PointF mExternalPointer = new PointF();

    private OnMoveGestureListener mListener;

    public MoveGestureDetector(Context context, OnMoveGestureListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void handleInProgressEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mListener.onMoveEnd(this);
                resetState();
                break;
            case MotionEvent.ACTION_MOVE:
                updateStateByEvent(event);
                boolean update = mListener.onMove(this);
                if (update) {
                    mPreMotionEvent.recycle();
                    mPreMotionEvent = MotionEvent.obtain(event);
                }
                break;
        }
    }

    @Override
    protected void handleStartProgressEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                resetState(); // 防止没有接收到CANCEL or UP，保险起见
                mPreMotionEvent = MotionEvent.obtain(event);
                updateStateByEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mGestureInProgress = mListener.onMoveBegin(this);
                break;
        }
    }

    @Override
    protected void updateStateByEvent(MotionEvent event) {

        final MotionEvent prev = mPreMotionEvent;

        mPrePointer = calculateFocalPointer(prev);
        mCurrentPointer = calculateFocalPointer(event);

        boolean mSkipThisMoveEvent = prev.getPointerCount() != event.getPointerCount();

        mExternalPointer.x = mSkipThisMoveEvent ? 0 : mCurrentPointer.x - mPrePointer.x;
        mExternalPointer.y = mSkipThisMoveEvent ? 0 : mCurrentPointer.y - mPrePointer.y;
    }


    /**
     * 计算多指中心
     *
     * @param event
     * @return
     */
    private PointF calculateFocalPointer(MotionEvent event) {
        final int count = event.getPointerCount();
        float x = 0, y = 0;
        for (int i = 0; i < count; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= count;
        y /= count;

        return new PointF(x, y);
    }

    public float getMoveX() {
        return mExternalPointer.x;
    }

    public float getMoveY() {
        return mExternalPointer.y;
    }

    public interface OnMoveGestureListener {
        boolean onMoveBegin(MoveGestureDetector detector);

        boolean onMove(MoveGestureDetector detector);

        void onMoveEnd(MoveGestureDetector detector);
    }

    public static class SimpleMoveGestureDetector implements OnMoveGestureListener {

        @Override
        public boolean onMoveBegin(MoveGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onMove(MoveGestureDetector detector) {
            return true;
        }

        @Override
        public void onMoveEnd(MoveGestureDetector detector) {
        }
    }

}
