package service;

import log.ExceptionLogger;

import java.util.*;

public class Scheduler {

    //Key = ClassName; value = HashMap {key = taskName; value = task}
    private static final HashMap<Taskable, HashMap<String, Thread>> executingTasks = new HashMap<>();

    private static boolean tasksWereStarted = false;

    public static ArrayList<String> getExecutingTasks() {
        ArrayList<String> tasksList = new ArrayList<>();
        executingTasks.forEach((taskable, hashmap) -> {

            StringBuilder builder = new StringBuilder();
            builder.append(taskable.getClass().getName()).append(" has next tasks:\r\n\t");

            hashmap.forEach((taskName, timer) -> {
                builder.append(taskName).append("\r\n\t");
            });
            tasksList.add(builder.toString());
        });
        return tasksList;
    }

    public static void runAllTasks() {
        if (!tasksWereStarted) {
            tasksWereStarted = true;
            executingTasks.forEach((object, tasks) ->
                    tasks.forEach((taskName, task) ->
                            task.start()));
        }
    }

    static void addTask(Taskable object, String taskName, TimerTask task) {
        Thread newThread = new Thread(task, taskName);

        if (!executingTasks.containsKey(object)) {
            HashMap<String, Thread> taskDictionary = new HashMap<>();
            taskDictionary.put(taskName, newThread);
            executingTasks.put(object, taskDictionary);
        }
        else {
            if (executingTasks.get(object).containsKey(taskName))
                reportError(object, taskName);
            else
                executingTasks.get(object).put(taskName, newThread);
        }

    }

    static void stopAndDeleteTask(Taskable object, String taskName) {

    }

    private static void reportError(Taskable object, String taskName) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String className = object.getClass().getName();
        String message = String.format("%s task is already executing by object's class %s", taskName, className);
        ExceptionLogger.writeException(stackTrace, "This task is already executing by this object");
    }
}
