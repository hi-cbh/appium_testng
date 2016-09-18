package com.study.code;



import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.*;



public class ChinaMoblie_testNg {

	AndroidDriver<WebElement> driver;
	private String packageName = "com.chinamobile.contacts.im";
	private String appActivity = ".Main";
	
	private String phone = "13427665104";
	private String email = "13427665104@163.com";
	private String passname = "321832750"; //互联网账号
	private String loginPwd = "yscs12345";
	private String sendPhone = "13427632604";
	private static String imagePath ="";

	
	@BeforeSuite(alwaysRun=true)
	@Parameters({"port","udid"})
	public void setUp(String port,String udid) throws Exception {

		System.out.println("current use port: "+port+", devices udid: "+udid);
		//String udid = "0bd08bcc";
		// 设置自动化相关参数
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");

		capabilities.setCapability("udid", udid);

		capabilities.setCapability("deviceName", "");

		// 设置安卓系统版本
		capabilities.setCapability("platformVersion", "4.3");

		// 设置app的主包名和主类名
		capabilities.setCapability("appPackage", packageName);
		capabilities.setCapability("appActivity", appActivity);

		// 支持中文输入，会自动安装Unicode 输入法。默认值为 false
		capabilities.setCapability("unicodeKeyboard", "True");

		// 在设定了 unicodeKeyboard 关键字的 Unicode 测试结束后，重置输入法到原有状态
		capabilities.setCapability("resetKeyboard", "True");

		// 初始化
		URL url = new URL("http://127.0.0.1:"+port+"/wd/hub");
		driver = new AndroidDriver<WebElement>(url, capabilities);
		System.out.println("setup running");
		imagePath =  "\\test-output\\pic\\" + port+"\\";
		System.out.println("errorImage path: " + imagePath);
	}

	/**
	 * case1: 使用已注册的号码，在注册页面进行注册。
	 */
	@Test(groups = { "webview" })
	public void testCase_webview_001() {
		// 点击拨号
		clickById("tab_call");

		// 点击云服务
		clickById("tab_cloud");

		// 点击更多
		clickById("iab_ib_more");

		// 点击登录
		clickById("setting_item_login");

		// 点击互联网登录
		clickById("btn_login_dynamic");

		// 点击注册
		clickById("setting_new_login_btn_register");

		// 清空并填写手机号码
		intoContentEditTextById("setting_register_phone_et", loginPwd);

		// 点击短信验证码
		clickById("setting_register_get_ver_btn");

		sleepTime(5000);
		
		reportLog("RegisteredPhone","使用已注册的号码，在注册页面进行注册");
	}

	/**
	 * 登录验证：手机号码登录
	 * 已注册的手机号，正确密码登录
	 */
	@Test(groups = { "login" })
	public void testCase_login_001() {
		Login(phone, loginPwd);
		Logout(phone);
		reportLog("testCase_login_001","已注册的手机号，正确密码登录");
	}

	/**
	 * 登录验证：使用手机号码、互联网通行证、邮箱地址登录
	 * 手机号码：13427665104
	 * 互联网   ： 321832750
	 * 邮箱地址：13427665104@163.com
	 * 不同类型账号登录
	 */
	@Test(groups = { "login" })
	public void testCase_login_002(){
		//手机号码登录
		Login(phone, loginPwd);
		Logout(phone);
		
		//互联网通行证登录
		Login(passname, loginPwd);
		Logout(passname);
		
		//邮箱地址登录
		Login(email, loginPwd);
		Logout(phone);	
		
		reportLog("testCase_login_002","使用手机号码、互联网通行证、邮箱地址登录");
	}
	
	
	/**
	 * 异常登录测试使用不同的账号和密码，不停进行错误等
	 * 验证锁定IP登录（同一网络IP，使用不同账号登录50次以上，失败了超过50%，在该IP下5分钟内无法登录）
	 */
	@Test(groups = { "login_longtime" })
	public void testCase_login_003(){

		// 点击和通讯录
		clickById("iab_title");

		// 点击设置
		clickById("setting_layout");

		String username = "1381013801";
		String password = "138001";
		
		for(int i = 0; i < 62; i++){
			System.out.println("Login IP error " + (i + 1) + " times");
			// System.out.println("isLoginState() " + isLoginState());
			// 判断是否为登录状态，若未登录状态，进行下一步；否则返回
			if (isLoginState("")) {
				// 点击退出
				clickById("setting_item_login_logout_text");

				// 点击确认
				clickById("dialog_btn_positive");
			}
			
			//
			sleepTime(1000);

			// 点击登录
			clickById("setting_item_login");

			// 点击互联网登录
			clickById("btn_login_dynamic");

			// 输入用户名
			intoContentEditTextById("setting_new_login_mobile_et_num", username+i);

			// 输入密码
			intoContentEditTextById("setting_new_login_mobile_et_password",
					password+i);

			// 点击登录
			clickById("setting_new_login_mobile_btn_login");

			// 等待登录时间(根据网络状态)
			sleepTime(3000);
			
		
		}
		
		//用正确密码登录进行验证，验证结果应该登录失败
		
		// 输入用户名
		intoContentEditTextById("setting_new_login_mobile_et_num", this.phone);

		// 输入密码
		intoContentEditTextById("setting_new_login_mobile_et_password",
				this.loginPwd);

		// 点击登录
		clickById("setting_new_login_mobile_btn_login");

		// 等待登录时间(根据网络状态)
		sleepTime(10000);
		
		//验证未没登录状态
		//Assert.assertTrue(!this.isLoginState(this.phone));
		Myassert("使用正确的密码登录成功，但需求应为登录失败", !this.isLoginState(this.phone), "testCase_login_003");
		
		//返回主界面
		back("tab_call");
		
		reportLog("testCase_login_003","验证锁定IP登录（同一网络IP，使用不同账号登录50次以上，失败了超过50%，在该IP下5分钟内无法登录）");
	}
	
	/**
	 * 通话记录-陌生人-新建联系人
	 * 拨号盘中，创建联系人,输入号码，添加为联系人，并保存
	 */
	@Test(groups = { "call" })
	public void testCase_call_001(){
		//清理
		deleteAllContacts();
		deleteAllCall();
		back("call");

		//点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		//点击拨号
		clickById("tab_call");

		//点击输入框
		clickById("digits");

		//点击键盘数字
		//tab_dialer 拨号
		//action_sms 发短信
		touchCallNumber("13822138001");
		
		//验证拨号页没有改号码
		//Assert.assertTrue(driver.findElementByName("添加为联系人").isDisplayed());
		//环境准备
		if(!getElementsByClassAndIndex("android.widget.TextView", 0).equals("saveAsContact")){

			sleepTime(2000);
			//点击存在的联系人
			clickById("call_detail");
			//点击菜单，选择删除
			clickMenuAndSelect(2);
			
			//点击确认
			clickByName("删除");
		}
		
		
		//点击添加为联系人
		clickByName("添加为联系人");
		
		//添加新建联系人
		clickByName("新建联系人");
		
		//判断当前页面为新建联系人
		Assert.assertTrue(driver.findElementByName("新建联系人").isDisplayed());
		
		//获取界面所有的EditView元素
		List<WebElement> editText = driver
				.findElementsByClassName("android.widget.EditText");

		//第一个元素收入
		editText.get(0).sendKeys("saveAsContact");
		
		//点击屏幕，功能缺陷
		touchWindows();
		
		//点击保存
		clickById("iab_ib_action");
		
		sleepTime(2000);
		
		//判读联系是否被创建
		//Assert.assertTrue(this.searchContact("saveAsContact", 0));
		Myassert("联系人没有被创建：saveAsContact", searchContact("saveAsContact", 0), "testCase_call_001");
		sleepTime(2000);
		
		//清理联系人
		//this.deleteContactsByPhone("13822138001");
		deleteAllContacts();
		deleteAllCall();
		reportLog("testCase_call_001","拨号盘中，创建联系人,输入号码，添加为联系人，并保存");
	}
	
	/**
	 * 通话记录-陌生人-添加到已有联系人
	 * 添加为已有联系人
	 */
	@Test(groups = { "call" })
	public void testCase_call_002(){
		//清理
		deleteAllContacts();
		deleteAllCall();
		back("call");

		//添加测试数据
		this.createContacts("saveContacts", "13824452646");
		
		//点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		//点击拨号
		clickById("tab_call");

		//点击输入框
		clickById("digits");

		//点击键盘数字
		touchCallNumber("84850922");
			
		//点击添加为联系人
		clickByName("添加为联系人");
		
		//添加新建联系人
		clickByName("添加到已有联系人");
		
		//判断当前页面为新建联系人
		Assert.assertTrue("没有进入新建联系人", driver.findElementByName("新建联系人").isDisplayed());
		
		intoContentEditTextById("contact_search_bar", "13824452646");

		searchWebElement("saveContacts").click();
		
		Assert.assertTrue("没有进入编辑联系人", driver.findElementByName("编辑联系人").isDisplayed());
		
		//点击保存
		clickById("iab_ib_action");
				
		sleepTime(2000);
		
		//deleteContactsByName("saveContacts");
		//清理
		deleteAllContacts();
		deleteAllCall();
		reportLog("testCase_call_002","通话记录-陌生人-添加到已有联系人");
	}
	
	/**
	 * 拨号盘 -搜索本地联系人（按拼音或号码搜索）
	 * 本地联系人检测
	 */
	@Test(groups = { "call" })
	public void testCase_call_003(){
		//boolean bl;
		//清理
		deleteAllContacts();
		deleteAllCall();
		back("call");

		//创建本地联系人
		createContacts("通讯录", "13800138000");
		
		//点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		//点击拨号
		clickById("tab_call");

		//点击输入框
		clickById("digits");

		//点击键盘数字
		touchCallNumber("138001");

		//bl = this.searchContact("通讯录", 0);
		//Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0), "testCase_call_003");
		
		//清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");

		//点击键盘数字
		touchCallNumber("001380");

//		bl = this.searchContact("通讯录", 0);
//		Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0), "testCase_call_003");
		
		//清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");
		
		//点击键盘数字
		touchCallNumber("138000");
	
//		bl = this.searchContact("通讯录", 0);
//		Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0), "testCase_call_003");
		
		//清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");
		
		
		//点击键盘数字,tongx转化为键盘数字为86649
		touchCallNumber("86649");
		
//		bl = this.searchContact("通讯录", 0);
//		Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0), "testCase_call_003");
		
		//清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");
		
		
		//点击键盘数字,txl转化为键盘数字为895
		touchCallNumber("895");
		
//		bl = this.searchContact("通讯录", 0);
//		Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0), "testCase_call_003");
		
		//清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");
		
		
		//点击键盘数字,xunlu转化为键盘数字为98658
		touchCallNumber("98658");
		
//		bl = this.searchContact("通讯录", 0);
//		Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0), "testCase_call_003");
		
		//清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");
		

		sleepTime(3000);
		
		//删除联系人
		//deleteContactsByPhone("13800138000");
		deleteAllContacts();
		
		reportLog("testCase_call_003","拨号盘 -搜索本地联系人（按拼音或号码搜索）");
	}


	
	/**
	 * 将拨号记录中一号码添加到黑名单
	 * 通话记录-陌生人-加入黑名单（详细页，选择加入黑名单）
	 */
	@Test(groups = { "call" })
	public void testCase_call_004(){
		//清理
		deleteAllContacts();
		
		back("call");
		
		//进入管理黑名单
		OpenTabMenu("防打扰" , "黑名单");
		
		//搜索当前页面是否含有该号码记录
		if(searchContact("13813881499", 0)){
			//点击清空
			clickById("iab_ib_action");

			//点击清空
			clickByName("清空");
		}
		
		//返回主界面
		back("tab_call");
		
		//清空所有通话记录
		deleteAllCall();
		
		//添加数据
		callNumber("13813881499");
		
		//点击拨号详细记录
		clickById("call_detail");
		
		//点击加入黑名单
		clickMenuAndSelect(4);
		
		// 返回主界面
		driver.sendKeyEvent(AndroidKeyCode.BACK);
		sleepTime(1000);
		
		//进入管理黑名单
		OpenTabMenu("防打扰" , "黑名单");
		
		//检测当前界面为防打扰页
		Assert.assertTrue("没有进入黑名单管理页", driver.findElementByName("管理黑名单").isDisplayed());
		
		boolean bl = searchContact("13813881499", 0);
		
		//检测当前是否存储骚扰电话
		//Assert.assertTrue(bl);
		Myassert("黑名单中，没有找到该联系人：13813881499", bl, "testCase_call_004");
		
		sleepTime(1000);
		
		//清空数据
		if(bl){
			//点击清空
			clickById("iab_ib_action");
				
			//点击清空
			clickById("dialog_btn_positive");
		}
		//返回主界面
		back("tab_call");
		
		//清空所有通话记录
		deleteAllCall();
		
		reportLog("testCase_call_004","通话记录-陌生人-加入黑名单（详细页，选择加入黑名单）");
	}
	
	/**
	 * 1标记为广告推销且勾选加入黑名单
	 * 通话记录-陌生人-标记
	 */
	@Test(groups = { "call" })
	public void testCase_call_005(){
		back("call");
		//清空所有通话记录
		deleteAllCall();
		
		//添加数据
		callNumber("13813881423");
		
		//点击拨号详细记录
		clickById("call_detail");
		
		//点击标记
		clickMenuAndSelect(3);
		
		//广告推销
		clickByName("广告推销");
		
		//同时加入黑名单
		clickByName("同时加入黑名单");
		
		//点击确定
		clickByName("确定");
		
		sleepTime(3000);
		
		//验证号码是否标记
		//Assert.assertTrue(getTextViewNameById("call_stranger_detail_company").contains("广告推销"));
		boolean b0 = getTextViewNameById("call_stranger_detail_company").contains("广告推销");
		Myassert("没有标记", b0, "testCase_call_005");
		
		//返回主界面
		back("tab_call");
		
		//进入管理黑名单
		OpenTabMenu("防打扰" , "黑名单");
		
		//检测当前界面为防打扰页
		Assert.assertTrue("没有进入黑名单管理页", driver.findElementByName("管理黑名单").isDisplayed());
		
		boolean bl = searchContact("13813881423", 0);
		
		//检测当前是否存储骚扰电话
		//Assert.assertTrue(bl);
		Myassert("黑名单中，没有找到该联系人：13813881423", bl, "testCase_call_005");
		
		sleepTime(1000);
		
		//清空数据
		if(bl){
			//点击清空
			clickById("iab_ib_action");
				
			//点击清空
			clickById("dialog_btn_positive");
		}
		//返回主界面
		back("tab_call");

		//点击拨号详细记录
		clickById("call_detail");
		
		//点击取消标记
		clickMenuAndSelect(3);
		
		//返回主界面
		back("tab_call");
	
		//清空记录
		deleteAllCall();
		
		reportLog("testCase_call_005","通话记录-陌生人-标记");
	}
	
	/**
	 * 长按号码，将其添加到黑名单
	 * 拨号 - 更多操作- 加入黑名单（长按记录，选择加入黑名单）
	 */
	@Test(groups = { "call" })
	public void testCase_call_006(){
		back("call");
		//清空黑名单管理内容
		deleteBlacklist();
		
		//清空所有通话记录
		deleteAllCall();
		
		//添加数据
		callNumber("13813881423");
		
		//长按号码
		this.clickLongByElementUseJs(this.searchWebElement("13813881423"));
		
		//点击加入黑名单按钮
		clickById("mca_msg_txt");
		
		//长按号码
		this.clickLongByElementUseJs(this.searchWebElement("13813881423"));
				
		sleepTime(1000);
		
		//验证
		//Assert.assertTrue(getTextViewNameById("mca_msg_txt").contains("取消黑名单"));
		Myassert("取消黑名单失败", getTextViewNameById("mca_msg_txt").contains("取消黑名单"), "testCase_call_006");
		
		//点击加入取消黑名单
		clickById("mca_msg_txt");
		
		//清空所有通话记录
		deleteAllCall();
		
		reportLog("testCase_call_006","拨号 - 更多操作- 加入黑名单（长按记录，选择加入黑名单）");
	}
	
	/**
	 * 添加IP拨号，选择17951
	 * 拨号 - 更多操作 - IP拨号-选择已有的前缀
	 */
	@Test(groups = { "call" })
	public void testCase_call_007(){
		back("call");
		//清空所有通话记录
		deleteAllCall();
		
		//添加数据
		callNumber("13813881420");
		
		//长按号码
		this.clickLongByElementUseJs(this.searchWebElement("13813881420"));
		
		//点击IP拨号
		clickById("mca_call_txt");
		
		//判断是否存在多个自定义前缀
		if(isExistenceById("del_voip_phone"))
		{
			//点击清除
			clickById("del_voip_phone");
			
			//点击删除
			clickByName("删除");
			
			//长按号码
			this.clickLongByElementUseJs(this.searchWebElement("13813881420"));
			
			//点击IP拨号
			clickById("mca_call_txt");
		}
		
		//在弹窗选择
		menuList("add_text", 1);
		
		sleepTime(3000);
		//点击点击确定
		clickByName("确定");
		
		//Assert.assertTrue(getTextViewNameById("line1").contains("17951"));
		Myassert("没有找到拨号记录中，含有IP号码：17951",getTextViewNameById("line1").contains("17951"), "testCase_call_007");
		
		//清空所有通话记录
		deleteAllCall();
		
		reportLog("testCase_call_007","拨号 - 更多操作 - IP拨号-选择已有的前缀");
	}
	
	/**
	 * 添加IP拨号，添加自定义前缀
	 * 拨号 - 更多操作 - IP拨号-选择手动添加的前缀
	 */
	@Test(groups = { "call" })
	public void testCase_call_008(){
		back("call");
		String tmpNum = "13813771420";
		//清空所有通话记录
		deleteAllCall();
		
		//添加数据
		callNumber(tmpNum);
		
		//长按号码
		this.clickLongByElementUseJs(this.searchWebElement(tmpNum));
		
		//点击IP拨号
		clickById("mca_call_txt");
		
		//判断是否存在多个自定义前缀
		if(isExistenceById("del_voip_phone"))
		{
			//点击清除
			clickById("del_voip_phone");
			
			//点击删除
			clickByName("删除");
			
			//长按号码
			this.clickLongByElementUseJs(this.searchWebElement(tmpNum));
			
			//点击IP拨号
			clickById("mca_call_txt");
			
		}
		
		//点击添加前缀号码
		clickById("add_voip_phone");
		
		//输入内容
		this.intoContentEditTextById("content", "138438");
		
		//点击确定
		clickByName("确定");
		
		sleepTime(3000);
		
		//点击点击确定
		clickByName("确定");
		
		//Assert.assertTrue(getTextViewNameById("line1").contains("138438"));
		Myassert("没有找到拨号记录中，含有IP号码：138438",getTextViewNameById("line1").contains("138438"), "testCase_call_008");
		
		reportLog("testCase_call_008","拨号 - 更多操作 - IP拨号-选择手动添加的前缀");
	}
	
	/**
	 * 拨打本地联系人号码
	 * 拨号- 拨打本地联系人
	 */
	@Test(groups = { "call" })
	public void testCase_call_009(){
		deleteAllContacts();
		back("call");
		
		//准备数据，创建联系人
		this.createContacts("testCase_call_009", "13504168016");

		//点击拨号
		displaykeyboardCall();

		//点击输入框
		clickById("digits");
		
		//点击键盘数字
		touchCallNumber("13504");
		
		//点击搜索记录，
		clickById("listview_rl");
		
		sleepTime(2000);
		//点击确定
		clickByName("确定");
		
		sleepTime(2000);
		
		//Assert.assertTrue(this.searchContact("testCase016", 0));
		Myassert("没有找到测试数据：testCase_call_009", searchContact("testCase_call_009", 0), "testCase_call_009");
		
		//清空拨号记录
		this.deleteAllCall();
		
		//删除联系人
		//this.deleteContactsByName("testCase016");
		deleteAllContacts();
		reportLog("testCase_call_009","拨号- 拨打本地联系人");
	}
	
	/**
	 * 本地联系人通话记录，加入白名单
	 * 拨号 - 加入白名单
	 */
	@Test(groups = { "call" })
	public void testCase_call_010(){
		
		deleteAllContacts();
		back("call");
		
		this.createContacts("testCase_call_010", "13533168167");

		//点击显示拨号盘
		displaykeyboardCall();

		//点击输入框
		clickById("digits");
		
		//点击键盘数字
		touchCallNumber("13533");
		
		//点击搜索记录，
		clickById("listview_rl");
		
		sleepTime(2000);
		//点击确定
		clickByName("确定");
		
		sleepTime(2000);
		
		//点击拨号详细记录
		clickById("call_detail");
		
		//点击加入白名单
		clickMenuAndSelect(4);
		
		//返回主界面
		back("tab_call");
		
		//进入管理白名单
		OpenTabMenu("防打扰" , "白名单");
		
		//检测当前界面为防打扰页
		Assert.assertTrue("没有进入白名单管理页",driver.findElementByName("管理白名单").isDisplayed());
		
		boolean bl = searchContact("13533168167", 0);
		
		//检测当前是否存储骚扰电话
		//Assert.assertTrue(bl);
		this.Myassert("没有找到联系人：13533168167", bl, "testCase_call_010");
		
		sleepTime(1000);
		
		//清空数据
		if(bl){
			//点击清空
			clickById("iab_ib_action");
				
			//点击清空
			clickById("dialog_btn_positive");
		}
		//返回主界面
		back("tab_call");
		
		//this.deleteContactsByName("testCase017");
		deleteAllContacts();
		deleteAllCall();
		
		reportLog("testCase_call_010","拨号 - 加入白名单");
	}
	
	/**
	 * 拨号修改编辑
	 * 通话记录-本地联系人-编辑
	 */
	@Test(groups = { "call" })
	public void testCase_call_011(){
		deleteAllContacts();
		back("call");
		
		this.createContacts("testCase_call_011", "13511168169");

		//点击拨号
		displaykeyboardCall();

		//点击输入框
		clickById("digits");
		
		//点击键盘数字
		touchCallNumber("135111");
		
		//点击搜索记录，
		clickById("listview_rl");
		
		sleepTime(2000);
		//点击确定
		clickByName("确定");
		
		sleepTime(2000);
		
		//点击拨号详细记录
		clickById("call_detail");
		
		//点击编辑
		clickById("iab_ib_action");
		
		//获取界面所有的EditView元素
		List<WebElement> editText = driver
				.findElementsByClassName("android.widget.EditText");


		//clearText(editText.get(4).getAttribute("text"));
		editText.get(4).sendKeys("13522168199");

		//点击保存
		clickById("iab_ib_action");
		
		sleepTime(3000);
		
		//返回拨号页
		back("tab_call");
		
		displaykeyboardCall();
		
		//点击输入框
		clickById("digits");
		
		//点击键盘数字
		touchCallNumber("13522168199");
		
		//Assert.assertTrue(this.searchContact("testCase018", 0));
		Myassert("没有找到联系人：testCase018", searchContact("testCase_call_011", 0), "testCase_call_011");
		
		clickById("tab_contacts");
		
		//this.deleteContactsByName("testCase018");
		deleteAllContacts();
		//进入拨号
		clickById("tab_call");
		
		//清除记录
		this.deleteAllCall();
		
		reportLog("testCase_call_011","通话记录-本地联系人-编辑");
	}
	
