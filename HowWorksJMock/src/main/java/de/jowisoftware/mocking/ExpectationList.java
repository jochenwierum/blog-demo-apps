package de.jowisoftware.mocking;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.jowisoftware.mocking.results.MockCallResult;
import de.jowisoftware.mocking.results.MockCallThrowableResult;
import de.jowisoftware.mocking.results.MockCallValueResult;

public class ExpectationList {
    private static class Expectation {
        public final Method method;
        public final Object[] arguments;

        public MockCallResult result;

        public Expectation(final Method method, final Object[] arguments) {
            this.method = method;
            this.arguments = arguments;
        }

        public boolean matches(final Method calledMethod,
                final Object[] methodArguments) {
            return calledMethod.equals(method)
                    && Arrays.deepEquals(arguments, methodArguments);
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
}
