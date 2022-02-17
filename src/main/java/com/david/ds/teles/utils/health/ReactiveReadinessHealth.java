package com.david.ds.teles.utils.health;

import io.quarkus.logging.Log;
import io.smallrye.health.api.AsyncHealthCheck;
import io.smallrye.mutiny.Uni;
import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

/**
 * Used to simulates a connection to an external service provider such as a
 * database
 *
 * @author davidteles
 *
 */
@ApplicationScoped
@Readiness
public class ReactiveReadinessHealth implements AsyncHealthCheck {

	@Override
	public Uni<HealthCheckResponse> call() {
		Log.info("running health check ReactiveReadinessHealth");
		return Uni
			.createFrom()
			.item(HealthCheckResponse.up("reactive readiness health check"))
			.onItem()
			.delayIt()
			.by(Duration.ofMillis(10));
	}
}
