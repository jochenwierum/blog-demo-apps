package de.jowisoftware.myDI.integrationtests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jowisoftware.myDI.DIContext;
import de.jowisoftware.myDI.DIXmlContext;
import de.jowisoftware.myDI.integrationtests.test1.Class1;
import de.jowisoftware.myDI.integrationtests.test1.Class2;

/*
 * Exactly like the first one, but with another way of receiving the beans:
 * by class name
 */

public class Test1AIT {
    @Test
    public void contextIsInitializedCorrectly() {
        final DIContext context = new DIXmlContext(
                "/integrationTests/dependencies.test1.xml");

        final Class1 bean1 = context.getBean(Class1.class);
        final Class2 bean2 = context.getBean(Class2.class);

        assertThat(bean1.getProp1(), is(equalTo("val1")));
        assertThat(bean2.getProp3(), is(equalTo(4)));
    }
}
