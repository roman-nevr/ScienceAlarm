package org.berendeev.roma.sciencealarm.presentation.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.berendeev.roma.sciencealarm.presentation.presenter.AlarmListPresenter;


public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    private final AlarmListPresenter presenter;

    public SwipeHelper(AlarmListPresenter presenter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.presenter = presenter;
    }

    @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;

    }

    @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        AlarmListAdapter.AlarmHolder holder= (AlarmListAdapter.AlarmHolder) viewHolder;
        presenter.onAlarmSwiped(holder.id);
        holder.cancelTimer();
    }

}
