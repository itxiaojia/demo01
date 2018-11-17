package com.itheima.freemarker.test;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/14 21:38
 */
public class Demo1 {

	public static void main(String[] args) {

		int i = "".hashCode();
		System.out.println(i);

		String s=null;
		int i1 = s.hashCode();
		System.out.println(i1);
	}
}
