package a.lianne.quarto;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class ReminderFragment extends Fragment {

    public static final String CHANNEL_ID = "reminder_channel";
    public static final int NOTIFICATION_ID = 1;
    private TimePicker timePicker;
    private Switch switchReminder;
    private Button saveButton;

    private boolean isReminderEnabled = false;
    private int savedHour = 18;
    private int savedMinute = 0;

    public ReminderFragment() {
        // Required empty public constructor
    }
    public static ReminderFragment newInstance() {
        ReminderFragment fragment = new ReminderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CharSequence name = "Quarto Game";
        String description = "Play the game!";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent != null) {
            Log.d("QB", "Reminder already set");
            isReminderEnabled = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timePicker = view.findViewById(R.id.timePicker);
        switchReminder = view.findViewById(R.id.switchReminder);
        saveButton = view.findViewById(R.id.saveButton);

        switchReminder.setChecked(isReminderEnabled);
        timePicker.setIs24HourView(true);

        timePicker.setHour(savedHour);
        timePicker.setMinute(savedMinute);

        saveButton.setOnClickListener(v -> {
            savedHour = timePicker.getHour();
            savedMinute = timePicker.getMinute();
            if (switchReminder.isChecked()) {
                // schedule
                scheduleReminder(savedHour, savedMinute);
                Toast.makeText(requireContext(), "Reminder set for " + savedHour + ":" + savedMinute, Toast.LENGTH_SHORT).show();
            } else {
                // cancel
                cancelReminder();
                Toast.makeText(requireContext(), "Reminder canceled", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void scheduleReminder(int hour, int minute) {
        Intent intent = new Intent(requireContext(), ReminderReceiver.class);
        intent.putExtra("hour", savedHour);
        intent.putExtra("minute", savedMinute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        if (pendingIntent != null) {
            Log.d("QB", "Reminder already set 2");
        } else {
            Log.d("QB", "Reminder not set");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (calendar.get(Calendar.HOUR_OF_DAY) >= hour && calendar.get(Calendar.MINUTE) >= minute)
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelReminder() {
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}