package pl.kurylek.lunapark.common;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContextProducer {

	public static AnnotationConfigApplicationContext produce(String... basePackages) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.scan(basePackages);
		ctx.refresh();
		return ctx;
	}
}
