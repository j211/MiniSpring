package context;

import annotations.Bean;
import annotations.Configuration;
import context.Circle;

@Configuration
public class ContextApp {
    @Bean
    public Circle circle() { return new Circle();
    }

    @Bean
    public Rectangle rectangle() {
        return new Rectangle();
    }

    @Bean
    public Square square() {
        return new Square();
    }

}
