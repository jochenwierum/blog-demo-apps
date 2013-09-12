package de.jowisoftware.mocking;

import org.junit.Assert;
import org.junit.Test;

public class MockTest {
    @Test
    public void testVoidMock() {
        final MockContext context = new MockContext();
        final MyInterface mock = context.mock(MyInterface.class);

        context.defineExpectations(new ExpectationsBuilder() {
            @Override
            void build() {
                oneOf(mock).method1();
                oneOf(mock).method2();
            }
        });

        mock.method1();
        Assert.assertEquals(0, mock.method2());

        boolean cought = false;
        try {
            mock.method1();
        } catch (final AssertionError e) {
            cought = true;
        }
        Assert.assertTrue("Expected exception, bot got none", cought);

        context.verify();
    }

    @Test
    public void testtMockWithResults() {
        final MockContext context = new MockContext();
        final MyInterface mock = context.mock(MyInterface.class);

        context.defineExpectations(new ExpectationsBuilder() {
            @Override
            void build() {
                oneOf(mock).method2();
                willReturn(2);
                oneOf(mock).method2();
                willReturn(4);
            }
        });

        Assert.assertEquals(2, mock.method2());
        Assert.assertEquals(4, mock.method2());

        boolean cought = false;
        try {
            mock.method2();
        } catch (final AssertionError e) {
            cought = true;
        }
        Assert.assertTrue("Expected exception, bot got none", cought);
        context.verify();
    }

    @Test
    public void testMockWithParameters() {
        final MockContext context = new MockContext();
        final MyInterface mock = context.mock(MyInterface.class);

        context.defineExpectations(new ExpectationsBuilder() {
            @Override
            void build() {
                oneOf(mock).method3(2);
                oneOf(mock).method3(5);
            }
        });

        mock.method3(2);

        boolean cought = false;
        try {
            mock.method3(7);
        } catch (final AssertionError e) {
            cought = true;
        }
        Assert.assertTrue("Expected exception, bot got none", cought);

        mock.method3(5);

        context.verify();
    }

    @Test(expected = AssertionError.class)
    public void testMockWithTooLessCalls() {
        final MockContext context = new MockContext();
        final MyInterface mock = context.mock(MyInterface.class);

        context.defineExpectations(new ExpectationsBuilder() {
            @Override
            void build() {
                oneOf(mock).method1();
                oneOf(mock).method1();
            }
        });

        context.verify();
    }
}
