package de.jowisoftware.mocking;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Matcher;

import de.jowisoftware.mocking.results.MockCallResult;
import de.jowisoftware.mocking.results.MockCallThrowableResult;
import de.jowisoftware.mocking.results.MockCallValueResult;

public class ExpectationList {
    private static class Expectation {
        private final Method method;
        private final Object[] arguments;

        public MockCallResult result;

        private boolean useMatchers = false;
        private Matcher<?>[] matchers;

        public Expectation(final Method method, final Object[] arguments) {
            this.method = method;
            this.arguments = arguments;
        }

        public void registerMatchers(final Matcher<?>[] matchers) {
            if (matchers.length != arguments.length) {
                throw new IllegalStateException(
                        "Either all or none arguments must use 'with'");
            }

            useMatchers = true;
            this.matchers = matchers;
        }

        public boolean matches(final Method calledMethod,
                final Object[] methodArguments) {
            if (!calledMethod.equals(method)) {
                return false;
            }

            if (!useMatchers) {
                return Arrays.deepEquals(arguments, methodArguments);
            } else {
                for (int i = 0; i <matchers.length; ++i) {
                    if (!matchers[i].matches(methodArguments[i])) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private final List<Expectation> expectations = new ArrayList<>();
    private Expectation lastExpectation;

    public void expectCall(final Method method, final Object[] arguments) {
        final Expectation expectation = new Expectation(method, arguments);
        final Object returnValue = MockUtils.getDefaultValue(method.getReturnType());
        expectation.result = new MockCallValueResult(returnValue);
        expectations.add(expectation);
        lastExpectation = expectation;
    }

    public MockCallResult registerCall(final Method method,
            final Object[] arguments) {

        for (final Iterator<Expectation> it = expectations.iterator(); it
                .hasNext();) {
            final Expectation expectation = it.next();

            if (expectation.matches(method, arguments)) {
                it.remove();
                return expectation.result;
            }
        }

        return null;
    }

    public void addResultToLastCall(final Object result) {
        lastExpectation.result = new MockCallValueResult(result);
    }

    public void addThrowableToLastCall(final Throwable throwable) {
        lastExpectation.result = new MockCallThrowableResult(throwable);
    }

    public boolean isEmpty() {
        return expectations.isEmpty();
    }

    public void registerMatcherToLastCall(final Matcher<?>[] matchersArray) {
        lastExpectation.registerMatchers(matchersArray);
    }
}
