package com.david.ds.teles.core.domain;

import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import com.david.ds.teles.utils.exceptions.MyExceptionError;
import com.david.ds.teles.utils.i18n.AppMessages;
import com.david.ds.teles.utils.validator.MyValidatorGroups;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Email
	private String email;

	@NotBlank(groups = { MyValidatorGroups.Update.class })
	private String provider;

	@PastOrPresent(groups = { MyValidatorGroups.Update.class })
	private OffsetDateTime updatedAt;

	public void create(AppMessages message) {

		if (this.email == null)
			throw new MyExceptionError(message.invalid_email(email));

		Pattern p = Pattern.compile("^.+@foo\\.com");
		Matcher m = p.matcher(this.email);

		if (m.matches())
			throw new MyExceptionError(message.invalid_email(email));
	}
}
