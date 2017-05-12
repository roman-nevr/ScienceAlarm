package org.berendeev.roma.sciencealarm.presentation;

import android.content.Intent;

import org.berendeev.roma.sciencealarm.domain.entity.Alarm;

import java.util.List;

public interface AlarmListView {

    void setAlarms(List<Alarm> alarms);

    public interface Router{
        void addNewAlarm(int time);

        void removeAlarm(long id);

        void toggleAlarm(long id);
    }
}