	/**
	 * 拨号页，删除联系人
	 * 通话记录-本地联系人-删除联系人
	 */
	@Test(groups = { "call" })
	public void testCase_call_012(){
		
		deleteAllContacts();
		back("call");
		this.createContacts("testCase_call_012", "13500168169");

		displaykeyboardCall();

		//点击输入框
		clickById("digits");
		
		//点击键盘数字
		touchCallNumber("13500");
		
		//点击搜索记录，
		clickById("listview_rl");
		
		sleepTime(2000);
		//点击确定
		clickByName("确定");
		
		sleepTime(2000);
		
		//点击拨号详细记录
		clickById("call_detail");
		
		//点击删除联系人
		clickMenuAndSelect(2);
		
		//点击删除
		clickByName("删除");
		
		//切换到联系人页
		clickById("tab_contacts");
		
		//搜索当前页面
		//Assert.assertTrue(!this.searchContact("13500168169", 0));
		Myassert("联系人删除失败：13500168169", !this.searchContact("13500168169", 0), "testCase_call_012");
		//进入拨号
		clickById("tab_call");
		
		//清除记录
		this.deleteAllCall();
		
		reportLog("testCase_call_012","通话记录-本地联系人-删除联系人");
	}
	
	/**
	 * 一键拨号-设置快捷拨号-从联系人列表选择
	 */
	@Test(groups = { "call" })
	public void testCase_call_013(){
		back("call");
		String name = "num";
		String phone = "1353316816";
		//创建数据
		createDate(name, phone);

		//已经包含清除
		List<Point> poList = getPointList();

		//切换到拨号页
		clickById("tab_call");
		
		//选择一键拨号
		clickMenuAndSelect(2);
		
		//验证当前页
		Assert.assertTrue("当前页面不在一键拨号设置页",driver.findElementByName("一键拨号设置").isDisplayed());

		int i = 0;
		for(Point point : poList){
			sleepTime(3000);
				i++;
				//不执行第一个元素
				if(i == 1){
					continue;
				}

				//点击屏幕
				touchScreen(point);
				
				//从联系人列表获得
				contextMenuTitleSelect(1);
				
				//在搜索框内输入
				intoContentEditTextById("contact_search_bar", name + i);
				
				//选中联系人
				searchWebElement(name + i).click();
				
				//点击添加
				clickById("selection_ok");
				
				Assert.assertTrue("没有找到该联系人："+(name + i),driver.findElementByName(name + i).isDisplayed());
				
			}
		
		//清除一键拨号设置
		deleteAllOneCall();
		
		back("tab_call");
		
		//清除数据
		deleteAllContacts();
		reportLog("testCase_call_013","一键拨号-设置快捷拨号-从联系人列表选择");
	}
	
	/**
	 * 一键拨号-设置快捷拨号-键盘输入
	 */
	@Test(groups = { "call" })
	public void testCase_call_014(){
		back("call");
		String name = "num";
		String phone = "1353316816";
		//创建数据
		createDate(name, phone);

		//已经包含清除
		List<Point> poList = getPointList();

		//切换到拨号页
		clickById("tab_call");
		
		//选择一键拨号
		clickMenuAndSelect(2);
		
		//验证当前页
		Assert.assertTrue("没有进入一键拨号设置页",driver.findElementByName("一键拨号设置").isDisplayed());

		int i = 0;
		for(Point point : poList){
			sleepTime(3000);
				i++;
				//不执行第一个元素
				if(i == 1){
					continue;
				}

				//点击屏幕
				touchScreen(point);
				
				//从联系人列表获得
				contextMenuTitleSelect(2);
				
				//在搜索框内输入
				intoContentEditTextById("content", phone + i);
				
				//点击添加
				clickById("dialog_btn_positive");
				
				Assert.assertTrue("没有找到该联系人："+(name + i), driver.findElementByName(name + i).isDisplayed());
				
			}
		
		//清除一键拨号设置
		deleteAllOneCall();
		
		back("tab_call");
		
		//清除数据
		deleteAllContacts();
		
		reportLog("testCase_call_014","一键拨号-设置快捷拨号-键盘输入");
	
	}
	
	/**
	 * 一键拨号-拨打快捷拨号
	 */
	@Test(groups = { "call" })
	public void testCase_call_015(){
		back("call");
		String name = "num";
		String phone = "1353316816";
		
		//创建数据
		createDate(name, phone);

		//已经包含清除
		List<Point> poList = getPointList();
		ArrayList<String> strArray = getNumberList();
		
		//切换到拨号页
		clickById("tab_call");
		
		//选择一键拨号
		clickMenuAndSelect(2);
		
		//验证当前页
		//Assert.assertTrue(driver.findElementByName("一键拨号设置").isDisplayed());
		Myassert("没有进入一键拨号设置页", driver.findElementByName("一键拨号设置").isDisplayed(), "testCase_call_015");

		int i = 0;
		for(Point point : poList){
			sleepTime(3000);
				i++;
				//不执行第一个元素
				if(i == 1){
					continue;
				}

				//点击屏幕
				touchScreen(point);
				
				//从联系人列表获得
				contextMenuTitleSelect(2);
				
				//在搜索框内输入
				intoContentEditTextById("content", phone + i);
				
				//点击添加
				clickById("dialog_btn_positive");
				
				//Assert.assertTrue(driver.findElementByName(name + i).isDisplayed());
				
				Myassert("没有找到该联系人："+(name + i),driver.findElementByName(name + i).isDisplayed(), "testCase_call_015");
			}
		back("tab_call");
		
		int j;
		for(j = 0; j < 8; j++){
			
			displaykeyboardCall();
			//长按菜单
			clickLongByIdUseJs(strArray.get(j));
			
			sleepTime(2000);
			
			clickByName("确定");
			
			//Assert.assertTrue(searchContact("num"+(j+2), 0));
			Myassert("没有找到联系人："+"num"+(j+2), searchContact("num"+(j+2), 0), "testCase_call_015");
			deleteAllCall();
			
			
		}

		
		//清除一键拨号设置
		deleteAllOneCall();
		
		back("tab_call");
		
		//清除数据
		deleteAllContacts();
		
		reportLog("testCase_call_015","一键拨号-拨打快捷拨号");
	}
	
	/**
	 * 一键拨号-未设置-手动输入
	 */
	@Test(groups = { "call" })
	public void testCase_call_016(){
		back("call");
		String name = "num";
		String phone = "1353316816";
		
		//创建数据
		createDate(name, phone);
		
		//切换到拨号页
		clickById("tab_call");
		
		//选择一键拨号
		clickMenuAndSelect(2);
		
		//清空所有已设置按钮
		deleteAllOneCall();
		ArrayList<String> strArray = getNumberList();
		
		back("tab_call");
		
		for(int i = 0; i < 8; i++){

			displaykeyboardCall();
			
			//长按数字按钮
			clickLongByIdUseJs(strArray.get(i));
			
			//验证弹窗
			//Assert.assertTrue(driver.findElementByName("温馨提示").isDisplayed());
			this.Myassert("没有弹出提示",driver.findElementByName("温馨提示").isDisplayed(), "testCase_call_016");
			//点击确认
			clickByName("是");

			//手动输入
			contextMenuTitleSelect(2);
			
			//输入框添加号码
			intoContentEditTextById("content", phone + (i+2));
			
			//点击添加
			clickById("dialog_btn_positive");
			
			//Assert.assertTrue(searchContact("num"+(i+2), 0));
			this.Myassert("没有找到测试数据："+"num"+(i+2),searchContact("num"+(i+2), 0), "testCase_call_016");
			
			//返回
			back("tab_call");
		}
		
		//清除数据
		deleteAllContacts();
		reportLog("testCase_call_016","一键拨号-未设置-手动输入");
	}
	
	/**
	 * 一键拨号-未设置-从联系人列表获得
	 */
	@Test(groups = { "call" })
	public void testCase_call_017(){
		back("call");
		String name = "num";
		String phone = "1353316816";
		
		//创建数据
		createDate(name, phone);

		//切换到拨号页
		clickById("tab_call");
		
		//选择一键拨号
		clickMenuAndSelect(2);
		
		//清空所有已设置按钮
		deleteAllOneCall();
		ArrayList<String> strArray = getNumberList();
		
		back("tab_call");
		
		for(int i = 0; i < 8; i++){
			//显示
			displaykeyboardCall();
			
			//长按数字按钮
			clickLongByIdUseJs(strArray.get(i));
			
			//验证弹窗
			Assert.assertTrue("没有弹出提示", driver.findElementByName("温馨提示").isDisplayed());
			
			//点击确认
			clickByName("是");

			//手动输入
			contextMenuTitleSelect(1);
			
			//在搜索框内输入
			intoContentEditTextById("contact_search_bar", name + (i+2));
			
			//选中联系人
			searchWebElement(name + (i+2)).click();
			
			//点击添加
			clickById("selection_ok");
			
			Assert.assertTrue("没有找到测试数据："+(name + (i+2)),driver.findElementByName(name + (i+2)).isDisplayed());
			
			//返回
			back("tab_call");
		}

		//清除数据
		deleteAllContacts();
		reportLog("testCase_call_017","一键拨号-未设置-从联系人列表获得");
	}


	/**
	 * 创建联系人
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_001(){
		deleteAllContacts();
		back("tab_contact");
		//创建联系人
		createContacts("firstContacts", "13542320000");
		createContacts("secondContacts", "13542323330");
		
		//删除联系人
		deleteContactsByPhone("13542320000");
		deleteContactsByPhone("13542323330");
		
		reportLog("testCase_contact_001","创建联系人");
	}
	
	/**
	 * 创建详细的联系人
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_002(){
		deleteAllContacts();
		back("tab_contact");
		
		//创建联系人
		createContacts();
		//清理数据
		deleteContactsByPhone("13800138000");
		
		reportLog("testCase_contact_002","创建详细的联系人");
	}
	/**
	 * 进入联系人详情页
	 * 删除一个联系人
	 * 联系人详细页 - 删除联系人
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_003(){
		deleteAllContacts();
		back("tab_contact");
		
		//准备测试数据
		createContacts("deletecontact", "13843438888");
		
		// 点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");
		
		//判断列表是否存在该联系人
		Assert.assertTrue(searchContact("13843438888" , 1));
		
		//点击进入联系人
		getFirstTextView("13843438888" , 1).click();
		
		//进入菜单，选择删除
		clickMenuAndSelect(2);
		
		//点击确定
		clickById("dialog_btn_positive");
		
		back("tab_contacts");
		Myassert("删除失败", !isExistenceByName("deletecontact"), "testCase_contact_003");
		
		sleepTime(5000);
		
		reportLog("testCase_contact_003","联系人详细页 - 删除联系人");
	}
	
	/**
	 * 长按联系人，选中全选，点击删除
	 * 联系人 - 更多操作 - 删除（长按联系人）
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_004(){
		deleteAllContacts();
		back("tab_contact");
		
		//准备测试数据
		createContacts("deleteLongPhone", "1374242111");
		
		//点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		//输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "1374242111");
		
		//判断列表是否存在该联系人
		//Assert.assertTrue(searchContact("1374242111" , 1));
		Myassert("列表中没找到该联系人:1374242111", searchContact("1374242111" , 1), "testCase_contact_004");
		
		//长按联系人
		//clickLongWebElement(getFirstTextView("10086"));
		clickLongByNameUseJs("1374242111");
		
		//点击全选按钮
		clickById("iab_ib_more");
		
		//点击删除
		clickById("mca_delete_icon");
		
		//点击确定
		clickById("dialog_btn_positive");
		
		back("tab_contacts");
		Myassert("删除失败", !isExistenceByName("deleteLongPhone"), "testCase_contact_004");
		
		deleteAllContacts();
		reportLog("testCase_contact_004","联系人 - 更多操作 - 删除（长按联系人）");
	}
	
	/**
	 * 输入搜索条件，获取联系人信息。
	 * 联系人列表 - 搜索本地联系人
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_005(){
		deleteAllContacts();
		back("tab_contact");
		
		
		//创建联系人，准备测试数据
		createContacts("张小花", "13824451649");
		createContacts("通讯录", "13843845678");
		
		//输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "张小花");
		
		//判断列表是否存在该联系人
		//Assert.assertTrue(searchContact("13824451649" , 1));
		Myassert("列表中没找到该联系人:张小花", searchContact("13824451649" , 1), "testCase_contact_005");
		
		//输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "zxh");
		
		//判断列表是否存在该联系人
		//Assert.assertTrue(searchContact("13824451649" , 1));
		Myassert("列表中没找到该联系人:张小花", searchContact("13824451649" , 1), "testCase_contact_005");
		
		//输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "zhangxiaohua");
		
		//判断列表是否存在该联系人
		//Assert.assertTrue(searchContact("13824451649" , 1));
		Myassert("列表中没找到该联系人:张小花", searchContact("13824451649" , 1), "testCase_contact_005");
		//输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "13824451649");
		
		//判断列表是否存在该联系人
		//Assert.assertTrue(searchContact("13824451649" , 1));
		Myassert("列表中没找到该联系人:张小花", searchContact("13824451649" , 1), "testCase_contact_005");
		//清理数据
		deleteContactsByPhone("13824451649");
		deleteContactsByPhone("13843845678");
		
		reportLog("testCase_contact_005","联系人列表 - 搜索本地联系人");
	}
	
	
	/**
	 * 创建联系人后，新增或修改联系人信息
	 * 联系人详细页 - 编辑
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_006(){
		deleteAllContacts();
		back("contact");
		//创建联系人
		createContacts("testCase_contact_006", "13542320025");

		// 进入新创建的联系详细页
		clickById("tab_contacts");

		// 搜索联系人
		intoContentEditTextById("contact_search_bar", "13542320025");

		// 点击搜索记录，
		searchWebElement("testCase_contact_006").click();

		sleepTime(2000);

		// 点击编辑
		clickById("iab_ib_action");

		// 获取界面所有的EditView元素
		List<WebElement> editText = driver
				.findElementsByClassName("android.widget.EditText");

		// clearText(editText.get(4).getAttribute("text"));
		editText.get(4).sendKeys("13522168025");

		// 点击保存
		clickById("iab_ib_action");

		sleepTime(3000);

		// 返回拨号页
		back("tab_contacts");

		intoContentEditTextById("contact_search_bar", "13522168025");

		//Assert.assertTrue(this.searchContact("testCase_025", 0));
		Myassert("没有找到测试数据：testCase_contact_006", searchContact("testCase_contact_006", 0), "testCase_contact_006");

		clickById("tab_contacts");

		//this.deleteContactsByName("testCase_025");
		deleteAllContacts();
		reportLog("testCase_contact_006","联系人详细页 - 编辑");

	}

	/**
	 * 联系人详细页 - 收藏
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_007(){
		deleteAllContacts();
		back("contact");
		//创建测试数据
		createContacts("testCase_contact_007", "13522168026");
		
		// 进入新创建的联系详细页
		clickById("tab_contacts");

		// 搜索联系人
		intoContentEditTextById("contact_search_bar", "13522168026");

		// 点击搜索记录，
		searchWebElement("testCase_contact_007").click();

		sleepTime(2000);
		
		//点击收藏按钮
		clickById("contact_detail_starred");
		
		//验证收藏标志被选上
		boolean bl = driver.findElementById("contact_detail_starred").getAttribute("checked").equals("true");
		Myassert("联系人没有被标识为收藏", bl, "testCase_contact_007");
		//Assert.assertTrue("",driver.findElementById("contact_detail_starred").getAttribute("checked").equals("true"));
		
		//返回主页
		back("tab_contacts");
		
		//清空记录
		clearTextAndNote();
		
		//搜索列表中，是否存在 收藏联系人选项
		Assert.assertTrue("不存在收藏列表",driver.findElementByName("收藏联系人").isDisplayed());

		
		//删除用户名
		deleteContactsByName("testCase_contact_007");
		//这里不能用全部删除联系人
		//deleteAllContacts();
		reportLog("testCase_contact_007","联系人详细页 - 收藏");
	}
	
	
	/**
	 * 新建联系人（屏幕下拉）
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_008(){
		String username = "testCase_contact_008";
		String phone = "13522168029";
		
		//进入联系人列表
		clickById("tab_contacts");
		
		//下拉列表
		swipeToDown();
		
		//保存联系信息
		saveContact(username, phone);
		
		//休眠3秒
		sleepTime(3000);
		
		//输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", phone);
		
		//判断是否通过
		//Assert.assertTrue(searchContact(phone, 1));
		Myassert("创建联系人失败", searchContact(phone, 1), "testCase_contact_008");
		
		//删除联系人
		//deleteContactsByName(username);
		deleteAllContacts();
		reportLog("testCase_contact_008","新建联系人（屏幕下拉）");
	}
	
	
	/**
	 * 联系人-加入白名单
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_009(){
		String username = "testCase_contact_009";
		String phone = "13522168030";
		
		deleteAllContacts();
		clickById("tab_contacts");
		
		//创建联系人
		createContacts(username, phone);
		
		//清空记录
		clearTextAndNote();
		
		sleepTime(4000);
		
		//长按选择联系人
		this.clickLongByElementUseJs(this.searchWebElement(phone));
		
		
		//点击
		clickByName("加入白名单");
		
		//进入管理白名单
		OpenTabMenu("防打扰" , "白名单");
		
		//检测当前界面为防打扰页
		Assert.assertTrue("没有进入白名单",driver.findElementByName("管理白名单").isDisplayed());
		
		boolean bl = searchContact(phone, 0);
		
		//检测当前是否存储骚扰电话
		//Assert.assertTrue(bl);
		Myassert("列表中，找不到联系人：" + phone ,bl, username);
		
		
		sleepTime(1000);
		
		//清空数据
		if(bl){
			//点击清空
			clickById("iab_ib_action");
				
			//点击清空
			clickById("dialog_btn_positive");
		}
		//返回主界面
		back("tab_contacts");
		
		//删除
		//deleteContactsByName(phone);
		deleteAllContacts();
		//清空记录
		clearTextAndNote();
		reportLog("testCase_contact_009","联系人-加入白名单");
	}
	
	/**
	 * 联系人页-加入白名单
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_010(){
		String username = "testCase_contact_010";
		String phone = "13522168031";
		
		deleteAllContacts();
		
		clickById("tab_contacts");
		
		//创建联系人
		createContacts(username, phone);
		
		//清空记录
		clearTextAndNote();
		
		sleepTime(4000);
		 
		//点击进入
		clickByName(phone);
		
		//选择加入白名单
		this.clickMenuAndSelect(4);
		
		//返回联系人页
		back("tab_contacts");
		
		
		//进入管理白名单
		OpenTabMenu("防打扰" , "白名单");
		
		//检测当前界面为防打扰页
		Assert.assertTrue("没有进入白名单管理",driver.findElementByName("管理白名单").isDisplayed());
		
		boolean bl = searchContact(phone, 0);
		
		//检测当前是否存储骚扰电话
		//Assert.assertTrue(bl);
		Myassert("没有找到联系人："+ phone, bl, username);
		
		sleepTime(1000);
		
		//清空数据
		if(bl){
			//点击清空
			clickById("iab_ib_action");
				
			//点击清空
			clickById("dialog_btn_positive");
		}
		//返回主界面
		back("tab_contacts");
		
		//删除
		//deleteContactsByName(phone);
		deleteAllContacts();
		//清空记录
		clearTextAndNote();
		reportLog("testCase_contact_010","联系人页-加入白名单");
	}
	
	/**
	 * 创建详细联系人，添加头像
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_011(){
		deleteAllContacts();
		//创建联系人
		createContacts();
		//获取联系人图标
		Point point = getContactsPoint().get(0);
		//System.out.println("Point: " + point);
		
		BufferedImage subImg1 = getContactHead(point);
		
		//点击一个联系人
		clickById("contact_name");
		
		//选择图片
		selectImage();
		
		back("tab_contacts");
		
		BufferedImage subImg2 = getContactHead(point);
		//System.out.println("Point: " + point);
		
		//对比图片
		boolean bl = sameAs(subImg1, subImg2, 0.9);
		
		//清理数据
		this.deleteAllContacts();
		
		//判断
		//Assert.assertTrue(!bl);
		Myassert("添加图片失败", !bl, "testCase_contact_011");
		
		reportLog("testCase_contact_011","创建详细联系人，添加头像");
	}
	
	/**
	 *获取搜索输入框内的联系人个数 
	 * 联系人数量在0个是对比，1个是对比。超于6个或7个联系人联系人数量无法对比，列表创建原理。
	 */
	@Test(groups={"contact"})
	public void testCase_contact_012(){
		
		//清空本地联系人
		this.deleteAllContacts();
		back("tab_contacts");
		//当联系人为0时
		
		//进入联系人列表，获取联系人数量，getContactCount();
		//获取搜索框内容的数字，getNumById("contact_search_bar");
		
		//验证人数是否匹对
		boolean b1 = getContactCount() == getNumById("contact_search_bar");
		//Assert.assertTrue(getContactCount() == getNumById("contact_search_bar"));
		Myassert("联系人数量不一致", b1, "testCase_contact_012");
		
		//创建联系人数据集
		createContacts("testCase_contact_012", "13522168033");
		boolean b2 = getContactCount() == getNumById("contact_search_bar");
		//Assert.assertTrue(getContactCount() == getNumById("contact_search_bar"));
		Myassert("联系人数量不一致", b2, "testCase_contact_012");
		
		//清空本地联系人
		deleteAllContacts();
		
		reportLog("testCase_contact_012","获取搜索输入框内的联系人个数");
	}
	
	/**
	 * 分组管理，创建分组并添加成员
	 */
	@Test(groups={"contact"})
	public void testCase_contact_013(){
		clickById("tab_contacts");
		
		deleteGroup("陈策");
		
		deleteAllContacts();
		
		createContacts("testCase_contact_013", "13522168034");
		createContacts("testCase1_contact_013", "13521168034");

		//创建分组，并留住当前页面
		createGroup("陈策", 1);
		
		//点击我的分组
		clickById("headview_item_name");
		
		//进入分组
		clickByName("陈策");
		
		//点击添加成员
		clickByName("添加成员");
		//sleepTime(10000);
		
		//验证当前页面
		Assert.assertTrue("当前页不在我的分组内",isExistenceByName("分组添加成员"));
		
		//获取联系人列表联系人数量
		int firNum = getNumById("contact_search_bar");
		
		//点击更多按钮
		clickById("iab_ib_more");

		//点击添加
		clickById("selection_ok");
		
		//获取联系人列表联系人数量
		int secNum = getNumById("contact_search_bar");
		
		//验证数量是否一致
		//Assert.assertTrue(firNum == secNum);
		Myassert("数量是否一致", firNum == secNum, "testCase_contact_013");

		back("tab_contacts");
		
		//点击我的分组
		clickById("headview_item_name");
	
		deleteGroup("陈策");
		
		back("tab_contacts");
		
		deleteAllContacts();
		
		reportLog("testCase_contact_013","分组管理，创建分组并添加成员");
	}
	
