package pl.kurylek.lunapark.employees;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.kurylek.lunapark.common.SpringContextProducer;

import static pl.kurylek.lunapark.common.SpringExtension.SpringExtProvider;

public class EmployeesApplication {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = SpringContextProducer.produce("pl.kurylek.lunapark.employees");

		ActorSystem system = ctx.getBean(ActorSystem.class);
		system.actorOf(SpringExtProvider.get(system).props("rollercoasterGatekeeper"), "rollercoaster-gatekeeper");
		system.actorOf(SpringExtProvider.get(system).props("kioskSalesman"), "kiosk-salesman");
	}
}
