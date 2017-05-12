package org.berendeev.roma.sciencealarm.presentation.entity;

public class AlarmViewModel {
    public long id;
    public int time;
    public boolean isStarted;

    public AlarmViewModel(long id, int time, boolean isStarted) {
        this.id = id;
        this.time = time;
        this.isStarted = isStarted;
    }
}
