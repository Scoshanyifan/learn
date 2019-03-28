package com.scosyf.learn.string;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 字符串连接器：能够按指定分割符分隔，并且有前后缀
 *
 * StringBuilder
 * StringJoiner
 * Collectors.join
 *
 * @author kunbu
 * @time 2019/2/18 14:00
 **/
public class StringBuilderTest {

	private static final List<String> AREAS = Lists.newArrayList("广州", "上海", "杭州", "宁波");

	private static final String DELIMIETER  = ",";
	private static final String PREFIX      = "{";
	private static final String SUFFIX	  	= "}";

	public static String testStringBuilder(List<String> target, String delimiter, String prefix, String suffix) {
		if (!CollectionUtils.isEmpty(target)) {
			StringBuilder builder = new StringBuilder();
			builder.append(prefix);
			AREAS.stream().forEach(x -> {
				builder.append(x).append(delimiter);
			});
			//去除最后一个逗号
			builder.deleteCharAt(builder.length() - 1).append(suffix);

			return builder.toString();
		}
		return null;
	}

	public static String testStringJoiner(List<String> target, String delimiter, String prefix, String suffix) {
		if (!CollectionUtils.isEmpty(target)) {
			StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
			AREAS.stream().forEach(x -> {
				joiner.add(x);
			});

			return joiner.toString();
		}
		return null;
	}

	public static String testCollectorsJoining(List<String> target, String delimiter, String prefix, String suffix) {
		if (!CollectionUtils.isEmpty(target)) {
			return AREAS.stream()
					.collect(Collectors.joining(delimiter, prefix, suffix));
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(testStringBuilder(AREAS, DELIMIETER, PREFIX, SUFFIX));
		System.out.println(testStringJoiner(AREAS, DELIMIETER, PREFIX, SUFFIX));
		System.out.println(testCollectorsJoining(AREAS, DELIMIETER, PREFIX, SUFFIX));
	}
	
}
