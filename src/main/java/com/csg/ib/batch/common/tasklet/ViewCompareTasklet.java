package com.csg.ib.batch.common.tasklet;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.csg.ib.batch.support.cache.CustomCacheManager;
import com.csg.ib.batch.support.infra.jdbc.DbConnectionUtil;

@Component("test.job1.compare.view.tasklet")
@StepScope
public class ViewCompareTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewCompareTasklet.class);

	@Autowired
	@Qualifier("getDB1Connection")
	private Connection dbConnection1;

	@Autowired
	@Qualifier("getDB2Connection")
	private Connection dbConnection2;

	@Value("${enable.matching.view}")
	private boolean enabled;

	@Value("${spring.datasource.DB_1.username}")
	private String userName1;

	@Value("${spring.datasource.DB_2.username}")
	private String userName2;

	private static final String OBJECT_NAME = "VIEW";

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		LOGGER.info("Compare : {}", OBJECT_NAME);

		if (enabled) {
			List<String> objects1 = DbConnectionUtil.getAll(dbConnection1, userName1, OBJECT_NAME);
			List<String> objects2 = DbConnectionUtil.getAll(dbConnection2, userName2, OBJECT_NAME);

			if (CollectionUtils.isEmpty(objects1) && CollectionUtils.isEmpty(objects2)) {
				LOGGER.info("{} : NOT present in Both Schema", OBJECT_NAME);
			} else {
				List<String> missinginDB1 = new ArrayList<>(objects2);
				List<String> missinginDB2 = new ArrayList<>(objects1);

				missinginDB1.removeAll(objects1);
				missinginDB2.removeAll(objects2);

				if (missinginDB1.size() < 1 && missinginDB2.size() < 1) {
					LOGGER.info("{} : Matched in Both Schema", OBJECT_NAME);
				} else {
					LOGGER.error("{} : NOT Matched in Both Schema", OBJECT_NAME);
					LOGGER.error("{} : Missing in Schema 1: {}", OBJECT_NAME, missinginDB1);
					LOGGER.error("{} : Missing in Schema 2: {}", OBJECT_NAME, missinginDB2);
					CustomCacheManager.reportingCache.put(OBJECT_NAME + " : Missing in Schema 1", missinginDB1);
					CustomCacheManager.reportingCache.put(OBJECT_NAME + " : Missing in Schema 2", missinginDB2);
				}
			}
		}

		return RepeatStatus.FINISHED;
	}

}
