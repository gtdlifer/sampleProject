package com.gtdilfer.notautoload.tobean.xml;

public class User {
	private String name;

	private int age;

	private String[] info;

	/**
	 * @return {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            {@linkplain #name 参见}
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return {@link #age}
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            {@linkplain #age 参见}
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return {@link #info}
	 */
	public String[] getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            {@linkplain #info 参见}
	 */
	public void setInfo(String[] info) {
		this.info = info;
	}

}
