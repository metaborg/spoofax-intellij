package org.metaborg.spoofax.intellij.jps.processing;

import javax.annotation.Nullable;

/**
 * Tracks the progress of a task.
 */
public interface ITaskProgress {

    /**
     * Gets the parent task.
     * @return The parent task; or null when there is none.
     */
    @Nullable
    ITaskProgress parent();

    /**
     * Gets the name of the task.
     * @return The name of the task; or null when not specified.
     */
    @Nullable
    String name();

    /**
     * Gets the total number of work units.
     * @return The total number of work units.
     */
    int totalWork();

    /**
     * Gets the number of used work units.
     * @return The number of used work units.
     */
    int usedWork();

    /**
     * Gets the number of remaining work units.
     * @return The number of remaining work units.
     */
    int remainingWork();

    /**
     * Gets the progress.
     * @return The progress as a value between 0.0 and 1.0;
     * or {@link Float#NaN} when the progress is unknown.
     */
    double progress();

    /**
     * Gets the progress state.
     * @return A member of the {@link TaskProgressState} enumeration.
     */
    TaskProgressState state();



    /**
     * Reports that the specified number of work units have been completed.
     * @param units The number of units that have been completed.
     */
    void setWork(int units);

    /**
     * Reports that the specified number of work units are remaining.
     * @param units The number of units that are remaining.
     */
    void setRemainingWork(int units);

    /**
     * Creates a new sub task that represents the specified number of work units.
     * @param name A name for the task; or null to specify none.
     * @param units The number of work units in this task that represent the sub task.
     * @return The created sub task.
     */
    ITaskProgress createSubTask(@Nullable String name, int units);

    /**
     * Begins the task with the specified amount of work units.
     * @param totalWork The total amount of work units.
     */
    void beginTask(int totalWork);

    /**
     * Ends the task with the specified state.
     * @param state A member of the {@link TaskCompletedState} enumeration.
     */
    void endTask(TaskCompletedState state);

}
