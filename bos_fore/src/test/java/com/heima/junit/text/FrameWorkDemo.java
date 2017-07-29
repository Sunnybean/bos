package com.heima.junit.text;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FrameWorkDemo {

	@Test
	public void testss() throws IOException, TemplateException {
		// 配置对象，配置模板
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
		configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/template"));
		
		// 获取模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//动态数据对象
		Map<String,String> map = new  HashMap<>();
		map.put("title","xinxi");
		map.put("msg","哈哈");
		
		template.process(map, new PrintWriter(System.out));
		
		
	}
}
