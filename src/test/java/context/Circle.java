package context;

import annotations.Autowired;
import annotations.Qualifier;

public class Circle {
    private TestBean bean;

    @Autowired
    public Circle(@Qualifier("testBean") TestBean bean) {
        this.bean = bean;
    }

    public Circle() {
    }

    public TestBean getBean() {
        return bean;
    }
}
