package de.jowisoftware.myDI.integrationtests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import de.jowisoftware.myDI.DIXmlContext;
import de.jowisoftware.myDI.exceptions.DIException;
import de.jowisoftware.myDI.integrationtests.test4.Class1;

/*
 * Fourth goal: No new feature, but great error handling.
 * Throw exceptions when a context cannot be initialized - provide as many information as possible
 */

public class Test4IT {
    @Test
    public void nonExistingClassThrowsError() {
        assertError("/integrationTests/dependencies.test4.error1.xml",
                "MissingClass", "beanid");
    }

    @Test
    public void nonExistingReferenceThrowsError() {
        assertError("/integrationTests/dependencies.test4.error2.xml",
                "beanid", "date");
    }

    @Test
    public void illegalSetterAreRejected() {
        assertError("/integrationTests/dependencies.test4.error3.xml",
                "beanid", "date");
    }

    @Test
    public void illegalValuesAreRejected() {
        assertError("/integrationTests/dependencies.test4.error4.xml",
                "beanid", "date", "transform", "7");
    }

    @Test
    public void illegalRefsAreRejected() {
        assertError("/integrationTests/dependencies.test4.error5.xml",
                "beanid", "Class1", "Date");
    }

    @Test
    public void exceptionsAreHandled() {
        assertError("/integrationTests/dependencies.test4.error6.xml",
                "beanid", "initialize");
    }

    @Test
    public void getBeanHandlesInvalidIDs() {
        // oh yeah... using a functional approach would have saved us from this
        // duplicated code :-)
        try {
            new DIXmlContext("/integrationTests/dependencies.test4.error7.xml")
                    .getBean("wrongBean");
            fail("Expected Exception was not thrown!");
        } catch (final Exception e) {
            assertThat(e, is(instanceOf(DIException.class)));
            assertThat(e.getMessage(), containsString("wrongBean"));
            assertThat(e.getMessage(), containsString("not exist"));
        }
    }

    @Test
    public void getBeanHandlesInvalidClasses() {
        try {
            new DIXmlContext("/integrationTests/dependencies.test4.error7.xml")
                    .getBean(Date.class);
            fail("Expected Exception was not thrown!");
        } catch (final Exception e) {
            assertThat(e, is(instanceOf(DIException.class)));
            assertThat(e.getMessage(), containsString("Date"));
            assertThat(e.getMessage(), containsString("not exist"));
        }
    }

    @Test
    public void getBeanHandlesAmbiguousClasses() {
        try {
            new DIXmlContext("/integrationTests/dependencies.test4.error7.xml")
                    .getBean(Class1.class);
            fail("Expected Exception was not thrown!");
        } catch (final Exception e) {
            assertThat(e, is(instanceOf(DIException.class)));
            assertThat(e.getMessage(), containsString("Class1"));
            assertThat(e.getMessage(), containsString("Ambiguous"));
        }
    }

    private void assertError(final String xmlFile, final String ... messages) {
        try {
            new DIXmlContext(xmlFile);
            fail("Expected Exception was not thrown!");
        } catch (final Exception e) {
            assertThat(e,
                    is(instanceOf(DIException.class)));

            for (final String message : messages) {
                assertThat(e.getMessage(), containsString(message));
            }
        }
    }
}
