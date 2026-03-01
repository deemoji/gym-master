package ru.yandex.practicum.gym;

public class CounterOfTrainings {

    private final Coach coach;

    private final int trainingsCount;

    public CounterOfTrainings(Coach coach, int trainingsCount) {
        this.coach = coach;
        this.trainingsCount = trainingsCount;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getTrainingsCount() {
        return trainingsCount;
    }
}
