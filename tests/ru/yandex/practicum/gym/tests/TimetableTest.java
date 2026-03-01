package ru.yandex.practicum.gym.tests;

import ru.yandex.practicum.gym.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие

        var timesPerDay = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, timesPerDay.size());
        var sessionsPerTime = timesPerDay.get(new TimeOfDay(13, 0));
        assertEquals(1, sessionsPerTime.size());


        //Проверить, что за вторник не вернулось занятий

        assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие

        var timesPerMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, timesPerMonday.size());
        var sessionsInMonday = timesPerMonday.get(new TimeOfDay(13, 0));
        assertEquals(1, sessionsInMonday.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00

        var timesPerThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, timesPerThursday.size());
        TimeOfDay[] times = timesPerThursday.keySet().toArray(new TimeOfDay[0]);
        assertTrue(times[0].getHours() < times[1].getHours());

        // Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionWithoutExtraSessions() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSessionOne = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSessionTwo = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(trainingSessionOne);
        timetable.addNewTrainingSession(trainingSessionTwo);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY,
                new TimeOfDay(13,0)
        );

        assertEquals(1, sessions.size());
    }

    @Test
    void testTrainingSessionIsSameWhenTryingToAddExtraSession() {
        Timetable timetable = new Timetable();

        Group groupOne = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupTwo = new Group("Акробатика для взрослых", Age.ADULT, 60);

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSessionOne = new TrainingSession(groupOne, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSessionTwo = new TrainingSession(groupTwo, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(trainingSessionOne);
        timetable.addNewTrainingSession(trainingSessionTwo);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY,
                new TimeOfDay(13,0)
        );

        assertEquals(1, sessions.size());
        assertEquals(sessions.getFirst().getGroup(), groupOne);
    }
    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY,
                new TimeOfDay(13,0)
        );

        assertEquals(1, sessions.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY,
                new TimeOfDay(14,0)
        ));
    }

    @Test
    void testGetThreeTrainingSessionsInRightOrder() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession trainingSessionOne = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));


        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession trainingSessionTwo = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSessionThree = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(trainingSessionOne);
        timetable.addNewTrainingSession(trainingSessionTwo);
        timetable.addNewTrainingSession(trainingSessionThree);

        var timesPerMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        TimeOfDay[] timesArray = timesPerMonday.keySet().toArray(new TimeOfDay[0]);

        assertTrue(timesArray[0].getHours() < timesArray[1].getHours());
        assertTrue(timesArray[1].getHours() < timesArray[2].getHours());

    }

    @Test
    void testGetCountByCoachesCheckIncrement() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSessionOne = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSessionTwo = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));

        timetable.addNewTrainingSession(trainingSessionOne);
        timetable.addNewTrainingSession(trainingSessionTwo);

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();
        assertEquals(2, counters.getFirst().getTrainingsCount());
    }

    @Test
    void testGetCounterOfTrainingsInRightOrder() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coachOne = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachTwo = new Coach("Андреев", "Максим", "Валентинович");
        Coach coachThree = new Coach("Иванов", "Алексей", "Дмитриевич");

        TrainingSession[] sessions = new TrainingSession[] {
                new TrainingSession(group, coachOne,
                        DayOfWeek.MONDAY, new TimeOfDay(13, 0)),
                new TrainingSession(group, coachOne,
                        DayOfWeek.MONDAY, new TimeOfDay(14, 0)),
                new TrainingSession(group, coachOne,
                        DayOfWeek.MONDAY, new TimeOfDay(15, 0)),
                new TrainingSession(group, coachTwo,
                        DayOfWeek.MONDAY, new TimeOfDay(13, 0)),
                new TrainingSession(group, coachTwo,
                        DayOfWeek.MONDAY, new TimeOfDay(14, 0)),
                new TrainingSession(group, coachThree,
                        DayOfWeek.MONDAY, new TimeOfDay(13, 0)),
        };

        for (TrainingSession session : sessions) {
            timetable.addNewTrainingSession(session);
        }

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();

        assertEquals(3, counters.get(0).getTrainingsCount());
        assertEquals(2, counters.get(1).getTrainingsCount());
        assertEquals(1, counters.get(2).getTrainingsCount());
    }

    @Test
    void testCounterOfTrainingsNotCountExtraTrainings() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSessionOne = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSessionTwo = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(trainingSessionOne);
        timetable.addNewTrainingSession(trainingSessionTwo);

        CounterOfTrainings counter = timetable.getCountByCoaches().getFirst();

        assertEquals(1, counter.getTrainingsCount());
    }
}

