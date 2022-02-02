package com.david.ds.teles.api;

import com.david.ds.teles.core.domain.Account;
import com.david.ds.teles.core.services.AccountService;
import io.smallrye.mutiny.Uni;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/account")
public class AccountApi {
	private AccountService service;

	@Inject
	public AccountApi(AccountService service) {
		this.service = service;
	}

	@Path("/all")
	@GET
	public Uni<List<Account>> findAll() {
		return this.service.findAll();
	}

	@POST
	public Uni<Account> create(Account account) {
		Uni<Account> result = service.save(account);
		return result;
	}

	@PUT
	public Uni<Void> update(Account account) {
		return service.update(account);
	}
}
