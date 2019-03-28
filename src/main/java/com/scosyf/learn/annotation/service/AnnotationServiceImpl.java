package com.scosyf.learn.annotation.service;

import org.springframework.stereotype.Service;

import com.scosyf.learn.annotation.common.ApiAnnotation;

@Service("annotationService")
public class AnnotationServiceImpl implements AnnotationService {

	@ApiAnnotation(notes = "hello")
	@Override
	public void anno() {
		System.out.println("hi");
	}
}
