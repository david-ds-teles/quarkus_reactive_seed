package com.david.ds.teles.utils.health;

import io.quarkus.logging.Log;
import io.smallrye.health.api.AsyncHealthCheck;
import io.smallrye.mutiny.Uni;
import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Startup;

/**
 * Startup procedures are defined as an option for slow starting containers
 * which will take over from startup once the startup responds UP for the first
 * time
 *
 * @author davidteles
 *
 */
@ApplicationScoped
@Startup
public class ReactiveStartupHealth implements AsyncHealthCheck {

	@Override
	public Uni<HealthCheckResponse> call() {
		Log.info("running health check ReactiveStartupHealth");
		return Uni
			.createFrom()
			.item(HealthCheckResponse.up("reactive startup health check"))
			.onItem()
			.delayIt()
			.by(Duration.ofMillis(10));
	}
}
