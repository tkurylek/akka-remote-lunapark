package pl.kurylek.lunapark.test;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import org.junit.After;
import org.mockito.MockitoAnnotations;

public abstract class AkkaTestBase {
	protected ActorSystem system;

	public AkkaTestBase() {
		system = ActorSystem.create();
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void teardown() {
		JavaTestKit.shutdownActorSystem(system);
		system = null;
	}
}
