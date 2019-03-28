package com.scosyf.learn.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.scosyf.learn.annotation.service.AnnotationService;
import com.scosyf.learn.annotation.service.AnnotationServiceImpl;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class TestDB {
	
	@Test
	public void testAop() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
		AnnotationService service = (AnnotationService) ac.getBean("annotationService");
		service.anno();
	}

}
