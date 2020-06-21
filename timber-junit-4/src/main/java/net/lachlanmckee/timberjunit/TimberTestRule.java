package net.lachlanmckee.timberjunit;

import android.util.Log;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import timber.log.Timber;

/**
 * A JUnit {@link TestRule} that plants a {@link Timber.Tree} before a test is executed,
 * and uproots the {@link Timber.Tree} afterwards.
 * <p>
 * This is useful as a planted {@link Timber.Tree} would exist between test classes, which may be
 * undesirable.
 */
public class TimberTestRule implements TestRule {
    private final Rules mRules;

    public TimberTestRule(Rules rules) {
        mRules = rules;
    }

    /**
     * @return a {@link TimberTestRule} that logs messages of any priority regardless of the
     * test outcome
     */
    public static TimberTestRule logAllAlways() {
        return new TimberTestRule(new Rules().onlyLogWhenTestFails(false));
    }

    /**
     * @return a {@link TimberTestRule} that logs all messages, regardless of their priority.
     */
    public static TimberTestRule logAllWhenTestFails() {
        return new TimberTestRule(new Rules());
    }

    /**
     * @return a {@link TimberTestRule} that only logs error messages regardless of the test
     * outcome.
     */
    public static TimberTestRule logErrorsAlways() {
        return new TimberTestRule(new Rules()
                .onlyLogWhenTestFails(false)
                .minPriority(Log.ERROR));
    }

    /**
     * @return a {@link TimberTestRule} that only logs error messages when a unit test fails.
     */
    public static TimberTestRule logErrorsWhenTestFails() {
        return new TimberTestRule(new Rules().minPriority(Log.ERROR));
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new TimberStatement(base, mRules);
    }

    /**
     * The JUnit statement that plants before the unit test, and uproots it after completion.
     */
    private static class TimberStatement extends Statement {
        private final Statement mNext;
        private final BufferedJUnitTimberTree mTree;

        TimberStatement(Statement base, Rules rules) {
            mNext = base;
            mTree = new BufferedJUnitTimberTree(rules);
        }

        @Override
        public void evaluate() throws Throwable {
            Timber.plant(mTree);
            try {
                mNext.evaluate();

            } catch (Throwable t) {
                mTree.flushLogs();
                throw t;

            } finally {
                // Ensure the tree is removed to avoid duplicate logging.
                Timber.uproot(mTree);
            }
        }
    }
}
