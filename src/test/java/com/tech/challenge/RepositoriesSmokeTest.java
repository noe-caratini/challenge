package com.tech.challenge;

import com.tech.challenge.dao.CustomerRepository;
import com.tech.challenge.dao.LicenseRepository;
import com.tech.challenge.dao.MatchRepository;
import com.tech.challenge.dao.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepositoriesSmokeTest {

	@Autowired private DataSource dataSource;
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private EntityManager entityManager;

	@Autowired private TournamentRepository tournamentRepository;
	@Autowired private MatchRepository matchRepository;
	@Autowired private CustomerRepository customerRepository;
	@Autowired private LicenseRepository licenseRepository;

	@Test
	public void injectedComponentsAreNotNull(){
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();

		assertThat(tournamentRepository).isNotNull();
		assertThat(matchRepository).isNotNull();
		assertThat(customerRepository).isNotNull();
		assertThat(licenseRepository).isNotNull();
	}
}
