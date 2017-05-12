package org.berendeev.roma.sciencealarm.presentation;

import org.berendeev.roma.sciencealarm.domain.entity.Alarm;

import java.util.List;

public interface AlarmListView {

    void setAlarms(List<Alarm> alarms);
}
