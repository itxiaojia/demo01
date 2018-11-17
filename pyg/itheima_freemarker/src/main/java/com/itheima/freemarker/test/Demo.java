package com.itheima.freemarker.test;

import jdk.nashorn.internal.ir.Flags;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/12 23:48
 */
public class Demo {


	public static void main(String[] args) {
		new Demo().print(args==null||new Demo(){{Demo.main(null);}}.equals(null));
	}

	public void print(boolean flag){
		if (flag){
			System.out.print("a");
		}else{
			System.out.print("b");
		}
	}
}
