package org.berendeev.roma.sciencealarm.domain;

import org.berendeev.roma.sciencealarm.domain.entity.Alarm;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface AlarmRepository {

    Completable saveAlarm(Alarm alarm);

    Observable<Alarm> getAlarm(long id);

    Completable deleteAlarm(long id);

    Observable<List<Alarm>> getAllAlarms();
}
