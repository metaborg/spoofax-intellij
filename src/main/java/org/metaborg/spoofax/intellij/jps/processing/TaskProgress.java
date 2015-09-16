package org.metaborg.spoofax.intellij.jps.processing;

import javax.annotation.Nullable;

public class TaskProgress implements ITaskProgress {

    private final String name;
    private int totalWork;
    private int usedWork;
    private final ITaskProgress parent;
    private TaskProgressState state;

    public TaskProgress(@Nullable String name, @Nullable ITaskProgress parent) {
        this.name = name;
        this.totalWork = 0;
        this.usedWork = 0;
        this.parent = parent;
        this.state = TaskProgressState.Created;
    }

    @Override
    @Nullable
    public ITaskProgress parent() {
        return this.parent;
    }

    @Override
    @Nullable
    public String name() {
        return this.name;
    }

    @Override
    public int totalWork() {
        return this.totalWork;
    }

    @Override
    public int usedWork() {
        return this.usedWork;
    }

    @Override
    public int remainingWork() {
        return this.totalWork - this.usedWork;
    }

    @Override
    public double progress() {
        return (double)this.usedWork / (double)this.totalWork;
    }

    @Override
    public TaskProgressState state() {
        return this.state;
    }

    @Override
    public void setWork(int units) {
        assertDoing();
        this.usedWork = clamp(0, units, this.totalWork);
    }

    @Override
    public void setRemainingWork(int units) {
        assertDoing();
        if (units <= 0)
            return;
        this.totalWork = this.usedWork + units;
    }

    @Override
    public ITaskProgress createSubTask(@Nullable String name, int units) {
        assertDoing();
        // TODO: reserve `units` work units of this task for the sub task
        // and let the sub task report its progress to this task.
        return new TaskProgress(name, this);
    }

    @Override
    public void beginTask(int totalWork) {
        assertCreated();
        this.state = TaskProgressState.Doing;
        this.totalWork = totalWork;
    }

    @Override
    public void endTask(TaskCompletedState state) {
        assertDoing();
        switch (state) {
            case Successful:
                this.state = TaskProgressState.Done;
                break;
            case Cancelled:
                this.state = TaskProgressState.Cancelled;
                break;
        }
    }

    private void assertCreated() {
        assert this.state == TaskProgressState.Created;
    }

    private void assertDoing() {
        assert this.state == TaskProgressState.Doing;
    }

    private int clamp(int min, int value, int max)
    {
        return Math.max(min, Math.min(value, max));
    }

    private double clamp(double min, double value, double max)
    {
        return Math.max(min, Math.min(value, max));
    }
}
