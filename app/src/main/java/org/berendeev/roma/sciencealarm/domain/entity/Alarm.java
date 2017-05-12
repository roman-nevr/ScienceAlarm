package org.berendeev.roma.sciencealarm.domain.entity;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Alarm {

    abstract public long id();

    abstract public String card();

    abstract public int time();

    abstract public boolean isStarted();



    public static Builder builder() {
        return new AutoValue_Alarm.Builder();
    }

    abstract public Builder toBuilder();

    public static Alarm create(long id, String card, int time, boolean isStarted) {
        return builder()
                .id(id)
                .card(card)
                .time(time)
                .isStarted(isStarted)
                .build();
    }

    @AutoValue.Builder public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder card(String card);

        public abstract Builder time(int time);

        public abstract Builder isStarted(boolean isStarted);

        public abstract Alarm build();
    }
}
