package org.berendeev.roma.sciencealarm.presentation.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import org.berendeev.roma.sciencealarm.R;


public class TimerPicker extends AlertDialog implements DialogInterface.OnClickListener,
        TimePicker.OnTimeChangedListener {
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String IS_24_HOUR = "is24hour";

    private final TimePicker mTimePicker = null;
    private final TimePickerDialog.OnTimeSetListener mTimeSetListener;

    private final int initialMinute;
    private final int initialSecond;
    private final boolean mIs24HourView;

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (e.g. they clicked on the 'OK' button).
     */
    public interface OnTimeSetListener {
        /**
         * Called when the user is done setting a new time and the dialog has
         * closed.
         *
         * @param view the view associated with this listener
         * @param hourOfDay the hour that was set
         * @param minute the minute that was set
         */
        void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }

    /**
     * Creates a new time picker dialog.
     *
     * @param context the parent context
     * @param listener the listener to call when the time is set
     * @param hourOfDay the initial hour
     * @param minute the initial minute
     * @param is24HourView whether this is a 24 hour view or AM/PM
     */
    public TimerPicker(Context context, TimePickerDialog.OnTimeSetListener listener, int hourOfDay, int minute,
                            boolean is24HourView) {
        this(context, 0, listener, hourOfDay, minute, is24HourView);
    }

    static int resolveDialogTheme(Context context, int resId) {
        if (resId == 0) {
            final TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.timePickerDialogTheme, outValue, true);
            return outValue.resourceId;
        } else {
            return resId;
        }
    }

    /**
     * Creates a new time picker dialog with the specified theme.
     * <p>
     * The theme is overlaid on top of the theme of the parent {@code context}.
     * If {@code themeResId} is 0, the dialog will be inflated using the theme
     * specified by the
     * {@link android.R.attr#timePickerDialogTheme android:timePickerDialogTheme}
     * attribute on the parent {@code context}'s theme.
     *
     * @param context the parent context
     * @param themeResId the resource ID of the theme to apply to this dialog
     * @param listener the listener to call when the time is set
     * @param hourOfDay the initial hour
     * @param minute the initial minute
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public TimerPicker(Context context, int themeResId, TimePickerDialog.OnTimeSetListener listener,
                            int hourOfDay, int minute, boolean is24HourView) {
        super(context, resolveDialogTheme(context, themeResId));

        mTimeSetListener = listener;
        initialMinute = hourOfDay;
        initialSecond = minute;
        mIs24HourView = is24HourView;

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
//        final View view = inflater.inflate(R.layout.time_picker_dialog, null);
//        setView(view);
//        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
//        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
//        setButtonPanelLayoutHint(LAYOUT_HINT_SIDE);

//        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);
//        mTimePicker.setIs24HourView(mIs24HourView);
//        mTimePicker.setCurrentHour(initialMinute);
//        mTimePicker.setCurrentMinute(initialSecond);
//        mTimePicker.setOnTimeChangedListener(this);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        /* do nothing */
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    /**
     * Sets the current time.
     *
     * @param hourOfDay The current hour within the day.
     * @param minuteOfHour The current minute within the hour.
     */
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour);
    }

    @Override
    public Bundle onSaveInstanceState() {
        final Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int hour = savedInstanceState.getInt(HOUR);
        final int minute = savedInstanceState.getInt(MINUTE);
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
    }
}
