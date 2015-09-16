package org.metaborg.spoofax.intellij.jps.processing;

import org.apache.commons.lang.NotImplementedException;
import org.metaborg.core.processing.IProgressReporter;

// TODO: Add this to Metaborg Core?
/**
 * A simple progress reporter that determines the progress as a floating-point
 * value between 0.0 and 1.0.
 */
public final class SimpleProgressReporter implements IProgressReporter {

    /**
     * Reports progress as a value between 0.0 and 1.0.
     * @param done The progress as a value between 0.0 and 1.0;
     *             or {@link Float#NaN} when the progress is unknown.
     */
    public void reportProgress(float done) {
        throw new NotImplementedException();
    }

    public interface Progress {
        void report(float done);
    }

    private static final int MINIMUM_RESOLUTION = 1000;

    private int totalForParent;
    private int totalForChildren;
    private int usedForParent = 0;
    private double usedForChildren = 0.0d;

    private SimpleProgressReporter lastSubMonitor = null;

    public SimpleProgressReporter() {
        this(0);
    }

    public SimpleProgressReporter(int availableToChildren) {
        this(MINIMUM_RESOLUTION, availableToChildren);
    }

    public SimpleProgressReporter(int totalWork, int availableToChildren) {
        this.totalForParent = (totalWork > 0) ? totalWork : 0;
        this.totalForChildren = availableToChildren;
    }

    @Override
    public void setWorkRemaining(int remaining) {
        // Ensure positive:
        remaining = Math.max(0, remaining);

        if (this.totalForChildren > 0 && this.totalForParent > this.usedForParent) {
            double remainingForParent = this.totalForParent * (1.0d - (this.usedForChildren / this.totalForChildren));
            this.usedForChildren = remaining * (1.0d - remainingForParent / (totalForParent - usedForParent));
        } else {
            this.usedForChildren = 0.0d;
        }

        this.totalForParent = this.totalForParent - this.usedForParent;
        this.usedForParent = 0;
        this.totalForChildren = remaining;
    }

    private int consume(double ticks) {
        if (this.totalForParent == 0 || this.totalForChildren == 0)
            // No work to report.
            return 0;

        this.usedForChildren = clamp(0.0, this.usedForChildren + ticks, this.totalForChildren);

        int parentPosition = (int)(this.totalForParent * this.usedForChildren / this.totalForChildren);
        int delta = parentPosition - this.usedForParent;

        usedForParent = parentPosition;
        return delta;
    }

    @Override
    public void work(int work) {
        internalWorked(work);
    }

    public void internalWorked(double work) {
        int delta = consume(Math.max(0.0d, work));
        if (delta != 0) {
            // root worked delta
        }
    }

    private double clamp(double min, double value, double max)
    {
        return Math.max(min, Math.min(value, max));
    }

    @Override
    public IProgressReporter subProgress(int totalWork) {
        double totalWorkDouble = clamp(0, totalWork, this.totalForChildren - this.usedForChildren);

        return new SimpleProgressReporter(consume(totalWorkDouble), (int)totalWorkDouble);
    }

}
