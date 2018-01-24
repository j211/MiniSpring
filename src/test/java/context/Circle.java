package context;

import annotations.Autowired;
import annotations.Qualifier;

public class Circle {
    private TestBean bean;
    private Square squ;

    @Autowired
    public Circle(@Qualifier("testBean1") TestBean bean) {
        this.bean = bean;
    }

    public Circle() {}

    @Autowired
    public void setSqu(Square squ) {
        this.squ = squ;
    }

    public TestBean getFieldBean() {
        return bean;
    }

    public Square getSqu() {
        return squ;
    }
}
