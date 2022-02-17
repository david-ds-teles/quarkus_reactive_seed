package com.david.ds.teles.utils.health;

import io.quarkus.logging.Log;
import io.smallrye.health.api.AsyncHealthCheck;
import io.smallrye.mutiny.Uni;
import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

/**
 * Always will be called by the health/live endpoint to check if the app is
 * living.
 *
 * @author davidteles
 *
 */
@ApplicationScoped
@Liveness
public class ReactiveLivenessHealth implements AsyncHealthCheck {

	@Override
	public Uni<HealthCheckResponse> call() {
		Log.info("running health check ReactiveLivenessHealth");
		return Uni
			.createFrom()
			.item(HealthCheckResponse.up("reactive liveness health check"))
			.onItem()
			.delayIt()
			.by(Duration.ofMillis(10));
	}
}