	/**
	 * 分组管理，修改组名
	 * 分别创建两个分组，一个联系人为空，另一个联系人数量不为空
	 */
	@Test(groups={"contact"})
	public void testCase_contact_014(){
		

		clearGroup();
		deleteAllContacts();
		
		//创建联系人
		createContacts("testCase0_contact_014", "13522168035");
		createContacts("testCase1_contact_014", "13521168035");
		createContacts("testCase2_contact_014", "13521068035");
		
		//创建分组1
		createGroup("TestGroup_01", 1);
		
		//创建分组2，分组2添加联系人
		createGroup("TestGroup_02", 1);
		
		//分组添加联系人
		addAllGroupMembers("TestGroup_02");
		
		intoMyGroupPage();
		
		renameGroup("TestGroup_01", "你猜猜");
		
		intoMyGroupPage();
		
		renameGroup("TestGroup_02", "猜不到啊");
		
		Assert.assertTrue("修改分组失败",isExistenceByName("你猜猜")&&isExistenceByName("猜不到啊"));

		deleteGroup("你猜猜");
		
		deleteGroup("猜不到啊");
		
		deleteAllContacts();
		
		reportLog("testCase_contact_014","分组管理，修改组名");
	}
	
	/**
	 * 分组管理，解除分组
	 */
	@Test(groups={"contact"})
	public void testCase_contact_015(){
		clickById("tab_contacts");
		
		clearGroup();

		deleteAllContacts();
		
		//创建联系人
		createContacts("testCase0_contact_015", "13522168036");
		createContacts("testCase1_contact_015", "13521168036");
		createContacts("testCase2_contact_015", "13521068036");
		
		//创建分组1
		createGroup("TestGroup01_36", 1);
		
		//创建分组2，分组2添加联系人
		createGroup("TestGroup02_36", 1);
		
		//分组添加联系人
		addAllGroupMembers("TestGroup02_36");
		
		//进入我的分组
		intoMyGroupPage();
		
		//删除分组
		deleteGroup("TestGroup01_36");
		
		//进入我的分组
		intoMyGroupPage();
		
		//删除分组
		deleteGroup("TestGroup02_36");
		
		Assert.assertTrue("删除分组失败",!(isExistenceByName("TestGroup01_36")&&isExistenceByName("TestGroup02_36")));
		
		deleteAllContacts();
		reportLog("testCase_contact_015","分组管理，解除分组");
	}
	
	/**
	 * 分组管理，添加成员
	 */
	@Test(groups={"contact"})
	public void testCase_contact_016(){
		//清空数据
		clearGroup();
		
		deleteAllContacts();
		
		//创建联系人
		createContacts("testCase0_contact_016", "13522068037");
		createContacts("testCase1_contact_016", "13521168037");
		createContacts("testCase2_contact_016", "13521268037");
		createContacts("testCase3_contact_016", "13522368037");
		createContacts("testCase4_contact_016", "13521468037");
		createContacts("testCase5_contact_016", "13521568037");
		createContacts("testCase6_contact_016", "13521668037");
		
		//创建分组，分组添加联系人
		//不添加联系人
		createGroup("TestGroup0_37", 1);
		//添加1个联系人
		createGroup("TestGroup1_37", 1);
		//添加2个联系人
		createGroup("TestGroup2_37", 1);
		//添加所有联系人
		createGroup("TestGroup3_37", 1);
		
		//添加完后，判断未分组的联系人数量是否为0
		
		//添加1个联系人
		addGroupMembers("TestGroup1_37", 1);
		
		//添加2个联系人
		addGroupMembers("TestGroup2_37", 2);
		
		//添加7个联系人
		addAllGroupMembers("TestGroup3_37");
		
		//进入我的分组
		intoMyGroupPage();
		
		List<WebElement> list = this.getWebElementList("TextView", "groupNumber");
		
		Assert.assertTrue("分成成员数量有异常:未分组",getNumerByText(list.get(getGroupNum("未分组")).getText()) == 0);
		
		Assert.assertTrue("分成成员数量有异常:TestGroup0_37",getNumerByText(list.get(getGroupNum("TestGroup0_37")).getText()) == 0);
		
		Assert.assertTrue("分成成员数量有异常:TestGroup1_37",getNumerByText(list.get(getGroupNum("TestGroup1_37")).getText()) == 1);
		
		Assert.assertTrue("分成成员数量有异常:TestGroup2_37",getNumerByText(list.get(getGroupNum("TestGroup2_37")).getText()) == 2);
		
		Assert.assertTrue("分成成员数量有异常:TestGroup3_37",getNumerByText(list.get(getGroupNum("TestGroup3_37")).getText()) == 7);

		//清除数据
		deleteGroup("TestGroup0_37");
		deleteGroup("TestGroup1_37");
		deleteGroup("TestGroup2_37");
		deleteGroup("TestGroup3_37");
		
		deleteAllContacts();
		reportLog("testCase_contact_016","分组管理，添加成员");
		
	}
	
	/**
	 * 分组管理，排序
	 */
	@Test(groups={"contact"})
	public void testCase_contact_017(){
		
		//删除已有分组，预防影响测试
		clearGroup();
		
		deleteAllContacts();

		
		createContacts("testCase_contact_017", "13522068038");
		
		//创建分组
		createGroup("testCase01_038", 1);
		createGroup("testCase02_038", 1);
		createGroup("testCase03_038", 1);
		createGroup("testCase04_038", 1);
		
		//验证
		Assert.assertTrue("没找到我的分组入口",isExistenceById("headview_item_name"));
		
		intoMyGroupPage();
		
		int groupSize = getWebElementList("TextView","txt_group_name").size();
		
		//点击排序
		clickByName("排序");
		
		sleepTime(3000);
		
		//移动分组
		groupMoveto("testCase04_038", "up");

		sleepTime(1000);
		
		//移动分组
		groupMoveto("testCase01_038","down");
		
		//点击保存
		clickByName("保存");
		
		sleepTime(2000);
		
		//验证
		Assert.assertTrue("分组排序顺序有异常（非第二）：testCase04_038",getGroupNum("testCase04_038") == 1);

		Assert.assertTrue("分组排序顺序有异常（非末尾）：testCase01_038",getGroupNum("testCase01_038") == (groupSize - 1));

		deleteGroup("testCase01_038");
		deleteGroup("testCase02_038");
		deleteGroup("testCase03_038");
		deleteGroup("testCase04_038");
		
		deleteAllContacts();
		
		reportLog("testCase_contact_017","分组管理，排序");
	}
	
	/**
	 * 分组管理，移除成员
	 */
	@Test(groups={"contact"})
	public void testCase_contact_018(){
		//清空数据
		clearGroup();
		
		deleteAllContacts();
		
		//创建联系人
		createContacts("testCase0_039", "13522068039");
		createContacts("testCase1_039", "13521168039");
		createContacts("testCase2_039", "13521268039");
		createContacts("testCase3_039", "13522368039");
		createContacts("testCase4_039", "13521468039");
		createContacts("testCase5_039", "13521568039");
		createContacts("testCase6_039", "13521668039");
		
		//创建分组，分组添加联系人
		//不添加联系人
		createGroup("TestGroup0_39", 1);
		//添加1个联系人
		createGroup("TestGroup1_39", 1);
		//添加2个联系人
		createGroup("TestGroup2_39", 1);
		//添加所有联系人
		createGroup("TestGroup3_39", 1);
		
		
		//添加7个联系人
		addAllGroupMembers("TestGroup0_39");
		
		addAllGroupMembers("TestGroup1_39");
		
		addAllGroupMembers("TestGroup2_39");
		
		addAllGroupMembers("TestGroup3_39");
		
		//不移除
		deleteGroupMembers("TestGroup0_39", 0);
		
		//移除1个
		deleteGroupMembers("TestGroup1_39", 1);
		
		//移除2个
		deleteGroupMembers("TestGroup2_39", 2);
		
		//移除所有，版本改动，去除所有分组自动删除
		deleteAllGroupMembers("TestGroup3_39");
		
		//进入我的分组
		intoMyGroupPage();
		
		List<WebElement> list = this.getWebElementList("TextView", "groupNumber");
		
		Assert.assertTrue("分组成员数量有异常：未分组",getNumerByText(list.get(getGroupNum("未分组")).getText()) == 0);
		
		Assert.assertTrue("分组成员数量有异常：TestGroup0_39",getNumerByText(list.get(getGroupNum("TestGroup0_39")).getText()) == 7);
		
		Assert.assertTrue("分组成员数量有异常：TestGroup1_39",getNumerByText(list.get(getGroupNum("TestGroup1_39")).getText()) == 6);
		
		Assert.assertTrue("分组成员数量有异常：TestGroup2_39",getNumerByText(list.get(getGroupNum("TestGroup2_39")).getText()) == 5);
		
		//不验证
		//Assert.assertTrue("分组成员数量有异常：TestGroup3_39",getNumerByText(list.get(getGroupNum("TestGroup3_39")).getText()) == 0);

		//清除数据
		deleteGroup("TestGroup0_39");
		deleteGroup("TestGroup1_39");
		deleteGroup("TestGroup2_39");
		deleteGroup("TestGroup3_39");
		
		deleteAllContacts();
		reportLog("testCase_contact_018","分组管理，移除成员");
	}

	
	/**
	 * 分组管理，重命名
	 */
	@Test(groups={"contact"})
	public void testCase_contact_019(){
		//清除数据
		clearGroup();
		
		deleteAllContacts();
		
		//创建联系人
		createContacts("testCase0_040", "13522068040");
		createContacts("testCase1_040", "13521168040");
		createContacts("testCase2_040", "13521268040");
		
		//添加分组
		createGroup("TestGroup0_40", 1);
		
		createGroup("TestGroup1_40", 1);
		
		createGroup("TestGroup2_40", 1);
		
		
		//添加1个联系人
		addGroupMembers("TestGroup1_40", 1);
		
		//添加所有个联系人
		addAllGroupMembers("TestGroup2_40");
		
		//重命名
		renameOneGroup("TestGroup0_40", "Rename01");
		renameOneGroup("TestGroup1_40", "Rename02");
		renameOneGroup("TestGroup2_40", "Rename03");
		
		intoMyGroupPage();
		//验证
		Assert.assertTrue("重命名失败:TestGroup0_40",isExistenceByName("Rename01") &&(!isExistenceByName("TestGroup0_40")));
		
		Assert.assertTrue("重命名失败:TestGroup1_40",isExistenceByName("Rename02") &&(!isExistenceByName("TestGroup1_40")));
		
		Assert.assertTrue("重命名失败:TestGroup2_40",isExistenceByName("Rename03") &&(!isExistenceByName("TestGroup2_40")));
		
		//清除数据
		deleteGroup("TestGroup0_40");
		deleteGroup("TestGroup1_40");
		deleteGroup("TestGroup2_40");
		deleteGroup("Rename01");
		deleteGroup("Rename02");
		deleteGroup("Rename03");
		
		deleteAllContacts();
		
		reportLog("testCase_contact_019","分组管理，重命名");
	}
	
	/**
	 * 分组管理，解散分组
	 */
	@Test(groups={"contact"})
	public void testCase_contact_020(){
		//清除数据
		clearGroup();
		
		deleteAllContacts();
		//创建联系人
		createContacts("testCase0_041", "13522068041");
		createContacts("testCase1_041", "13521168041");
		createContacts("testCase2_041", "13521268041");
		
		//添加分组
		createGroup("TestGroup0_41", 1);
		
		createGroup("TestGroup1_41", 1);
		
		createGroup("TestGroup2_41", 1);
		
		
		//添加1个联系人
		addGroupMembers("TestGroup1_41", 1);
		
		//添加所有个联系人
		addAllGroupMembers("TestGroup2_41");
		

		//删除
		deleteOneGroup("TestGroup0_41");
			
		deleteOneGroup("TestGroup1_41");
		
		deleteOneGroup("TestGroup2_41");
		
		//判断
		Assert.assertTrue("解散分组失败：TestGroup0_41", !isExistenceByName("TestGroup0_41"));
		Assert.assertTrue("解散分组失败：TestGroup1_41", !isExistenceByName("TestGroup1_41"));
		Assert.assertTrue("解散分组失败：TestGroup2_41", !isExistenceByName("TestGroup2_41"));
		
		deleteAllContacts();
		
		reportLog("testCase_contact_020","分组管理，解散分组");
	}
	
	/**
	 * 分组管理，批量设置铃声
	 */
	@Test(groups={"contact"})
	public void testCase_contact_021(){
		//清除联系人
		clearGroup();
		
		deleteAllContacts();
		
		//创建联系人
		createContacts("testCase0_042", "13522068042");
		createContacts("testCase1_042", "13521168042");
		createContacts("testCase2_042", "13521268042");
		
		//添加分组
		createGroup("TestGroup0_42", 1);
		
		createGroup("TestGroup1_42", 1);
		
		createGroup("TestGroup2_42", 1);
		
		//添加联系人
		addGroupMembers("TestGroup0_42", 0);
		
		addGroupMembers("TestGroup1_42", 1);
		
		addAllGroupMembers("TestGroup2_42");
		
		//添加铃声
		scrollAndSelect("TestGroup0_42", "top");
		
		scrollAndSelect("TestGroup1_42", "mid");
		
		scrollAndSelect("TestGroup2_42", "bottom");
		
		//这方法只能通过拨号来验证
		
		//清除联系人
		deleteGroup("TestGroup0_42");
		deleteGroup("TestGroup1_42");
		deleteGroup("TestGroup2_42");
		
		deleteAllContacts();
		
		reportLog("testCase_contact_021","分组管理，批量设置铃声");
	}
	
	/**
	 * 分组管理，组内搜索
	 */
	@Test(groups={"contact"})
	public void testCase_contact_022(){
		//清理数据
		deleteAllContacts();
		
		//创建联系人
		createContacts("testCase0_043", "13522068043");
		createContacts("testCase1_043", "13521168043");
		createContacts("testCase2_043", "13521268043");
		createContacts("testCase3_043", "13522368043");
		createContacts("testCase4_043", "13521468043");
		createContacts("testCase5_043", "13521568043");
		
		//有联系人才能删除分组
		deleteGroup("TestGroup0_43");
		
		//添加分组
		createGroup("TestGroup0_43", 1);

		//添加联系人
		addGroupMembers("TestGroup0_43", 5);
		
		intoMyGroupPage();
		
		clickByName("TestGroup0_43");
		
		//输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "13522068043");
		//搜索存在的联系人
		//Assert.assertTrue(searchContact("13522068043" , 1));
		Myassert("找不到联系人：13522068043", searchContact("13522068043" , 1), "testCase_043");
		
		//输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "13521568043");
		
		//搜索不存在的联系人
		Assert.assertTrue("搜索到联系人，理应搜索不到",isExistenceById("no_contact_text"));
		
		//返回
		back("tab_contacts");
		
		deleteGroup("TestGroup0_43");
		
		//清理数据
		deleteAllContacts();
		
		reportLog("testCase_contact_022","分组管理，组内搜索");
	}
	
	/**
	 * 手势滑动侧边栏与拨号页、联系人、信息切换。
	 */
	@Test(groups = { "other" })
	public void testCase_other_001(){
		//联系人
		clickById("tab_contacts");
		for(int i=0; i < 80; i++){
			swipeToRight();
			sleepTime(1000);
			swipeToLeft();
			sleepTime(1000);
		}
		//判断当前界面存在和通讯录标题
		//Assert.assertTrue("当前界面不存在和通讯录", this.isExistenceById("iab_title"));
		Myassert("当前界面不存在和通讯录", isExistenceById("iab_title"), "testCase_other_001");
		
		clickById("tab_call");
		for(int i=0; i < 80; i++){
			swipeToRight();
			sleepTime(1000);
			swipeToLeft();
			sleepTime(1000);
		}
		//判断当前界面存在和通讯录标题
		Myassert("当前界面不存在和通讯录", isExistenceById("iab_title"), "testCase_other_001");
		
		clickById("tab_mms");
		for(int i=0; i < 80; i++){
			swipeToRight();
			sleepTime(1000);
			swipeToLeft();
			sleepTime(1000);
		}
		//判断当前界面存在和通讯录标题
		Myassert("当前界面不存在和通讯录", isExistenceById("iab_title"), "testCase_other_001");
		reportLog("testCase_other_001","手势滑动侧边栏与拨号页、联系人、信息切换。");
	}
	
	/**
	 * 验证侧边栏切换
	 */
	@Test(groups={"test"})
	public void testCase_other_002() {
		int i;
		int num;
		for (i = 0; i < 10; i++) {
			num = (int) (Math.random() * 10) % 5;
			switch (num) {
			case 4:
				// 点击拨号
				clickById("tab_call");
				//sleepTime(1000);
				break;

			case 3:
				// 点击联系人
				clickById("tab_contacts");
				//sleepTime(1000);
				break;

			case 2:
				// 点击信息
				clickById("tab_mms");
				//sleepTime(1000);
				break;

			case 1:
				// 点击生活助手
				clickById("tab_cloud");
				//sleepTime(1000);
				break;

			case 0:
				// 点击和通讯录
				clickById("iab_title");
				//sleepTime(1000);

				// 点击返回
				driver.sendKeyEvent(AndroidKeyCode.BACK);
				sleepTime(1000);
				break;
			}

		}
		//Assert.assertTrue(isExistenceById("iab_title"));
		Myassert("当前界面不存在和通讯录", isExistenceById("iab_title"), "testCase_other_002");
		reportLog("testCase_other_002","验证侧边栏切换");
	}
	
	/**
	 * 新建短信，下拉创建短信
	 */
	@Test(groups={"mms"})
	public void testCase_mms_001(){
		//清除短信
		deleteAllMMs();

		//创建短信
		createMMs("13522068044","testCase0_044");
		createMMs("13522168044","testCase1_044");
		
		
		deleteAllMMs();
		//创建短信
		reportLog("testCase_mms_001","新建短信，下拉创建短信");
	}
	
	/**
	 * 创建联系人，点击新建，点击选择已有的联系人
	 */
	@Test(groups={"mms"})
	public void testCase_mms_002(){
		deleteAllMMs();
		deleteAllContacts();
		
		createContacts("testCase0_045", "13522068045");
		createContacts("testCase1_045", "13522168045");
		
		createMMs("testCase_045_MMS_0");
		
		createMMs("testCase_045_MMS_1");
		
		//清除
		deleteAllContacts();
		deleteAllMMs();
		
		reportLog("testCase_mms_002","创建联系人，点击新建，点击选择已有的联系人");
	}
	
	/**
	 * 编辑常用语，选择第一条常用语，并发送
	 */
	@Test(groups={"mms"})
	public void testCase_mms_003(){
		//清除
		deleteAllMMs();
		
		clickById("tab_mms");
		
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新建信息页", isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068046");
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//右滑
		swipeToRight("bottom");
		
		//验证
		Assert.assertTrue("没有进入编辑常用语",isExistenceById("mms_buttom_useful_sms_edit_btn"));
		
		//点击第一个
		WebElement we = getWebElementInList("list_item_txt","start");

		String str = we.getText().toString().trim();
		
		we.click();
		
		//点击确定
		clickById("send_button");
		
		//返回上一页
		back("tab_mms");
		
		//去除信息回收站
		if(isExistenceById("tv_title")){
			clickById("notice_delete");
		}
		
