package com.weina.imhistory.common;

import java.util.Random;


public class RandomUtils {

	/**
	 * 产生0～max的随机整数 包括0 不包括max
	 * 
	 * @param max 随机数的上限
	 * @return int
	 */
	public static int getRandom(int max) {
		return new Random().nextInt(max);
	}

	/**
	 * 产生 min～max的随机整数 包括 min 但不包括 max
	 * 
	 * @param min 最小数
	 * @param max 最大数
	 * @return int
	 */
	public static int getRandom(int min, int max) {
		int r = getRandom(max - min);
		return r + min;
	}

	/**
	 * 产生0～max的随机整数 包括0 不包括max
	 * 
	 * @param max 随机数的上限
	 * @return long
	 */
	public static long getRandomLong(long max) {
		return (long) (Math.random() * max);// + minId;
	}

	/**
	 * 产生 min～max的随机整数 包括 min 但不包括 max
	 * 
	 * @param min 最小值
	 * @param max 最大值
	 * @return  long
	 */
	public static long getRandomLong(long min, long max) {
		long r = getRandomLong(max - min);
		return r + min;
	}

	/**
	 * 生产长度为length的随机码纯数字
	 * 
	 * @param length 长度
	 */
	public static String createCode(int length) {
		String code = "";
		for (int i = 0; i < length; i++) {
			int s = (int) (Math.random() * 10);
			code += s + "";
		}
		return code;
	}

	// 生成随机数字和字母,
	public static String getStringRandom(int length) {
		String val = "";
		Random random = new Random();
		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	// 随机生成大写字母
	public static String getUppercaseZ() {
		Random random = new Random();
		final int A = 'A';
		StringBuilder sb = new StringBuilder();
		int number =A+ random.nextInt(26);
		if (number >= A) {
			sb.append((char) number);
		}
		return sb.toString();
	}

}
