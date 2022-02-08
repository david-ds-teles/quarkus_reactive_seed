package com.david.ds.teles.test.resources;

import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * testcontainer configuration to start a docker-compose with needed services
 *
 * @author davidteles
 *
 */
public class TestContainerResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {
	DockerComposeContainer<?> testInfra;
	static final int DB_PORT = 3306;

	@SuppressWarnings("resource")
	@Override
	public Map<String, String> start() {
		Log.info("starting test container");

		try {
			testInfra =
				new DockerComposeContainer<>(new File("docker-compose.yml"))
				.withExposedService(
						"mysql",
						DB_PORT,
						Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(20))
					);

			testInfra.start();

			Log.info("test container started");

			Map<String, String> conf = new HashMap<>();
			return conf;
		} catch (Throwable e) {
			Log.error("failed to start test container", e);
			throw e;
		}
	}

	@Override
	public void stop() {
		System.out.println("stopping test container");
		testInfra.stop();
		System.out.println("test container stopped");
	}
}