		//System.out.println("str " + str);
		Assert.assertTrue("内容不一致",getTextViewNameById("subject").toString().trim().equals(str));
		reportLog("testCase_mms_003","编辑常用语，选择第一条常用语，并发送");
		deleteAllMMs();
	}	 
	
	/**
	 * 编辑常用语，选择最后一条常用语，并发送
	 */
	@Test(groups={"mms"})
	public void testCase_mms_004(){
		//清除
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新建信息页",isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068047");
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//右滑
		swipeToRight("bottom");
		
		//验证
		Assert.assertTrue("没有进入编辑常用语",isExistenceById("mms_buttom_useful_sms_edit_btn"));
		
		//向上滑
		swipeToUp();
		
		//点击最后一条
		WebElement we = getWebElementInList("list_item_txt","end");

		String str = we.getText().toString().trim();
		
		we.click();
			
		//点击确定
		clickById("send_button");
		
		//返回上一页
		back("tab_mms");
		
		//去除信息回收站
		if(isExistenceById("tv_title")){
			clickById("notice_delete");
		}
		
		System.out.println("str " + str);
		Assert.assertTrue("未存在短信",isExistenceById("subject"));
		Assert.assertTrue("内容对比不一致",getTextViewNameById("subject").toString().trim().equals(str));
		reportLog("testCase_mms_004","编辑常用语，选择最后一条常用语，并发送");
		deleteAllMMs();
	}
	
	/**
	 * 编辑常用语，新增常用语
	 */
	@Test(groups={"mms"})
	public void testCase_mms_005(){
		//清除
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068048");
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//右滑
		swipeToRight("bottom");
		
		//验证
		Assert.assertTrue("没有进入常用短信编辑",isExistenceById("mms_buttom_useful_sms_edit_btn"));
		
		//点击编辑常用语
		clickById("mms_buttom_useful_sms_edit_btn");
		
		//点击添加
		clickById("mms_buttom_useful_sms_add_btn");
		
		//添加常用语
		intoContentEditTextById("content", "这是一段测试常用语01");
		
		//点击确定
		clickById("dialog_btn_positive");

		//等待
		sleepTime(3000);
		
		//向上滑
		swipeToUp();
		
		//点击最后一条
		WebElement we = getWebElementInList("list_item_txt","end");

		String str = we.getText().toString().trim();
		
		we.click();
		
		//点击确定
		clickById("send_button");
		
		//返回上一页
		back("tab_mms");
		
		//去除信息回收站
		if(isExistenceById("tv_title")){
			clickById("notice_delete");
		}
		
		//System.out.println("str " + str);
		Assert.assertTrue("内容对比不一致",getTextViewNameById("subject").toString().trim().equals(str));
		reportLog("testCase_mms_005","编辑常用语，新增常用语");
		deleteAllMMs();
	}
	
	/**
	 * 编辑常用语，删除常用语
	 */
	@Test(groups={"mms"})
	public void testCase_mms_006(){
		//清除
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//右滑
		swipeToRight("bottom");
		sleepTime(2000);
		
		//验证
		Assert.assertTrue("没有进入常用编辑页",isExistenceById("mms_buttom_useful_sms_edit_btn"));
		
		//向上滑
		swipeToUp();
		
		//点击最后一条
		WebElement we = getWebElementInList("list_item_txt","end");

		String str = we.getText().toString().trim();
		
		//判断是否为自定义常用语
		if(getNumerByText(str)>0){
			//判断为有自定义的常用语，什么都不做
		}
		//否则新建一自定义常用语
		else{
			//点击编辑常用语
			clickById("mms_buttom_useful_sms_edit_btn");
			
			//点击添加
			clickById("mms_buttom_useful_sms_add_btn");
			
			//添加常用语
			intoContentEditTextById("content", "这是一段测试常用语02");
			
			//点击确定
			clickById("dialog_btn_positive");
			
			sleepTime(2000);
		}
		
		
		//点击编辑常用语
		clickById("mms_buttom_useful_sms_edit_btn");
		
		//向上滑
		swipeToUp();
		
		//获取最后一个元素
		WebElement we2 = getWebElementInList("list_item_txt","end");
		
		//判断为自定义常用语
		Assert.assertTrue("最有一条常用语非自定义",getNumerByText(we2.getText().toString().trim()) > 0);
	
		//获取最后一个元素
		WebElement del = getWebElementInList("list_item_btn","end");
		
		//删除
		del.click();
		
		//点击退出编辑
		clickById("mms_buttom_useful_sms_exit_btn");
		
		sleepTime(2000);
		
		swipeToUp();
		//再次获取最后元素
		WebElement we3 = getWebElementInList("list_item_txt","end");
		//判断为非自定义常用语
		Assert.assertTrue("最后一条不是常用语",getNumerByText(we3.getText().toString().trim()) < 0);
		reportLog("testCase_mms_006","编辑常用语，删除常用语");
	}
	
	/**
	 * 设置时间、日期方法已经实现;版本为3.9.6点击发送后，跳转到空白页，无法删除已设置的短信
	 */
	@Test(groups={"mms_error"})
	public void testCase_mms_007(){
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//定时
		clickById("timinglayout");
		
		//验证
		Assert.assertTrue("没有进入时间设置页",isExistenceByName("请设定短信发送时间"));
		
		//设置时间
		//setTime("16", "45");
		
		//设置年份
		setDate("2017", "03", "21");
		
		//点击保存
		clickById("dialog_btn_positive");
		
		sleepTime(3000);
		reportLog("testCase_mms_007","未实现有Bug");
	}
	
	
	/**
	 * 发送表情,选择最左端
	 */
	@Test(groups={"mms"})
	public void testCase_mms_008(){
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//点击表情
		clickById("emoticons");
		
		//验证
		Assert.assertTrue("没有进入表情选项",isExistenceById("emoticon_viewpager"));
		
		//点击默认
		clickById("emotionbar_radiobtn_feixin");
		
		//选择表情
		selectEmoticon(0);
		
		//选择表情
		selectEmoticon(1);
		
		//删除第二个表情
		selectEmoticon(20);
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068051");
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		back("tab_mms");

		//Assert.assertTrue("发送表情错误",getTextViewNameById("subject").contains("微笑"));
		Myassert("发送表情错误", getTextViewNameById("subject").contains("微笑"), "testCase_051");
		
		reportLog("testCase_mms_008","发送表情,选择最左端");
		sleepTime(2000);
		
		deleteAllMMs();
	}
	
	/**
	 * 发送表情,选择最右端
	 */
	@Test(groups={"mms"})
	public void testCase_mms_009(){
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//点击表情
		clickById("emoticons");
		
		//验证
		Assert.assertTrue("没有进入表情选项",isExistenceById("emoticon_viewpager"));
		
		//点击默认
		clickById("emotionbar_radiobtn_feixin");
		
		//向左滑
		swipeToLeft("bottom");
		sleepTime(1000);
		swipeToLeft("bottom");
		sleepTime(1000);
		swipeToLeft("bottom");
		sleepTime(1000);
		
		
		
		//选择表情
		selectEmoticon(6);
		
		//选择表情
		selectEmoticon(7);
		
		//删除第二个表情
		selectEmoticon(20);
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068052");
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		back("tab_mms");

		//Assert.assertTrue(getTextViewNameById("subject").contains("恶魔"));
		Myassert("发送表情错误", getTextViewNameById("subject").contains("恶魔"), "testCase_mms_009");
		reportLog("testCase_mms_009","发送表情,选择最右端");
		sleepTime(2000);
		
		deleteAllMMs();
	}
	
	
	/**
	 * 精选短信,选择顶部
	 */
	@Test(groups={"mms"})
	public void testCase_mms_010(){
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//点击表情
		clickById("featuremms");
		
		//验证
		Assert.assertTrue("没有进入精选短信页",isExistenceByName("精选短信"));
			
		//选择精选短信
		selectFeaturemms(0);
		
		//选择第一个
		WebElement we = getWebElementInList("text","start");
		String subStr = we.getText().toString().substring(0, 4);

		we.click();
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068053");
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		back("tab_mms");

		//Assert.assertTrue(getTextViewNameById("subject").contains(subStr));
		Myassert("内容对比不一致", getTextViewNameById("subject").contains(subStr), "testCase_mms_010");
		reportLog("testCase_mms_010","精选短信,选择顶部");
		sleepTime(2000);
		
		deleteAllMMs();
	}
	
	/**
	 * 精选短信,选择底部
	 */
	@Test(groups={"mms"})
	public void testCase_mms_011(){
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项页",isExistenceById("MMS_option"));
		
		//点击表情
		clickById("featuremms");
		
		//验证
		Assert.assertTrue("没有进入精选短信页",isExistenceByName("精选短信"));
		
		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);
		
		//选择精选短信
		selectFeaturemms(9);
		
		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);
			
		//选择
		WebElement we = getWebElementInList("text","end");
		String subStr = we.getText().toString().substring(0, 4);

		we.click();
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068054");
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		back("tab_mms");

		//Assert.assertTrue(getTextViewNameById("subject").contains(subStr));
		Myassert("内容对比不一致", getTextViewNameById("subject").contains(subStr), "testCase_mms_011");
		reportLog("testCase_mms_011","精选短信,选择底部");
		sleepTime(2000);
		
		deleteAllMMs();
	}
	
	/**
	 * 发彩信，选择本地图片
	 */
	@Test(groups={"mms"})
	public void testCase_mms_012(){
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068055");
		
		intoContentEditTextById("embedded_text_editor", "testCase_055");
		
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//点击图片
		clickById("picture");
		
		//本地相册
		contextMenuTitleSelect(1);
		//选择相册
		
		clickByName("相册");

		//再次选择相册
		clickByName("相册");
		
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
		
        Point point = new Point(width/4, height/2);
        
        sleepTime(1000);
        this.touchScreen(point);
        
        Point point2 = new Point(width/2, height/5);
        sleepTime(1000);
        this.touchScreen(point2);
        
        sleepTime(2000);
        //点击完成
        clickByName("完成");
		
        sleepTime(3000);
        
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		back("tab_mms");

		Assert.assertTrue("彩信发送失败1",getTextViewNameById("subject").contains("testCase_055"));
		Assert.assertTrue("彩信发送失败2",isExistenceById("attachment"));
		reportLog("testCase_mms_012","发彩信，选择本地图片");
		sleepTime(2000);
		
		deleteAllMMs();
	}
	
	/**
	 * 发送名片，单个
	 */
	@Test(groups={"mms"})
	public void testCase_mms_013(){
		//清除
		deleteAllMMs();
		deleteAllContacts();
		
		createContacts("testCase0_056", "13522068056");
		createContacts("testCase1_056", "13522168056");
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068056");
			
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选项",isExistenceById("MMS_option"));
		
		//点击分享名片
		clickById("send_card");
		
		//验证
		Assert.assertTrue("没有进入联系人选择页",isExistenceByName("联系人选择"));
		
		//选择联系人数量
		addMMsContactMembers(1);
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		back("tab_mms");
		
		Assert.assertTrue("发送失败",isExistenceById("from"));
		
		//进入第一条短信
		clickById("from");
		
		String allString = getTextViewNameById("text_view");
		
		Assert.assertTrue("短信发送失败",(allString.contains("testCase0_056")) && (allString.contains("13522068056")));
		reportLog("testCase_mms_013","发送名片，单个");
		back("tab_mms");
		
		//清除
		deleteAllMMs();
		
		deleteAllContacts();
		
	}
	
	/**
	 * 发送名片，多个
	 */
	@Test(groups={"mms"})
	public void testCase_mms_014(){
		//清除
		deleteAllMMs();
		deleteAllContacts();
		
		createContacts("testCase0_057", "13522068057");
		createContacts("testCase1_057", "13522168057");
		createContacts("testCase2_057", "13522268057");
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068056");
			
		//短信选项中，更多
		clickById("add_mmspart_button");
		
		//验证
		Assert.assertTrue("没有进入更多选择",isExistenceById("MMS_option"));
		
		//点击分享名片
		clickById("send_card");
		
		//验证
		Assert.assertTrue("没有进入联系人选择页",isExistenceByName("联系人选择"));
		
		//选择联系人数量
		addMMsContactMembers(3);
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		back("tab_mms");
		
		Assert.assertTrue("短信发送失败",isExistenceById("from"));
		
		//进入第一条短信
		clickById("from");
		
		String allString = getTextViewNameById("text_view");
		
		Assert.assertTrue("发送失败，内容不含：testCase0_057",(allString.contains("testCase0_057")) && (allString.contains("13522068057")));
		Assert.assertTrue("发送失败，内容不含：testCase1_057",(allString.contains("testCase1_057")) && (allString.contains("13522168057")));
		Assert.assertTrue("发送失败，内容不含：testCase2_057",(allString.contains("testCase2_057")) && (allString.contains("13522268057")));
		reportLog("testCase_mms_014","发送名片，多个");
		back("tab_mms");
		
		//清除
		deleteAllMMs();
		
		deleteAllContacts();
		
	}
	
	/**
	 * 输入用户、内容，返回.出现草稿箱
	 */
	@Test(groups={"mms"})
	public void testCase_mms_015(){
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068058");
		
		intoContentEditTextById("embedded_text_editor", "testCase_058");
		
		back("tab_mms");
		
		//Assert.assertTrue("",isExistenceById("from")&&getTextViewNameById("from").contains("草稿"));
		Myassert("没有生产草稿", isExistenceById("from")&&getTextViewNameById("from").contains("草稿"), "testCase_058");

		reportLog("testCase_mms_015","输入用户、内容，返回.出现草稿箱");
		deleteAllMMs();
	}
	
	/**
	 * 输入用户、内容，清空，返回，不产生草稿
	 */
	@Test(groups={"mms"})
	public void testCase_mms_016(){
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068059");
		
		intoContentEditTextById("embedded_text_editor", "testCase_059");
		
		back("tab_mms");
		
		Assert.assertTrue("没有生成草稿",isExistenceById("from")&&getTextViewNameById("from").contains("草稿"));
		
		//再次进入
		clickById("from");
		
		//清空内容
		intoContentEditTextById("embedded_text_editor", "");
		
		back("tab_mms");
		
		//验证
		Assert.assertTrue("草稿删除失败",isExistenceById("no_conversation_bigtext"));
		reportLog("testCase_mms_016","输入用户、内容，清空，返回，不产生草稿");
		deleteAllMMs();
	}
	
	/**
	 * 输入用户、内容，清空，返回，不产生草稿
	 */
	@Test(groups={"mms"})
	public void testCase_mms_017(){
		deleteAllMMs();
		
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//验证
		Assert.assertTrue("没有进入新增信息页",isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068060");
		
		back("tab_mms");
		
		//验证
		Assert.assertTrue("草稿没有被删除",isExistenceById("no_conversation_bigtext"));
		
		reportLog("testCase_mms_017","输入用户、内容，清空，返回，不产生草稿");
		
		deleteAllMMs();
	}
	
	
	
	/**
	 * 短信对聊页,点击短信的号码(只能是纯号码)-拨号(原来是40条用例，调整顺序)
	 */
	@Test(groups={"mms"})
	public void testCase_mms_017_1(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_081", sendPhone);
		
		createMMs(phone);
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		String currentPhone = getTextViewNameById("text_view").toString().trim();
		sleepTime(2000);
		//System.out.println("找到了text_view?: " + isExistenceById("text_view"));
		clickById("text_view");
		
		//clickbyWebElementByPointCenter("text_view");
		
		sleepTime(2000);
		
		Assert.assertTrue("没有弹出选择框", isExistenceById("title"));
		Assert.assertTrue("不是对短信上的号码进行操作", getTextViewNameById("title").contains(currentPhone));
		
		//打电话
		contextMenuTitleSelect(1);
		
		clickById("iab_ib_action");
		
		sleepTime(2000);
		//点击确定
		clickByName("确定");
		
		sleepTime(2000);

		back("tab_call");

		Assert.assertTrue("没有找到拨号记录", isExistenceById("line1"));
		
		Assert.assertTrue("拨号记录，拨打的号码不一致", getTextViewNameById("line1").equals(currentPhone));

		reportLog("testCase_mms_040","短信对聊页,点击号码选择拨号");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
	
	}
	
	/**
	 * 短信对聊页,点击短信的号码(只能是纯号码)-发短信(临时调整顺序)
	 */
	@Test(groups={"mms"})
	public void testCase_mms_017_2(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_082", sendPhone);
		
		createMMs(phone);
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		String currentPhone = getTextViewNameById("text_view").toString().trim();
		
		clickById("text_view");
		
		Assert.assertTrue("没有弹出选择框", isExistenceById("title"));
		Assert.assertTrue("不是对短信上的号码进行操作", getTextViewNameById("title").contains(currentPhone));
		
		//发短信
		contextMenuTitleSelect(2);
		
		Assert.assertTrue("没有进入新建短信页", getTextViewNameById("iab_sub_title").contains(currentPhone));
		
		intoContentEditTextById("embedded_text_editor", "testCase_082,点击短信的号码(只能是纯号码)-发短信");

		clickById("send_button");
		
		back("tab_mms");
		
		Assert.assertTrue("没有找到新号码的短信", searListContainName(getLisWebElementById("from"), currentPhone));
		

		reportLog("testCase_mms_041","对聊页， 点击短信的号码(只能是纯号码)-发短信");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
	
	}
	
	/**
	 * 短信对聊页,点击短信的号码(只能是纯号码)-添加为联系人(临时调整顺序)
	 */
	@Test(groups={"mms"})
	public void testCase_mms_017_3(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_083", sendPhone);
		
		createMMs(phone);
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		String currentPhone = getTextViewNameById("text_view").toString().trim();
		
		clickById("text_view");
		
		Assert.assertTrue("没有弹出选择框", isExistenceById("title"));
		Assert.assertTrue("不是对短信上的号码进行操作", getTextViewNameById("title").contains(currentPhone));
		
		//添加为联系人
		contextMenuTitleSelect(3);
		
		Assert.assertTrue("没有进入新建联系人页", getTextViewNameById("iab_title").equals("新建联系人"));
		
		intoContentEditTextByName("姓名", "testCase01_083");

		clickById("iab_ib_action");
		
		sleepTime(2000);
		
		back("tab_contacts");
		
		Assert.assertTrue("没有找到新号码的联系人", searListContainName(getLisWebElementById("contact_name"), "testCase01_083"));
		
		reportLog("testCase_mms_042","短信对聊页,点击号码选择添加为联系人");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
	
	}
	
	/**
	 * 短信对聊页,点击短信的号码(只能是纯号码)-复制(临时调整顺序)
	 */
	@Test(groups={"mms"})
	public void testCase_mms_017_4(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_084", sendPhone);
		
		createMMs(phone);
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		String currentPhone = getTextViewNameById("text_view").toString().trim();
		
		clickById("text_view");
		
		Assert.assertTrue("没有弹出选择框", isExistenceById("title"));
		Assert.assertTrue("不是对短信上的号码进行操作", getTextViewNameById("title").contains(currentPhone));
		
		//添加复制
		contextMenuTitleSelect(4);
		
		//粘贴到输入框
		pasteString("embedded_text_editor");
		
		clickById("send_button");
		
		//获取列表
		List<WebElement> ll = getLisWebElementById("text_view");
		
		Assert.assertTrue("没有复制成功", ll.size() == 2);
		
		Assert.assertTrue("复制的内容不一致", ll.get(0).getText().toString().trim().equals(ll.get(1).getText().toString().trim()));

		back("tab_mms");
		
		reportLog("testCase_mms_043","短信对聊页,点击号码选择添加为联系人");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
		
	}
	
	
	/**
	 * 短信模块，全部标为已读(测试机需要安装短信生成器)
	 */
	@Test(groups={"mms"})
	public void testCase_mms_018(){
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		//清空短信
		//准备未读短信，已经包括了清空短信
		prepareUnreadMMS();
		
		//标为已读
		clickMenuAndSelect(1);
		
		List<WebElement> list = getLisWebElementById("unread");
		
		Assert.assertTrue("全部标为已读失败", list.size() == 0);
		
		reportLog("testCase_mms_018","短信模块，全部标为已读(测试机需要安装短信生成器)");
		
		deleteAllMMs();
		
	}
	
	
	/**
	 * 短信模块，未读信息(测试机需要安装短信生成器)
	 */
	@Test(groups={"debug"})
	public void testCase_mms_019(){
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		//清空短信
		//准备未读短信，已经包括了清空短信
		int size = prepareUnreadMMS();
		
		//标为未读
		clickMenuAndSelect(6);
		
		//获取当前短信数量
		Assert.assertTrue("未读短信数量不一致：准备的未读短信为："+ size, getMMsCount() == size);
		
		reportLog("testCase_mms_019","短信模块，未读信息(测试机需要安装短信生成器)");
		
		back("tab_mms");
		
		deleteAllMMs();
	}
	
	
	
	/**
	 * 短信模块，未读信息读取一条，未读短信变成已读(测试机需要安装短信生成器)
	 */
	@Test(groups={"debug"})
	public void testCase_mms_020(){
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		int size = prepareUnreadMMS();
		
		//标为未读
		clickMenuAndSelect(6);		
		//获取当前短信数量
		Assert.assertTrue("未读短信数量不一致：准备的未读短信为："+ size, getMMsCount() == size);
		
		clickById("tab_mms");
		clickMenuAndSelect(6);
		List<WebElement> listFir = getLisWebElementById("from");
		
		String PhoneFir = listFir.get(0).getText();
		
		//进入短信
		listFir.get(0).click();
		sleepTime(1000);
		
		//返回未读短信
		this.backPage(1);
		Assert.assertTrue("未能返回未读信息列表页", isExistenceByName("未读信息"));
		
		//第二次获取
		List<WebElement> listSec = getLisWebElementById("from");
		
		String PhoneSec = listSec.get(0).getText();
		
		Assert.assertTrue("未读信息读取一条，未读短信未能变成已读", (listFir.size()==(listSec.size() + 1)) && !(PhoneFir.equals(PhoneSec)));

		reportLog("testCase_mms_020","短信模块，未读信息读取一条，未读短信变成已读(测试机需要安装短信生成器)");

		back("tab_mms");
		
		deleteAllMMs();
	}
	
	/**
	 * 短信对聊页,清空信息
	 */
	@Test(groups={"debug"})
	public void testCase_mms_021(){
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS();
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		String firPhone = list.get(0).getText();
		int firSize = list.size();
		
		list.get(0).click();
		
		//选择清空信息
		clickMenuAndSelect(1);
		
		//点击删除
		clickById("dialog_btn_positive");
		
		//等待页面刷新
		sleepTime(3000);
		list = getLisWebElementById("from");
		String secPhone = list.get(0).getText();
		int secSize = list.size();
		
		//自动返回列表
		Assert.assertTrue("没有自动返回短信列表", getTextViewNameById("iab_title").equals("和通讯录"));
		Assert.assertTrue("删除短信失败", (firSize == secSize+1) && (!firPhone.equals(secPhone)));
		
		reportLog("testCase_mms_022","短信对聊页,清空信息");

		back("tab_mms");
		
		deleteAllMMs();
	}
	
	
	/**
	 * 短信对聊页,新建联系人
	 */
	@Test(groups={"mms"})
	public void testCase_mms_023(){
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		//setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS();
		
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//新建联系人
		clickMenuAndSelect(2);
		
		Assert.assertTrue("没有进入新建联系人", getTextViewNameById("iab_title").equals("新建联系人"));
		
		intoContentEditTextByName("姓名", "testCase_065");
		
		//点击保存
		clickById("iab_ib_action");
		
		Assert.assertTrue("姓名保存失败", getTextViewNameById("iab_title").equals("testCase_065"));

		reportLog("testCase_mms_023","短信对聊页,新建联系人");

		back("tab_mms");
		
		deleteAllMMs();
	}
	
	/**
	 * 短信对聊页,添加到已有联系人
	 */
	@Test(groups={"mms"})
	public void testCase_mms_024(){
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		//setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS();
		
		
		createContacts("testCase_066", "13522068066");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//新建联系人
		clickMenuAndSelect(3);

		Assert.assertTrue("没有进入新建联系人", getTextViewNameById("iab_title").equals("新建联系人"));
		
		Assert.assertTrue("没有联系人", isExistenceById("contact_name"));
		
		//选择联系人
		clickById("contact_name");
		
		//点击保存
		clickById("iab_ib_action");
		
		Assert.assertTrue("姓名保存失败", getTextViewNameById("iab_title").equals("testCase_066"));

		reportLog("testCase_mms_024","短信对聊页,添加到已有联系人");

		back("tab_mms");
		
		deleteAllMMs();
	}
	

	
	/**
	 * 短信对聊页,加入黑名单
	 */
	@Test(groups={"mms"})
	public void testCase_mms_025(){
		deleteBlacklist();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		//setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS();
		
		
		deleteBlacklist();
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//黑名单
		clickMenuAndSelect(4);
		
		String tmpPhone =  getTextViewNameById("iab_sub_title");
		
		back("tab_mms");
		
		OpenTabMenu("防打扰" , "黑名单");

		Assert.assertTrue("不存在黑名单联系人", isExistenceByName("管理黑名单"));
		
		Assert.assertTrue("加入黑名单的联系人不一致", getTextViewNameById("phone_name").equals(tmpPhone));

		back("tab_mms");
		
		deleteBlacklist();
		
		reportLog("testCase_mms_025","短信对聊页,加入黑名单");

		back("tab_mms");
		
		deleteAllMMs();
	}
	
	
	/**
	 * 短信对聊页,全选
	 */
	@Test(groups={"mms"})
	public void testCase_mms_026(){
		deleteAllMMs();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		//setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2","4");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//更多
		clickMenuAndSelect(5);
		
		//点击全选
		clickById("mca_ib_select");
		
		int num = getNumerByText(getTextViewNameById("mca_title"));
		
		Assert.assertTrue("没有全选联系人", num == 4);
		
		backPage(1);
		
		reportLog("testCase_mms_026","短信对聊页,全选");

		back("tab_mms");
		
		deleteAllMMs();
	}
	
	/**
	 * 短信对聊页,多选
	 */
	@Test(groups={"mms"})
	public void testCase_mms_027(){
		deleteAllMMs();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		//setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2","4");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//更多
		clickMenuAndSelect(5);
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		//点击最后两个
		ll.get(ll.size()-1).click();
		
		ll.get(ll.size()-2).click();
		
		int num = getNumerByText(getTextViewNameById("mca_title"));
		
		Assert.assertTrue("没有联系人数量选择有异常", num == 2);
		
		backPage(1);
		
		reportLog("testCase_mms_027","短信对聊页,多选");

		back("tab_mms");
		
		deleteAllMMs();
	}
	
	/**
	 * 短信对聊页,删除
	 */
	@Test(groups={"mms"})
	public void testCase_mms_028(){
		deleteAllMMs();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		//setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2","4");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//更多
		clickMenuAndSelect(5);
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		int firSize = ll.size();
		
		//点击最后两个
		ll.get(firSize-1).click();
		
		//点击删除
		clickByName("删除");
		
		//确认删除
		clickById("dialog_btn_positive");

		ll = getLisWebElementById("text_view");
		int secSize = ll.size();
		
		Assert.assertTrue("短信删除失败", firSize == secSize + 1);
		
		reportLog("testCase_mms_028","短信对聊页,删除短信");
		
		back("tab_mms");
		
		deleteAllMMs();
	}
	
	/**
	 * 短信对聊页,收藏
	 */
	@Test(groups={"mms"})
	public void testCase_mms_029(){
		deleteAllMMs();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2","4");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//更多
		clickMenuAndSelect(5);
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		int firSize = ll.size();
		
		//点击最后
		ll.get(firSize-1).click();
		
		//点击收藏
		clickByName("收藏");
		
		String tmpPhone = getTextViewNameById("iab_sub_title");
		
		backPage(1);
		
		//进入信息收藏
		clickMenuAndSelect(4);
		
		Assert.assertTrue("未进入信息收藏页", getTextViewNameById("iab_title").equals("信息收藏"));
		
		Assert.assertTrue("没有发现收藏的信息号码", getTextViewNameById("from").equals(tmpPhone));
		
		clickByName(tmpPhone);
		
		//清空信息
		clickMenuAndSelect(1);
		
		back("tab_mms");
	
		reportLog("testCase_mms_029","短信对聊页,收藏");

		deleteAllMMs();
	}
	
	
	
	/**
	 * 短信对聊页,转发（填写手机号码）
	 */
	@Test(groups={"mms"})
	public void testCase_mms_030(){
		deleteAllMMs();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2","4");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//更多
		clickMenuAndSelect(5);
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		int firSize = ll.size();
		
		//点击最后
		ll.get(firSize-1).click();
		
		//点击收藏
		clickByName("转发");
		
		Assert.assertTrue("没有进入新信息页", getTextViewNameById("iab_title").equals("新信息"));
		
		intoContentEditTextByName("收件人:", phone);
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		Assert.assertTrue("没有发送成功", getTextViewNameById("iab_sub_title").equals(phone));
		
		back("tab_mms");
		
		reportLog("testCase_mms_031","短信对聊页,转发（填写手机号码）");

		deleteAllMMs();
	}
	
	
	/**
	 * 短信对聊页,转发（选择分组联系人）
	 */
	@Test(groups={"mms"})
	public void testCase_mms_031(){
		deleteAllMMs();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		createContacts("testCase_073", phone);
		prepareUnreadMMS("2","4");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//更多
		clickMenuAndSelect(5);
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		int firSize = ll.size();
		
		//点击最后
		ll.get(firSize-1).click();
		
		//点击收藏
		clickByName("转发");
		
		Assert.assertTrue("没有进入新信息页", getTextViewNameById("iab_title").equals("新信息"));
		
		//点击添加按钮
		clickById("add_recipients");
		
		//点击联系人
		clickById("contact_name");
		
		//点击添加
		clickById("selection_ok");
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		Assert.assertTrue("没有发送成功", getTextViewNameById("iab_sub_title").equals(phone));
		
		back("tab_mms");
		
		reportLog("testCase_mms_031","短信对聊页,转发（选择分组联系人）");

		deleteAllContacts();
		deleteAllMMs();
	}
	
	
	/**
	 * 短信对聊页,长按转发（填写手机号码）
	 */
	@Test(groups={"mms"})
	public void testCase_mms_032(){
		deleteAllMMs();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2","1");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		int firSize = ll.size();
		
		//判断短信为自己发送的，还是接收
		if(isExistenceById("avatar_right"))
		{
			//长按最后一个
			clickLongByElementUseJs(ll.get(firSize-1));
		
			//点击转发
			contextMenuTitleSelect(2);
		}
		else{
			//长按最后一个
			clickLongByElementUseJs(ll.get(firSize-1));
		
			//点击转发
			contextMenuTitleSelect(1);
		}
		
		
		Assert.assertTrue("没有进入新信息页", getTextViewNameById("iab_title").equals("新信息"));
		
		intoContentEditTextByName("收件人:", phone);
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		Assert.assertTrue("没有发送成功", getTextViewNameById("iab_sub_title").equals(phone));
		
		back("tab_mms");
		
		reportLog("testCase_mms_032","短信对聊页,长按转发（填写手机号码）");

		deleteAllMMs();
	}
	
	/**
	 * 短信对聊页,长按转发（选择分组联系人）
	 */
	@Test(groups={"mms"})
	public void testCase_mms_033(){
		deleteAllMMs();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		//清空短信
		//准备未读短信（保证两条以上），已经包括了清空短信
		createContacts("testCase_075", phone);
		prepareUnreadMMS("2","4");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		int firSize = ll.size();
		
		//长按最后一个
		clickLongByElementUseJs(ll.get(firSize-1));
		
		//点击转发
		contextMenuTitleSelect(1);
		
		Assert.assertTrue("没有进入新信息页", getTextViewNameById("iab_title").equals("新信息"));
		
		//点击添加按钮
		clickById("add_recipients");
		
		//点击联系人
		clickById("contact_name");
		
		//点击添加
		clickById("selection_ok");
		
		//点击发送
		clickById("send_button");
		
		sleepTime(2000);
		
		Assert.assertTrue("没有发送成功", getTextViewNameById("iab_sub_title").equals(phone));
		
		back("tab_mms");
		
		reportLog("testCase_mms_033","短信对聊页,转发（选择分组联系人）");

		deleteAllContacts();
		deleteAllMMs();
	}
	
	
	/**
	 * 短信对聊页,长按重发（需要点击自己发送的短信）
	 */
	@Test(groups={"mms"})
	public void testCase_mms_034(){
		
		deleteAllMMs();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_076", sendPhone);
		
		//新建短信
		createMMs("testCase_076，重发短信测试");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		//长按第一个
		clickLongByElementUseJs(ll.get(0));
		
		Assert.assertTrue("没有重发按钮", getLisWebElementById("context_menu_title").get(0).getText().equals("重发"));
		//点击重发
		contextMenuTitleSelect(1);
		
		sleepTime(2000);
		
		//Assert.assertTrue("重发失败", isExistenceById("delivered_indicator"));
		
		reportLog("testCase_mms_034","短信对聊页,长按重发（需要点击自己发送的短信）");
		
		deleteAllContacts();
		
		deleteAllMMs();
	}
	
	/**
	 * 短信对聊页,长按查看信息详情
	 */
	@Test(groups={"mms"})
	public void testCase_mms_035(){
		
		deleteAllMMs();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。

		prepareUnreadMMS("3","1");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		String currentPhone = getTextViewNameById("iab_sub_title");
		
		
		
		//判断短信为自己发送的，还是接收
		if(isExistenceById("avatar_right"))
		{
			//长按第一个
			clickLongByElementUseJs(ll.get(0));
			//点击查看信息详情
			contextMenuTitleSelect(4);
		}
		else{
			//长按第一个
			clickLongByElementUseJs(ll.get(0));
			//点击查看信息详情
			contextMenuTitleSelect(3);
		}
		
		Assert.assertTrue("没有弹出信息详情", isExistenceById("title"));
		
		//是否需要添加其他验证
		Assert.assertTrue("没有找到号码", getTextViewNameById("hints").contains(currentPhone));
		
		clickById("dialog_btn_positive");
		
		back("tab_mms");
		
		reportLog("testCase_mms_035","短信对聊页,长按查看信息详情");
		
		deleteAllContacts();
		
		deleteAllMMs();
	}
	
	
	/**
	 * 短信对聊页,长按收藏短信
	 */
	@Test(groups={"mms"})
	public void testCase_mms_036(){
		
		deleteAllMMs();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。

		prepareUnreadMMS("3","1");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		String currentPhone = getTextViewNameById("iab_sub_title");
		
		
		
		//判断短信为自己发送的，还是接收
		if(isExistenceById("avatar_right"))
		{
			//长按第一个
			clickLongByElementUseJs(ll.get(0));
			//点击查看信息详情
			contextMenuTitleSelect(5);
		}
		else{
			//长按第一个
			clickLongByElementUseJs(ll.get(0));
			//点击查看信息详情
			contextMenuTitleSelect(4);
		}
		
		backPage(1);
		
		//进入信息收藏
		clickMenuAndSelect(4);
		
		Assert.assertTrue("未进入信息收藏页", getTextViewNameById("iab_title").equals("信息收藏"));
		
		Assert.assertTrue("没有发现收藏的信息号码", getTextViewNameById("from").equals(currentPhone));
		
		clickByName(currentPhone);
		
		//清空信息
		clickMenuAndSelect(1);
		
		back("tab_mms");
	
		reportLog("testCase_mms_036","短信对聊页,长按收藏短信");
		
		deleteAllContacts();
		
		deleteAllMMs();
		
	}	
	
	/**
	 * 短信对聊页,长按删除
	 */
	@Test(groups={"mms"})
	public void testCase_mms_037(){
		
		deleteAllMMs();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。

		prepareUnreadMMS("3","1");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		int firSize = list.size();
		
		
		list.get(0).click();
		
		//点击全选
		List<WebElement> ll = getLisWebElementById("text_view");
		
		//判断短信为自己发送的，还是接收
		if(isExistenceById("avatar_right"))
		{
			//长按第一个
			clickLongByElementUseJs(ll.get(0));
			//点击删除
			contextMenuTitleSelect(6);
		}
		else{
			//长按第一个
			clickLongByElementUseJs(ll.get(0));
			//点击删除
			contextMenuTitleSelect(5);
		}
		
		back("tab_mms");
		
		list = getLisWebElementById("from");
		
		int secSize = list.size();

		Assert.assertTrue("删除短信失败", firSize == secSize + 1);
	
		reportLog("testCase_mms_037","短信对聊页,长按删除");
		
		deleteAllContacts();
		
		deleteAllMMs();
		
	}
	
	/**
	 * 短信对聊页,拨号
	 */
	@Test(groups={"mms"})
	public void testCase_mms_038(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信。

		prepareUnreadMMS("3","1");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		list.get(0).click();
		
		String currentPhone = getTextViewNameById("iab_sub_title");
		
		clickById("iab_ib_action");
		
		sleepTime(2000);
		//点击确定
		clickByName("确定");
		
		sleepTime(2000);

		back("tab_call");

		Assert.assertTrue("没有找到拨号记录", isExistenceById("line1"));
		
		Assert.assertTrue("拨号记录，拨打的号码不一致", getTextViewNameById("line1").equals(currentPhone));

		reportLog("testCase_mms_039","短信对聊页,拨号");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
		
	}

	
	/**
	 * 短信列表,搜索短信，搜索条件号码或内容
	 */
	@Test(groups={"mms"})
	public void testCase_mms_044(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("7", "2");
		
		createMMs(phone,"testCase_085：短信列表,搜索短信内容：是我就是我；搜索条件号码 : "+sendPhone);
		
		clickById("tab_mms");
		
		//搜索框输入搜索内容：内容中的号码
		intoContentEditTextById("contact_search_bar", sendPhone);
		
		Assert.assertTrue("没有搜索到相关的结果", isExistenceById("sms_search"));
		Assert.assertTrue("搜索结果中，不含搜索内容", getTextViewNameById("subject").contains(sendPhone));
		
		
		//搜索框输入搜索内容：发送者号码
		intoContentEditTextById("contact_search_bar", phone);
		
		Assert.assertTrue("没有搜索到相关的结果", isExistenceById("sms_search"));
		Assert.assertTrue("搜索结果中，不含搜索内容", getTextViewNameById("from").contains(phone));
	
		
		//搜索框输入搜索内容：内容前部分内容
		intoContentEditTextById("contact_search_bar", "testCase_085");
		
		Assert.assertTrue("没有搜索到相关的结果", isExistenceById("sms_search"));
		Assert.assertTrue("搜索结果中，不含搜索内容", getTextViewNameById("subject").contains("testCase_085"));
		
		
		//搜索框输入搜索内容：内容后部分的内容
		intoContentEditTextById("contact_search_bar", "是我就是我");
		
		Assert.assertTrue("没有搜索到相关的结果", isExistenceById("sms_search"));
		Assert.assertTrue("搜索结果中，不含搜索内容", getTextViewNameById("subject").contains("是我就是我"));
		
		back("tab_mms");
		
		reportLog("testCase_mms_044","短信列表,搜索短信，搜索条件号码或内容");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
		
	}
	

	/**
	 * 短信列表,加入黑名单（长按短信）
	 */
	@Test(groups={"mms"})
	public void testCase_mms_045(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("3", "1");
		
		deleteBlacklist();
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		WebElement we = list.get(0);
		
		String currentPhone =  we.getText().toString().trim();
		
		//长按第一个
		clickLongByElementUseJs(we);
		
		clickByName("加入黑名单");
		
		OpenTabMenu("防打扰" , "黑名单");

		Assert.assertTrue("不存在黑名单联系人", isExistenceByName("管理黑名单"));
		
		Assert.assertTrue("加入黑名单的联系人不一致", currentPhone.contains(getTextViewNameById("phone_name")));

		back("tab_mms");
		
		deleteBlacklist();
		
		reportLog("testCase_mms_045","短信列表,加入黑名单（长按短信）");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
		
	}
	
	
	/**
	 * 短信列表,标为已读（长按短信）
	 */
	@Test(groups={"mms"})
	public void testCase_mms_046(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("7", "4");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("unread");
		
		int size = list.size() - 1;
		//System.out.println("size= " + size);
		
		Assert.assertTrue("不存在未读短信", (size >= 0));
	
		
		
		for(int i = size; i >= 0; i--){
			//长按
			clickLongByElementUseJs(list.get(i));
			sleepTime(1000);
			
			clickByName("标记为已读");
			sleepTime(1000);
		}
		
		Assert.assertTrue("仍有未读短信 ", !isExistenceById("unread"));
			
		reportLog("testCase_mms_046","短信列表,标为已读（长按短信）");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
		
	}
	
	/**
	 * 短信列表,批量回复（长按短信）
	 */
	@Test(groups={"mms"})
	public void testCase_mms_047(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("2", "1");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		String phonestring = new String("");
		//为了方便，添加一段字符串中
		for(WebElement we : list){
			
			phonestring = phonestring + we.getText()+ ";";
		}
		
		System.out.println("查找的字符串：" + phonestring);
		
		clickLongByElementUseJs(list.get(0));
		
		//点击全选
		clickById("mca_ib_select");
		
		clickByName("批量回复");
		
		Assert.assertTrue("没有进入新信息页", isExistenceById("iab_title"));
		Assert.assertTrue("进入页面错误", getTextViewNameById("iab_title").equals("新信息"));
		
		intoContentEditTextById("embedded_text_editor", "testCase_088: 批量回复");
		
		clickById("send_button");
		
		List<WebElement> toList = getLisWebElementById("to_contact_name");
		
		for(int j = 0; j <= toList.size()-1; j++){
			Assert.assertTrue("号码中没有包含："+ toList.get(j).getText(), phonestring.contains(getNumerByText(toList.get(j).getText())+""));
		}
		
		backPage(2);
		
		reportLog("testCase_mms_047","短信列表,批量回复（长按短信）");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
		
	}
	
	/**
	 * 短信列表,删除（长按短信）
	 */
	@Test(groups={"mms"})
	public void testCase_mms_048(){
		
		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		//环境准备
		//接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("4", "1");
		
		clickById("tab_mms");
		
		List<WebElement> list = getLisWebElementById("from");
		
		clickLongByElementUseJs(list.get(0));
		
		//点击全选
		clickById("mca_ib_select");
		
		clickByName("删除");

		//确定删除
		clickById("dialog_btn_positive");
		
		sleepTime(3000);
		
		Assert.assertTrue("删除短信失败", isExistenceById("no_conversation_bigtext"));
		
		reportLog("testCase_mms_048","短信列表,删除（长按短信）");
		
		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
	}
	
	 @BeforeMethod(alwaysRun = true)
	    public void BeforeMethod() {
		 back("tab_contacts");
	 }
	
//	 @AfterMethod(alwaysRun = true)
//	    public void afterMethod() {
//		 back("tab_contacts");
//	 }
	
	
	@AfterSuite(alwaysRun=true)
	public void tearDown() throws Exception {
		driver.quit();
	}
	
//////////////////////////////////////辅助方法//////////////////////////////////////////	

	/**
	 * 根据控件ID获取整个控件的bounds范围，点击它的中奖坐标
	 * @param id
	 */
	public void clickbyWebElementByPointCenter(String id){
		System.out.println("clickbyWebElementByPointCenter: " + id);
		WebElement we = driver.findElementById(id);
		Point point = we.getLocation();
		int point_y = point.y;
		int point_x = point.x;
		int height = we.getSize().height;
		int width = we.getSize().width;
		
		int newPoint_x = point_x + width/2 ;
		int newPoint_y = point_y + height/2 ;
		
		//Point newPoint = new Point(newPoint_x,newPoint_y);
		new TouchAction(driver).tap(newPoint_x, newPoint_y).waitAction().perform();
		System.out.println("touch Point: "+ newPoint_x + ", "+ newPoint_y);
	}
	
	
////////////////////短信模块/////////////////////	
	
	/**
	 * 清除所有不相干的分组
	 */
	public void clearGroup(){
		
		intoMyGroupPage();
		
		List<WebElement> list = getLisWebElementById("txt_group_name");
		String name;
		int num = 0;
		//循环删除
		for(;;){
			num = list.size();
			//System.out.println("********************first: " + num);
			//Log.info("cbh", "********************first: " + num);
			
			for(int i = num - 1; i>=0; i--)
			{
				name = list.get(i).getText();
				//System.out.println("********************name: " +name + "; i " + i);
				//Log.info("cbh", "********************name: " +name + "; i " + i);
				if(!isContains(name)){
					//长按分组名
					clickLongByNameUseJs(name);
					//点击解散分组
					clickByName("解散分组");
					//点击解散
					clickByName("解散");
					
					sleepTime(1000);
				}
			}
			
			//再获取列表
			list = getLisWebElementById("txt_group_name");
			//System.out.println("********************second: " + list.size());
			//Log.info("cbh", "********************second: " + list.size());
			//退出标准
			if((list.size()<=5)){
				if(num == list.size()){
					break;
				}
			}
		}
		
		back("tab_contacts");
	}
	
	/**
	 * 判断字符含有字符
	 * @param name
	 * @return
	 */
	public boolean isContains(String name){
		if(name.contains("未分组") || name.contains("紧急联系人") || name.contains("家人")){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 准备未读短信,创建含多条的短信
	 */
	public void prepareUnreadMMS(String num1, String num2){
		// 创建未读短信数量
		openAppByName("SMSToolDemo");
		setSmsToolDemo(num1, num2);
		openAppByName("和通讯录");

	}
	
	/**
	 * 准备未读短信
	 */
	public int prepareUnreadMMS(){
		// 确保存在未读短信
		List<WebElement> list;
		do {
			deleteAllMMs();

			// 创建未读短信数量
			openAppByName("SMSToolDemo");
			setSmsToolDemo("7", "1");
			openAppByName("和通讯录");
			Assert.assertTrue("创建的短信数量有异常", getMMsCount() == 7);

			clickById("tab_mms");
			list = getLisWebElementById("unread");

			System.out.println("未读短信有：" + list.size());
			
		} while (list.size() <= 1);

		return list.size();
	}
	
	
	/**
	 * 取消接收新消息弹窗：true为开启；false为关闭
	 */
	public void setMessagePop(boolean bl){
		//进入短信提醒设置
		clickById("tab_mms");
		
		clickMenuAndSelect(7);
		
		Assert.assertTrue("没有进入信息设置页",isExistenceByName("信息设置"));
		
		clickById("setting_message_remind");
		
		Assert.assertTrue("没有进入短信提醒设置",isExistenceByName("短信提醒设置"));
		
		//setting_message_pop_cb
		String  result = driver.findElementById("setting_message_pop_cb").getAttribute("checked");
		String state = bl + "";
		if(result.equals(state)){
			//什么都不做
		}
		else{
			clickById("setting_message_pop_cb");
			sleepTime(1000);
		}
		
		Assert.assertTrue("设置失败",driver.findElementById("setting_message_pop_cb").getAttribute("checked").equals(state));
		
		back("tab_mms");
	}
	
	
	/**
	 * 在精选短信列表
	 * @param num
	 */
	public void selectFeaturemms(int num){
		String UiSelector = ""
				+ "new UiSelector().resourceIdMatches(\".+id/listview\").childSelector("
				+ "new UiSelector().className(\"android.widget.RelativeLayout\").index("+num+"))"
				;
		driver.findElementByAndroidUIAutomator(UiSelector).click();
		
	}
	
	/**
	 * 选择选择第几个表情（0-19），20时删除
	 * @param id
	 * @param num
	 */
	public void selectEmoticon(int num){
		String UiSelector = ""
				+ "new UiSelector().resourceIdMatches(\".+id/emoticon_viewpager\").childSelector("
				+ "new UiSelector().className(\"android.widget.GridView\").childSelector("
				+ "new UiSelector().className(\"android.widget.ImageView\").index("+num+")))"
				;
		driver.findElementByAndroidUIAutomator(UiSelector).click();
		
	}
	
	
	/**
	 * 输入时、分，设置时间。注意时间的取值。
	 * @param hour
	 * @param minute
	 */
	public void setDate(String year, String month, String date){
		//点击时间
		clickById("date_layout");
		sleepTime(2000);
		
		//设置 年
		Assert.assertTrue(isExistenceById("picker_year"));
		setValue("picker_year",year);
		
		//设置 月
		Assert.assertTrue(isExistenceById("picker_month"));
		setValue("picker_month",month);
		
		//设置 日
		Assert.assertTrue(isExistenceById("picker_date"));
		setValue("picker_date", date);
	}
	
	/**
	 * 输入时、分，设置时间。注意时间的取值。
	 * @param hour
	 * @param minute
	 */
	public void setTime(String hour, String minute){
		//点击时间
		clickById("time_layout");
		sleepTime(2000);
		
		//设置 时
		Assert.assertTrue(isExistenceById("picker_hour"));
		setValue("picker_hour",hour);
		
		//设置 分
		Assert.assertTrue(isExistenceById("picker_minute"));
		setValue("picker_minute", minute);
	}
	
	
	/**
	 * 通过控件id，判断是设置什么类型的时间。
	 * <p>设置时间（1-24）注意12或24时间。
	 * <p>设置分钟（0-59）
	 * <p>注意：没有设置异常机制，自行判断。
	 * @param time
	 */
	public void setValue(String id, String time) {
		WebElement mine;

		for(;;){
			//获取当前时间
			mine = getWebElementByAndroidUIAutomator(id, 3);
			
			if(mine.getText().equals(time)){
				return;
			}
			else{
				//点击下一个
				getWebElementByAndroidUIAutomator(id, 4).click();
				sleepTime(1000);
			}
		
		}
		
	}
	
	
	/**
	 * 通过控件ID,输入第几个，获取元素对象。
	 * <p>（用于定时发送短信，时间控件内选择时间或日期）
	 * @param id
	 * @param num
	 * @return
	 */
	public WebElement getWebElementByAndroidUIAutomator(String id, int num){
		String UiSelector = ""
				+ "new UiSelector().resourceIdMatches(\".+id/"+id+"\").childSelector("
				+ "new UiSelector().className(\"android.widget.TextView\").index("+num+"))"
				;
		WebElement we = driver.findElementByAndroidUIAutomator(UiSelector);
		return we;
	}
	
	
	
	
	/**
	 * 点击新建，选择联系人列表中，默认是第一个联系人,输入短信内容(短信内容不支持空格)
	 * @param num
	 * @param content
	 */
	public void createMMs(String content){
		clickById("tab_mms");
		
		//点击新建短信
		clickById("iab_ib_action");
		
		//点击添加收件人
		clickById("add_recipients");
		
		//验证
		Assert.assertTrue(isExistenceByName("选择收件人"));
		
		//添加1个联系人
		addMMsContactMembers(1);
				
		//输入短信内容
		intoContentEditTextById("embedded_text_editor", content);
		
		//点击确定
		clickById("send_button");
		
		sleepTime(2000);
		back("tab_mms");
		
		//去除信息回收站
		if(isExistenceById("tv_title")){
			clickById("notice_delete");
		}
		
		//向输入框输入内容
		intoContentEditTextById("contact_search_bar", content);
		
		//判断是否通过
		Assert.assertTrue(searchContactInMMs(content));
	}
	
	/**
	 * 在短信选择联系人页，选择联系人的个数（num<7）
	 * @param num
	 */
	public void addMMsContactMembers(int num){
		
		
		int members = getNumById("contact_search_bar");
		//判断联系人数量，若为0，退出。
		if(members == 0){
			//返回上一页
			backPage(1);
			return;
		}
		
		//获取单选框列表
		List<WebElement> list = getWebElementList("CheckBox", "contact_check");
		
		//若联系人数量，超于列表的数量，按照列表的数量选择
		if(num > members){
			int tmp = 1;
			//遍历控件，点击
			for(WebElement we : list){
				System.out.println("click");
				we.click();
				System.out.println("click");
				if(members == tmp){
					break;
				}
				tmp++;
			}
		}
		//若联系人数量，不超过列表数量，按照输入的数量选择
		else{
			
			int tmp = 1;
			//遍历控件，点击
			for(WebElement we : list){
				System.out.println("click");
				we.click();
				System.out.println("click");
				if(num == tmp){
					break;
				}
				tmp++;
			}
		}
		//点击添加
		clickById("selection_ok");
	}
	
	/**
	 * 在短信页，根据搜索内容获取结果
	 */
	public boolean searchContactInMMs(String contact){
		clearTextAndNote();
		//如果没有短信
		if(isExistenceById("notcontent")){
			return false;
		}
		
		if(getWebElementInList(getAllTextView(),contact) != null){
			return true;
		}else{
			return false;
		}
		
	}
	
	
	/**
	 * 短信新建，输入号码，短信内容，点击发送。（下拉新建短信）
	 * @param phone
	 * @param content
	 */
	public void createMMs(String phone, String content){
		clickById("tab_mms");
		
		//下拉
		swipeToDown();
		Assert.assertTrue(isExistenceByName("新信息"));
		
		//向输入框收入内容
		intoContentEditTextByName("收件人:", phone);
		
		//输入短信内容
		intoContentEditTextById("embedded_text_editor", content);
		
		//点击确定
		clickById("send_button");
		
		sleepTime(2000);
		back("tab_mms");
		
		//向输入框输入内容
		intoContentEditTextById("contact_search_bar", phone);
		
		//判断是否通过
		Assert.assertTrue(searchContactInMMs(phone));
		
	}
	
	
	/**
	 * 获取短信列表中，联系人图标坐标列表，空值返回null
	 * @return
	 */
	public List<Point> getMMsPoint(){
		
		List<Point> list = new ArrayList<Point>();
		
		for(WebElement webElement : getAllImageView()){
			String str = webElement.getAttribute("resourceId");
			String subStr = str.substring(str.indexOf('/') + 1);			
			if(subStr.equals("avatar")){
			//System.out.println(webElement.getAttribute("resourceId"));
				//System.out.println(webElement.getLocation());
				list.add(webElement.getLocation());
			}
		}
		//如果没有联系人
		if(list.size() == 0)
		{
			//返回null
			return null;
		}else
		{
			//否则返回列表null
			return list;
		}
	}
	
	/**
	 * 获取短信列表中的数量，通过获取头像的控件来获取数量，超于一页的联系人数量无法获取。
	 */
	public int getMMsCount(){
		//获取列表个数
		List<Point> list = getMMsPoint();
		
		//判断返回列表个数
		if(list == null)
		{
			System.out.println("getMMsCount(): " + 0);
			return 0;
		}
		else
		{
			System.out.println("getMMsCount(): " + list.size());
			return list.size();
		}
	}
	
	/**
	 * 清除联系人列表中所有的短信
	 */
	public void deleteAllMMs(){
		System.out.println("[start] deleteAllMMs");
		back("tab_mms");
		
		//如果发现无短信，马上退出
		if(isExistenceById("no_conversation_bigtext")){
			System.out.println("[ end ] deleteAllMMs");
			return;
		}
		
		for(int i = 0; i<3; i++){
			sleepTime(2000);
			//第一次获取，清除列表中异常短信	
			clearSpecialMMs(driver.findElementsById("from"));
			
			//如果发现无短信，马上退出
			if(isExistenceById("no_conversation_bigtext")){
				System.out.println("[ end ] deleteAllMMs");
				return;
			}
			
			//再次获取列表
			List<WebElement> list = driver.findElementsById("from");
			
			//获取列表长度
			int size = list.size();
			
			if(size > 1){
				//点击一个联系人，全选-删除
				clickLongByElementUseJs(list.get(0));
					
				//点击全选
				clickById("mca_ib_select");
				
				//点击删除
				clickById("mca_del");
				
				//点击确认删除
				clickById("dialog_btn_positive");
				
				
				//去除信息回收站
				if(isExistenceById("tv_title")){
					clickById("notice_delete");
				}
				sleepTime(2000);
			}else if(size == 1){
				//只有条短信
				clickLongByIdUseJs("from");
				
				//点击删除
				clickById("mca_del");
				
				//点击确认删除
				clickById("dialog_btn_positive");
				
				//去除信息回收站
				if(isExistenceById("tv_title")){
					clickById("notice_delete");
				}
				sleepTime(2000);
			}else{
				//没有联系人
				System.out.println("[ end ] deleteAllMMs");
				
				sleepTime(2000);
				return;
			}
		}
		System.out.println("[ end ] deleteAllMMs");
		sleepTime(2000);
	}
	
	/**
	 * 清除特殊类型的短信
	 */
	public void clearSpecialMMs(List<WebElement> list){
		System.out.println("[start] clearSpecialMMs");
		
		WebElement we = getWebElementInList(list, "139邮件提醒");
		if(we != null ){
			//点击元素
			we.click();
			
			Assert.assertTrue(isExistenceByName("139邮件提醒"));
			
			//清空记录
			clickMenuAndSelect(1);
			
			//点击清空
			clickById("dialog_btn_positive");
			sleepTime(15000);
			
			back("tab_mms");
		}
		
		WebElement we1 = getWebElementInList(list, "通知短信归档");
		
		if(we1 != null ){
			//点击元素
			we1.click();
			
			Assert.assertTrue(isExistenceByName("通知短信归档"));
			
			//批量删除
			clickMenuAndSelect(1);
			
			//点击更多
			clickById("mca_ib_select");
			
			//点击删除
			clickById("mca_sure");
			
			//确认删除
			clickById("dialog_btn_positive");
			
			sleepTime(30000);
				
			back("tab_mms");
		}
		
		System.out.println("[ end ] clearSpecialMMs");
	}
	

////////////////////////分组管理////////////////////////
	
	/**
	 * 选择列表中什么位置的文件(top \ mid \ bottom)
	 * @param location
	 */
	public void scrollAndSelect(String GroupName, String location){
		
		//进入我的分组
		intoMyGroupPage();
		
		//选择一分组
		clickByName(GroupName);
		
		//进入批量选择铃声
		clickMenuAndSelect(3);
		
		//弹窗铃声选择框
		Assert.assertTrue(isExistenceById("alertTitle"));
		
		
		//选择默认铃声下的第一个铃声
		if(location.equals("top"))
		{
			//使用api提供的方法，获取列表中的元素列表
			List<WebElement> list = driver.findElementsById("text1");
			list.get(1).click();
			
			sleepTime(2000);
			
			clickByName("确定");
		}
		//移动到中部，但不精准
		else if(location.equals("mid")){
			scrollToMidAndSelect();
		}
		else if(location.equals("bottom")){
			scrollToBottomAndSelect();
		}

		//返回上一页
		backPage(1);
	}
	
	/**
	 * 选择列表中间的铃声
	 */
	public void scrollToMidAndSelect(){
		List<WebElement> list;

		int num = 0;
		for(;;){
			//第一次获取列表底部文字
			list = driver.findElementsById("text1");
			String str1 = list.get(list.size() - 1).getText();
			//滑动屏幕
			swipeToUp();
			num++;
			sleepTime(1000);
			
			//第二次获取
			list = driver.findElementsById("text1");
			String str2 = list.get(list.size() - 1).getText();
			
			//如果相等，则退出
			if(str1.equals(str2)){
				break;
			}
		}
		
		num = num / 2 - 1;
		for(int i = 0; i < num; i++){
			System.out.println("swipeToDown");
			swipeToDown();
		}
		
		//点击列表中间的节点
		list = driver.findElementsById("text1");
		list.get((list.size() - 1)/2).click();;
		sleepTime(2000);
		clickByName("确定");
	}
	
	/**
	 * 移动到列表的底部，并选择铃声
	 */
	public void scrollToBottomAndSelect(){
		List<WebElement> list; 

		for(;;){
			//第一次获取列表底部文字
			list = driver.findElementsById("text1");
			String str1 = list.get(list.size() - 1).getText();
			//滑动屏幕
			swipeToUp();
			sleepTime(1000);
			//第二次获取
			list = driver.findElementsById("text1");
			String str2 = list.get(list.size() - 1).getText();
			
			//如果相等，则退出
			if(str1.equals(str2)){
				list.get(list.size() - 1).click();
				clickByName("确定");
				break;
			}
		}

	}
	
	
	/**
	 * 删除分组联系人，输入组名及删除数量
	 * @param GroupName
	 * @param num
	 */
	public void deleteGroupMembers(String GroupName, int num){
		//点击我的分组
		intoMyGroupPage();
		
		//进入分组
		clickByName(GroupName);
		
		//点击添加成员
		clickByName("移除成员");
		
		//如果移动数量为0，返回
		if(num == 0){
			//返回上一页
			backPage(1);
			return;
		}
		
		//获取搜索框内的数字
		int members = getNumById("contact_search_bar");
		//判断联系人数量，若为0，退出。
		if(members == 0){
			//返回上一页
			backPage(1);
			return;
		}
		
		//获取单选框列表
		List<WebElement> list = getWebElementList("CheckBox", "contact_check");
		
		//若联系人数量，超于列表的数量，按照列表的数量选择
		if(num > members){
			int tmp = 1;
			//遍历控件，点击
			for(WebElement we : list){
				System.out.println("click");
				we.click();
				System.out.println("click");
				if(members == tmp){
					break;
				}
				tmp++;
			}
		}
		//若联系人数量，不超过列表数量，按照输入的数量选择
		else{
			
			int tmp = 1;
			//遍历控件，点击
			for(WebElement we : list){
				System.out.println("click");
				we.click();
				System.out.println("click");
				if(num == tmp){
					break;
				}
				tmp++;
			}
		}
		//点击移除
		clickById("mca_sure");
	}
	
	
	/**
	 * 参数num为添加人数，num必须少于7（控件仅获取当前页的控件）
	 * 参数GroupName分组名
	 * @param GroupName
	 * @param num
	 */
	public void addGroupMembers(String GroupName, int num){
		//点击我的分组
		intoMyGroupPage();
		
		//进入分组
		clickByName(GroupName);
		
		//点击添加成员
		clickByName("添加成员");
		
		//获取搜索框内的数字
		int members = getNumById("contact_search_bar");
		//判断联系人数量，若为0，退出。
		if(members == 0){
			//返回上一页
			backPage(1);
			return;
		}
		
		//获取单选框列表
		List<WebElement> list = getWebElementList("CheckBox", "contact_check");
		
		//若联系人数量，超于列表的数量，按照列表的数量选择
		if(num > members){
			int tmp = 1;
			//遍历控件，点击
			for(WebElement we : list){
				System.out.println("click");
				we.click();
				System.out.println("click");
				if(members == tmp){
					break;
				}
				tmp++;
			}
		}
		//若联系人数量，不超过列表数量，按照输入的数量选择
		else{
			
			int tmp = 1;
			//遍历控件，点击
			for(WebElement we : list){
				System.out.println("click");
				we.click();
				System.out.println("click");
				if(num == tmp){
					break;
				}
				tmp++;
			}
		}
		//点击添加
		clickById("selection_ok");
	}
	
	/**
	 * 参数分别为用户名、移动方向(down or up)，移动到列表顶部或底部。
	 * 注意的是，分组列只能是少于或等于一页
	 * @param GroupName
	 * @param direction
	 */
	public void groupMoveto(String GroupName, String direction){

		//获取分组开始时的位置
		int first = getGroupNum(GroupName);
		
		//获取移动的控件图标列表
		List<WebElement> list = getWebElementList("ImageView", "drag_handle");

		//获取当前元素
		WebElement e1 = list.get(first);

		//申明TouchAciton对象
		TouchAction ta = new TouchAction(driver);

		// 判断移动方向
		if (direction.equals("down")) {

			// 向下移动
			ta.longPress(e1).moveTo(list.get(list.size() - 1)).release().perform();
			
		} 
		else if (direction.equals("up")) {

			// 向上移动
			ta.longPress(e1).moveTo(list.get(0)).release().perform();
		
		}
	}
	

	
	/**
	 * 输入组名，获取在列表中，排第几
	 * @param groupName
	 * @return
	 */
	public int getGroupNum(String groupName){
		int num = 0;
		for(WebElement webElement : getAllTextView())
		{
			String str = webElement.getAttribute("resourceId");
			String subStr = str.substring(str.indexOf('/') + 1);
			if(subStr.equals("txt_group_name")){
				
				if(webElement.getText().equals(groupName)){
					System.out.println(groupName + " getGroupNum" + num);
					return num;
				}
				num++;
			}
		}
		return -1;
	}
	
	
	/**
	 * 确保进入我的分组页
	 */
	public void intoMyGroupPage(){
		//退出
		back("tab_contacts");
		
		//进入我的分组
		if(isExistenceById("headview_item_name")){
			clickById("headview_item_name");
		}
		
		
	}
	
	/**
	 * 点击进入已有分组，移除当前列表所有联系人
	 * @param name
	 */
	public void deleteAllGroupMembers(String name){
		//点击我的分组
		intoMyGroupPage();
		
		//进入分组
		clickByName(name);
		
		//点击添加成员
		clickByName("移除成员");
		//sleepTime(10000);
		
		//获取联系人列表联系人数量
		if(getNumById("contact_search_bar") > 0){
			//点击更多按钮
			clickById("mca_ib_select");
			
			sleepTime(1000);
			//点击移除
			clickById("mca_sure");
			
			//确认移除
			clickById("dialog_btn_positive");
			
		}else{
			//返回上一页
			backPage(1);
		}
		//返回上一页
		backPage(1);
	}
	
	/**
	 * 点击进入已有分组，添加当前列表所有联系人
	 * @param name
	 */
	public void addAllGroupMembers(String name){
		//点击我的分组
		intoMyGroupPage();
		
		//进入分组
		clickByName(name);
		
		//点击添加成员
		clickByName("添加成员");
		//sleepTime(10000);
		
		//获取联系人列表联系人数量
		if(getNumById("contact_search_bar") > 0){
			//点击更多按钮
			clickById("iab_ib_more");
			//点击添加
			clickById("selection_ok");
		}else{
			//返回上一页
			backPage(1);
		}
		//返回上一页
		backPage(1);
	}

	/**
	 * 在分组页内，长按分组名-重命名，修改已有的分组，参数分别是原有的分组名，修改后的分组名
	 * @param srcName
	 * @param name
	 */
	public void renameGroup(String srcName, String name){
		//如果分组名不存在，直接返回
		if(!isExistenceByName(srcName)){
			return;
		}
		
		//如果修改的名称为空，退出
		if(name.equals("")){
			return;
		}
		
		//长按分组名
		clickLongByNameUseJs(srcName);
		
		//点击重命名
		clickByName("重命名分组");
		
		//输入修改的名称
		intoContentEditTextById("content", name);
		
		//点击保存
		clickById("dialog_btn_positive");
	
	}
	
	/**
	 * 在个别的分组内点击菜单-重命名，进入分组，参数分别是原有的分组名，修改后的分组名
	 * @param srcName
	 * @param name
	 */
	public void renameOneGroup(String srcName, String name){
		//进入我的分组
		intoMyGroupPage();
		
		//如果分组名不存在，直接返回
		if(!isExistenceByName(srcName)){
			return;
		}
		
		//如果修改的名称为空，退出
		if(name.equals("")){
			return;
		}
		
		//点击进入分组
		clickByName(srcName);
		
		//点击重命名
		clickMenuAndSelect(1);
		
		//输入修改的名称
		intoContentEditTextById("content", name);
		
		//点击保存
		clickById("dialog_btn_positive");
	
	}
	
	/**
	 * 在分组页内调用，长按删除，删除分组（先删除分组，再删除联系人）
	 */
	public void deleteGroup(String name){
		System.out.println("[ start ] deleteGroup: " + name);
		
		// 判断当前页面为我的分组
		if (getTextViewNameById("iab_title").equals("我的分组")) {
			// 什么都不做
		}
		// 不存在
		else {
			// 如果没我的分组，退出
			if (!isExistenceById("headview_item_name")) {
				System.out.println("[ end ] deleteGroup: 没有进入我的分组");
				return;
			} else {
				// 进入我的分组
				intoMyGroupPage();
			}

		}
	

		//如果分组名不存在，直接返回
		if(!isExistenceByName(name)){
			System.out.println("[ end ] deleteGroup: 分组名不存在");
			back("tab_contacts");
			return;
		}
		
		//长按分组名
		clickLongByNameUseJs(name);
		//点击解散分组
		clickByName("解散分组");
		//点击解散
		clickByName("解散");
		System.out.println("[ end ] deleteGroup: ");
		back("tab_contacts");
	}
	
	/**
	 * 进入某个分组，点击菜单选择解散分组，删除分组
	 */
	public void deleteOneGroup(String name){
		//进入我的分组
		intoMyGroupPage();
		//如果分组名不存在，直接返回
		if(!isExistenceByName(name)){
			back("tab_contacts");
			return;
		}
		
		//进入分组
		clickByName(name);
		
		//选择解散分组
		clickMenuAndSelect(2);
		
		//点击解散
		clickByName("解散");
		
		back("tab_contacts");
	}
	
	/**
	 * 创建分组，若已存在后，直接放回；不存在，新建分组。
	 * <p>state</p> 0,创建后是否保存在当前页; 1,返回主页
	 */
	public void createGroup(String name, int state){
		
		Assert.assertTrue(isExistenceById("headview_item_name"));
		if(isExistenceByName("我的分组")){
			
			//点击我的分组
			clickById("headview_item_name");
			
			Assert.assertTrue(isExistenceByName("新建分组"));
			
			//如果已存在分组名，退出
			if(isExistenceByName(name)){
				return;
			}
			
			//点击创建分组
			clickByName("新建分组");
			
			//输入分组名
			intoContentEditTextById("content", name);
			
			//点击保存
			clickById("dialog_btn_positive");
		}
		
		if(state == 0){
			return;
		}
		
		back("tab_contacts");
	}


	
	
///////////////////////联系人模块//////////////////////

	/**
	 * 去除联系人回收站
	 */
	public void contactsRecycle(){
		if(isExistenceById("notice_delete")){
			clickById("notice_delete");
		}
	}

	/**
	 * 根据坐标点，获取头像图片
	 * @param point
	 * @return
	 */
	public BufferedImage getContactHead(Point point){
		
		String savePath = "C:/Users/Administrator/workspace_appium/AppiumTest_Appium/test-output/picture/";
		String fileName = "1.jpg";
		File path = null;
		File srcFile = null;
		
		try {
			//文件路径
			path = new File(savePath+fileName);
			
			//源文件
			srcFile = driver.getScreenshotAs(OutputType.FILE);
			
			//复制文件
			FileUtils.copyFile(srcFile, path);
			
			//获取文件缓存
			BufferedImage image = getImageFromFile(srcFile);

			//截图图片大小
			return getSubImage(image, point.x, point.y, 100, 100);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 选择图片，但不稳定
	 */
	public void selectImage(){
		//点击头像
		clickById("contact_detail_header_icon_layout");
		
		//点击从图库中选择照片
		clickByName("从图库中选择照片");
		
		//选择相册
		clickByName("相册");

		//再次选择相册
		clickByName("相册");
		
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
		
        Point point = new Point(width/4, height/2);
        
        sleepTime(1000);
        this.touchScreen(point);
        
        Point point2 = new Point(width/2, height/5);
        sleepTime(1000);
        this.touchScreen(point2);
        
        sleepTime(2000);
        //点击完成
        clickByName("完成");
	}
	
	
	

	
	/**
	 * 获取联系人列表中，联系图标坐标列表，空值返回null
	 * @return
	 */
	public List<Point> getContactsPoint(){
		List<Point> list = new ArrayList<Point>();
		
		for(WebElement webElement : getAllImageView()){
			String str = webElement.getAttribute("resourceId");
			String subStr = str.substring(str.indexOf('/') + 1);			
			if(subStr.equals("contact_icon")){
			//System.out.println(webElement.getAttribute("resourceId"));
				//System.out.println(webElement.getLocation());
				list.add(webElement.getLocation());
			}

		}
		//如果没有联系人
		if(list.size() == 0)
		{
			//返回null
			return null;
		}else
		{
			//否则返回列表null
			return list;
		}
		
	}
	
	/**
	 * 获取联系人列表中的数量，通过获取头像的控件来获取数量，超于一页的联系人数量无法获取。
	 */
	public int getContactCount(){
		//获取列表个数
		List<Point> list = getContactsPoint();
		
		//判断返回列表个数
		if(list == null)
		{
			System.out.println("getContactCount(): " + 0);
			return 0;
		}
		else
		{
			System.out.println("getContactCount(): " + list.size());
			return list.size();
		}
	}
	
	/**
	 * 清空所有的联系人
	 */
	public void deleteAllContacts(){
		back("tab_contacts");
		int size = getContactCount();
		if(size > 1){
			//点击一个联系人，全选-删除
			if(isExistenceById("contact_name")){
				clickLongByIdUseJs("contact_name");
				
				//点击全选
				clickById("mca_ib_select");
				
				//点击删除
				clickById("mca_delete_layout");
				
				//点击确认删除
				clickById("dialog_btn_positive");
			}
			contactsRecycle();
		}else if(size == 1){
			//只有一个联系人
			clickLongByIdUseJs("contact_name");
			
			//点击删除
			clickById("mca_delete_layout");
			
			//点击确认删除
			clickById("dialog_btn_positive");
			
			contactsRecycle();
		}else{
			//没有联系人
			contactsRecycle();
			return;
		}
	}
	
	/**
	 * 保存简单的联系人信息核心部分
	 * @param username
	 * @param phone
	 */
	public void saveContact(String username, String phone){

		// 验证当前页面为新建联系人
		Assert.assertTrue(driver.findElement(By.name("新建联系人")).isDisplayed());

		//获取界面所有的EditView元素
		//List<WebElement> editText = driver
		//		.findElementsByClassName("android.widget.EditText");

		//第一个元素收入
		//editText.get(0).sendKeys(username);
		
		this.intoContentEditTextByName("姓名", username);
		
		
		//点击屏幕，功能缺陷
		//touchWindows();
		
		//第四个元素输入
		//editText.get(3).sendKeys(phone);

		//点击屏幕
		//touchWindows();

		intoContentEditTextByName("电话号码", phone);
		
		//点击保存
		clickById("iab_ib_action");
	}
	
	
	/**
	 * 在联系人模块，创建联系人，详细信息
	 * 
	 */
	public void createContacts() {

		String username = "陈测试";
		String phone = "13800138000";

		// 点击联系人
		clickById("tab_contacts");

		// 点击新增按钮
		clickById("iab_ib_action");

		// 验证当前页面为新建联系人
		Assert.assertTrue(driver.findElement(By.name("新建联系人")).isDisplayed());

		// 第一步先添加邮箱选项

		// 点击“添加更多资料”
		clickById("add_more_attribute");

		// 点击邮箱
		contextMenuTitleSelect(2);
		
		
		intoContentEditTextByName("姓名", username);
		
		intoContentEditTextByName("公司", "广州银泰华有限责任公司");
		
		intoContentEditTextByName("部门", "质量保证");
		
		intoContentEditTextByName("职位", "高级测试工程师");

		intoContentEditTextByName("电话号码", phone);
		
		intoContentEditTextByName("邮箱", "helloworld@164.com");
		

		// 点击保存
		clickById("iab_ib_action");

		// 休眠3秒
		sleepTime(3000);

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", phone);

		// 判断是否通过
		Assert.assertTrue(searchContact(phone, 1));

	}

	/**
	 * 创建详细联系人，添加头像
	 */
	public void createContactsAddImage() {

		String username = "加头像测试";
		String phone = "13100131000";

		// 点击联系人
		clickById("tab_contacts");

		// 点击新增按钮
		clickById("iab_ib_action");

		// 验证当前页面为新建联系人
		Assert.assertTrue(driver.findElement(By.name("新建联系人")).isDisplayed());

		// 第一步先添加邮箱选项

		// 点击“添加更多资料”
		clickById("add_more_attribute");

		// 点击邮箱
		contextMenuTitleSelect(2);
		
		intoContentEditTextByName("姓名", username);
		
		intoContentEditTextByName("公司", "广州银泰华有限责任公司");
		
		intoContentEditTextByName("部门", "质量保证");
		
		intoContentEditTextByName("职位", "高级测试工程师");

		intoContentEditTextByName("电话号码", phone);
		
		intoContentEditTextByName("邮箱", "helloworld@164.com");

		// 点击保存
		clickById("iab_ib_action");

		// 休眠3秒
		sleepTime(3000);

		// 获取默认头像

		//

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", phone);

		// 判断是否通过
		Assert.assertTrue(searchContact(phone, 1));
	}

	/**
	 * 在联系人模块，创建联系人：通过姓名、号码，新建联系人
	 * 
	 * @param username
	 * @param phone
	 */
	public void createContacts(String username, String phone) {
		// 点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		// 点击新增按钮
		clickById("iab_ib_action");

		// 保存信息
		saveContact(username, phone);

		// 休眠3秒
		sleepTime(3000);

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", phone);

		// 判断是否通过
		Assert.assertTrue(searchContact(phone, 1));

		clearTextAndNote();
	}

	/**
	 * 联系人模块，删除联系人：通过名称，删除联系人 参数name：手机号码
	 * 
	 * @param name
	 */
	public void deleteContactsByPhone(String name) {
		// 点击联系人
		clickById("tab_contacts");

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", name);

		// 判断列表是否存在该联系人
		// Assert.assertTrue(searchContact("10086" , 1));

		// 长按联系人
		// clickLongWebElement(getFirstTextView("10086"));
		clickLongByNameUseJs(name);

		// 点击删除
		clickById("mca_delete_icon");

		// 点击确定
		clickById("dialog_btn_positive");
	}

	/**
	 * 联系人模块，删除联系人：通过名称，删除联系人 参数name：手机号码
	 * 
	 * @param name
	 */
	public void deleteContactsByName(String name) {
		// 点击联系人
		clickById("tab_contacts");

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", name);

		// 判断列表是否存在该联系人
		// Assert.assertTrue(searchContact("10086" , 1));

		// 长按联系人
		// clickLongWebElement(getFirstTextView("10086"));
		clickLongByElementUseJs(searchWebElement(name));

		// 点击删除
		clickById("mca_delete_icon");

		// 点击确定
		clickById("dialog_btn_positive");
	}

	/**
	 * 点击屏幕
	 */
	public void touchWindows() {
		int x = driver.manage().window().getSize().width;
		int y = driver.manage().window().getSize().height;
		TouchAction ta = new TouchAction(driver);
		ta.press(x - 1, y - 1).waitAction().perform();
		ta.press(x - 1, y - 1).waitAction().perform();
		System.out.println("touch Windows");
	}
	
	

/////////////拨号模块/////////////////////////////////	
	
	/**
	 * 隐藏拨号盘的输入盘
	 */
	public void hidekeyboardCall(){
		if(isExistenceById("two")){
			//true为显示键盘，点击键盘隐藏
			clickById("tab_call");
		}
	}
	
	/**
	 * 显示拨号盘的输入键盘
	 */
	public void displaykeyboardCall(){
		if(isExistenceById("two"))
		{
				
		}
		else
		{
			//flase为显示键盘，点击键盘显示
			clickById("tab_call");
		}
		
	}
	
	/**
	 * 获取字符串数组 2到9
	 * @return
	 */
	public ArrayList<String> getNumberList(){
		ArrayList<String> strArray = new ArrayList<String> ();
		strArray.add("two");
		strArray.add("three");
		strArray.add("four");
		strArray.add("five");
		strArray.add("six");
		strArray.add("seven");
		strArray.add("eight");
		strArray.add("nine");
		return strArray;
	}
	/*
	 * 创建测试数据集(num2 - num9),用于一键
	 */
	public void createDate(String num, String phone){
		
		deleteAllContacts();
		
		// 创建
		for(int i = 2; i < 10; i++){
			createContacts("num" + i, phone + i);
		}
		
	}
	
	/**
	 * 创建测试数据，num是联系人数量
	 * @param num
	 */
	public void createTestDate(int num){
		if(num <= 0){
			return;
		}
		else if(num < 10){
			//1-9个联系人
			for(int i = 1; i < num; i++){
				createContacts("TestDate_" + i, "1352211203" + i);
			}
		}else{
			//10个以上
			for(int i = 1; i <= num; i++){
				if(i <= 9){
					createContacts("TestDate_0" + i, "1352211203" + i);
				}
				else{
					createContacts("TestDate_" + i, "135221120" + i);
				}
				
			}
		}
		
	}
	

	
	/**
	 * 点击屏幕，其他坐标添加偏移值，容易触发事件
	 * @param point
	 */
	public void touchScreen(Point point){
		System.out.println("[start] touchScreen");
		System.out.println("point" + point.toString());
		new TouchAction(driver).tap(point.x + 5, point.y + 5).waitAction().perform();
		System.out.println("[ end ] touchScreen");
	}
	
	
	/**
	 * 这个方法必须是在未设置快捷键时运行。否则获取不全或为空。
	 * @return
	 */
	public List<Point> getPointList(){
		
		//切换到拨号页
		clickById("tab_call");
		
		//选择一键拨号
		clickMenuAndSelect(2);
		
		//清空所有已设置按钮
		deleteAllOneCall();
		
		List<Point> list = new ArrayList<Point>();
		
		for(WebElement webElement : getAllImageView()){
			if(getAttributeResourceId(webElement) == 1){
				//System.out.println("point " + webElement.getLocation().toString());
				list.add(webElement.getLocation());
			}
		}
		back("tab_call");
		
		return list;
	}
	
	
	/**
	 * 清除一键拨号页所有已设置的按键,其中算法有问题，添加一个计数器，加快清理
	 */
	public void deleteAllOneCall(){
		int i = 1;
		for(WebElement we : getAllImageView()){
			//清除已设置的快捷键
			if(getAttributeResourceId(we) == 2){
				i++;
				//
				we.click();
				//清除
				contextMenuTitleSelect(3);
			}
			if(i>8)
			{
				break;
			}
		}
	}
	
	/**
	 * 判断当前元素是否设置为一键拨号
	 * <p>返回 1 为未设置快捷;</p> <p> 返回2 为已设置；</p>返回0为都不符合
	 * @param WebElement
	 * @return
	 */
	public int getAttributeResourceId(WebElement we){
		System.out.println("[start] getAttributeResourceId");
		String str = we.getAttribute("resourceId");
		String str1 = str.substring(str.indexOf('/') + 1);
		//未设置的快捷键
		if(str1.equals("nofastcontactpic")){
			System.out.println("[end] getAttributeResourceId nofastcontactpic");
			return 1;
		}
		//已经设置
		else if(str1.equals("fastcontactpic")){
			System.out.println("[end] getAttributeResourceId nofastcontactpic");
			return 2;
		}
		//都不符合
		System.out.println("[end] getAttributeResourceId null");
		return 0;
	}
	
	
	/**
	 * 清空黑名单管理中的内容
	 */
	public void deleteBlacklist(){
		//进入管理黑名单
		OpenTabMenu("防打扰" , "黑名单");
		
		//判断清空按钮是否存在
		if(isExistenceById("iab_ib_action")){
			//点击清空
			clickById("iab_ib_action");
				
			//点击清空
			clickById("dialog_btn_positive");
		}
		
		//返回主界面
		back("tab_contact");
	}

	
	/**
	 * 进入防打扰设置
	 */
	public void OpenTabMenu(String tab1, String tab2) {
		// 点击和通讯录
		clickById("iab_title");

		sleepTime(1000);

		// 点击防打扰
		clickByName(tab1);

		// 检测当前界面为防打扰页
		Assert.assertTrue(driver.findElementByName(tab1).isDisplayed());

		// 点击更多
		clickById("iab_ib_more");

		sleepTime(1000);

		// 点击黑名单
		clickByName(tab2);

	}
	
	/**
	 * 清空通话记录
	 */
	public void deleteAllCall(){
		//点击拨号
		clickById("tab_call");
		
		//隐藏输入盘
		this.hidekeyboardCall();
		
		//点击清空通话记录
		clickMenuAndSelect(1);
		
		sleepTime(1000);
		
		//点击清空按钮
		clickById("dialog_btn_positive");
		
	}
	
	/**
	 * 清理拨号记录
	 */
	public void deleteCall(String phone){
		//拨号
		//this.callNumber("13813881499");
		
		//sleepTime(8000);
		
		clickById("tab_call");
		
		if(searchContact("13813881499", 0)){
			//长按记录
			clickLongByNameUseJs("13813881499");
			
			//点击删除
			clickByName("删除");
			
			//确认删除
			clickByName("删除");
		}

	}
	
	
	/**
	 * 拨号盘点击号码
	 * @param str
	 */
	public void touchCallNumber(String str){
		int len = str.length();
		int i;
		for(i = 0; i< len; i++){
			digitsChangeName(Integer.parseInt(str.charAt(i) + "") );
		}
	}

	/**
	 * 仅用于拨号盘点击数字
	 * @param chr
	 */
	public void digitsChangeName(int chr){
		switch(chr)
		{
		case 1:
			clickById("one");
			break;
		case 2:
			clickById("two");
			break;
		case 3:
			clickById("three");
			break;
		case 4:
			clickById("four");
			break;
		case 5:
			clickById("five");
			break;
		case 6:
			clickById("six");
			break;
		case 7:
			clickById("seven");
			break;
		case 8:
			clickById("eight");
			break;
			
		case 9:
			clickById("nine");
			break;
			
		case 0:
			clickById("zero");
			break;
		}
	}

	/**
	 * 拨号：拨打号码（在断网情况下使用）
	 */
	public void callNumber(String number){
		//点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		//点击拨号
		clickById("tab_call");

		//点击输入框
		clickById("digits");

		//点击键盘数字
		touchCallNumber(number);
		
		//点击拨号
		clickById("tab_dialer");
		
		//点击点击确定
		clickByName("确定");
		
	}
	


///////////////////登录模块/////////////////////////////	
	
	
	/**
	 * 是否在登录状态，false为未登录状态，true为已登录
	 * @param username
	 * @return
	 */
	public boolean isLoginState(String username) {
		try {
			// 验证第二元素是否包含"登录"
			WebElement el = driver.findElementsByClassName(
					"android.widget.TextView").get(1);
			String str = el.getText();
			Boolean bl = str.contains(username);
			System.out.println("isLoginState: " + bl);
			return bl;
		} catch (Exception ex) {
			// 找不到元素
		}
		System.out.println("isLoginState: false");
		return false;
	}
	
	/**
	 * 登录：输入用户、密码，登录
	 * @param username
	 * @param password
	 */
	public void Logout(String username) {

		//点击和通讯录
		clickById("iab_title");	

		//点击设置
		clickById("setting_layout");

		// 判断是否为已登录，若已登录状态，直接返回
		if (isLoginState(username)) {
			// 点击退出
			clickById("setting_item_login_logout_text");

			// 点击确认
			clickById("dialog_btn_positive");
		}

		// 验证第二元素是否包含"登录"
		Assert.assertTrue(driver
				.findElementsByClassName("android.widget.TextView").get(1)
				.getText().contains("登录"));

		// 退出登录页
		driver.sendKeyEvent(AndroidKeyCode.BACK);

		sleepTime(1000);
		//返回主界面
		driver.sendKeyEvent(AndroidKeyCode.BACK);

		sleepTime(5000);
	}

	
	/**
	 * 输入用户、密码，登录
	 * @param username
	 * @param password
	 */
	public void Login(String username, String password) {
		
		//点击和通讯录
		clickById("iab_title");	

		//点击设置
		clickById("setting_layout");
		
		// System.out.println("isLoginState() " + isLoginState());
		// 判断是否为登录状态，若未登录状态，进行下一步；否则返回
		if (!isLoginState(username)) {
			//
			sleepTime(3000);

			// 点击登录
			clickById("setting_item_login");

			// 点击互联网登录
			clickById("btn_login_dynamic");

			// 输入用户名
			intoContentEditTextById("setting_new_login_mobile_et_num", username);

			// 输入密码
			intoContentEditTextById("setting_new_login_mobile_et_password",password);

			// 点击登录
			clickById("setting_new_login_mobile_btn_login");

			// 等待登录时间(根据网络状态)
			sleepTime(10000);
		}

		// 验证第二元素是否包含登录号码
		Assert.assertTrue(driver
				.findElementsByClassName("android.widget.TextView").get(1)
				.getText().contains(username));

		// 退出登录页
		driver.sendKeyEvent(AndroidKeyCode.BACK);

		sleepTime(1000);
		//返回主界面
		driver.sendKeyEvent(AndroidKeyCode.BACK);
		sleepTime(1000);

	}
	
	
	
	
	/////////////////////////////公共模块///////////////////
	
	/**
	 * 短信生成器，输入两个参数，分别是会话条数，信息数量。
	 * @param cscnt
	 * @param msscnt
	 */
	public void setSmsToolDemo(String cscnt, String msscnt){
		
			//输入数字
			intoContentEditTextByName("input conversation count", cscnt);
			intoContentEditTextByName("input messages count", msscnt);
			//点击创建
			clickByName("create conversation");
			
			//清除
			intoContentEditTextByName(cscnt, "");
			intoContentEditTextByName(msscnt, "");
			
			Assert.assertTrue("还原不了默认内容，下次运行可能出错", isExistenceByName("input conversation count")&&isExistenceByName("input messages count"));

	}
	
	/**
	 * 该方法内不可使用控件的id（不同包名），只能用name
	 */
	public void openAppByName(String name){
		
		driver.sendKeyEvent(AndroidKeyCode.HOME);
		sleepTime(2000);
		
		clickByName("应用程序");
		sleepTime(2000);
		for(;;){
			if(isExistenceByName(name))
			{
				break;
			}
			swipeToRight();
		}
		
		clickByName(name);
		
		if(name.equals("SMSToolDemo")){
			String tmp_activity ="com.er.zjj.demo.SendSmsToolsDemo.SmsToolDemo";
			Assert.assertTrue("没有进入短信生成器",driver.currentActivity().equals(tmp_activity));
		}
		else if(name.equals("和通讯录")){
			
			clickById("tab_mms");
			sleepTime(1000);
			Assert.assertTrue("没有进入和通讯录主页",driver.currentActivity().equals(".Main"));
		}
		sleepTime(2000);
	}
	

	/**
	 * 报告输入用到
	 * @param name
	 * @param log
	 */
	public void reportLog(String name, String log){
		Reporter.log(name + " 测试通过；用例（功能点）描述: " + log);
	}
	
	/**
	 * 判断是否通过验证，如果不通过结束并截图
	 * @param bl
	 * @param caseName
	 */
	public void Myassert(boolean bl, String caseName){
		if(bl == false){
			ChinaMoblie_testNg.snapshot(driver, caseName);
		}
		sleepTime(3000);
		Assert.assertTrue(bl);
	}
	
	/**
	 * 判断是否通过验证，如果不通过结束并截图,并添加具体问题描述
	 * @param bl
	 * @param caseName
	 */
	public void Myassert(String message, boolean bl, String caseName){
		if(bl == false){
			ChinaMoblie_testNg.snapshot(driver, caseName);
		}
		sleepTime(3000);
		Assert.assertTrue(message, bl);
	}
	
	/**
	 * 清除搜索记录和搜索框内容
	 */
	public void clearTextAndNote(){
		if(isExistenceById("contact_search_del_btn")){
			clickById("contact_search_del_btn");
		}
		if(isExistenceById("clean_button")){
			clickById("clean_button");
		}
		if(isExistenceById("contact_search_cancel_btn")){
			clickById("contact_search_cancel_btn");
		}
	}
	
	/**
	 * 点击多少次返回主界面
	 * @param num
	 */
	public void backPage(int num){
		// 返回主界面
		for(int i = 0; i< num; i++){
			driver.sendKeyEvent(AndroidKeyCode.BACK);
			sleepTime(1000);
		}
	}
	
	//返回
	public void back(String name){
		System.out.println("[start] back");
		for(;;){
			if(isExistenceById("dialog_btn_positive")){
				//屏幕中出现确定等按钮
				clickById("dialog_btn_positive");
			}
			//确认页面是否有回退“按钮”
			else if(isExistenceById("iab_back"))
			{
				driver.sendKeyEvent(AndroidKeyCode.BACK);
				sleepTime(1000);
				System.out.println("[ doing ] back");
			}
			else{
				//点击两次
				//第一次点击返回
				clickById("tab_call");
				//第二次返回选中页
				clickById(name);
				
				clearTextAndNote();
				
				System.out.println("[ end ] back");
				sleepTime(3000);
				return;
			}
			sleepTime(1000);
			
		}
	}
		
	
///////////////////////////////通用基础方法////////////////////////////////////////////////////
	

	/**
	 * 点击控件ID，粘贴已有的内容
	 */
	public void pasteString(String id){
		clickById(id);
		//粘贴
		//使用3.1版本：AndroidKeyCode才有定义具体静态常量
		driver.sendKeyEvent(AndroidKeyCode.KEYCODE_V, AndroidKeyCode.META_CTRL_MASK);
		//driver.sendKeyEvent(50, 28672);//1.3版本
	}
	
	
	/**
	 * 输入元素列表，查找的字段
	 * @param list
	 * @param search
	 * @return
	 */
	public boolean searListContainName(List<WebElement> list, String search){
		//如果等于空或者null返回false
		if((list.isEmpty()) || list == null){
			return false;
		}
		
		//找到返回true
		for(WebElement we : list){
			if(we.getText().contains(search))
			{
				return true;
			}
		}
		
		//找不到返回false
		return false;
	}
	
	
	/**
	 * 根据控件ID获取 元素列表
	 * @param id
	 * @return
	 */
	public List<WebElement> getLisWebElementById(String id){
		List<WebElement> list = driver.findElementsById(id);
		return list;
	}

	
	/**
	 * 返回列表元素，根据列表的顶部，或底部
	 * location: start\end
	 * @param id
	 * @param location
	 * @return
	 */
	public WebElement getWebElementInList(String id, String location){
		
		//获取元素列表
		List<WebElement> list = driver.findElementsById(id);
		//获取列表长度
		int size = list.size();
		
		WebElement we = null;
		
		if(location.equals("start")){
			System.out.println("getWebElementInList size: " + size);
			we = list.get(0);
		}
		else if(location.equals("end")){
			System.out.println("getWebElementInList size: " + size);
			we = list.get(size - 1);
		}
		else{
			System.out.println("getWebElementInList size: " + size);
			we = null;
		}
		
		return we;
	}
	
	
	/**
	 * 对列表元素遍历，搜索“含有” content字段的元素。（使用contains方法）
	 * @param list
	 * @param content
	 * @return
	 */
	public WebElement getWebElementInList(List<WebElement> list, String content){
		System.out.println("[start] getWebElementInList");
		//如果为空，返回
		if(list == null){
			System.out.println("[ end ] getWebElementInList is null");
			return null;
		}
		for(WebElement we : list){
			System.out.println("text " + we.getText());
			if(we.getText().contains(content)){
				System.out.println("[ end ] getWebElementInList not null");
				return we;
			}
		}
		System.out.println("[ end ] getWebElementInList is null");
		return null;
	}
	
	
	/**
	 * 根据控件ID获取元素列表，sytle分别是(Image、TextView、CheckBox)类型。
	 * @param id
	 * @param sytle
	 * @return
	 */
	public List<WebElement> getWebElementList(String sytle, String id) {
		System.out.println("[ start ] getWebElementList");
		List<WebElement> weList = new ArrayList<WebElement>();
		//获取Image类型，且匹配的元素
		if (sytle.equals("ImageView")) {
			for (WebElement webElement : getAllImageView()) {
				String str = webElement.getAttribute("resourceId");
				String subStr = str.substring(str.indexOf('/') + 1);
				if (subStr.equals(id)) {
					weList.add(webElement);
				}
			}
			System.out.println("[ return ] ImageView size: " + weList.size());
		}//获取TextView类型，且匹配的元素 
		else if (sytle.equals("TextView")) {
			for (WebElement webElement : getAllTextView()) {
				String str = webElement.getAttribute("resourceId");
				String subStr = str.substring(str.indexOf('/') + 1);
				if (subStr.equals(id)) {
					weList.add(webElement);
				}
			}
			System.out.println("[ return ] TextView size: " + weList.size());
		}
		//获取CheckBox类型，且匹配的元素
		else if(sytle.equals("CheckBox")){
			for (WebElement webElement : getAllCheckBox()) {
				String str = webElement.getAttribute("resourceId");
				String subStr = str.substring(str.indexOf('/') + 1);
				if (subStr.equals(id)) {
					weList.add(webElement);
				}
			}
			System.out.println("[ return ] CheckBox size: " + weList.size());
		}
		//若不匹配类型，返回空列表
		else{
			weList = null;
		}
		System.out.println("[ end ] getWebElementList");
		return weList;
	}
	
	
	/**
	 * 根据文字，获取其中的数字
	 */
	public int getNumerByText(String txt){
		//提取数字
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(txt);
		//发生异常时，返回-1
		int num = 0;
		try{
			//将获取数据转为int类型
			num =  Integer.parseInt(m.replaceAll("").trim());
		}catch(Exception ex){
			
			num = -1;
			//System.out.println("");
		}
		System.out.println("getNumerByText: " + num);

		return num;
		
	}
	
	/**
	 * 根据控件的ID，获取控件文字中的数字
	 * @param id
	 * @return
	 */
	public int getNumById(String id){
		//获取元素ID
		WebElement we = driver.findElementById(id);
		
		//获取控件text
		String str = we.getAttribute("text");
		
		//提取数字
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(str);
		
		//将获取数据转为int类型
		int num =  Integer.parseInt(m.replaceAll("").trim());
		System.out.println("getNumById: " + num);
		return num;
	}
	
	/**
	 * 手势向左滑动
	 */
	public void swipeToLeft(){
		int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 500);
        System.out.println("[ doing ] swipeToLeft ");
	}
	
	/**
	 * 手势向右滑动
	 */
	public void swipeToRight(){
		int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width / 4, height / 2, width * 3 / 4, height / 2, 500);
        System.out.println("[ doing ] swipeToRight ");
	}
	
	/**
	 * 手势向右滑动,添加水平高度变量（即点击屏幕高、中、低，向右滑）
	 * location选项为high\mid\bottom
	 */
	public void swipeToLeft(String location){
		int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        if(location.equals("bottom"))
        {
        	driver.swipe(width * 3/ 4, height * 3 / 4, width / 4, height * 3 / 4, 500);
            System.out.println("[ doing ] swipeToLeft ");
        }
        else if(location.equals("mid")){
        	driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 500);
            System.out.println("[ doing ] swipeToLeft ");
        }
		else if(location.equals("high")){
			driver.swipe(width * 3 / 4, height / 4, width / 4, height / 4, 500);
            System.out.println("[ doing ] swipeToLeft ");
		}
		else {
			System.out.println("[ error ] location null ");
		}
        
	}
	
	
	/**
	 * 手势向右滑动,添加水平高度变量（即点击屏幕高、中、低，向右滑）
	 * location选项为high\mid\bottom
	 */
	public void swipeToRight(String location){
		int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        if(location.equals("bottom"))
        {
        	driver.swipe(width / 4, height * 3 / 4, width * 3 / 4, height * 3 / 4, 500);
            System.out.println("[ doing ] swipeToRight ");
        }
        else if(location.equals("mid")){
        	driver.swipe(width / 4, height / 2, width * 3 / 4, height / 2, 500);
            System.out.println("[ doing ] swipeToRight ");
        }
		else if(location.equals("high")){
			driver.swipe(width / 4, height / 4, width * 3 / 4, height / 4, 500);
            System.out.println("[ doing ] swipeToRight ");
		}
		else {
			System.out.println("[ error ] location null ");
		}
        
	}
	
	/**
	 * 手势向下滑动
	 */
	public void swipeToDown() {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width / 2, height / 4, width / 2, height * 3 / 4, 1000);
        // wait for page loading
    }
	 
	/**
     * 
     * 手势向上滑动
     */
    public void swipeToUp() {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width / 2, height * 3 / 4, width / 2, height / 4, 1000);
        // wait for page loading
    }

    
	/**
	 * 判断某个元素是否存在,true存在；false不存在。
	 */
	public boolean isExistenceById(String id){
		System.out.println("isExistenceById: " + id );
		try {
			driver.findElementById(id);
			System.out.println("isExistenceById: true");
			return true;
		} catch (Exception ex) {
			// 找不到元素
		}
		System.out.println("isExistenceById: false");
		return false;
	}
	
	/**
	 * 在指定时间内，判断元素是否存在，true存在；false不存在。
	 */
	public boolean isExistenceById(String id, int timeout){
		try {
			driver.findElementById(id);
			new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(id)));
			System.out.println("isExistenceById: true");
			return true;
		} catch (Exception ex) {
			// 找不到元素
		}
		System.out.println("isExistenceById: false");
		return false;
	}
	/**
	 * 判断某个元素是否存在,true存在；false不存在。
	 */
	public boolean isExistenceByName(String name){
		System.out.println("isExistenceByName: " + name);
		try {
			driver.findElementByName(name);
			System.out.println("isExistenceByName: true");
			return true;
		} catch (Exception ex) {
			// 找不到元素
		}
		System.out.println("isExistenceByName: false");
		return false;
	}
	
	
	/**
	 * 实现按钮长按，传参为name。（该类方法不建议使用，容易报错）
	 * @param name
	 */
	public void clickLongByName(String name) {
		try {
			TouchAction ta = new TouchAction(driver);
			WebElement el = driver.findElementByName("name");
			//WebElement el = driver.findElementById(packageName + ":id/" + name);
			ta.longPress(el).waitAction(5000).release().perform();
		} catch (Exception e) {
			System.out.println("clickLongTime error.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 实现按钮长按，传参为WebElement（该类方法不建议使用，容易报错）
	 * @param el
	 */
	public void clickLongByWebElement(WebElement el) {
		try {
			TouchAction ta = new TouchAction(driver);
			ta.longPress(el).waitAction(2000).release().perform();
		} catch (Exception e) {
			System.out.println("clickLongTime error.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 调用JS脚本实现长按，参数是WebElement元素
	 * @param element
	 */
	public void clickLongByElementUseJs(WebElement element){
		System.out.println("[start] clickLongByElementUseJs");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String , String> tapObject = new HashMap<String , String>();
		tapObject.put("element", ((RemoteWebElement)element).getId());
		js.executeScript("mobile: longClick", tapObject);
		System.out.println("[ end ] clickLongByElementUseJs");
		
	}
	
	
	/**
	 * 调用JS脚本实现长按，参数是txt
	 * @param TextViewName
	 */
	public void clickLongByNameUseJs(String TextViewName){
		System.out.println("[start] clickLongByNameUseJs " + TextViewName);
		//这里容易出问题，查找控件经常出错
		WebElement element = getFirstTextView(TextViewName , 1);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String , String> tapObject = new HashMap<String , String>();
		tapObject.put("element", ((RemoteWebElement)element).getId());
		js.executeScript("mobile: longClick", tapObject);
		System.out.println("[ end ] clickLongByNameUseJs");
		
	}
	
	/**
	 * 调用JS脚本实现长按，参数是txt
	 * @param TextView id
	 */
	public void clickLongByIdUseJs(String id){
		System.out.println("[start] clickLongByIdUseJs");
		WebElement element = driver.findElementById(id);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String , String> tapObject = new HashMap<String , String>();
		tapObject.put("element", ((RemoteWebElement)element).getId());
		js.executeScript("mobile: longClick", tapObject);
		System.out.println("[ end ] clickLongByIdUseJs");
	
	}

	/**
	 * 通过控件的id，点击控件
	 * @param id
	 */
	public void clickById(String id) {
		try {
			System.out.println("[start] click: " + id);
			driver.findElement(By.id(id)).click();
			System.out.println("[ end ] " + id + ".click");
		} catch (Exception ex) {
			System.out.println("Can not find " + id);
			// ex.printStackTrace();
		}
	}

	/**
	 * 通过控件的txt，点击控件
	 * @param name
	 */
	public void clickByName(String name) {
		try {
			sleepTime(1000);
			System.out.println("[start] click: " + name);
			driver.findElement(By.name(name)).click();
			System.out.println("[ end ] " + name + ".click");
		} catch (Exception ex) {
			System.out.println("Can not find " + name);
			// ex.printStackTrace();
		}
	}
	

	
	/**
	 * 清空输入框内容
	 * @param text
	 */
	public void clearText(String text) {
		System.out.println("[start] Clear Text " + text);
		if (text == null) {
			return;
		}

		//光标移动到末尾
		driver.sendKeyEvent(123);

		//逐一删除内容
		for (int i = 0; i < text.length(); i++) {
			driver.sendKeyEvent(AndroidKeyCode.DEL);
		}
		System.out.println("[ end ] Clear Text finish ");
	}
	
	/**
	 * 获取当前界面上，所有的textView，运行比较慢；
	 * 注意：若列表过长不建议使用，容易出现找不到元素，返回null
	 * @return
	 */
	public List<WebElement> getAllTextView(){
		System.out.println("find All TextView");
		List<WebElement> lis = driver.findElementsByClassName("android.widget.TextView");
		return lis;
	}
	
	/**
	 * 获取当前界面上，所有的CheckBox，运行比较慢；
	 * 注意：若列表过长不建议使用，容易出现找不到元素，返回null
	 * @return
	 */
	public List<WebElement> getAllCheckBox(){
		System.out.println("find All CheckBox");
		List<WebElement> lis = driver.findElementsByClassName("android.widget.CheckBox");
		return lis;
	}
	
	/**
	 * 获取当前界面上，所有的ImageView，运行比较慢；
	 * 注意：若列表过长不建议使用，容易出现找不到元素，返回null
	 * @return
	 */
	public List<WebElement> getAllImageView(){
		System.out.println("find All ImageView");
		List<WebElement> lis = driver.findElementsByClassName("android.widget.ImageView");
		return lis;
	}
	/**
	 * 对上面的获取所有TextView进行优化，获取返回元素的，第二元素
	 * <p>调整index可以获取第一元素：默认是1、其他情况为0</p>
	 * 返回时元素对象
	 * <p>name：查找的字符串</p>
	 * @return
	 */
	public WebElement getFirstTextView(String name, int index){
		System.out.println("[start] getFirstTextView");
		for(WebElement el : getElementsByClassAndIndex("android.widget.TextView", index)){
			System.out.println("getFirstTextView " + el.getText().toString());
			if(el.getText().toString().equals(name)){
				System.out.println("[ end ] getFirstTextView");
				return el;
			}
		}
		System.out.println("[ end ] getFirstTextView");
		return null;
	}
	
	/**
	 * 通过使用UiSelector，获取当前列表某个元素，比较高效。返回List<WebElement>
	 * @param classname
	 * @param index
	 * @return
	 */
	public List<WebElement> getElementsByClassAndIndex(String classname,int index){
		List<WebElement> lis =null;
		System.out.println("[start] findElementsByAndroidUIAutomator");
		lis = driver.findElementsByAndroidUIAutomator("new UiSelector().className("+classname+").index("+index+")");
		System.out.println("[ end ] findElementsByAndroidUIAutomator");
		return lis;
		}
	
	/**
	 * 获取点击菜单，选择某个选项
	 * @param num
	 */
	public void clickMenuAndSelect(int num){
		//点击菜单按钮
		driver.sendKeyEvent(AndroidKeyCode.MENU);
		//pop_navi_text 菜单的listView id
		menuList("pop_navi_text", num);

	}

	
	/**
	 * 输入控件ID，获取点击菜单，选择某个选项
	 * @param num
	 */
	public void clickMenuAndSelect(String id, int num){
		//点击菜单按钮
		driver.sendKeyEvent(AndroidKeyCode.MENU);
		//pop_navi_text 菜单的listView id
		menuList(id, num);

	}
	
	/**
	 * 获取更多选项，点击菜单选项
	 * @param num
	 */
	public void contextMenuTitleSelect(int num){
		//context_menu_title更多选项中的ListView id
		menuList("context_menu_title", num);
	}
	
	/**
	 * 获取点击菜单选项
	 * @param id
	 * @param num
	 */
	public void menuList(String id, int num){
		//获取列表所有元素
		List<WebElement> menulist = driver.findElementsById(id);
		int len = menulist.size();
		System.out.println("len " + len);
		if(num >=1 && num <= len){
			//点击菜单中的选项
			WebElement tmpel = menulist.get(num-1);
			System.out.println("menuList" + tmpel.getText());
			tmpel.click();
			//menulist.get(num-1).click();
		}
		sleepTime(2000);
	}

	/**
	 * 向输入框收入内容
	 * @param id
	 * @param content
	 */
	public void intoContentEditTextById(String id, String content) {

		try {
			
			System.out.println("[start] into name: " + id);

			WebElement e = driver.findElementById(id);
			e.click();
			//清除搜索记录和清空内容
			clearTextAndNote();
			
			String text = e.getAttribute("text");
			clearText(text);
			e.sendKeys(content);
			// e.submit(); //用户提交内容，一般不用
			System.out.println("[ end ] into content: " + content);
		} catch (Exception ex) {
			System.out.println("[ error ]can not find " + id);
			ex.printStackTrace();
		}
	}
	
	/**
	 * 向输入框收入内容，若该控件没有ID，只有name，可以使用该方面
	 * @param name
	 * @param content
	 */
	public void intoContentEditTextByName(String name, String content) {

		try {
			
			System.out.println("[start] into name: " + name);

			WebElement e = driver.findElementByName(name);
			e.click();
			//清除搜索记录和清空内容
			clearTextAndNote();
			
			String text = e.getAttribute("text");
			clearText(text);
			e.sendKeys(content);
			// e.submit(); //用户提交内容，一般不用
			System.out.println("[ end ] into content: " + content);
		} catch (Exception ex) {
			System.out.println("[ error ]can not find " + name);
			ex.printStackTrace();
		}
	}
	/**	
	 * 通过控件ID，获取输入框内容
	 * @param name
	 * @return
	 */
	public String getEditTextContent(String name) {

		try {
			System.out.println("[start] get EditText name: " + name);
			WebElement PhoneContent = driver.findElementById(name);
			PhoneContent.click();
			String text = PhoneContent.getAttribute("text");
			System.out.println("[ end ] get EditText text: " + text);
			return text;
		} catch (Exception ex) {
			System.out.println("can not find " + name);
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * 这方法适用于查找联系人，不通用。
	 * 判断页面是否存在TextView name的元素
	 * mode等于1的时候，使用简单模式；等于0的时候，使用获取所有TextView元素
	 * 使用简单模式容易找不到内容，注意使用。
	 * @param search
	 * @param mode
	 * @return
	 */
	public boolean searchContact(String search , int mode){
		System.out.println("[start] searchContact");
		
		//mode 等于1的时候，使用简单模式；等于0的时候，使用获取所有TextView元素
		if(mode == 1){
			//搜索所有的textView，找到匹配到联系人
			WebElement el = getFirstTextView(search ,1);
			if(el.getText().equals(search)){
				System.out.println("getFirstTextView search text: " + el.getText());
				//找到符合的显示true
				System.out.println("[ end ] searchContact");
				return true;
			}
			//没有找到返回false
			System.out.println("[ end ] searchContact");
			return false;
			
		}else if (mode == 0){
				// 搜索所有的textView，找到匹配到联系人
				for (WebElement el : getAllTextView()) {
					if (el.getText().equals(search)) {
						System.out.println("getAllTextView " + el.getText());
						System.out.println("[ end ] searchContact");
						// 找到符合的显示true
						return true;
					}
			}
			// 没有找到返回false
			System.out.println("[ end ] searchContact");
			return false;

		}
		System.out.println("[ end ] searchContact");
		return false;
	}
	
	/**
	 * 通过textView name 查找元素，返回找到WebElement对象
	 * @param search
	 * @return
	 */
	public WebElement searchWebElement(String search){
		
		//搜索所有的textView，找到匹配到联系人
		for(WebElement el : getAllTextView()){
			if(el.getText().equals(search)){
				System.out.println("find text: " + el.getText());
				//找到符合的显示true
				return el;
			}
		}
		//没有找到返回null
		return null;
	}
	
	
	/**
	 * 根据控件ID获取该控件的text
	 * @param id
	 * @return
	 */
	public String getTextViewNameById(String id){
		try {
			System.out.println("[start] get TextView id: " + id);
			String text = driver.findElementById(id).getAttribute("text");
			System.out.println("[ end ] get TextView id: " + text);
			return text;
		} catch (Exception ex) {
			System.out.println("can not find " + id);
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 休眠 1000 = 1秒
	 * @param num
	 */
	public void sleepTime(int num) {
		try {
			System.out.println("Wait time: " + num / 1000 + "s, doing something...");
			Thread.sleep(num);
		} catch (Exception e) {
			System.out.println("[ error ] Time out");

		}
	}

	/**
	 * //此方法为屏幕截图
	 * @param drivername
	 * @param filename
	 */
	public static void snapshot(AndroidDriver<WebElement> driver, String testCasename) {
		// 获取当前工作路径
		String currentPath = System.getProperty("user.dir"); 
		
		//利用 TakesScreenshot接口提供的 getScreenshotAs()方法捕捉屏幕,会将获取到的截图存放到一个临时文件中														
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		//保存路径
		String filePath = currentPath +imagePath;
		
		//文件名
		String filename =testCasename +"_"+ getCurrentDateTime() +".jpg";
		
		try {
			System.out.println("save snapshot path is:" + currentPath + "/test-output/pic/"
					+ filename);
			//将临时文件拷贝到当前工作路径
			FileUtils.copyFile(scrFile, new File(filePath + filename));
		} catch (IOException e) {
			System.out.println("Can't save screenshot");
			e.printStackTrace();
		} finally {
			System.out.println("screen shot finished, it's in " + filePath
					+ " folder");
		}
	}
	
	public static String getCurrentDateTime(){
		   SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");//设置日期格式
		   return df.format(new Date());
		}
	
	
	///获取截图，并对比
	public static boolean sameAs(BufferedImage myImage,
			BufferedImage otherImage, double percent) {
		// BufferedImage otherImage = other.getBufferedImage();
		// BufferedImage myImage = getBufferedImage();
		if (otherImage.getWidth() != myImage.getWidth()) {
			return false;
		}
		if (otherImage.getHeight() != myImage.getHeight()) {
			return false;
		}
		//int[] otherPixel = new int[1];
		//int[] myPixel = new int[1];
		int width = myImage.getWidth();
		int height = myImage.getHeight();
		int numDiffPixels = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (myImage.getRGB(x, y) != otherImage.getRGB(x, y)) {
					numDiffPixels++;
				}
			}
		}
		double numberPixels = height * width;
		double diffPercent = numDiffPixels / numberPixels;
		return percent <= 1.0D - diffPercent;
	}

	public static BufferedImage getSubImage(BufferedImage image, int x, int y,
			int w, int h) {
		return image.getSubimage(x, y, w, h);
	}
	
	/**
	 * 获取文件
	 * @param f
	 * @return
	 */
	public static BufferedImage getImageFromFile(File f) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch (IOException e) {
			// if failed, then copy it to local path for later check:TBD
			// FileUtils.copyFile(f, new File(p1));
			e.printStackTrace();
			System.exit(1);
		}
		return img;
	}
	
/////////////////////其他模块//////////////////////////////	
	/**
	 * 图片保存，保存模块，用于学习
	 */
	public void testDebug(){
		//createContacts("testCase_cmp", "13522132032");
		sleepTime(3000);
		String savePath = "C:/Users/Administrator/workspace_appium/AppiumTest_Appium/test-output/picture/";
		String fileName = "1.jpg";
		File path = new File(savePath+fileName);
		File srcFile = driver.getScreenshotAs(OutputType.FILE);
		
		try {
			FileUtils.copyFile(srcFile, path);
			BufferedImage img = getImageFromFile(srcFile);
			//截图图片大小
			BufferedImage subImg1 = getSubImage(img, 20, 427, 120 - 20, 527 - 427);
			//截图图片大小[20,793][120,893]
			BufferedImage subImg2 = getSubImage(img, 20, 793, 120 - 20, 893 - 793);
			
			Boolean same = sameAs(subImg1, subImg2, 0.9);
			
			//Assert.assertTrue(same); 
			
			File f3 = new File(savePath + "sub-1.png");
			ImageIO.write(subImg1, "PNG", f3);
			
			File f4 = new File(savePath + "sub-2.png");
			ImageIO.write(subImg2, "PNG", f4);
			
			Assert.assertTrue(same);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
