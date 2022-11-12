package com.csg.ib.batch.common.tasklet;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.csg.ib.batch.domain.ColumnDetail;
import com.csg.ib.batch.support.cache.CustomCacheManager;
import com.csg.ib.batch.support.infra.jdbc.DbConnectionUtil;

@Component("test.job1.compare.table.object.column.tasklet")
@StepScope
public class TableObjectColumnCompareTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory.getLogger(TableObjectColumnCompareTasklet.class);

	@Autowired
	@Qualifier("getDB1Connection")
	private Connection dbConnection1;

	@Autowired
	@Qualifier("getDB2Connection")
	private Connection dbConnection2;

	@Value("${enable.matching.table.object.column}")
	private boolean enabled;

	@Value("${spring.datasource.DB_1.username}")
	private String userName1;

	@Value("${spring.datasource.DB_2.username}")
	private String userName2;

	private static final String OBJECT_NAME = "TABLE_COLUMN";

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		LOGGER.info("Compare : {}", OBJECT_NAME);

		if (enabled) {

			List<String> matchedTableInBothSchemas = CustomCacheManager.matchedTableCache;

			for (String tablename : matchedTableInBothSchemas) {

				Map<String, ColumnDetail> objects1 = DbConnectionUtil.getAllColumnFromTable(dbConnection1, userName1,
						tablename);
				Map<String, ColumnDetail> objects2 = DbConnectionUtil.getAllColumnFromTable(dbConnection2, userName2,
						tablename);

				List<String> missinginDB1 = new ArrayList<>(objects2.keySet());
				List<String> missinginDB2 = new ArrayList<>(objects1.keySet());
				List<String> commonColumn = new ArrayList<>(objects2.keySet());
				List<String> unMatchedColumn = new ArrayList<>(objects2.keySet());

				missinginDB1.removeAll(objects1.keySet());
				missinginDB2.removeAll(objects2.keySet());
				commonColumn.retainAll(objects1.keySet());

				if (missinginDB1.size() > 0 && missinginDB2.size() > 0) {
					LOGGER.info("{} : Comparison Failed for TABLE NAME : {} , Missing in DB1 : {}, Missing in DB2 : {}",
							OBJECT_NAME, tablename, missinginDB1, missinginDB2);
					CustomCacheManager.reportingTableCache.add(OBJECT_NAME + " Comparison Failed for TABLE NAME : "
							+ tablename + " Missing in DB1 : " + missinginDB1 + " Missing in DB2 : " + missinginDB2);
				} else if (commonColumn.size() > 0) {

					for (String columnName : commonColumn) {
						if (!objects1.get(columnName).equals(objects2.get(columnName)))
							unMatchedColumn.add(columnName);

					}
					if (unMatchedColumn.size() > 0) {

					}

				}

			}

		}

		return RepeatStatus.FINISHED;
	}

}
