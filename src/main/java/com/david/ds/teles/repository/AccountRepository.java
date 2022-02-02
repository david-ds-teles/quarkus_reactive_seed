package com.david.ds.teles.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.david.ds.teles.core.domain.Account;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

	public Uni<Account> findByEmail(String email) {
		return find("email", email).firstResult();
	}

	public Uni<List<Account>> getAll() {
		return listAll(Sort.by("email"));
	}
}
