package com.gtdilfer.notautoload.tobean.xml;

public class Database {
	private String url;

	private int port;

	private String login;

	private String password;

	/**
	 * @return {@link #url}
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            {@linkplain #url 参见}
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return {@link #port}
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            {@linkplain #port 参见}
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return {@link #login}
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            {@linkplain #login 参见}
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return {@link #password}
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            {@linkplain #password 参见}
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
