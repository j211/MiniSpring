package context;

import annotations.Autowired;
import annotations.Qualifier;

public class Rectangle {
    @Autowired
    @Qualifier("testBean")
    private TestBean testBean;

    public TestBean getTestBean() {
        return testBean;
    }


}
