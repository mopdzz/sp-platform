package com.sp.platform.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ReduceAlgorithm {
    private static Logger logger = LoggerFactory.getLogger(ReduceAlgorithm.class);

	public static Map<String, List<Integer>> reduceCache = new ConcurrentHashMap<String, List<Integer>>();

	// private final int RANGE = 100; // 总范围
	// private int FIRSTRANGE = 90; // 第一次范围

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// int j = 0;
		// String cpid = "2";
		// int rate = 5;
		//
		// for (int i = 0; i < 1000; i++) {
		// if (ReduceAlgorithm.isReduce(cpid, rate, i)) {
		// System.out.println(i + "被扣量");
		// j++;
		// }
		// }
		// System.out.println("总共扣量" + j + "个");
		//
		// getReduceNum(0, 100, 15);

		String sfId = "97";
		String province = "山东";
		List<String> list = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();

		for (int i = 1; i <= 600; i++) {
			if (i < 400) {
				if (ReduceAlgorithm.isReduce(sfId + "-" + province, 20, i)) {
					list.add(i + "省扣量");
				}
			} else if (ReduceAlgorithm.isReduce(sfId, 10, i)) {
				list2.add(i + "默认扣量");
			}
		}

		for (String string : list) {
			System.out.println(string);
		}
		System.out.println("--------------------");
		for (String string : list2) {
			System.out.println(string);
		}

	}
	/**
	 * 
	 * @param range
	 *            范围
	 * @param reduceNum
	 *            产生不重复的随机数个数
	 * @return 随机数list
	 */

	public static List<Integer> getReduceNum(int range, int reduceNum) {
		if (reduceNum > range)
			reduceNum = range;
		int randnum = 0;
		Random rand = new Random();
		List<Integer> ls = new ArrayList<Integer>();
		boolean[] bool = new boolean[range];
		StringBuilder randString = new StringBuilder();
		for (int i = 0; i < reduceNum; i++) {
			do {
				// 如果产生的数相同继续循环
				randnum = rand.nextInt(range);

			} while (bool[randnum]);
			bool[randnum] = true;
			ls.add(randnum);
			randString.append(randnum).append(" ");
		}
		logger.info(">>>总共产生随机数" + ls.size() + "个 -->>> "
				+ randString.toString());
		return ls;
	}

	/**
	 * 
	 * @param startNo
	 *            起始数
	 * @param endNo
	 *            终点数(不包含)
	 * @param reduceNum
	 *            产生不重复的随机数个数
	 * @return 随机数list
	 */

	public static List<Integer> getReduceNum(int startNo, int endNo,
			int reduceNum) {
		int range = (endNo - startNo);
		if (reduceNum > range)
			reduceNum = range;
		int randnum = 0;
		Random rand = new Random();
		List<Integer> ls = new ArrayList<Integer>();
		boolean[] bool = new boolean[range];
		StringBuilder randString = new StringBuilder();
		for (int i = 0; i < reduceNum; i++) {
			do {
				// 如果产生的数相同继续循环
				randnum = rand.nextInt(range);

			} while (bool[randnum]);
			bool[randnum] = true;
			ls.add(randnum + startNo);
			randString.append(randnum + startNo).append(" ");
		}
		logger.info(">>>总共产生随机数" + ls.size() + "个 -->>> "
				+ randString.toString());
		return ls;
	}

	public static boolean isReduce(String unid, int range, int reduceNum,
			int number) {
		int imod = number % range;
		List<Integer> ls = reduceCache.get(unid);
		if ((ls == null || ls.size() <= 0) || imod == 0) {
			ls = getReduceNum(range, reduceNum);
			reduceCache.put(unid, ls);
		}
		if (ls.contains(imod)) {
			return true;
		}
		return false;
	}

	public static boolean isReduce(String unid, int rate, int number) {
		int range = 100;
		/* int reduceNum = range / rate; */
		int reduceNum = rate; // 新版
		int imod = number % range;
		List<Integer> ls = reduceCache.get(unid);
		if ((ls == null || ls.size() <= 0)) {
			ls = getReduceNum(10, 100, reduceNum);// 前10条不扣量
			reduceCache.put(unid, ls);
		} else if (imod == 0) {
			ls = getReduceNum(range, reduceNum);
			reduceCache.put(unid, ls);
		}
		if (ls.contains(imod)) {
			return true;
		}
		return false;
	}
}
