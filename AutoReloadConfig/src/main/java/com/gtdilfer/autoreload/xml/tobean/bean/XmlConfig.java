package com.gtdilfer.autoreload.xml.tobean.bean;

import java.util.List;

public class XmlConfig {
	
	private int configVersion;
	
	private int version;

	private String[] names;

	private List<String> nameList;

	private Database database;

	private Database[] databaseArr;

	private List<Database> databases;

	private List<User> users;

	/**
	 * @return {@link #version}
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            {@linkplain #version 参见}
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return {@link #names}
	 */
	public String[] getNames() {
		return names;
	}

	/**
	 * @param names
	 *            {@linkplain #names 参见}
	 */
	public void setNames(String[] names) {
		this.names = names;
	}

	/**
	 * @return {@link #nameList}
	 */
	public List<String> getNameList() {
		return nameList;
	}

	/**
	 * @param nameList
	 *            {@linkplain #nameList 参见}
	 */
	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}

	/**
	 * @return {@link #database}
	 */
	public Database getDatabase() {
		return database;
	}

	/**
	 * @param database
	 *            {@linkplain #database 参见}
	 */
	public void setDatabase(Database database) {
		this.database = database;
	}

	/**
	 * @return {@link #databaseArr}
	 */
	public Database[] getDatabaseArr() {
		return databaseArr;
	}

	/**
	 * @param databaseArr
	 *            {@linkplain #databaseArr 参见}
	 */
	public void setDatabaseArr(Database[] databaseArr) {
		this.databaseArr = databaseArr;
	}

	/**
	 * @return {@link #databases}
	 */
	public List<Database> getDatabases() {
		return databases;
	}

	/**
	 * @param databases
	 *            {@linkplain #databases 参见}
	 */
	public void setDatabases(List<Database> databases) {
		this.databases = databases;
	}

	/**
	 * @return {@link #users}
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            {@linkplain #users 参见}
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	public int getConfigVersion() {
		return configVersion;
	}

	/**
	 * @param configVersion
	 */
	public void setConfigVersion(int configVersion) {
		this.configVersion = configVersion;
	}	
}
