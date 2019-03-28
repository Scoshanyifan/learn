package com.scosyf.learn.other;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * 如何再大数据中判断元素是否存在的问题
 * @author syf
 * 2018年12月10日
 */
public class BloomFilterTest {
	
	public static final int LOW_SIZE 		= 100_000;
	public static final int HIGH_SIZE 		= 10_000_000;
	
	public static final int TEST_NUMBER_1 	= 2333;
	public static final int TEST_NUMBER_2 	= 9_999_999;
	public static final int TEST_NUMBER_3 	= Integer.MAX_VALUE;

	/**
	 * 1.使用HashSet来实现简单的查找
	 */
	public static void testHashSet() {
		Set<Integer> nums = Sets.newHashSetWithExpectedSize(HIGH_SIZE);
		for (int i = 0; i < HIGH_SIZE; i++) {
			nums.add(i);
		}
		long t0 = System.nanoTime();
		boolean ifExist = nums.contains(TEST_NUMBER_1);
		System.out.println(TEST_NUMBER_1 + " exists: " + ifExist + ", cost: " + (System.nanoTime() - t0) + " ns");
	}
	
	/**
	 * 2.自定义布隆过滤器
	 * 
	 * 提高数组大小可以降低误报率，但是会提升性能消耗，打印GC可以看出full GC 达到11次
	 */
	public static void testMyBloomFilter() {
		MyBloomFilter bloomFilter = new MyBloomFilter(HIGH_SIZE);
		for (int i = 0; i < HIGH_SIZE; i++) {
			bloomFilter.addElem(Integer.toString(i));
		}
		long t0 = System.nanoTime();
		boolean ifExist1 = bloomFilter.check(Integer.toString(TEST_NUMBER_1));
		boolean ifExist2 = bloomFilter.check(Integer.toString(TEST_NUMBER_2));
		//Integer.MAX_VALUE返回true属于误报
		boolean ifExist3 = bloomFilter.check(Integer.toString(TEST_NUMBER_3));
		System.out.println(ifExist1 + "," + ifExist2 + "," +ifExist3 + ", cost: " + (System.nanoTime() - t0) + " ns");
	}
	
	/**
	 * 3.使用Guava的实现版本
	 */
	public static void testGuavaBloomFilter() {
		BloomFilter<Integer> bloomFilter = BloomFilter
				.create(Funnels.integerFunnel(), HIGH_SIZE, 0.01);
		for (int i = 0; i < HIGH_SIZE; i++) {
			bloomFilter.put(i);
		}
		long t0 = System.nanoTime();
		boolean ifExist1 = bloomFilter.mightContain(TEST_NUMBER_1);
		boolean ifExist2 = bloomFilter.mightContain(TEST_NUMBER_2);
		boolean ifExist3 = bloomFilter.mightContain(TEST_NUMBER_3);
		System.out.println(ifExist1 + "," + ifExist2 + "," +ifExist3 + ", cost: " + (System.nanoTime() - t0) + " ns");
	}
	
	public static void main(String[] args) {
//		testHashSet();
//		testMyBloomFilter();
		testGuavaBloomFilter();
	}
}

class MyBloomFilter {
	private int arrSize;
	private int[] arr;
	
	public MyBloomFilter(int arrSize) {
		this.arrSize = arrSize;
		arr = new int[arrSize];
	}
	
	public void addElem(String key) {
		int hash1 = hashcode_1(key);
		int hash2 = hashcode_2(key);
		arr[hash1 % arrSize] = 1;
		arr[hash2 % arrSize] = 1;
	}
	
	public boolean check(String key) {
		int hash1 = hashcode_1(key);
		int hash2 = hashcode_2(key);
		if (arr[hash1 % arrSize] == 0) {
			return false;
		}
		if (arr[hash2 % arrSize] == 0) {
			return false;
		}
		return true;
	}
	
	private int hashcode_1(String key) {
		int hash = 0;
		for (int i = 0; i < key.length(); i++) {
			hash = 33 * hash + key.charAt(i);
		}
		return Math.abs(hash);
	}
	
    private int hashcode_2(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash);
    }
}