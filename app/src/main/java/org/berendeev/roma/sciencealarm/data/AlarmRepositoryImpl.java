package org.berendeev.roma.sciencealarm.data;

import org.berendeev.roma.sciencealarm.data.sqlite.AlarmDataSource;
import org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper;
import org.berendeev.roma.sciencealarm.domain.AlarmRepository;
import org.berendeev.roma.sciencealarm.domain.entity.Alarm;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;


public class AlarmRepositoryImpl implements AlarmRepository {

    private DatabaseOpenHelper openHelper;
    private AlarmDataSource alarmDataSource;

    public AlarmRepositoryImpl(DatabaseOpenHelper openHelper) {
        this.openHelper = openHelper;
        alarmDataSource = new AlarmDataSource(openHelper);
    }

    @Override public Completable saveAlarm(Alarm alarm) {
        return Completable.fromAction(() -> {
            alarmDataSource.saveAlarm(alarm);
        });
    }

    @Override public Observable<Alarm> getAlarm(long id) {
        return Observable.fromCallable(() -> {
            return alarmDataSource.getAlarm(id);
        });
    }

    @Override public Completable deleteAlarm(long id) {
        return Completable.fromAction(() -> {
            alarmDataSource.deleteAlarm(id);
        });
    }

    @Override public Observable<List<Alarm>> getAllAlarms() {
        return Observable.fromCallable(() -> {
            return alarmDataSource.getAllAlarm();
        });
    }
}
