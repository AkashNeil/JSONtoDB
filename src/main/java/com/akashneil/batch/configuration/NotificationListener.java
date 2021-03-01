package com.akashneil.batch.configuration;

import com.akashneil.batch.dao.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener extends JobExecutionListenerSupport{

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NotificationListener(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("JOB FINISHED!");

            jdbcTemplate.query("SELECT first_name, last_name FROM person",
                    (rs, row) -> new Person(
                            rs.getString(1),
                            rs.getString(2))
            ).forEach(person -> LOGGER.info("Found <" + person + "> in the database."));
        }
    }
}
