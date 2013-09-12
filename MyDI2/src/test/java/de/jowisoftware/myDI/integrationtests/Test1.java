package de.jowisoftware.myDI.integrationtests;

import org.junit.Test;

import de.jowisoftware.myDI.integrationtests.test1.Class1;
import de.jowisoftware.myDI.integrationtests.test1.Class2;
import de.jowisoftware.myDI2.DIContext;

public class Test1 {
    @Test
    public void beansAreFound() {
        final DIContext context = new DIContext(
                "de.jowisoftware.myDI.integrationtests.test1", "org.hamcrest");
        context.getBean(Class1.class).greet();
        context.getBean(Class2.class).getBean().greet();
    }
}
