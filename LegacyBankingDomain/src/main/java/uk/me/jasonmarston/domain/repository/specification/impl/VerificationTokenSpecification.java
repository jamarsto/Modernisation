package uk.me.jasonmarston.domain.repository.specification.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;

public class VerificationTokenSpecification {
	public static class VerificationTokenIsExpired 
			implements Specification<VerificationToken> {
		private static final long serialVersionUID = 1L;

		public VerificationTokenIsExpired() {
		}
		
		@Override
		public Predicate toPredicate(
				final Root<VerificationToken> root, 
				final CriteriaQuery<?> query, 
				final CriteriaBuilder builder) {

			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Timestamp(cal.getTime().getTime()));
			cal.add(Calendar.MINUTE, 65);

			return builder.greaterThanOrEqualTo(
					root.<Date>get("expiryDate"),
					cal.getTime());
		}
	}
}