package com.legendwd.hyperpay.aelf.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

public class CommonItemTouchHelper extends ItemTouchHelper {

    private OnItemTouchCallbackListener mListener;

    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    private CommonItemTouchHelper(CommonItemTouchCallback callback) {
        super(callback);
    }

    public OnItemTouchCallbackListener getListener() {
        return mListener;
    }
//    public void setOnItemTouchCallbackListener(OnItemTouchCallbackListener listener){
//        mListener = listener;
//    }

    public interface OnItemTouchCallbackListener {
        boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);

        void onSwipe(RecyclerView.ViewHolder viewHolder, int direction);

        void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);

        void onSwapped(int fromIndex, int toIndex);

        void onRemoved(int index);
    }

    private static class CommonItemTouchCallback extends Callback {
        public int selectedBackgroundColor = Color.LTGRAY;
        public int unSelectedBackgroundColor = Color.TRANSPARENT;
        public boolean canDrag = true;
        public boolean canSwipe = false;
        public boolean isLongPressDragEnable = false;
        public OnItemTouchCallbackListener listener;
        private int actionType;//1:move,2:swipe,3:default(no action)
        private int fromIndex;
        private int toIndex;

        @Override
        public boolean isLongPressDragEnabled() {
            return isLongPressDragEnable;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return canSwipe;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0;
            int swipeFlags = 0;
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                if (canDrag) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
                if (canSwipe) {
                    if (((GridLayoutManager) manager).getOrientation() == RecyclerView.HORIZONTAL) {
                        swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    } else {
                        swipeFlags = ItemTouchHelper.LEFT;
                    }
                }

            } else if (manager instanceof LinearLayoutManager) {
                if (canDrag) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }
                if (canSwipe) {
                    swipeFlags = ItemTouchHelper.LEFT;
                }
            }

            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }
            actionType = 1;
            if (listener != null) {
                return listener.onMove(recyclerView, viewHolder, target);
            }
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            actionType = 2;
            if (listener != null) {
                listener.onSwipe(viewHolder, direction);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

            if (viewHolder != null && actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(selectedBackgroundColor);
                fromIndex = viewHolder.getAdapterPosition();
                toIndex = viewHolder.getAdapterPosition();
                actionType = 0;
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        //重绘item，在item下面
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (viewHolder != null) {
                getDefaultUIUtil().onDraw(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);
            }

        }

        //重绘item，在item上面
        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (viewHolder != null) {
                getDefaultUIUtil().onDraw(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);
            }

        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder != null) {
                getDefaultUIUtil().clearView(viewHolder.itemView);
                viewHolder.itemView.setBackgroundColor(unSelectedBackgroundColor);
                if (viewHolder.getAdapterPosition() != -1) {
                    toIndex = viewHolder.getAdapterPosition();
                }
                if (listener != null) {
                    if (actionType == 1) {
                        if (fromIndex != toIndex && fromIndex >= 0 && toIndex >= 0) {
                            listener.onSwapped(fromIndex, toIndex);
                        }
                    } else if (actionType == 2) {
                        listener.onRemoved(fromIndex);
                    }
                    listener.clearView(recyclerView, viewHolder);
                }
            }
        }
    }

    public static class Builder {
        private List mData;
        private CommonItemTouchCallback callback;
        private CommonItemTouchHelper helper;

        public Builder() {
            callback = new CommonItemTouchCallback();
            helper = new CommonItemTouchHelper(callback);
        }

        public Builder itemTouchCallbackListener(OnItemTouchCallbackListener listener) {
            callback.listener = listener;
            helper.mListener = listener;
            return this;
        }

        public Builder selectedBackgroundColor(@ColorInt int color) {
            callback.selectedBackgroundColor = color;
            return this;
        }

        public Builder unSelectedBackgroundColor(@ColorInt int color) {
            callback.unSelectedBackgroundColor = color;
            return this;
        }

        public Builder canDrag(boolean canDrag) {
            callback.canDrag = canDrag;
            return this;
        }

        public Builder canSwipe(boolean canSwipe) {
            callback.canSwipe = canSwipe;
            return this;
        }

        public Builder isLongPressDragEnable(boolean enable) {
            callback.isLongPressDragEnable = enable;
            return this;
        }

        public CommonItemTouchHelper build() {
            return helper;
        }

    }
}
