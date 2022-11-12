package com.csg.ib.batch.support.infra.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csg.ib.batch.custom.exception.ErrorCode;
import com.csg.ib.batch.custom.exception.InfrastructureException;
import com.csg.ib.batch.domain.ColumnDetail;

public class DbConnectionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DbConnectionUtil.class);

	public static Connection getDBConnection(String dbUrl, String userId, String password) {

		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			connection = DriverManager.getConnection(dbUrl, userId, password);
		} catch (ClassNotFoundException | SQLException ex) {
			// TODO Auto-generated catch block
			LOGGER.error("DB Connection issue : {}", ex.getMessage());
			throw new InfrastructureException("DB Connection issue", ex, ErrorCode.IO);
		}

		return connection;
	}

	/*
	 * 
	 * It gets the DB objects names from teh schema
	 * 
	 */
	public static List<String> getAll(Connection connection, String uID, String dbObjectType) {

		List<String> objects = new ArrayList<>();
		DatabaseMetaData metaData;

		try {
			metaData = connection.getMetaData();
			String[] names = { dbObjectType };
			ResultSet rs = metaData.getTables(null, uID, null, names);
			while (rs.next()) {
				String name = rs.getString(3);
				LOGGER.trace("Schema Name : {}, dbObjectType : {}, Name : {}", uID, dbObjectType, name);
				objects.add(name);
			}
			rs.close();

		} catch (SQLException ex) {
			LOGGER.error("DB Connection issue : {}", ex.getMessage());
			throw new InfrastructureException("DB Connection issue", ex, ErrorCode.IO);
		}

		return objects;

	}

	/*
	 * 
	 * It gets the DB function names from the schema
	 * 
	 */
	public static List<String> getAllFunctions(Connection connection, String uID) {

		List<String> objects = new ArrayList<>();
		DatabaseMetaData metaData;

		try {
			metaData = connection.getMetaData();
			ResultSet rs = metaData.getFunctions(null, uID, "%");
			while (rs.next()) {
				String name = rs.getString(3);
				LOGGER.trace("Schema Name : {}, FunctionName : {}", uID, name);
				objects.add(name);
			}
			rs.close();

		} catch (SQLException ex) {
			LOGGER.error("DB Connection issue Functions : {}", ex.getMessage());
			throw new InfrastructureException("DB Connection issue", ex, ErrorCode.IO);
		}

		return objects;

	}

	/*
	 * 
	 * It gets the Foreign keys of the table and map them against the table name for
	 * which the column is derived
	 * 
	 */
	public static Map<String, TreeSet<String>> getAllFKFromTable(Connection connection, String uID, String tableName) {

		Map<String, TreeSet<String>> objects = new HashMap<>();
		DatabaseMetaData metaData;

		try {
			metaData = connection.getMetaData();
			ResultSet rs = metaData.getImportedKeys(null, uID, tableName);
			while (rs.next()) {
				String pkTable = rs.getString("PKTABLE_NAME");
				String pkTableColumn = rs.getString("PKCOLUMN_NAME");
				LOGGER.trace("Schema Name : {}, Foreign Key Table : {} is : {} and Column Name : {}", uID, tableName,
						pkTable, pkTableColumn);

				if (objects.containsKey(pkTable)) {
					objects.get(pkTable).add(pkTableColumn);
				} else {
					TreeSet<String> s = new TreeSet<>();
					s.add(pkTableColumn);
					objects.put(pkTable, s);
				}

			}
			rs.close();

		} catch (SQLException ex) {
			LOGGER.error("DB Connection issue Foreign Key : {}", ex.getMessage());
			throw new InfrastructureException("DB Connection issue", ex, ErrorCode.IO);
		}

		return objects;

	}

	/*
	 * 
	 * It gets the Reference Foreign keys of the table and map them against the
	 * table name for which the column is derived
	 * 
	 */
	public static Map<String, TreeSet<String>> getAllFKReferencesFromTable(Connection connection, String uID,
			String tableName) {

		Map<String, TreeSet<String>> objects = new HashMap<>();
		DatabaseMetaData metaData;

		try {
			metaData = connection.getMetaData();
			ResultSet rs = metaData.getExportedKeys(null, uID, tableName);
			while (rs.next()) {
				String pkTable = rs.getString("FKTABLE_NAME");
				String pkTableColumn = rs.getString("FKCOLUMN_NAME");
				LOGGER.trace("Schema Name : {}, Foreign Key Reference of Table : {} is : {} and Column Name : {}", uID,
						tableName, pkTable, pkTableColumn);

				if (objects.containsKey(pkTable)) {
					objects.get(pkTable).add(pkTableColumn);
				} else {
					TreeSet<String> s = new TreeSet<>();
					s.add(pkTableColumn);
					objects.put(pkTable, s);
				}

			}
			rs.close();

		} catch (SQLException ex) {
			LOGGER.error("DB Connection issue Foreign Key Reference : {}", ex.getMessage());
			throw new InfrastructureException("DB Connection issue", ex, ErrorCode.IO);
		}

		return objects;

	}

	/*
	 * 
	 * It gets all the indexes of table
	 * 
	 */
	public static Map<String, TreeSet<String>> getAllIndexFromTable(Connection connection, String uID,
			String tableName) {

		Map<String, TreeSet<String>> objects = new HashMap<>();
		DatabaseMetaData metaData;

		try {
			metaData = connection.getMetaData();
			/*
			 * if 4th parameter is set to false, it will fetch all teh index regardless of
			 * whether unique or not
			 * 
			 * 
			 */
			ResultSet rs = metaData.getIndexInfo(null, uID, tableName, false, false);
			while (rs.next()) {
				String name = rs.getString("INDEX_NAME");
				LOGGER.trace("Schema Name : {}, Table : {}  and Index Name : {}", uID, tableName, name);

				if (null != name) {

					if (objects.containsKey(name)) {
						objects.get(name).add(rs.getString("COLUMN_NAME"));
					} else {
						TreeSet<String> s = new TreeSet<>();
						s.add(rs.getString("COLUMN_NAME"));
						objects.put(name, s);
					}
				}

			}
			rs.close();

		} catch (SQLException ex) {
			LOGGER.error("DB Connection issue Index : {}", ex.getMessage());
			throw new InfrastructureException("DB Connection issue", ex, ErrorCode.IO);
		}

		return objects;

	}

	/*
	 * 
	 * It gets all the columns of table
	 * 
	 */
	public static Map<String, ColumnDetail> getAllColumnFromTable(Connection connection, String uID, String tableName) {

		Map<String, ColumnDetail> objects = new HashMap<>();
		DatabaseMetaData metaData;

		try {
			metaData = connection.getMetaData();

			ResultSet rs = metaData.getColumns(null, uID, tableName, "%");
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				String dataType = rs.getString("DATA_TYPE");
				String columnSize = rs.getString("COLUMN_SIZE");
				String decimalDigits = rs.getString("DECIMAL_DIGITS");
				String isNullable = rs.getString("IS_NULLABLE");
				// LOGGER.trace("Schema Name : {}, Table : {} and Index Name : {}", uID,
				// tableName, name);

				ColumnDetail columnDetail = new ColumnDetail(columnName, dataType, columnSize, decimalDigits,
						isNullable);
				objects.put(columnName, columnDetail);

			}
			rs.close();

		} catch (SQLException ex) {
			LOGGER.error("DB Connection issue Column Details : {}", ex.getMessage());
			throw new InfrastructureException("DB Connection issue", ex, ErrorCode.IO);
		}

		return objects;

	}

	/*
	 * 
	 * It gets Primary keys from the Table
	 * 
	 */
	public static List<String> getAllPrimaryKeysFromTable(Connection connection, String uID, String tableName) {

		List<String> objects = new ArrayList<>();
		DatabaseMetaData metaData;

		try {
			metaData = connection.getMetaData();
			ResultSet rs = metaData.getPrimaryKeys(null, uID, tableName);
			while (rs.next()) {
				String name = rs.getString("COLUMN_NAME");
				LOGGER.trace("Schema Name : {}, Table Name : {}, PK : {}", uID, tableName, name);
				objects.add(name);
			}
			rs.close();

		} catch (SQLException ex) {
			LOGGER.error("DB Connection issue PK : {}", ex.getMessage());
			throw new InfrastructureException("DB Connection issue", ex, ErrorCode.IO);
		}

		return objects;

	}

}
