package de.jowisoftware.mocking;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExpectationList {
    private static class Expectation {
        public final Method method;
        public final Object[] arguments;

        public boolean hasResult;
        public Object result;

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
                return new MockCallResult(true, expectation.hasResult, expectation.result);
            }
        }

        return new MockCallResult(false, false, null);
    }

    public void addResultToLastCall(final Object result) {
        lastExpectation.hasResult = true;
        lastExpectation.result = result;
    }

    public boolean isEmpty() {
        return expectations.isEmpty();
    }
}
