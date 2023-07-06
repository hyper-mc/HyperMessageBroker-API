package net.hyper.mc.msgbrokerapi.task.listener;

import balbucio.responsivescheduler.event.Listener;
import balbucio.responsivescheduler.event.impl.*;

public class HMBListener implements Listener {
    @Override
    public void asyncTaskStarted(AsyncTaskStartedEvent asyncTaskStartedEvent) {

    }

    @Override
    public void asyncTaskFinished(AsyncTaskFinishedEvent asyncTaskFinishedEvent) {

    }

    @Override
    public void taskStatedEvent(TaskStartedEvent taskStartedEvent) {

    }

    @Override
    public void taskFinishedEvent(TaskFinishedEvent taskFinishedEvent) {
        taskFinishedEvent.rerun();
    }

    @Override
    public void taskProblemEvent(TaskProblemEvent taskProblemEvent) {
    }

    @Override
    public void scheduledTask(ScheduledTaskEvent scheduledTaskEvent) {

    }
}
