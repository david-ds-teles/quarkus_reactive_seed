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

	/**
	 * for some reason testcontainers doesn't look to exposed port, but to inner
	 * container port.
	 */
	static final int DB_INNER_PORT = 3306;
	static final int DB_PORT_FORWARD = 3360;

	@SuppressWarnings("resource")
	@Override
	public Map<String, String> start() {
		Log.info("starting test container");

		try {
			testInfra =
				new DockerComposeContainer<>(new File("src/test/resources/test-docker-compose.yml"))
				.withExposedService(
						"mysql",
						DB_INNER_PORT,
						Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30))
					);

			testInfra.start();

			Log.info("test container started");

			Map<String, String> conf = new HashMap<>();
			conf.put(
				"quarkus.datasource.reactive.url",
				"mysql://localhost:" + DB_PORT_FORWARD + "/quarkus"
			);

			return conf;
		} catch (Throwable e) {
			Log.error("failed to start test container", e);
			throw e;
		}
	}

	@Override
	public void stop() {
		Log.info("stopping test container");
		testInfra.stop();
		Log.info("test container stopped");
	}
}
