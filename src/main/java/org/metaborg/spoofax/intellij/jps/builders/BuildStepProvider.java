package org.metaborg.spoofax.intellij.jps.builders;

import org.metaborg.core.project.IProject;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BuildStepProvider implements IBuildStepProvider {


    /**
     * Returns an ordered list of build steps.
     * @param project The project that will be built.
     * @param rootSteps A collection of root step descriptors where everything starts.
     * @return A list of build steps, from first to last.
     */
    protected static List<IBuildStep> getBuildSteps(IProject project, Collection<IBuildStepDescriptor> rootSteps) {
        List<IBuildStepDescriptor> ordered = sortTopologically(rootSteps);
        return ordered.stream().map(descriptor -> descriptor.createStep(project)).collect(Collectors.toList());
    }

    /**
     * Sorts a list of build step descriptors toplogically.
     * @param rootSteps The collection of root steps.
     * @return An ordered list of build steps, from first to last.
     */
    private static List<IBuildStepDescriptor> sortTopologically(Collection<IBuildStepDescriptor> rootSteps) {
        List<IBuildStepDescriptor> worklist = new ArrayList<>(rootSteps);

        List<IBuildStepDescriptor> ordered = new ArrayList<>();
        HashSet<IBuildStepDescriptor> visited = new HashSet<>();

        // Build a topological ordering of build steps.
        while (!worklist.isEmpty()) {
            IBuildStepDescriptor step = worklist.remove(worklist.size() - 1);
            satisfyDependencies(step, ordered, visited);
        }
        return ordered;
    }

    /**
     * Satisfies the dependencies of the specified build step.
     * @param step The build step whose dependencies to satisfy.
     * @param ordered The ordered list of build steps, from first to last.
     */
    private static void satisfyDependencies(IBuildStepDescriptor step, List<IBuildStepDescriptor> ordered, HashSet<IBuildStepDescriptor> visited) {
        if (visited.contains(step))
            throw new RuntimeException("The build step has already been visited. There is a cycle!");
        visited.add(step);
        for (IBuildStepDescriptor dependency : step.dependencies()) {
            satisfyDependencies(dependency, ordered, visited);
        }
        // Assert: all dependencies of this step have been satisfied.
        ordered.add(step);
    }
}
