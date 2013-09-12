package de.jowisoftware.myDI.integrationtests.test1;

import de.jowisoftware.myDI.integrationtests.test1.subpackage.Class4;
import de.jowisoftware.myDI2.annotations.Bean;
import de.jowisoftware.myDI2.annotations.Resource;

@Resource
public class Class2 {
    @Bean
    private Class4 subBean;

    public Class4 getBean() {
        return subBean;
    }
}
