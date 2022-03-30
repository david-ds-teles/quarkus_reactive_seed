package com.david.ds.teles.core.services;

import com.david.ds.teles.core.domain.Account;
import com.david.ds.teles.repository.AccountRepository;
import com.david.ds.teles.utils.exceptions.MyExceptionError;
import com.david.ds.teles.utils.logging.DefaultLogging.Logged;
import com.david.ds.teles.utils.validator.MyValidatorGroups;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

@ApplicationScoped
@Logged
public class AccountService {
	private Validator validator;

	private AccountRepository accountRepo;

	public AccountService(AccountRepository accountRepo, Validator validator) {
		this.accountRepo = accountRepo;
		this.validator = validator;
	}

	@ReactiveTransactional
	public Uni<Account> save(@Valid @ConvertGroup(to = Default.class) Account account) {
		account.create();

		Uni<Account> result =
			this.accountRepo.persist(account)
				.onFailure()
				.transform(
					e -> {
						Log.error("error traying to save an account", e);
						return new MyExceptionError(500, "failed_with", new String[] { "SQL Error" });
					}
				);

		return result;
	}

	public Uni<Void> update(Account account) {
		account.setUpdatedAt(OffsetDateTime.now());

		Set<ConstraintViolation<Account>> violations =
			this.validator.validate(account, Default.class, MyValidatorGroups.Update.class);

		if (!violations.isEmpty()) throw new ConstraintViolationException(violations);

		Log.info("validation complete");

		Uni<Integer> updated =
			this.accountRepo.update(
					Queries.UPDATE,
					account.getProvider(),
					account.getUpdatedAt(),
					account.getId()
				);

		Uni<Void> result = updated
			.onFailure()
			.transform(
				e -> {
					Log.error("error traying to update an account", e);

					return new MyExceptionError(500, "failed_with", new String[] { "SQL Error" });
				}
			)
			.onItem()
			.transformToUni(
				qtdUpdated -> {
					Log.infof("checking updated results %d", qtdUpdated);

					if (qtdUpdated <= 0) return Uni
						.createFrom()
						.failure(new MyExceptionError(404, "not_updated", null));

					return Uni.createFrom().nullItem();
				}
			);

		return result;
	}

	public Uni<List<Account>> findAll() {
		Uni<List<Account>> result = this.accountRepo.getAll();
		return result;
	}

	private static interface Queries {
		public static final String UPDATE = "provider=?1, updatedAt=?2 where id = ?3";
	}
}
