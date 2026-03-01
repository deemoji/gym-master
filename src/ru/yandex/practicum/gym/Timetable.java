package ru.yandex.practicum.gym;

import javax.swing.*;
import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();
    private final Map<Coach, Integer> sessionCountByCoach = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, List<TrainingSession>> sessionsPerDay = timetable.getOrDefault(day, new TreeMap<>());
        List<TrainingSession> sessionsPerTime = sessionsPerDay.getOrDefault(time, new ArrayList<>());

        boolean sessionNotExists = !sessionsPerTime.contains(trainingSession);
        // Если тренировка с текущим временем и тренером еще не существует
        if (sessionNotExists) {
            // Добавляем в расписание
            sessionsPerTime.add(trainingSession);
            incrementCoachSession(trainingSession.getCoach());
        }

        sessionsPerDay.put(trainingSession.getTimeOfDay(), sessionsPerTime);
        timetable.put(day, sessionsPerDay);
    }

    private void incrementCoachSession(Coach coach) {
        sessionCountByCoach.put(coach, sessionCountByCoach.getOrDefault(coach, 0) + 1);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.containsKey(dayOfWeek)) {
            return timetable.get(dayOfWeek);
        }
        return null;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (timetable.containsKey(dayOfWeek)) {
            TreeMap<TimeOfDay, List<TrainingSession>> sessionsPerDay = timetable.get(dayOfWeek);

            if (sessionsPerDay.containsKey(timeOfDay)) {
                return sessionsPerDay.get(timeOfDay);
            }
        }
        return null;
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        ArrayList<CounterOfTrainings> counters = new ArrayList<>();

        for (Map.Entry<Coach, Integer> entry : sessionCountByCoach.entrySet()) {
            counters.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Comparator<CounterOfTrainings> comparator = new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings o1, CounterOfTrainings o2) {
                return o2.getTrainingsCount() - o1.getTrainingsCount();
            }
        };

        counters.sort(comparator);

        return counters;
    }
}

