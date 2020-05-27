package service;

import java.util.ArrayList;
import java.util.TimerTask;

//there should be better name then "Taskable"
public class Taskable {

    protected void addTask(Taskable object, String taskName, TimerTask task) {
        Scheduler.addTask(object, taskName, task);
    }

    protected static void stopAndDeleteTask(Taskable object, String taskName) {
        Scheduler.stopAndDeleteTask(object, taskName);
    }
}
