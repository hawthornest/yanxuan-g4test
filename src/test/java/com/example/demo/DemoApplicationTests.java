package com.example.demo;


import com.lynhaw.g4test.G4testApplication;
import com.lynhaw.g4test.SpringBootStartApplication;
import com.lynhaw.g4test.mybatis.SqlInfoService.SysInfoServiceImpl;
import com.lynhaw.g4test.service.PublicMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = G4testApplication.class)
public class DemoApplicationTests {
	@Autowired
	private WebApplicationContext webApp;

	SysInfoServiceImpl sysInfoServiceImpl = new SysInfoServiceImpl();

	@Autowired
	PublicMethod publicMethod;

	private MockMvc mockMvc;
	@Before
	public void init()
	{
		System.out.println("开始测试-----------------------");
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApp).build();
	}

	@After
	public void after()
	{
		System.out.println("结束测试------------------------");
	}


	@Test
	public void testTime()
	{
		System.out.println(sysInfoServiceImpl.updateCallBackTimeByTaskId("0bee6c96-ddd4-11e8-968a-719fc95538ae"));
		Assert.assertEquals(2,sysInfoServiceImpl.updateCallBackTimeByTaskId("0bee6c96-ddd4-11e8-968a-719fc95538ae"));
	}

	@Test
	public void testGetRandomString()
	{
		System.out.println(publicMethod.getRandomString(25));
		Assert.assertEquals("12315465",publicMethod.getRandomString(25));
	}

	@Test
	public void testTaskRun() throws Exception {
		MvcResult result = mockMvc.perform(get("/yanxuan/taskrun?testId=1024&environmentId=1i149811&sysId=3&callBackUrl=http%3a%2f%2\f" +
				"g4test.test.you.163.com%2fyanxuan%2fcallback&sysBranch=dev&iSNeed=need&swaggInfo=http://smartipc.you.163.com/v2/api-docs")).andReturn();
		System.out.println("返回包为:" + result.getResponse().getContentAsString());
	}
}
