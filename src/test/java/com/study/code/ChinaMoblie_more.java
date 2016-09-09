package com.study.code;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

public class ChinaMoblie_more {

	static AndroidDriver<WebElement> driver;
	private String packageName = "com.chinamobile.contacts.im";
	private String appActivity = ".Main";

	private String phone = "13427665104";
	private String email = "13427665104@163.com";
	private String passname = "321832750"; // 互联网账号
	private String loginPwd = "yscs12345";
	private String sendPhone = "13427632604";
	private static String imagePath = "";
	private static String myport = "";

	@BeforeSuite(alwaysRun = true)
	@Parameters({ "port", "udid", "ver" })
	public void setUp(String port, String udid, String ver) throws Exception {

		System.out.println("current use port: " + port + ", devices udid: "
				+ udid + " android verstion: " + ver);
		// String udid = "0bd08bcc";
		// 设置自动化相关参数
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");

		capabilities.setCapability("udid", udid);

		capabilities.setCapability("deviceName", "");

		// 设置安卓系统版本
		capabilities.setCapability("platformVersion", ver);

		// 设置app的主包名和主类名
		capabilities.setCapability("appPackage", packageName);
		capabilities.setCapability("appActivity", appActivity);

		// 支持中文输入，会自动安装Unicode 输入法。默认值为 false
		capabilities.setCapability("unicodeKeyboard", "True");

		// 在设定了 unicodeKeyboard 关键字的 Unicode 测试结束后，重置输入法到原有状态
		capabilities.setCapability("resetKeyboard", "True");

		// 初始化
		URL url = new URL("http://127.0.0.1:" + port + "/wd/hub");
		driver = new AndroidDriver<WebElement>(url, capabilities);
		
		AppUtil.driver = driver;
		
		System.out.println("setup running");

		imagePath = "\\Pic_output\\" + port + "\\" +AppUtil.getCurrentDate() +"\\";
		AppUtil.imagePath = imagePath;
		System.out.println("errorImage path: " + imagePath);

		myport = port;
		AppUtil.myport = port;
		System.out.println("MyTestCaseResult: port is " + myport + " setup() start");
		sleepTime(5000);
		
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() throws Exception {
		System.out.println("MyTestCaseResult: port is " + myport + " tearDown() end");
		AppUtil.driver.quit();
	}

	/**
	 * case1: 使用已注册的号码，在注册页面进行注册。
	 */
	public void testCase_webview_000() {
		startTestCase();
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

		reportLog("RegisteredPhone", "使用已注册的号码，在注册页面进行注册");
	}

	/**
	 * 登录验证：手机号码登录 已注册的手机号，正确密码登录
	 */
	@Test(groups = { "login" })
	public void testCase_login_001() {
		startTestCase();
		Login(phone, loginPwd);
		Logout(phone);
		reportLog("已注册的手机号，正确密码登录");
	}

	/**
	 * 登录验证：使用手机号码、互联网通行证、邮箱地址登录 手机号码：13427665104 互联网 ： 321832750
	 * 邮箱地址：13427665104@163.com 不同类型账号登录
	 */
	@Test(groups = { "login" })
	public void testCase_login_002() {
		startTestCase();
		// 手机号码登录
		Login(phone, loginPwd);
		Logout(phone);

		// 互联网通行证登录
		Login(passname, loginPwd);
		Logout(passname);

		// 邮箱地址登录
		Login(email, loginPwd);
		Logout(phone);

		reportLog("使用手机号码、互联网通行证、邮箱地址登录");
	}

	/**
	 * 异常登录测试使用不同的账号和密码，不停进行错误等
	 * 验证锁定IP登录（同一网络IP，使用不同账号登录50次以上，失败了超过50%，在该IP下5分钟内无法登录）
	 */
	@Test(groups = { "login_longtime" })
	public void testCase_login_003() {
		startTestCase();
		// 点击和通讯录
		clickById("iab_title");

		// 点击设置
		clickById("setting_layout");

		String username = "1381013801";
		String password = "138001";

		for (int i = 0; i < 62; i++) {
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
			intoContentEditTextById("setting_new_login_mobile_et_num", username
					+ i);

			// 输入密码
			intoContentEditTextById("setting_new_login_mobile_et_password",
					password + i);

			// 点击登录
			clickById("setting_new_login_mobile_btn_login");

			// 等待登录时间(根据网络状态)
			sleepTime(3000);

		}

		// 用正确密码登录进行验证，验证结果应该登录失败

		// 输入用户名
		intoContentEditTextById("setting_new_login_mobile_et_num", this.phone);

		// 输入密码
		intoContentEditTextById("setting_new_login_mobile_et_password",
				this.loginPwd);

		// 点击登录
		clickById("setting_new_login_mobile_btn_login");

		// 等待登录时间(根据网络状态)
		sleepTime(10000);

		// 验证未没登录状态
		// Assert.assertTrue(!this.isLoginState(this.phone));
		Myassert("使用正确的密码登录成功，但需求应为登录失败", !this.isLoginState(this.phone));

		// 返回主界面
		back("tab_call");

		reportLog("验证锁定IP登录（同一网络IP，使用不同账号登录50次以上，失败了超过50%，在该IP下5分钟内无法登录）");
	}

	//------------------------拨号模块测试用------------------------------------//
	
	
	/**
	 * 通话记录-陌生人-新建联系人 拨号盘中，创建联系人,输入号码，添加为联系人，并保存
	 */
	@Test(groups = { "call" })
	public void testCase_call_001() {
		startTestCase();
		setCallShockAndVoice(false);
		// 清理
		deleteAllContacts();
		deleteAllCall();
		back("tab_call");

		// 点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		// 点击拨号
		clickById("tab_call");

		// 点击输入框
		clickById("digits");

		// 点击键盘数字
		// tab_dialer 拨号
		// action_sms 发短信
		touchCallNumber("13822138001");

		// 验证拨号页没有改号码
		// Assert.assertTrue(driver.findElementByName("添加为联系人").isDisplayed());
		// 环境准备
		if (!getElementsByClassAndIndex("android.widget.TextView", 0).equals(
				"saveAsContact")) {

			sleepTime(2000);
			// 点击存在的联系人
			clickById("call_detail");
			// 点击菜单，选择删除
			clickMenuAndSelect(2);

			// 点击确认
			clickByName("删除");
		}

		// 点击添加为联系人
		clickByName("添加为联系人");

		// 添加新建联系人
		clickByName("新建联系人");

		// 判断当前页面为新建联系人
		Myassert("没有进入新建联系人页", driver.findElementByName("新建联系人").isDisplayed());

		// 获取界面所有的EditView元素
		List<WebElement> editText = driver
				.findElementsByClassName("android.widget.EditText");

		// 第一个元素收入
		editText.get(0).sendKeys("saveAsContact");

		// 点击屏幕，功能缺陷
		touchWindows();

		// 点击保存
		clickById("iab_ib_action");

		sleepTime(2000);

		// 判读联系是否被创建
		// Assert.assertTrue(this.searchContact("saveAsContact", 0));
		Myassert("联系人没有被创建：saveAsContact", searchContact("saveAsContact", 0));
		sleepTime(2000);

		// 清理联系人
		// this.deleteContactsByPhone("13822138001");
		deleteAllContacts();
		deleteAllCall();
		reportLog("拨号盘中，创建联系人,输入号码，添加为联系人，并保存");
	}

	/**
	 * 通话记录-陌生人-添加到已有联系人 添加为已有联系人
	 */
	@Test(groups = { "call" })
	public void testCase_call_002() {
		startTestCase();
		// 清理
		deleteAllContacts();
		deleteAllCall();
		back("tab_call");

		// 添加测试数据
		this.createContacts("saveContacts", "13824452646");

		// 点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		// 点击拨号
		clickById("tab_call");

		// 点击输入框
		clickById("digits");

		// 点击键盘数字
		touchCallNumber("84850922");

		// 点击添加为联系人
		clickByName("添加为联系人");

		// 添加新建联系人
		clickByName("添加到已有联系人");

		// 判断当前页面为新建联系人
		Myassert("没有进入新建联系人", driver.findElementByName("新建联系人").isDisplayed());

		intoContentEditTextById("contact_search_bar", "13824452646");

		searchWebElement("saveContacts").click();

		Myassert("没有进入编辑联系人", driver.findElementByName("编辑联系人").isDisplayed());

		// 点击保存
		clickById("iab_ib_action");

		sleepTime(2000);

		// deleteContactsByName("saveContacts");
		// 清理
		deleteAllContacts();
		deleteAllCall();
		reportLog("通话记录-陌生人-添加到已有联系人");
	}

	/**
	 * 拨号盘 -搜索本地联系人（按拼音或号码搜索） 本地联系人检测
	 */
	@Test(groups = { "call" })
	public void testCase_call_003() {
		startTestCase();
		// boolean bl;
		// 清理
		deleteAllContacts();
		deleteAllCall();
		back("tab_call");

		// 创建本地联系人
		createContacts("通讯录", "13800138000");

		// 点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		// 点击拨号
		clickById("tab_call");

		// 点击输入框
		clickById("digits");

		// 点击键盘数字
		touchCallNumber("138001");

		// bl = this.searchContact("通讯录", 0);
		// Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0));

		// 清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");

		// 点击键盘数字
		touchCallNumber("001380");

		// bl = this.searchContact("通讯录", 0);
		// Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0));

		// 清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");

		// 点击键盘数字
		touchCallNumber("138000");

		// bl = this.searchContact("通讯录", 0);
		// Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0));

		// 清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");

		// 点击键盘数字,tongx转化为键盘数字为86649
		touchCallNumber("86649");

		// bl = this.searchContact("通讯录", 0);
		// Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0));

		// 清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");

		// 点击键盘数字,txl转化为键盘数字为895
		touchCallNumber("895");

		// bl = this.searchContact("通讯录", 0);
		// Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0));

		// 清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");

		// 点击键盘数字,xunlu转化为键盘数字为98658
		touchCallNumber("98658");

		// bl = this.searchContact("通讯录", 0);
		// Assert.assertTrue(bl);
		Myassert("搜索失败，没有找到该联系人：通讯录", searchContact("通讯录", 0));

		// 清空输入框内容
		this.clickLongByIdUseJs("btn_backspace");

		sleepTime(3000);

		// 删除联系人
		// deleteContactsByPhone("13800138000");
		deleteAllContacts();

		reportLog("拨号盘 -搜索本地联系人（按拼音或号码搜索）");
	}

	/**
	 * 将拨号记录中一号码添加到黑名单 通话记录-陌生人-加入黑名单（详细页，选择加入黑名单）
	 */
	@Test(groups = { "call" })
	public void testCase_call_004() {
		startTestCase();
		// 清理
		deleteAllContacts();

		back("tab_call");

		// 进入管理黑名单
		OpenTabMenu("防打扰", "黑名单");

		// 搜索当前页面是否含有该号码记录
		if (searchContact("13813881499", 0)) {
			// 点击清空
			clickById("iab_ib_action");

			// 点击清空
			clickByName("清空");
		}

		// 返回主界面
		back("tab_call");

		// 清空所有通话记录
		deleteAllCall();

		// 添加数据
		callNumber("13813881499");

		// 点击拨号详细记录
		clickById("call_detail");

		// 点击加入黑名单
		clickMenuAndSelect(4);

		// 返回主界面
		driver.sendKeyEvent(AndroidKeyCode.BACK);
		sleepTime(1000);

		// 进入管理黑名单
		OpenTabMenu("防打扰", "黑名单");

		// 检测当前界面为防打扰页
		Myassert("没有进入黑名单管理页", driver.findElementByName("管理黑名单").isDisplayed());

		boolean bl = searchContact("13813881499", 0);

		// 检测当前是否存储骚扰电话
		// Assert.assertTrue(bl);
		Myassert("黑名单中，没有找到该联系人：13813881499", bl);

		sleepTime(1000);

		// 清空数据
		if (bl) {
			// 点击清空
			clickById("iab_ib_action");

			// 点击清空
			clickById("dialog_btn_positive");
		}
		// 返回主界面
		back("tab_call");

		// 清空所有通话记录
		deleteAllCall();

		reportLog("通话记录-陌生人-加入黑名单（详细页，选择加入黑名单）");
	}

	/**
	 * 1标记为广告推销且勾选加入黑名单 通话记录-陌生人-标记
	 */
	@Test(groups = { "call" })
	public void testCase_call_005() {
		startTestCase();
		back("tab_call");
		// 清空所有通话记录
		deleteAllCall();

		// 添加数据
		callNumber("13813881423");

		// 点击拨号详细记录
		clickById("call_detail");

		// 点击标记
		clickMenuAndSelect(3);

		// 广告推销
		clickByName("广告推销");

		// 同时加入黑名单
		clickByName("同时加入黑名单");

		// 点击确定
		clickByName("确定");

		sleepTime(3000);

		// 验证号码是否标记
		// Assert.assertTrue(getTextViewNameById("call_stranger_detail_company").contains("广告推销"));
		boolean b0 = getTextViewNameById("call_stranger_detail_company")
				.contains("广告推销");
		Myassert("没有标记", b0);

		// 返回主界面
		back("tab_call");

		// 进入管理黑名单
		OpenTabMenu("防打扰", "黑名单");

		// 检测当前界面为防打扰页
		Myassert("没有进入黑名单管理页", driver.findElementByName("管理黑名单").isDisplayed());

		boolean bl = searchContact("13813881423", 0);

		// 检测当前是否存储骚扰电话
		// Assert.assertTrue(bl);
		Myassert("黑名单中，没有找到该联系人：13813881423", bl);

		sleepTime(1000);

		// 清空数据
		if (bl) {
			// 点击清空
			clickById("iab_ib_action");

			// 点击清空
			clickById("dialog_btn_positive");
		}
		// 返回主界面
		back("tab_call");

		// 点击拨号详细记录
		clickById("call_detail");

		// 点击取消标记
		clickMenuAndSelect(3);

		// 返回主界面
		back("tab_call");

		// 清空记录
		deleteAllCall();

		reportLog("通话记录-陌生人-标记");
	}

	/**
	 * 长按号码，将其添加到黑名单 拨号 - 更多操作- 加入黑名单（长按记录，选择加入黑名单）
	 */
	@Test(groups = { "call" })
	public void testCase_call_006() {
		startTestCase();
		back("tab_call");
		// 清空黑名单管理内容
		deleteBlacklist();

		// 清空所有通话记录
		deleteAllCall();

		// 添加数据
		callNumber("13813881423");

		// 长按号码
		this.clickLongByElementUseJs(this.searchWebElement("13813881423"));

		// 点击加入黑名单按钮
		clickById("mca_msg_txt");

		// 长按号码
		this.clickLongByElementUseJs(this.searchWebElement("13813881423"));

		sleepTime(1000);

		// 验证
		// Assert.assertTrue(getTextViewNameById("mca_msg_txt").contains("取消黑名单"));
		Myassert("取消黑名单失败", getTextViewNameById("mca_msg_txt")
				.contains("取消黑名单"));

		// 点击加入取消黑名单
		clickById("mca_msg_txt");

		// 清空所有通话记录
		deleteAllCall();

		reportLog("拨号 - 更多操作- 加入黑名单（长按记录，选择加入黑名单）");
	}

	/**
	 * 添加IP拨号，选择17951 拨号 - 更多操作 - IP拨号-选择已有的前缀
	 */
	@Test(groups = { "call" })
	public void testCase_call_007() {
		startTestCase();
		back("tab_call");
		// 清空所有通话记录
		deleteAllCall();

		// 添加数据
		callNumber("13813881420");

		// 长按号码
		this.clickLongByElementUseJs(this.searchWebElement("13813881420"));

		// 点击IP拨号
		clickById("mca_call_txt");

		// 判断是否存在多个自定义前缀
		if (isExistenceById("del_voip_phone")) {
			// 点击清除
			clickById("del_voip_phone");

			// 点击删除
			clickByName("删除");

			// 长按号码
			this.clickLongByElementUseJs(this.searchWebElement("13813881420"));

			// 点击IP拨号
			clickById("mca_call_txt");
		}

		// 在弹窗选择
		menuList("add_text", 1);

		sleepTime(3000);
		// 点击点击确定
		clickByName("确定");

		// Assert.assertTrue(getTextViewNameById("line1").contains("17951"));
		Myassert("没有找到拨号记录中，含有IP号码：17951", getTextViewNameById("line1")
				.contains("17951"));

		// 清空所有通话记录
		deleteAllCall();

		reportLog("拨号 - 更多操作 - IP拨号-选择已有的前缀");
	}

	/**
	 * 添加IP拨号，添加自定义前缀 拨号 - 更多操作 - IP拨号-选择手动添加的前缀
	 */
	@Test(groups = { "call" })
	public void testCase_call_008() {
		startTestCase();
		back("tab_call");
		String tmpNum = "13813771420";
		// 清空所有通话记录
		deleteAllCall();

		// 添加数据
		callNumber(tmpNum);

		// 长按号码
		this.clickLongByElementUseJs(this.searchWebElement(tmpNum));

		// 点击IP拨号
		clickById("mca_call_txt");

		// 判断是否存在多个自定义前缀
		if (isExistenceById("del_voip_phone")) {
			// 点击清除
			clickById("del_voip_phone");

			// 点击删除
			clickByName("删除");

			// 长按号码
			this.clickLongByElementUseJs(this.searchWebElement(tmpNum));

			// 点击IP拨号
			clickById("mca_call_txt");

		}

		// 点击添加前缀号码
		clickById("add_voip_phone");

		// 输入内容
		this.intoContentEditTextById("content", "138438");

		// 点击确定
		clickByName("确定");

		sleepTime(3000);

		// 点击点击确定
		clickByName("确定");

		// Assert.assertTrue(getTextViewNameById("line1").contains("138438"));
		Myassert("没有找到拨号记录中，含有IP号码：138438", getTextViewNameById("line1")
				.contains("138438"));

		reportLog("拨号 - 更多操作 - IP拨号-选择手动添加的前缀");
	}

	/**
	 * 拨打本地联系人号码 拨号- 拨打本地联系人
	 */
	@Test(groups = { "call" })
	public void testCase_call_009() {
		startTestCase();
		deleteAllContacts();
		back("tab_call");

		// 准备数据，创建联系人
		this.createContacts("testCase_call_009", "13504168016");

		// 点击拨号
		displaykeyboardCall();

		// 点击输入框
		clickById("digits");

		// 点击键盘数字
		touchCallNumber("13504");

		// 点击搜索记录，
		clickById("listview_rl");

		sleepTime(2000);
		// 点击确定
		clickByName("确定");

		sleepTime(2000);

		// Assert.assertTrue(this.searchContact("testCase016", 0));
		Myassert("没有找到测试数据：testCase_call_009",
				searchContact("testCase_call_009", 0));

		// 清空拨号记录
		this.deleteAllCall();

		// 删除联系人
		// this.deleteContactsByName("testCase016");
		deleteAllContacts();
		reportLog("拨号- 拨打本地联系人");
	}

	/**
	 * 本地联系人通话记录，加入白名单 拨号 - 加入白名单
	 */
	@Test(groups = { "call" })
	public void testCase_call_010() {
		startTestCase();

		deleteAllContacts();
		back("tab_call");

		this.createContacts("testCase_call_010", "13533168167");

		// 点击显示拨号盘
		displaykeyboardCall();

		// 点击输入框
		clickById("digits");

		// 点击键盘数字
		touchCallNumber("13533");

		// 点击搜索记录，
		clickById("listview_rl");

		sleepTime(2000);
		// 点击确定
		clickByName("确定");

		sleepTime(2000);

		// 点击拨号详细记录
		clickById("call_detail");

		// 点击加入白名单
		clickMenuAndSelect(4);

		// 返回主界面
		back("tab_call");

		// 进入管理白名单
		OpenTabMenu("防打扰", "白名单");

		// 检测当前界面为防打扰页
		Myassert("没有进入白名单管理页", driver.findElementByName("管理白名单").isDisplayed());

		boolean bl = searchContact("13533168167", 0);

		// 检测当前是否存储骚扰电话
		// Assert.assertTrue(bl);
		this.Myassert("没有找到联系人：13533168167", bl);

		sleepTime(1000);

		// 清空数据
		if (bl) {
			// 点击清空
			clickById("iab_ib_action");

			// 点击清空
			clickById("dialog_btn_positive");
		}
		// 返回主界面
		back("tab_call");

		// this.deleteContactsByName("testCase017");
		deleteAllContacts();
		deleteAllCall();

		reportLog("拨号 - 加入白名单");
	}

	/**
	 * 拨号修改编辑 通话记录-本地联系人-编辑
	 */
	@Test(groups = { "call" })
	public void testCase_call_011() {
		startTestCase();
		deleteAllContacts();
		back("tab_call");

		this.createContacts("testCase_call_011", "13511168169");

		// 点击拨号
		displaykeyboardCall();

		// 点击输入框
		clickById("digits");

		// 点击键盘数字
		touchCallNumber("135111");

		// 点击搜索记录，
		clickById("listview_rl");

		sleepTime(2000);
		// 点击确定
		clickByName("确定");

		sleepTime(2000);

		// 点击拨号详细记录
		clickById("call_detail");

		// 点击编辑
		clickById("iab_ib_action");

		// 获取界面所有的EditView元素
		List<WebElement> editText = driver
				.findElementsByClassName("android.widget.EditText");

		// clearText(editText.get(4).getAttribute("text"));
		editText.get(4).sendKeys("13522168199");

		// 点击保存
		clickById("iab_ib_action");

		sleepTime(3000);

		// 返回拨号页
		back("tab_call");

		displaykeyboardCall();

		// 点击输入框
		clickById("digits");

		// 点击键盘数字
		touchCallNumber("13522168199");

		// Assert.assertTrue(this.searchContact("testCase018", 0));
		Myassert("没有找到联系人：testCase018", searchContact("testCase_call_011", 0));

		clickById("tab_contacts");

		// this.deleteContactsByName("testCase018");
		deleteAllContacts();
		// 进入拨号
		clickById("tab_call");

		// 清除记录
		this.deleteAllCall();

		reportLog("通话记录-本地联系人-编辑");
	}

	/**
	 * 拨号页，删除联系人 通话记录-本地联系人-删除联系人
	 */
	@Test(groups = { "call" })
	public void testCase_call_012() {
		startTestCase();

		deleteAllContacts();
		back("tab_call");
		this.createContacts("testCase_call_012", "13500168169");

		displaykeyboardCall();

		// 点击输入框
		clickById("digits");

		// 点击键盘数字
		touchCallNumber("13500");

		// 点击搜索记录，
		clickById("listview_rl");

		sleepTime(2000);
		// 点击确定
		clickByName("确定");

		sleepTime(2000);

		// 点击拨号详细记录
		clickById("call_detail");

		// 点击删除联系人
		clickMenuAndSelect(2);

		// 点击删除
		clickByName("删除");

		// 切换到联系人页
		clickById("tab_contacts");

		// 搜索当前页面
		// Assert.assertTrue(!this.searchContact("13500168169", 0));
		Myassert("联系人删除失败：13500168169", !this.searchContact("13500168169", 0));
		// 进入拨号
		clickById("tab_call");

		// 清除记录
		this.deleteAllCall();

		reportLog("通话记录-本地联系人-删除联系人");
	}

	/**
	 * 一键拨号-设置快捷拨号-从联系人列表选择
	 */
	@Test(groups = { "call","fixed" })
	public void testCase_call_013() {
		startTestCase();
		back("tab_call");
		String name = "num";
		String phone = "1353316816";
		// 创建数据
		createDate(name, phone);

		// 已经包含清除
		List<Point> poList = getPointList();

		// 切换到拨号页
		clickById("tab_call");

		// 选择一键拨号
		clickMenuAndSelect(2);

		// 验证当前页
		Myassert("当前页面不在一键拨号设置页", driver.findElementByName("一键拨号设置")
				.isDisplayed());

		int i = 0;
		for (Point point : poList) {
			sleepTime(3000);
			i++;
			// 不执行第一个元素
			if (i == 1) {
				continue;
			}

			// 点击屏幕
			touchScreen(point);

			// 从联系人列表获得
			contextMenuTitleSelect(1);

			// 在搜索框内输入
			intoContentEditTextById("contact_search_bar", name + i);

			// 选中联系人
			searchWebElement(name + i).click();

			// 点击添加
			clickById("selection_ok");

			Myassert("没有找到该联系人：" + (name + i),
					driver.findElementByName(name + i).isDisplayed());

		}

		// 清除一键拨号设置
		deleteAllOneCall();

		back("tab_call");

		// 清除数据
		deleteAllContacts();
		reportLog("一键拨号-设置快捷拨号-从联系人列表选择");
	}

	/**
	 * 一键拨号-设置快捷拨号-键盘输入
	 */
	@Test(groups = { "call","fixed" })
	public void testCase_call_014() {
		startTestCase();
		back("tab_call");
		String name = "num";
		String phone = "1353316816";
		// 创建数据
		createDate(name, phone);

		// 已经包含清除
		List<Point> poList = getPointList();

		// 切换到拨号页
		clickById("tab_call");

		// 选择一键拨号
		clickMenuAndSelect(2);

		// 验证当前页
		Myassert("没有进入一键拨号设置页", driver.findElementByName("一键拨号设置")
				.isDisplayed());

		int i = 0;
		for (Point point : poList) {
			sleepTime(3000);
			i++;
			// 不执行第一个元素
			if (i == 1) {
				continue;
			}

			// 点击屏幕
			touchScreen(point);

			// 从联系人列表获得
			contextMenuTitleSelect(2);

			// 在搜索框内输入
			intoContentEditTextById("content", phone + i);

			// 点击添加
			clickById("dialog_btn_positive");

			Myassert("没有找到该联系人：" + (name + i),
					driver.findElementByName(name + i).isDisplayed());

		}

		// 清除一键拨号设置
		deleteAllOneCall();

		back("tab_call");

		// 清除数据
		deleteAllContacts();

		reportLog("一键拨号-设置快捷拨号-键盘输入");

	}

	/**
	 * 一键拨号-拨打快捷拨号
	 */
	@Test(groups = { "call","fixed" })
	public void testCase_call_015() {
		startTestCase();
		back("tab_call");
		String name = "num";
		String phone = "1353316816";

		// 创建数据
		createDate(name, phone);

		// 已经包含清除
		List<Point> poList = getPointList();
		ArrayList<String> strArray = getNumberList();

		// 切换到拨号页
		clickById("tab_call");

		// 选择一键拨号
		clickMenuAndSelect(2);

		// 验证当前页
		// Assert.assertTrue(driver.findElementByName("一键拨号设置").isDisplayed());
		Myassert("没有进入一键拨号设置页", driver.findElementByName("一键拨号设置")
				.isDisplayed());

		int i = 0;
		for (Point point : poList) {
			sleepTime(3000);
			i++;
			// 不执行第一个元素
			if (i == 1) {
				continue;
			}

			// 点击屏幕
			touchScreen(point);

			// 从联系人列表获得
			contextMenuTitleSelect(2);

			// 在搜索框内输入
			intoContentEditTextById("content", phone + i);

			// 点击添加
			clickById("dialog_btn_positive");

			// Assert.assertTrue(driver.findElementByName(name +
			// i).isDisplayed());

			Myassert("没有找到该联系人：" + (name + i),
					driver.findElementByName(name + i).isDisplayed());
		}
		back("tab_call");

		int j;
		for (j = 0; j < 8; j++) {

			displaykeyboardCall();
			// 长按菜单
			clickLongByIdUseJs(strArray.get(j));

			sleepTime(2000);

			clickByName("确定");

			// Assert.assertTrue(searchContact("num"+(j+2), 0));
			Myassert("没有找到联系人：" + "num" + (j + 2),
					searchContact("num" + (j + 2), 0));
			deleteAllCall();

		}

		// 清除一键拨号设置
		deleteAllOneCall();

		back("tab_call");

		// 清除数据
		deleteAllContacts();

		reportLog("一键拨号-拨打快捷拨号");
	}

	/**
	 * 一键拨号-未设置-手动输入
	 */
	@Test(groups = { "call","fixed" })
	public void testCase_call_016() {
		startTestCase();
		back("tab_call");
		String name = "num";
		String phone = "1353316816";

		// 创建数据
		createDate(name, phone);

		// 切换到拨号页
		clickById("tab_call");

		// 选择一键拨号
		clickMenuAndSelect(2);

		// 清空所有已设置按钮
		deleteAllOneCall();
		ArrayList<String> strArray = getNumberList();

		back("tab_call");

		for (int i = 0; i < 8; i++) {

			displaykeyboardCall();

			// 长按数字按钮
			clickLongByIdUseJs(strArray.get(i));

			// 验证弹窗
			// Assert.assertTrue(driver.findElementByName("温馨提示").isDisplayed());
			this.Myassert("没有弹出提示", driver.findElementByName("温馨提示")
					.isDisplayed());
			// 点击确认
			clickByName("是");

			// 手动输入
			contextMenuTitleSelect(2);

			// 输入框添加号码
			intoContentEditTextById("content", phone + (i + 2));

			// 点击添加
			clickById("dialog_btn_positive");

			// Assert.assertTrue(searchContact("num"+(i+2), 0));
			this.Myassert("没有找到测试数据：" + "num" + (i + 2),
					searchContact("num" + (i + 2), 0));

			// 返回
			back("tab_call");
		}

		// 清除数据
		deleteAllContacts();
		reportLog("一键拨号-未设置-手动输入");
	}

	/**
	 * 一键拨号-未设置-从联系人列表获得
	 */
	@Test(groups = { "call","fixed" })
	public void testCase_call_017() {
		startTestCase();
		back("tab_call");
		String name = "num";
		String phone = "1353316816";

		// 创建数据
		createDate(name, phone);

		// 切换到拨号页
		clickById("tab_call");

		// 选择一键拨号
		clickMenuAndSelect(2);

		// 清空所有已设置按钮
		deleteAllOneCall();
		ArrayList<String> strArray = getNumberList();

		back("tab_call");

		for (int i = 0; i < 8; i++) {
			// 显示
			displaykeyboardCall();

			// 长按数字按钮
			clickLongByIdUseJs(strArray.get(i));

			// 验证弹窗
			Myassert("没有弹出提示", driver.findElementByName("温馨提示").isDisplayed());

			// 点击确认
			clickByName("是");

			// 手动输入
			contextMenuTitleSelect(1);

			// 在搜索框内输入
			intoContentEditTextById("contact_search_bar", name + (i + 2));

			// 选中联系人
			searchWebElement(name + (i + 2)).click();

			// 点击添加
			clickById("selection_ok");

			Myassert("没有找到测试数据：" + (name + (i + 2)),
					driver.findElementByName(name + (i + 2)).isDisplayed());

			// 返回
			back("tab_call");
		}

		// 清除数据
		deleteAllContacts();
		reportLog("一键拨号-未设置-从联系人列表获得");
	}

	
	/**
	 * 通话记录-陌生人(发短信)
	 */
	@Test(groups = { "call" })
	public void testCase_call_018(){
		startTestCase();
		String testphone = sendPhone;
		// 清空
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
		prepareUnreadCall(testphone, 15, 3, 1);
		
		back("tab_call");
		
		Myassert("没有生成测试用的号码：" + testphone, getTextViewNameById("line1").equals(testphone));
		
		clickById("call_detail");
		
		Myassert("没有进入陌生人详情页", getTextViewNameById("iab_title").equals("陌生人详情"));
		
		//点击信息
		clickById("sms");
		
		Myassert("没有进入新建信息页", isExistenceById("iab_title") && getTextViewNameById("iab_title").equals("陌生人"));
		
		//输入短信内容
		intoContentEditTextById("embedded_text_editor", "testCase_call_018");
		
		//点击发送
		sendMMS();

		
		back("tab_mms");
		
		Myassert("没有发送短信", isExistenceById("from") && getTextViewNameById("from").contains(testphone));
		
		reportLog("通话记录-陌生人(发短信)");
		
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
		
	}
	
	/**
	 * 通话记录-本地联系人(分享名片)
	 */
	@Test(groups = { "call" })
	public void testCase_call_019(){
		startTestCase();
		String casephone = sendPhone;
		String casename = getTestCaseName();
		
		// 清空
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
		//准备数据
		createContacts(casename, casephone);
		prepareUnreadCall(casephone, 15, 3, 1);

		back("tab_call");
		
		clickById("call_detail");
		
		Myassert("没有进入联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		//分享名片
		clickMenuAndSelect(1);
		
		Myassert("没有弹出选择分享软件", getTextViewNameById("android:id/alertTitle").equals("选择分享"));
		
		clickByName("和通讯录");
		
		sleepTime(1000);
		
		Myassert("没有进入新信息页", isExistenceById("iab_title") && getTextViewNameById("iab_title").equals("新信息"));
		
		intoContentEditTextByName("收件人:", phone);
		
		//点击发送
		sendMMS();
		
		back("tab_mms");
		
		Myassert("没有发送短信", isExistenceById("from") && getTextViewNameById("from").contains(phone));
		
		clickById("from");
		
		String text = getTextViewNameById("text_view");
		String adr = "http://pim.10086.cn/wapdownload.php ";
		
		Myassert("分享名片内容没有对应手机号" + casename, text.contains(casename));
		Myassert("分享名片内容没有对应名称" + casephone, text.contains(casephone));
		Myassert("分享名片内容没有对应地址" + adr, text.contains(adr));
		
		reportLog("通话记录-本地联系人(分享名片)");
		back("tab_call");
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
		
	}
	
	/**
	 * 通话记录-本地联系人(群组选择-选择已有)
	 */
	@Test(groups = { "call" })
	public void testCase_call_020(){
		startTestCase();
		String casephone = sendPhone;
		String casename = getTestCaseName();
		String casegroup = getTestGroupName();
		
		clearGroup();
		// 清空
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
		//准备数据
		createContacts(casename, casephone);
		prepareUnreadCall(casephone, 15, 3, 1);

		back("tab_contacts");
		createGroup(casegroup, 1);
		
		back("tab_call");
		
		clickById("call_detail");
		
		Myassert("没有进入联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		//分
		clickMenuAndSelect(3);
		
		Myassert("没有进入分组选择", getTextViewNameById("iab_title").equals("分组选择"));

		//选择分组
		getLisWebElementById("check_group_select").get(getGroupNum(casegroup)).click();
		
		sleepTime(2000);
		
		Myassert("没有选择分组", getLisWebElementById("group_choice_ok_btn").get(0).getAttribute("enabled").equals("true"));
		
		clickById("group_choice_ok_btn");
		
		//自动返回联系人详情页
		Myassert("没有自动返回联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		Myassert("联系人分组中，没有还有分组信息：" + casegroup, getTextViewNameById("contact_detail_groups_name").contains(casegroup));
		
		back("tab_contacts");
		
		clearGroup();
		
		reportLog("通话记录-本地联系人(群组选择-选择已有)");
		
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
	}
	
	

	/**
	 * 通话记录-本地联系人(群组选择-选择新建)
	 */
	@Test(groups = { "call" })
	public void testCase_call_021(){
		startTestCase();
		String casephone = sendPhone;
		String casename = getTestCaseName();
		String casegroup = getTestGroupName();
		
		clearGroup();
		// 清空
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
		//准备数据
		createContacts(casename, casephone);
		prepareUnreadCall(casephone, 15, 3, 1);
		
		back("tab_call");
		
		clickById("call_detail");
		
		Myassert("没有进入联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		//
		clickMenuAndSelect(3);
		
		Myassert("没有进入分组选择", getTextViewNameById("iab_title").equals("分组选择"));

		//点击新增
		clickById("iab_ib_more");
		
		//新建分组
		Myassert("没有进入新建分组", getTextViewNameById("title").equals("新建分组"));
		
		//输入分组名
		intoContentEditTextById("content", casegroup);
		
		//点击保存
		clickById("dialog_btn_positive");
		
		//点击确定
		clickById("group_choice_ok_btn");
		
		sleepTime(2000);
		
		//自动返回联系人详情页
		Myassert("没有自动返回联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		Myassert("联系人分组中，没有还有分组信息：" + casegroup, getTextViewNameById("contact_detail_groups_name").contains(casegroup));
		
		back("tab_contacts");
		
		clearGroup();
		
		reportLog("通话记录-本地联系人(群组选择-选择新建)");
		
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
	}
	
	/**
	 * 更多操作(一键拨号-1号按钮不可设置)
	 */
	@Test(groups = { "call" ,"fixed"})
	public void testCase_call_022(){
		startTestCase();

		// 切换到拨号页
		back("tab_call");

		// 选择一键拨号
		clickMenuAndSelect(2);

		// 验证当前页
		Myassert("没有进入一键拨号设置页", driver.findElementByName("一键拨号设置")
				.isDisplayed());
		
		Myassert("语音信箱能设置的1号数字", !isExistenceById("title"));
		
		back("tab_call");
		
		reportLog("更多操作(一键拨号-1号按钮不可设置)");
	}
	
	/**
	 * 拨号盘-拨打陌生人
	 */
	@Test(groups = { "call" })
	public void testCase_call_023(){
		startTestCase();
		String casephone = phone;
		
		deleteAllCall();
		deleteAllContacts();
		
		// 切换到拨号页
		back("tab_call");
		
		//显示键盘
		displaykeyboardCall();
		
		//点击键盘
		touchCallNumber(casephone);
		
		//点击拨号
		clickById("tab_dialer");
		
		sleepTime(1000);
		
		// 点击点击确定
		clickByName("确定");
		
		// 切换到拨号页
		back("tab_call");
		
		Myassert("没有找到拨号记录", getTextViewNameById("line1").contains(casephone));
		
		deleteAllCall();
		
		reportLog("拨号盘-拨打陌生人");
	}
	
	/**
	 * 拨号盘-拨打USSD(*#06#)
	 */
	@Test(groups = { "call" })
	public void testCase_call_024(){
		startTestCase();
		String casephone = "*#06#";
		
		deleteAllCall();
		deleteAllContacts();
		
		// 切换到拨号页	
		back("tab_call");
		
		//显示键盘
		displaykeyboardCall();
		
		//点击键盘
		touchCallNumber(casephone);
		
		sleepTime(1000);

		Myassert("没有弹出国际移动设备识别码", getTextViewNameById("title").contains("国际移动设备识别码"));
		
		// 点击点击确定
		clickByName("确定");
		
		clickById("tab_contacts");
		//切换模块，消除拨号
		clickById("tab_call");
		
		sleepTime(1000);
		
		deleteAllCall();
		
		reportLog("拨号盘-拨打USSD(*#06#)");
	}
	
	
	/**
	 * 通话记录-未接来电页-最新记录显示标识-列表顶部
	 */
	@Test(groups = { "call" })
	public void testCase_call_025(){
		startTestCase();
		String casephone = sendPhone;
		String casephone2 = phone;
		
		
		deleteAllCall();
		deleteAllContacts();
		
		//准备测试数据
		prepareUnreadCall(casephone, 15, 3, 1);
		
		prepareUnreadCall(casephone2, 15, 3, 1);
		
		back("tab_call");
		
		Myassert("最新拨号记录没有标注： " + casephone2, getFirstCallRecorder().contains(casephone2));
		
		sleepTime(2000);
		
		deleteAllCall();
		
		reportLog("通话记录-未接来电页-最新记录显示标识-列表顶部");
	}
	
	/**
	 * 通话记录-未接来电页-最新记录显示标识-点击后消失
	 */
	@Test(groups = { "call" })
	public void testCase_call_026(){
		startTestCase();
		String casephone = sendPhone;
		
		deleteAllCall();
		deleteAllContacts();
		
		//准备测试数据
		prepareUnreadCall(casephone, 15, 3, 1);
		
		back("tab_call");
		
		Myassert("没有生成未读的未接来电", isExistenceById("tvStrangeMark"));
		
		clickById("line1");
		
		sleepTime(2000);
		
		clickByName("确定");
		
		back("tab_call");
		
		Myassert("未读的未接来电的标识仍存在", !isExistenceById("tvStrangeMark"));
		
		sleepTime(2000);
		
		deleteAllCall();
		
		reportLog("通话记录-未接来电页-最新记录显示标识-点击后消失");
	}
	
	
	/**
	 * 通话记录-回拨-陌生号码
	 */
	@Test(groups = { "call" })
	public void testCase_call_027(){
		startTestCase();
		String casephone = sendPhone;
		
		deleteAllCall();
		deleteAllContacts();
		
		//准备测试数据
		prepareUnreadCall(casephone, 15, 3, 1);
		
		back("tab_call");
		
		clickById("line1");
		
		sleepTime(2000);
		
		clickByName("确定");
		
		back("tab_call");
		
		clickById("call_detail");
		
		sleepTime(1000);
		
		Myassert("没有对该号码进行回拨：" + casephone, isExistenceById("duration")&&getTextViewNameById("duration").contains("未接通"));
		
		back("tab_call");
		
		deleteAllCall();
		
		reportLog("通话记录-回拨-陌生号码");
	}
	

	/**
	 * 通话记录-回拨-本地联系人号码
	 */
	@Test(groups = { "call" })
	public void testCase_call_028(){
		startTestCase();
		String casephone = sendPhone;
		String casename = this.getTestCaseName();
		
		deleteAllCall();
		deleteAllContacts();
		
		//准备测试数据
		prepareUnreadCall(casephone, 15, 3, 1);
		createContacts(casename, casephone);
		
		back("tab_call");
		
		clickById("line1");
		
		sleepTime(2000);
		
		clickByName("确定");
		
		back("tab_call");
		
		clickById("call_detail");
		
		sleepTime(1000);
		
		Myassert("没有对该号码进行回拨：" + casephone, getTextViewNameById("duration").contains("未接通"));
		
		back("tab_call");
		
		deleteAllCall();
		deleteAllContacts();
		
		reportLog("通话记录-回拨-本地联系人号码");
	}
	
	/**
	 * 通话记录-时间，地区
	 */
	@Test(groups = { "call" })
	public void testCase_call_029(){
		startTestCase();
		String casephone = sendPhone;
		String date;
		String txttime;
		
		deleteAllCall();
		
		//准备测试数据
		date = prepareUnreadCall(casephone, 15, 3, 1);
		txttime = this.getSubString(date, "_", 1);
		
		back("tab_call");
		
		Myassert("生成时间与显示时间不对应：" + txttime, isExistenceById("date") && getTextViewNameById("date").equals(txttime));
		
		Myassert("拨号记录中的地区显示有误(对应手机号)：" + casephone, isExistenceById("loc")&&getTextViewNameById("loc").contains("广州"));
		
		deleteAllCall();

		reportLog("通话记录-时间，地区");
	}
	
	
	/**
	 * 通话记录-通话记录类型-已接-回拨
	 * 
	 */
	@Test(groups = { "call" })
	public void testCase_call_030(){
		startTestCase();
		String casephone = sendPhone;
		
		deleteAllCall();
		
		//准备测试数据
		//准备测试数据
		prepareUnreadCall(casephone, 15, 1, 1);
		
		back("tab_call");
		
		clickById("line1");
		
		sleepTime(2000);
		
		clickByName("确定");
		
		back("tab_call");
		
		clickById("call_detail");
		
		sleepTime(1000);
		
		Myassert("没有对该号码进行回拨：" + casephone, isExistenceById("duration")&&getTextViewNameById("duration").contains("未接通"));
		
		back("tab_call");
		
		deleteAllCall();
		
		reportLog("通话记录-通话记录类型-已接-回拨");
	}
	
	/**
	 * 通话记录-通话记录类型-已拨-回拨
	 * 
	 */
	@Test(groups = { "call" })
	public void testCase_call_031(){
		startTestCase();
		String casephone = sendPhone;
		
		deleteAllCall();
		
		//准备测试数据
		prepareUnreadCall(casephone, 15, 2, 1);
		
		back("tab_call");
		
		clickById("line1");
		
		sleepTime(2000);
		
		clickByName("确定");
		
		back("tab_call");
		
		clickById("call_detail");
		
		sleepTime(1000);
		
		Myassert("没有对该号码进行回拨：" + casephone, isExistenceById("duration")&&getTextViewNameById("duration").contains("未接通"));
		
		back("tab_call");
		
		deleteAllCall();
		
		reportLog("通话记录-通话记录类型-已拨-回拨");
	}
	
	/**
	 * 通话记录-通话记录类型-未接来电-已读-回拨
	 * 
	 */
	@Test(groups = { "call" })
	public void testCase_call_032(){
		startTestCase();
		String casephone = sendPhone;
		
		deleteAllCall();
		
		//准备测试数据
		//准备测试数据
		prepareUnreadCall(casephone, 15, 3, 1);
		
		back("tab_call");
		
		clickById("line1");
		
		sleepTime(2000);
		
		clickByName("确定");
		
		back("tab_call");
		
		clickById("call_detail");
		
		sleepTime(1000);
		
		Myassert("没有对该号码进行回拨：" + casephone, isExistenceById("duration")&&getTextViewNameById("duration").contains("未接通"));
		
		back("tab_call");
		
		deleteAllCall();
		
		reportLog("通话记录-通话记录类型-未接来电-已读-回拨");
	}
	
	/**
	 * 通话记录-通话记录类型-未接来电-未读-回拨
	 * 
	 */
	@Test(groups = { "call" })
	public void testCase_call_033(){
		startTestCase();
		String casephone = sendPhone;
		
		deleteAllCall();
		
		//准备测试数据
		prepareUnreadCall(casephone, 15, 3, 2);
		
		back("tab_call");
		
		clickById("line1");
		
		sleepTime(2000);
		
		clickByName("确定");
		
		back("tab_call");
		
		clickById("call_detail");
		
		sleepTime(1000);
		
		Myassert("没有对该号码进行回拨：" + casephone, isExistenceById("duration")&&getTextViewNameById("duration").contains("未接通"));
		
		back("tab_call");
		
		deleteAllCall();
		
		reportLog("通话记录-通话记录类型-未接来电-未读-回拨");
	}
	
//------------------------联系人模块测试用------------------------------------//
	
	/**
	 * 创建联系人
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_001() {
		startTestCase();
		String casephone = "1354232000";
		deleteAllContacts();
		back("tab_contacts");
		// 创建联系人
		createContacts("firstContacts", casephone + 1);
		createContacts("中文名的联系人", casephone + 2);
		createContacts("138438(数字联系人)", casephone + 3);

		// 删除联系人
		deleteContactsByPhone(casephone + 1);
		deleteContactsByPhone(casephone + 2);
		deleteContactsByPhone(casephone + 3);

		reportLog("创建联系人");
	}

	/**
	 * 创建详细的联系人
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_002() {
		startTestCase();
		deleteAllContacts();
		back("tab_contacts");

		// 创建联系人
		createContacts();
		// 清理数据
		
		deleteAllContacts();
		//deleteContactsByPhone(this.phone);

		reportLog("创建详细的联系人");
	}

	/**
	 * 进入联系人详情页 删除一个联系人 联系人详细页 - 删除联系人
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_003() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;
		deleteAllContacts();
		back("tab_contacts");

		// 准备测试数据
		createContacts(casename, casephone);

		// 点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		// 判断列表是否存在该联系人
		Myassert("不存在联系人：" + casename, searchContact(casephone, 1));

		// 点击进入联系人
		getFirstTextView(casephone, 1).click();

		// 进入菜单，选择删除
		clickMenuAndSelect(2);

		// 点击确定
		clickById("dialog_btn_positive");

		back("tab_contacts");
		Myassert("删除失败", !isExistenceByName(casename));

		sleepTime(5000);

		reportLog("联系人详细页 - 删除联系人");
	}

	/**
	 * 长按联系人，选中全选，点击删除 联系人 - 更多操作 - 删除（长按联系人）
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_004() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;

		deleteAllContacts();
		back("tab_contacts");

		// 准备测试数据
		createContacts(casename, casephone);

		// 点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", casephone);

		// 判断列表是否存在该联系人
		// Assert.assertTrue(searchContact("1374242111" , 1));
		Myassert("列表中没找到该联系人:" + casephone, searchContact(casephone, 1));

		// 长按联系人
		// clickLongWebElement(getFirstTextView("10086"));
		clickLongByNameUseJs(casephone);

		// 点击全选按钮
		clickById("iab_ib_more");

		// 点击删除
		clickById("mca_delete_icon");

		// 点击确定
		clickById("dialog_btn_positive");

		back("tab_contacts");
		Myassert("删除失败", !isExistenceByName(casename));

		deleteAllContacts();
		reportLog("联系人 - 更多操作 - 删除（长按联系人）");
	}

	/**
	 * 输入搜索条件，获取联系人信息。 联系人列表 - 搜索本地联系人
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_005() {
		startTestCase();

		deleteAllContacts();
		back("tab_contacts");

		// 创建联系人，准备测试数据
		createContacts("张小花", "13824451649");
		createContacts("通讯录", "13843845678");

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "张小花");

		// 判断列表是否存在该联系人
		// Assert.assertTrue(searchContact("13824451649" , 1));
		Myassert("列表中没找到该联系人:张小花", searchContact("13824451649", 1));

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "zxh");

		// 判断列表是否存在该联系人
		// Assert.assertTrue(searchContact("13824451649" , 1));
		Myassert("列表中没找到该联系人:张小花", searchContact("13824451649", 1));

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "zhangxiaohua");

		// 判断列表是否存在该联系人
		// Assert.assertTrue(searchContact("13824451649" , 1));
		Myassert("列表中没找到该联系人:张小花", searchContact("13824451649", 1));
		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "13824451649");

		// 判断列表是否存在该联系人
		// Assert.assertTrue(searchContact("13824451649" , 1));
		Myassert("列表中没找到该联系人:张小花", searchContact("13824451649", 1));
		// 清理数据
		deleteContactsByPhone("13824451649");
		deleteContactsByPhone("13843845678");

		reportLog("联系人列表 - 搜索本地联系人");
	}

	/**
	 * 创建联系人后，新增或修改联系人信息 联系人详细页 - 编辑
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_006() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;
		String casephone2 = phone;
		deleteAllContacts();
		back("contact");
		// 创建联系人
		createContacts(casename, casephone);

		// 进入新创建的联系详细页
		clickById("tab_contacts");

		// 搜索联系人
		intoContentEditTextById("contact_search_bar", casephone);

		// 点击搜索记录，
		searchWebElement(casename).click();

		sleepTime(2000);

		// 点击编辑
		clickById("iab_ib_action");

//		// 获取界面所有的EditView元素
//		List<WebElement> editText = driver
//				.findElementsByClassName("android.widget.EditText");
//
//		// clearText(editText.get(4).getAttribute("text"));
//		editText.get(4).sendKeys(casephone2);

		intoContentEditTextByName("电话号码", casephone2);
		
		
		// 点击保存
		clickById("iab_ib_action");

		sleepTime(3000);

		// 返回拨号页
		back("tab_contacts");

		intoContentEditTextById("contact_search_bar", casephone2);

		// Assert.assertTrue(this.searchContact("testCase_025", 0));
		Myassert("没有找到测试数据：" + casename, searchContact(casename, 0));

		clearTextAndNote();
		
		clickById("tab_contacts");

		// this.deleteContactsByName("testCase_025");
		deleteAllContacts();
		reportLog("联系人详细页 - 编辑");

	}

	/**
	 * 联系人详细页 - 收藏
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_007() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;
		deleteAllContacts();
		back("contact");
		// 创建测试数据
		createContacts(casename, casephone);

		// 进入新创建的联系详细页
		clickById("tab_contacts");

		// 搜索联系人
		intoContentEditTextById("contact_search_bar", casephone);

		// 点击搜索记录，
		searchWebElement(casename).click();

		sleepTime(2000);

		// 点击收藏按钮
		clickById("contact_detail_starred");

		// 验证收藏标志被选上
		boolean bl = driver.findElementById("contact_detail_starred")
				.getAttribute("checked").equals("true");
		Myassert("联系人没有被标识为收藏", bl);
		// Assert.assertTrue("",driver.findElementById("contact_detail_starred").getAttribute("checked").equals("true"));

		// 返回主页
		back("tab_contacts");

		// 清空记录
		clearTextAndNote();

		// 搜索列表中，是否存在 收藏联系人选项
		Myassert("不存在收藏列表", driver.findElementByName("收藏联系人").isDisplayed());

		// 删除用户名
		deleteContactsByName(casename);
		// 这里不能用全部删除联系人
		// deleteAllContacts();
		reportLog("联系人详细页 - 收藏");
	}

	/**
	 * 新建联系人（屏幕下拉）
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_008() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;

		// 进入联系人列表
		clickById("tab_contacts");

		// 下拉列表
		swipeToDown();

		// 保存联系信息
		saveContact(casename, casephone);

		// 休眠3秒
		sleepTime(3000);

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", casephone);

		// 判断是否通过
		// Assert.assertTrue(searchContact(phone, 1));
		Myassert("创建联系人失败", searchContact(casephone, 1));

		// 删除联系人
		// deleteContactsByName(username);
		deleteAllContacts();
		reportLog("新建联系人（屏幕下拉）");
	}

	/**
	 * 联系人-加入白名单
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_009() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;

		deleteAllContacts();
		clickById("tab_contacts");

		// 创建联系人
		createContacts(casename, casephone);

		sleepTime(4000);

		back("tab_contacts");
		// 长按选择联系人
		clickLongByIdUseJs("contact_name");

		// 点击
		clickByName("加入白名单");

		// 进入管理白名单
		OpenTabMenu("防打扰", "白名单");

		// 检测当前界面为防打扰页
		Myassert("没有进入白名单", driver.findElementByName("管理白名单").isDisplayed());

		boolean bl = searchContact(casephone, 0);

		// 检测当前是否存储骚扰电话
		// Assert.assertTrue(bl);
		Myassert("列表中，找不到联系人：" + casephone, bl);

		sleepTime(1000);

		// 清空数据
		if (bl) {
			// 点击清空
			clickById("iab_ib_action");

			// 点击清空
			clickById("dialog_btn_positive");
		}
		// 返回主界面
		back("tab_contacts");

		// 删除
		// deleteContactsByName(phone);
		deleteAllContacts();
		// 清空记录
		clearTextAndNote();
		reportLog("联系人-加入白名单");
	}

	/**
	 * 联系人页-加入白名单
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_010() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;

		deleteAllContacts();

		clickById("tab_contacts");

		// 创建联系人
		createContacts(casename, casephone);

		// 清空记录
		clearTextAndNote();

		sleepTime(4000);

		// 点击进入
		clickByName(casephone);

		// 选择加入白名单
		this.clickMenuAndSelect(4);

		// 返回联系人页
		back("tab_contacts");

		// 进入管理白名单
		OpenTabMenu("防打扰", "白名单");

		// 检测当前界面为防打扰页
		Myassert("没有进入白名单管理", isExistenceById("iab_title") && getTextViewNameById("iab_title").equals("管理白名单"));

		
		Myassert("不存在白名单联系人", isExistenceById("name"));
		Myassert("白名单联系人有误: " + casename, getTextViewNameById("name").equals(casename));
		
		//点击清空
		clickById("iab_ib_action");
		
		clickById("dialog_btn_positive");
		
		// 返回主界面
		back("tab_contacts");

		// 删除
		deleteAllContacts();
		// 清空记录
		clearTextAndNote();
		reportLog("联系人页-加入白名单");
	}

	/**
	 * 创建详细联系人，添加头像
	 */
	@Test(groups = { "debug" })
	public void testCase_contact_011() {
		startTestCase();
		deleteAllContacts();
		// 创建联系人
		createContacts();
		// 获取联系人图标
		Point point = getContactsPoint().get(0);
		// System.out.println("Point: " + point);

		BufferedImage subImg1 = getContactHead(point);

		// 点击一个联系人
		clickById("contact_name");

		// 选择图片
		selectImage();

		back("tab_contacts");

		BufferedImage subImg2 = getContactHead(point);
		// System.out.println("Point: " + point);

		// 对比图片
		boolean bl = sameAs(subImg1, subImg2, 0.9);

		// 清理数据
		this.deleteAllContacts();

		// 判断
		// Assert.assertTrue(!bl);
		Myassert("添加图片失败", !bl);

		reportLog("创建详细联系人，添加头像");
	}

	/**
	 * 获取搜索输入框内的联系人个数 联系人数量在0个是对比，1个是对比。超于6个或7个联系人联系人数量无法对比，列表创建原理。
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_012() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;
		
		
		// 清空本地联系人
		deleteAllContacts();
		
		
		back("tab_contacts");

		// 进入联系人列表，获取联系人数量，getContactCount();
		// 获取搜索框内容的数字，getNumById("contact_search_bar");

		createContacts(casename, casephone);
		back("tab_contacts");
		//boolean b2 = getContactCount() == getNumById("contact_search_bar");
		int num1 = getContactCount();
		int num2 = getNumById("contact_search_bar");
		
		// Assert.assertTrue(getContactCount() ==
		// getNumById("contact_search_bar"));
		Myassert("联系人数量不一致,分别是: " + num1 + ": " + num2, num1 == num2);

		// 清空本地联系人
		deleteAllContacts();

		reportLog("获取搜索输入框内的联系人个数");
	}

	/**
	 * 分组管理，创建分组并添加成员
	 */
	@Test(groups = { "contact","group" })
	public void testCase_contact_013() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;

		String casename2 = getTestCaseName()+"_2";
		String casephone2 = phone;

		back("tab_contacts");

		clearGroup();

		deleteAllContacts();

		createContacts(casename, casephone);
		createContacts(casename2, casephone2);

		// 创建分组，并留住当前页面
		createGroup("陈策", 1);

		// 点击我的分组
		clickById("headview_item_name");

		// 进入分组
		clickByName("陈策");

		// 点击添加成员
		clickByName("添加成员");
		// sleepTime(10000);

		// 验证当前页面
		Myassert("当前页不在我的分组内", isExistenceByName("分组添加成员"));

		// 获取联系人列表联系人数量
		int firNum = getNumById("contact_search_bar");

		// 点击更多按钮
		clickById("iab_ib_more");

		// 点击添加
		clickById("selection_ok");

		// 获取联系人列表联系人数量
		int secNum = getNumById("contact_search_bar");

		// 验证数量是否一致
		// Assert.assertTrue(firNum == secNum);
		Myassert("数量是否一致", firNum == secNum);

		back("tab_contacts");

		// 点击我的分组
		clickById("headview_item_name");


		back("tab_contacts");
		clearGroup();
		
		deleteAllContacts();

		reportLog("分组管理，创建分组并添加成员");
	}

	/**
	 * 分组管理，修改组名 分别创建两个分组，一个联系人为空，另一个联系人数量不为空
	 */
	@Test(groups = { "contact" ,"group"})
	public void testCase_contact_014() {
		startTestCase();

		clearGroup();
		deleteAllContacts();

		// 创建联系人
		createContacts("testCase0_contact_014", "13522168035");
		createContacts("testCase1_contact_014", "13521168035");
		createContacts("testCase2_contact_014", "13521068035");

		// 创建分组1
		createGroup("TestGroup_01", 1);

		// 创建分组2，分组2添加联系人
		createGroup("TestGroup_02", 1);

		// 分组添加联系人
		addAllGroupMembers("TestGroup_02");

		intoMyGroupPage();

		renameGroup("TestGroup_01", "你猜猜");

		intoMyGroupPage();

		renameGroup("TestGroup_02", "猜不到啊");

		Myassert("修改分组失败", isExistenceByName("你猜猜")
				&& isExistenceByName("猜不到啊"));

		deleteGroup("你猜猜");

		deleteGroup("猜不到啊");

		deleteAllContacts();

		reportLog("分组管理，修改组名");
	}

	/**
	 * 分组管理，解除分组
	 */
	@Test(groups = { "contact","group" })
	public void testCase_contact_015() {
		startTestCase();
		clickById("tab_contacts");

		clearGroup();

		deleteAllContacts();

		// 创建联系人
		createContacts("testCase0_contact_015", "13522168036");
		createContacts("testCase1_contact_015", "13521168036");
		createContacts("testCase2_contact_015", "13521068036");

		// 创建分组1
		createGroup("TestGroup01_36", 1);

		// 创建分组2，分组2添加联系人
		createGroup("TestGroup02_36", 1);

		// 分组添加联系人
		addAllGroupMembers("TestGroup02_36");

		// 进入我的分组
		intoMyGroupPage();

		// 删除分组
		deleteGroup("TestGroup01_36");

		// 进入我的分组
		intoMyGroupPage();

		// 删除分组
		deleteGroup("TestGroup02_36");

		Myassert(
				"删除分组失败",
				!(isExistenceByName("TestGroup01_36") && isExistenceByName("TestGroup02_36")));

		deleteAllContacts();
		reportLog("分组管理，解除分组");
	}

	/**
	 * 分组管理，添加成员
	 */
	@Test(groups = { "contact","group" })
	public void testCase_contact_016() {
		startTestCase();
		// 清空数据
		clearGroup();

		deleteAllContacts();

		// 创建联系人
		createContacts("testCase0_contact_016", "13522068037");
		createContacts("testCase1_contact_016", "13521168037");
		createContacts("testCase2_contact_016", "13521268037");
		createContacts("testCase3_contact_016", "13522368037");
		createContacts("testCase4_contact_016", "13521468037");
		createContacts("testCase5_contact_016", "13521568037");
		createContacts("testCase6_contact_016", "13521668037");

		// 创建分组，分组添加联系人
		// 不添加联系人
		createGroup("TestGroup0_37", 1);
		// 添加1个联系人
		createGroup("TestGroup1_37", 1);
		// 添加2个联系人
		createGroup("TestGroup2_37", 1);
		// 添加所有联系人
		createGroup("TestGroup3_37", 1);

		// 添加完后，判断未分组的联系人数量是否为0

		// 添加1个联系人
		addGroupMembers("TestGroup1_37", 1);

		// 添加2个联系人
		addGroupMembers("TestGroup2_37", 2);

		
		// 添加7个联系人
		addAllGroupMembers("TestGroup3_37");

		// 进入我的分组
		intoMyGroupPage();

		List<WebElement> list = this.getWebElementList("TextView",
				"groupNumber");

		Myassert("分成成员数量有异常:未分组", getNumerByText(list.get(getGroupNum("未分组"))
				.getText()) == 0);

		Myassert(
				"分成成员数量有异常:TestGroup0_37",
				getNumerByText(list.get(getGroupNum("TestGroup0_37")).getText()) == 0);

		Myassert(
				"分成成员数量有异常:TestGroup1_37",
				getNumerByText(list.get(getGroupNum("TestGroup1_37")).getText()) == 1);

		Myassert(
				"分成成员数量有异常:TestGroup2_37",
				getNumerByText(list.get(getGroupNum("TestGroup2_37")).getText()) == 2);

		Myassert(
				"分成成员数量有异常:TestGroup3_37",
				getNumerByText(list.get(getGroupNum("TestGroup3_37")).getText()) == 7);

		// 清除数据
		deleteGroup("TestGroup0_37");
		deleteGroup("TestGroup1_37");
		deleteGroup("TestGroup2_37");
		deleteGroup("TestGroup3_37");

		deleteAllContacts();
		reportLog("分组管理，添加成员");

	}

	/**
	 * 分组管理，排序
	 */
	@Test(groups = { "contact","group" })
	public void testCase_contact_017() {
		startTestCase();
		String casename = getTestCaseName();
		String casephone = sendPhone;
		// 删除已有分组，预防影响测试
		clearGroup();

		deleteAllContacts();

		createContacts(casename, casephone);

		// 创建分组
		createGroup("testCase01_038", 1);
		createGroup("testCase02_038", 1);
		createGroup("testCase03_038", 1);
		createGroup("testCase04_038", 1);

		// 验证
		Myassert("没找到我的分组入口", isExistenceById("headview_item_name"));

		intoMyGroupPage();

		int groupSize = getWebElementList("TextView", "txt_group_name").size();

		// 点击排序
		clickByName("排序");

		sleepTime(3000);

		// 移动分组
		groupMoveto("testCase04_038", "up");

		sleepTime(1000);

		// 移动分组
		groupMoveto("testCase01_038", "down");

		// 点击保存
		clickByName("保存");

		sleepTime(2000);

		// 验证
		Myassert("分组排序顺序有异常（非第二）：testCase04_038",
				getGroupNum("testCase04_038") == 1);

		Myassert("分组排序顺序有异常（非末尾）：testCase01_038",
				getGroupNum("testCase01_038") == (groupSize - 1));

		deleteGroup("testCase01_038");
		deleteGroup("testCase02_038");
		deleteGroup("testCase03_038");
		deleteGroup("testCase04_038");

		deleteAllContacts();

		reportLog("分组管理，排序");
	}

	/**
	 * 分组管理，移除成员
	 */
	@Test(groups = { "contact" ,"group"})
	public void testCase_contact_018() {
		startTestCase();
		// 清空数据
		clearGroup();

		String g1 = getTestGroupName() + "_1";

		String g3 = getTestGroupName() + "_3";

		
		deleteAllContacts();

		// 创建联系人
		createContacts("testCase0_039", "13522068039");
		createContacts("testCase1_039", "13521168039");
		createContacts("testCase2_039", "13521268039");
		createContacts("testCase3_039", "13522368039");
		createContacts("testCase4_039", "13521468039");
		createContacts("testCase5_039", "13521568039");
		createContacts("testCase6_039", "13521668039");

		// 创建分组，分组添加联系人
		// 不添加联系人
		createGroup(g1, 1);

		// 添加2个联系人
		createGroup(g3, 1);

		// 添加7个联系人
		addAllGroupMembers(g1);


		addAllGroupMembers(g3);


		// 不移除
		deleteGroupMembers(g1, 0);


		// 移除2个
		deleteGroupMembers(g3, 2);

		// 进入我的分组
		intoMyGroupPage();

		List<WebElement> list = this.getWebElementList("TextView",
				"groupNumber");

		Myassert("分组成员数量有异常：未分组", getNumerByText(list.get(getGroupNum("未分组"))
				.getText()) == 0);

		Myassert(
				"分组成员数量有异常：" + g1,
				getNumerByText(list.get(getGroupNum(g1)).getText()) == 7);

		Myassert(
				"分组成员数量有异常：" + g3,
				getNumerByText(list.get(getGroupNum(g3)).getText()) == 5);

		// 清除数据
		clearGroup();

		deleteAllContacts();
		reportLog("分组管理，移除成员");
	}

	/**
	 * 分组管理，重命名
	 */
	@Test(groups = { "contact","group" })
	public void testCase_contact_019() {
		startTestCase();
		// 清除数据
		clearGroup();

		deleteAllContacts();

		// 创建联系人
		createContacts("testCase0_040", "13522068040");
		createContacts("testCase1_040", "13521168040");
		createContacts("testCase2_040", "13521268040");

		// 添加分组
		createGroup("TestGroup0_40", 1);

		createGroup("TestGroup1_40", 1);

		createGroup("TestGroup2_40", 1);

		// 添加1个联系人
		addGroupMembers("TestGroup1_40", 1);

		// 添加所有个联系人
		addAllGroupMembers("TestGroup2_40");

		// 重命名
		renameOneGroup("TestGroup0_40", "Rename01");
		renameOneGroup("TestGroup1_40", "Rename02");
		renameOneGroup("TestGroup2_40", "Rename03");

		intoMyGroupPage();
		// 验证
		Myassert("重命名失败:TestGroup0_40", isExistenceByName("Rename01")
				&& (!isExistenceByName("TestGroup0_40")));

		Myassert("重命名失败:TestGroup1_40", isExistenceByName("Rename02")
				&& (!isExistenceByName("TestGroup1_40")));

		Myassert("重命名失败:TestGroup2_40", isExistenceByName("Rename03")
				&& (!isExistenceByName("TestGroup2_40")));

		// 清除数据
		deleteGroup("TestGroup0_40");
		deleteGroup("TestGroup1_40");
		deleteGroup("TestGroup2_40");
		deleteGroup("Rename01");
		deleteGroup("Rename02");
		deleteGroup("Rename03");

		deleteAllContacts();

		reportLog("分组管理，重命名");
	}

	/**
	 * 分组管理，解散分组
	 */
	@Test(groups = { "contact" ,"group"})
	public void testCase_contact_020() {
		startTestCase();
		// 清除数据
		clearGroup();

		deleteAllContacts();
		// 创建联系人
		createContacts("testCase0_041", "13522068041");
		createContacts("testCase1_041", "13521168041");
		createContacts("testCase2_041", "13521268041");

		// 添加分组
		createGroup("TestGroup0_41", 1);

		createGroup("TestGroup1_41", 1);

		createGroup("TestGroup2_41", 1);

		// 添加1个联系人
		addGroupMembers("TestGroup1_41", 1);

		// 添加所有个联系人
		addAllGroupMembers("TestGroup2_41");

		// 删除
		deleteOneGroup("TestGroup0_41");

		deleteOneGroup("TestGroup1_41");

		deleteOneGroup("TestGroup2_41");

		// 判断
		Myassert("解散分组失败：TestGroup0_41", !isExistenceByName("TestGroup0_41"));
		Myassert("解散分组失败：TestGroup1_41", !isExistenceByName("TestGroup1_41"));
		Myassert("解散分组失败：TestGroup2_41", !isExistenceByName("TestGroup2_41"));

		deleteAllContacts();

		reportLog("分组管理，解散分组");
	}

	/**
	 * 分组管理，批量设置铃声
	 */
	@Test(groups = { "contact","group" })
	public void testCase_contact_021() {
		startTestCase();
		// 清除联系人
		clearGroup();

		deleteAllContacts();

		// 创建联系人
		createContacts("testCase0_042", "13522068042");
		createContacts("testCase1_042", "13521168042");
		createContacts("testCase2_042", "13521268042");

		// 添加分组
		createGroup("TestGroup0_42", 1);

		createGroup("TestGroup1_42", 1);

		createGroup("TestGroup2_42", 1);

		// 添加联系人
		addGroupMembers("TestGroup0_42", 0);

		addGroupMembers("TestGroup1_42", 1);

		addAllGroupMembers("TestGroup2_42");

		// 添加铃声
		scrollAndSelect("TestGroup0_42", "top");

		scrollAndSelect("TestGroup1_42", "mid");

		scrollAndSelect("TestGroup2_42", "bottom");

		// 这方法只能通过拨号来验证

		// 清除联系人
		deleteGroup("TestGroup0_42");
		deleteGroup("TestGroup1_42");
		deleteGroup("TestGroup2_42");

		deleteAllContacts();

		reportLog("分组管理，批量设置铃声");
	}

	/**
	 * 分组管理，组内搜索
	 */
	@Test(groups = { "contact" ,"group"})
	public void testCase_contact_022() {
		startTestCase();
		// 清理数据
		deleteAllContacts();

		// 创建联系人
		createContacts("testCase0_043", "13522068043");
		createContacts("testCase1_043", "13521168043");
		createContacts("testCase2_043", "13521268043");
		createContacts("testCase3_043", "13522368043");
		createContacts("testCase4_043", "13521468043");
		createContacts("testCase5_043", "13521568043");

		// 有联系人才能删除分组
		deleteGroup("TestGroup0_43");

		// 添加分组
		createGroup("TestGroup0_43", 1);

		// 添加联系人
		addGroupMembers("TestGroup0_43", 5);

		intoMyGroupPage();

		clickByName("TestGroup0_43");

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "13522068043");
		// 搜索存在的联系人
		// Assert.assertTrue(searchContact("13522068043" , 1));
		Myassert("找不到联系人：13522068043", searchContact("13522068043", 1));

		// 输入框内，输入搜索内容
		intoContentEditTextById("contact_search_bar", "13521568043");

		// 搜索不存在的联系人
		Myassert("搜索到联系人，理应搜索不到", isExistenceById("no_contact_text"));

		// 返回
		back("tab_contacts");

		deleteGroup("TestGroup0_43");

		// 清理数据
		deleteAllContacts();

		reportLog("分组管理，组内搜索");
	}

	/**
	 * 联系人列表，发短信
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_023() {
		
		startTestCase();
		// 清理数据
		deleteAllContacts();
		deleteAllMMs();
		
		back("tab_contacts");
		
		String casename = this.getTestCaseName();
		String casephone = sendPhone;
		String casecontent = casename +": " + casephone;
		
		createContacts(casename, casephone);
		
		//长按
		clickLongByIdUseJs("contact_name");
		
		//点击发短信
		clickById("mca_msg_txt");
		
		Myassert("没有进入新信息页", isExistenceById("iab_title") && getTextViewNameById("iab_title").equals("新信息"));
		
		intoContentEditTextById("embedded_text_editor", casecontent);
		
		sendMMS();
		
		back("tab_mms");
		
		clickById("from");
		
		Myassert("发送的姓名不正确：" + casename, isExistenceById("iab_title") && getTextViewNameById("iab_title").equals(casename));
		
		Myassert("发送的号码不正确" + casephone, isExistenceById("iab_sub_title") && getTextViewNameById("iab_sub_title").equals(casephone));
		
		Myassert("发送内容不正确" + casecontent, isExistenceById("text_view") && getTextViewNameById("text_view").contains(casename));
		
		// 清理数据
		deleteAllContacts();
		deleteAllMMs();
		reportLog("联系人列表，发短信");
	}
	
	
	/**
	 * 联系人列表，群发短信
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_024() {
		
		startTestCase();
		// 清理数据
		deleteAllContacts();
		deleteAllMMs();
		
		back("tab_contacts");
		
		
		String casename = this.getTestCaseName();
		String casephone = sendPhone;
		
		String casename2 = this.getTestCaseName()+"2";
		String casephone2 = phone;
		
		
		String casecontent = getTestCaseName() + ": test_content";
		
		createContacts(casename, casephone);
		createContacts(casename2, casephone2);
		
		back("tab_contacts");
		
		//点击群发短信
		clickMenuAndSelect(1);
		
		//点击全选
		clickById("iab_ib_more");
		
		//点击添加
		clickById("selection_ok");
		
		
		Myassert("没有进入新信息页", isExistenceById("iab_title") && getTextViewNameById("iab_title").equals("新信息"));
		
		intoContentEditTextById("embedded_text_editor", casecontent);
		
		sendMMS();
		
		sleepTime(2000);
		
		if(isExistenceById("title"))
		{
			if(getTextViewNameById("title").equals("短信发送失败")){
				clickById("dialog_btn_negative");
			}
		}
		
		back("tab_mms");
		
		clickById("from");
		
		//
		Myassert("发送的号码不正确" + casephone, isExistenceById("iab_sub_title") && getTextViewNameById("iab_sub_title").contains(casephone));
		Myassert("发送的号码不正确" + phone, isExistenceById("iab_sub_title") && getTextViewNameById("iab_sub_title").contains(phone));
	
		List<WebElement> list = getLisWebElementById("text_view");
		
		Myassert("发出的号码数量不正确，应为：" + list.size(), list.size() == 2);
		Myassert("两组短信内容不一致" , list.get(0).getText().equals(list.get(1).getText()));
		Myassert("短信内容不正确：" + casecontent, list.get(0).getText().contains("test_content"));
		
		// 清理数据
		deleteAllContacts();
		deleteAllMMs();
		reportLog("联系人列表，群发短信");
		
	}
	
	
	
	
	/**
	 * 联系人详细页，名片分享
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_025() {
		
		startTestCase();
		// 清理数据
		deleteAllContacts();
		deleteAllMMs();
		
		back("tab_contacts");
		
		String casename = getTestCaseName();
		String casephone = sendPhone;
		
		createContacts(casename, casephone);
		
		back("tab_contacts");
		

		clickById("contact_name");
		
		Myassert("没有进入联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		//分享名片
		clickMenuAndSelect(1);
		
		Myassert("没有弹出选择分享软件", getTextViewNameById("android:id/alertTitle").equals("选择分享"));
		
		clickByName("和通讯录");
		
		sleepTime(1000);
		
		Myassert("没有进入新信息页", isExistenceById("iab_title") && getTextViewNameById("iab_title").equals("新信息"));
		
		intoContentEditTextByName("收件人:", phone);
		
		//点击发送
		sendMMS();
		sleepTime(2000);
		
		if(isExistenceById("title"))
		{
			if(getTextViewNameById("title").equals("短信发送失败")){
				clickById("dialog_btn_negative");
			}
		}
		
		back("tab_mms");
		
		Myassert("没有发送短信", isExistenceById("from") && getTextViewNameById("from").contains(phone));
		
		clickById("from");
		
		String text = getTextViewNameById("text_view");
		String adr = "http://pim.10086.cn/wapdownload.php ";
		
		Myassert("分享名片内容没有对应手机号" + casename, text.contains(casename));
		Myassert("分享名片内容没有对应名称" + casephone, text.contains(casephone));
		Myassert("分享名片内容没有对应地址" + adr, text.contains(adr));
		
		reportLog("联系人详细页，名片分享");
		
		deleteAllMMs();
		deleteAllContacts();
	}
	
	/**
	 * 联系人详细页，群组选择(选择已有)
	 */
	@Test(groups = { "contact","group" })
	public void testCase_contact_026() {
		
		startTestCase();
		// 清理数据
		clearGroup();
		deleteAllContacts();
		deleteAllMMs();
		
		back("tab_contacts");
		
		String casename = getTestCaseName();
		String casephone = sendPhone;
		String casegroup = getTestGroupName();
		createContacts(casename, casephone);
		createGroup(casegroup, 1);
		
		back("tab_contacts");
		
		clickById("contact_name");
		
		Myassert("没有进入联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		//分
		clickMenuAndSelect(3);
		
		Myassert("没有进入分组选择", getTextViewNameById("iab_title").equals("分组选择"));

		//选择分组
		getLisWebElementById("check_group_select").get(getGroupNum(casegroup)).click();
		
		sleepTime(2000);
		
		Myassert("没有选择分组", getLisWebElementById("group_choice_ok_btn").get(0).getAttribute("enabled").equals("true"));
		
		clickById("group_choice_ok_btn");
		
		//自动返回联系人详情页
		Myassert("没有自动返回联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		Myassert("联系人分组中，没有还有分组信息：" + casegroup, getTextViewNameById("contact_detail_groups_name").contains(casegroup));
		
		back("tab_contacts");
		
		clearGroup();
		
		reportLog("联系人详细页，群组选择");
		
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
	}
	
	
	/**
	 * 联系人详细页，群组选择(新建分组)
	 */
	@Test(groups = { "contact","group" })
	public void testCase_contact_027() {
		
		startTestCase();
		// 清理数据
		clearGroup();
		deleteAllContacts();
		deleteAllMMs();
		
		back("tab_contacts");
		
		String casename = getTestCaseName();
		String casephone = sendPhone;
		String casegroup = getTestGroupName();
		
		createContacts(casename, casephone);
		
		back("tab_contacts");
		
		clickById("contact_name");
		
		Myassert("没有进入联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		
		//
		clickMenuAndSelect(3);
		
		Myassert("没有进入分组选择", getTextViewNameById("iab_title").equals("分组选择"));

		//点击新增
		clickById("iab_ib_more");
		
		//新建分组
		Myassert("没有进入新建分组", getTextViewNameById("title").equals("新建分组"));
		
		//输入分组名
		intoContentEditTextById("content", casegroup);
		
		//点击保存
		clickById("dialog_btn_positive");
		
		//点击确定
		clickById("group_choice_ok_btn");
		
		sleepTime(2000);
		
		//自动返回联系人详情页
		Myassert("没有自动返回联系人详情页", getTextViewNameById("iab_title").equals("联系人详情"));
		
		Myassert("联系人分组中，没有还有分组信息：" + casegroup, getTextViewNameById("contact_detail_groups_name").contains(casegroup));
		
		back("tab_contacts");
		
		clearGroup();
		
		reportLog("联系人详细页，群组选择(选择新建)");
		
		deleteAllCall();
		deleteAllMMs();
		deleteAllContacts();
	}
	
	/**
	 * 分组-分组管理-群发短信
	 */
	@Test(groups = { "contact","group" })
	public void testCase_contact_028() {
		
		startTestCase();
		// 清理数据
		clearGroup();
		deleteAllContacts();
		deleteAllMMs();
		
		back("tab_contacts");
				
		String casename = this.getTestCaseName();
		String casephone = sendPhone;
		
		String casename2 = this.getTestCaseName()+"2";
		String casephone2 = phone;
		
		String casegroup = getTestGroupName();
		
		String casecontent = getTestCaseName() + ": test_content";
		
		createContacts(casename, casephone);
		createContacts(casename2, casephone2);
		
		createGroup(casegroup, 1);
		
		addAllGroupMembers(casegroup);
		
		back("tab_contacts");
		
		intoMyGroupPage();
		
		clickByName(casegroup);
		
		Myassert("没有进入新建的分组", isExistenceById("iab_title") && getTextViewNameById("iab_title").equals(casegroup));
		
		clickByName("群发短信");

		//点击添加
		clickById("selection_ok");	
		
		Myassert("没有进入新信息页", isExistenceById("iab_title") && getTextViewNameById("iab_title").equals("新信息"));
		
		intoContentEditTextById("embedded_text_editor", casecontent);
		
		sendMMS();
		
		sleepTime(2000);
		
		if(isExistenceById("title"))
		{
			if(getTextViewNameById("title").equals("短信发送失败")){
				clickById("dialog_btn_negative");
			}
		}
		
		back("tab_mms");
		
		clickById("from");
		
		//
		Myassert("发送的号码不正确" + casephone, isExistenceById("iab_sub_title") && getTextViewNameById("iab_sub_title").contains(casephone));
		Myassert("发送的号码不正确" + phone, isExistenceById("iab_sub_title") && getTextViewNameById("iab_sub_title").contains(phone));
	
		List<WebElement> list = getLisWebElementById("text_view");
		
		Myassert("发出的号码数量不正确，应为：" + list.size(), list.size() == 2);
		Myassert("两组短信内容不一致" , list.get(0).getText().equals(list.get(1).getText()));
		Myassert("短信内容不正确：" + casecontent, list.get(0).getText().contains("test_content"));
		
		// 清理数据
		clearGroup();
		deleteAllContacts();
		deleteAllMMs();
		reportLog("分组-分组管理-群发短信");
	}
	
	
	/**
	 * 联系人列表-长按联系人-名片分享
	 */
	@Test(groups = { "contact" })
	public void testCase_contact_029() {
		
		startTestCase();
		// 清理数据
		deleteAllContacts();
		deleteAllMMs();
		
		back("tab_contacts");
		
		String casename = getTestCaseName();
		String casephone = sendPhone;
		
		createContacts(casename, casephone);
		
		back("tab_contacts");
		
		clickLongByIdUseJs("contact_name");
		
		clickByName("分享名片");
		
		Myassert("没有弹出选择分享软件", getTextViewNameById("android:id/alertTitle").equals("选择分享"));
		
		clickByName("和通讯录");
		
		sleepTime(1000);
		
		Myassert("没有进入新信息页", isExistenceById("iab_title") && getTextViewNameById("iab_title").equals("新信息"));
		
		intoContentEditTextByName("收件人:", phone);
		
		//点击发送
		sendMMS();
		sleepTime(2000);
		
		if(isExistenceById("title"))
		{
			if(getTextViewNameById("title").equals("短信发送失败")){
				clickById("dialog_btn_negative");
			}
		}
		
		back("tab_mms");
		
		Myassert("没有发送短信", isExistenceById("from") && getTextViewNameById("from").contains(phone));
		
		clickById("from");
		
		String text = getTextViewNameById("text_view");
		String adr = "http://pim.10086.cn/wapdownload.php ";
		
		Myassert("分享名片内容没有对应手机号" + casename, text.contains(casename));
		Myassert("分享名片内容没有对应名称" + casephone, text.contains(casephone));
		Myassert("分享名片内容没有对应地址" + adr, text.contains(adr));
		
		reportLog("联系人详细页，名片分享");
		
		deleteAllMMs();
		deleteAllContacts();
	}
	
	/**
	 * 联系人列表-长按联系人-名片分享
	 */
	@Test(groups = { "debug" })
	public void testCase_contact_030() {
		//createContacts(username, phone);
		
		
		
	}
	
	/**
	 * 创建重复联系人的类型
	 * @param type
	 */
	public void createContactAsSame(int type){
		
	}

	
	
	/**
	 * 在联系人模块，创建联系人：通过姓名、号码，新建联系人
	 * 
	 * @param username
	 * @param phone
	 */
	public void createContactNameAsSame(String username, String phone) {
		
		// 点击拨号
		clickById("tab_call");

		// 点击联系人
		clickById("tab_contacts");

		// 点击新增按钮
		clickById("iab_ib_action");

		// 验证当前页面为新建联系人
		Assert.assertTrue(isExistenceByName("新建联系人"));

		intoContentEditTextByName("姓名", username);
		
		intoContentEditTextByName("电话号码", phone);

		// 点击保存
		clickById("iab_ib_action");
		
	}
	
	
	
	//------------------------辅助模块测试用------------------------------------//
	
	/**
	 * 手势滑动侧边栏与拨号页、联系人、信息切换。
	 */
	@Test(groups = { "other" })
	public void testCase_other_001() {
		startTestCase();
		// 联系人
		clickById("tab_contacts");
		for (int i = 0; i < 80; i++) {
			swipeToRight();
			sleepTime(1000);
			swipeToLeft();
			sleepTime(1000);
		}
		// 判断当前界面存在和通讯录标题
		// Assert.assertTrue("当前界面不存在和通讯录", this.isExistenceById("iab_title"));
		Myassert("当前界面不存在和通讯录", isExistenceById("iab_title"));

		clickById("tab_call");
		for (int i = 0; i < 80; i++) {
			swipeToRight();
			sleepTime(1000);
			swipeToLeft();
			sleepTime(1000);
		}
		// 判断当前界面存在和通讯录标题
		Myassert("当前界面不存在和通讯录", isExistenceById("iab_title"));

		clickById("tab_mms");
		for (int i = 0; i < 80; i++) {
			swipeToRight();
			sleepTime(1000);
			swipeToLeft();
			sleepTime(1000);
		}
		// 判断当前界面存在和通讯录标题
		Myassert("当前界面不存在和通讯录", isExistenceById("iab_title"));
		reportLog("手势滑动侧边栏与拨号页、联系人、信息切换。");
	}

	/**
	 * 验证侧边栏切换
	 */
	@Test(groups = { "other" })
	public void testCase_other_002() {
		int i;
		int num;
		for (i = 0; i < 200; i++) {
			num = (int) (Math.random() * 10) % 5;
			switch (num) {
			case 4:
				// 点击拨号
				clickById("tab_call");
				// sleepTime(1000);
				break;

			case 3:
				// 点击联系人
				clickById("tab_contacts");
				// sleepTime(1000);
				break;

			case 2:
				// 点击信息
				clickById("tab_mms");
				// sleepTime(1000);
				break;

			case 1:
				// 点击生活助手
				clickById("tab_cloud");
				// sleepTime(1000);
				break;

			case 0:
				// 点击和通讯录
				clickById("iab_title");
				// sleepTime(1000);

				// 点击返回
				driver.sendKeyEvent(AndroidKeyCode.BACK);
				sleepTime(1000);
				break;
			}

		}
		// Assert.assertTrue(isExistenceById("iab_title"));
		Myassert("当前界面不存在和通讯录", isExistenceById("iab_title"));
		reportLog("验证侧边栏切换");
	}

	//------------------------短信模块测试用------------------------------------//
	
	/**
	 * 新建短信，下拉创建短信
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_001() {
		startTestCase();

		// 清除短信
		deleteAllMMs();

		// 创建短信
		createMMs("13522068044", "testCase0_044");
		createMMs("13522168044", "testCase1_044");

		deleteAllMMs();
		// 创建短信
		reportLog("新建短信，下拉创建短信");
	}

	/**
	 * 创建联系人，点击新建，点击选择已有的联系人
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_002() {
		startTestCase();
		deleteAllMMs();
		deleteAllContacts();

		createContacts("testCase0_045", "13522068045");
		createContacts("testCase1_045", "13522168045");

		createMMs("testCase_045_MMS_0");

		createMMs("testCase_045_MMS_1");

		// 清除
		deleteAllContacts();
		deleteAllMMs();

		reportLog(getMethodName(), "创建联系人，点击新建，点击选择已有的联系人");
	}

	/**
	 * 编辑常用语，选择第一条常用语，并发送
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_003() {
		startTestCase();
		// 清除
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新建信息页", isExistenceByName("新信息"));

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068046");

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 右滑
		swipeToRight("bottom");

		// 验证
		Myassert("没有进入编辑常用语", isExistenceById("mms_buttom_useful_sms_edit_btn"));

		// 点击第一个
		WebElement we = getWebElementInList("list_item_txt", "start");

		String str = we.getText().toString().trim();

		we.click();

		// 点击确定
		sendMMS();

		sleepTime(2000);
		
		if(isExistenceById("title"))
		{
			if(getTextViewNameById("title").equals("短信发送失败")){
				clickById("dialog_btn_negative");
			}
		}
		// 返回上一页
		back("tab_mms");

		// 去除信息回收站
		if (isExistenceById("tv_title")) {
			clickById("notice_delete");
		}

		// System.out.println("str " + str);
		Myassert("内容不一致", getTextViewNameById("subject").toString().trim()
				.equals(str));
		reportLog(getMethodName(), "编辑常用语，选择第一条常用语，并发送");
		deleteAllMMs();
	}

	/**
	 * 编辑常用语，选择最后一条常用语，并发送
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_004() {
		startTestCase();
		// 清除
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新建信息页", isExistenceByName("新信息"));

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068047");

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 右滑
		swipeToRight("bottom");

		// 验证
		Myassert("没有进入编辑常用语", isExistenceById("mms_buttom_useful_sms_edit_btn"));

		// 向上滑
		swipeToUp();

		// 点击最后一条
		WebElement we = getWebElementInList("list_item_txt", "end");

		String str = we.getText().toString().trim();

		we.click();

		// 点击确定
		sendMMS();

		// 返回上一页
		back("tab_mms");

		// 去除信息回收站
		if (isExistenceById("tv_title")) {
			clickById("notice_delete");
		}

		System.out.println("str " + str);
		Myassert("未存在短信", isExistenceById("subject"));
		Myassert("内容对比不一致", getTextViewNameById("subject").toString().trim()
				.equals(str));
		reportLog("编辑常用语，选择最后一条常用语，并发送");
		deleteAllMMs();
	}

	/**
	 * 编辑常用语，新增常用语
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_005() {
		startTestCase();
		// 清除
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068048");

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 右滑
		swipeToRight("bottom");

		// 验证
		Myassert("没有进入常用短信编辑",
				isExistenceById("mms_buttom_useful_sms_edit_btn"));

		// 点击编辑常用语
		clickById("mms_buttom_useful_sms_edit_btn");

		// 点击添加
		clickById("mms_buttom_useful_sms_add_btn");

		// 添加常用语
		intoContentEditTextById("content", "这是一段测试常用语01");

		// 点击确定
		clickById("dialog_btn_positive");

		// 等待
		sleepTime(3000);

		// 向上滑
		swipeToUp();

		// 点击最后一条
		WebElement we = getWebElementInList("list_item_txt", "end");

		String str = we.getText().toString().trim();

		we.click();

		// 点击确定
		sendMMS();

		// 返回上一页
		back("tab_mms");

		// 去除信息回收站
		if (isExistenceById("tv_title")) {
			clickById("notice_delete");
		}

		// System.out.println("str " + str);
		Myassert("内容对比不一致", getTextViewNameById("subject").toString().trim()
				.equals(str));
		reportLog("编辑常用语，新增常用语");
		deleteAllMMs();
	}

	/**
	 * 编辑常用语，删除常用语
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_006() {
		startTestCase();
		// 清除
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 右滑
		swipeToRight("bottom");
		sleepTime(2000);

		// 验证
		Myassert("没有进入常用编辑页", isExistenceById("mms_buttom_useful_sms_edit_btn"));

		// 向上滑
		swipeToUp();

		// 点击最后一条
		WebElement we = getWebElementInList("list_item_txt", "end");

		String str = we.getText().toString().trim();

		// 判断是否为自定义常用语
		if (getNumerByText(str) > 0) {
			// 判断为有自定义的常用语，什么都不做
		}
		// 否则新建一自定义常用语
		else {
			// 点击编辑常用语
			clickById("mms_buttom_useful_sms_edit_btn");

			// 点击添加
			clickById("mms_buttom_useful_sms_add_btn");

			// 添加常用语
			intoContentEditTextById("content", "这是一段测试常用语02");

			// 点击确定
			clickById("dialog_btn_positive");

			sleepTime(2000);
		}

		// 点击编辑常用语
		clickById("mms_buttom_useful_sms_edit_btn");

		// 向上滑
		swipeToUp();

		// 获取最后一个元素
		WebElement we2 = getWebElementInList("list_item_txt", "end");

		// 判断为自定义常用语
		Myassert("最有一条常用语非自定义",
				getNumerByText(we2.getText().toString().trim()) > 0);

		// 获取最后一个元素
		WebElement del = getWebElementInList("list_item_btn", "end");

		// 删除
		del.click();

		// 点击退出编辑
		clickById("mms_buttom_useful_sms_exit_btn");

		sleepTime(2000);

		swipeToUp();
		// 再次获取最后元素
		WebElement we3 = getWebElementInList("list_item_txt", "end");
		// 判断为非自定义常用语
		Myassert("最后一条不是常用语",
				getNumerByText(we3.getText().toString().trim()) < 0);
		reportLog("编辑常用语，删除常用语");
	}

	/**
	 * 设置时间、日期方法已经实现;版本为3.9.6点击发送后，跳转到空白页，无法删除已设置的短信
	 */
	@Test(groups = { "mms_error" })
	public void testCase_mms_007() {
		startTestCase();
		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 定时
		clickById("timinglayout");

		// 验证
		Myassert("没有进入时间设置页", isExistenceByName("请设定短信发送时间"));

		// 设置时间
		// setTime("16", "45");

		// 设置年份
		setDate("2017", "03", "21");

		// 点击保存
		clickById("dialog_btn_positive");

		sleepTime(3000);
		reportLog("未实现有Bug");
	}

	/**
	 * 发送表情,选择最左端
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_008() {
		startTestCase();
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 点击表情
		clickById("emoticons");

		// 验证
		Myassert("没有进入表情选项", isExistenceById("emoticon_viewpager"));

		// 点击默认
		clickById("emotionbar_radiobtn_feixin");

		// 选择表情
		selectEmoticon(0);

		// 选择表情
		selectEmoticon(1);

		// 删除第二个表情
		selectEmoticon(20);

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068051");

		// 点击发送
		sendMMS();

		sleepTime(2000);

		back("tab_mms");

		// Assert.assertTrue("发送表情错误",getTextViewNameById("subject").contains("微笑"));
		Myassert("发送表情错误", getTextViewNameById("subject").contains("微笑"));

		reportLog("发送表情,选择最左端");
		sleepTime(2000);

		deleteAllMMs();
	}

	/**
	 * 发送表情,选择最右端
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_009() {
		startTestCase();
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 点击表情
		clickById("emoticons");

		// 验证
		Myassert("没有进入表情选项", isExistenceById("emoticon_viewpager"));

		// 点击默认
		clickById("emotionbar_radiobtn_feixin");

		// 向左滑
		swipeToLeft("bottom");
		sleepTime(1000);
		swipeToLeft("bottom");
		sleepTime(1000);
		swipeToLeft("bottom");
		sleepTime(1000);

		// 选择表情
		selectEmoticon(6);

		// 选择表情
		selectEmoticon(7);

		// 删除第二个表情
		selectEmoticon(20);

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068052");

		// 点击发送
		sendMMS();

		sleepTime(2000);

		back("tab_mms");

		// Assert.assertTrue(getTextViewNameById("subject").contains("恶魔"));
		Myassert("发送表情错误", getTextViewNameById("subject").contains("恶魔"));
		reportLog("发送表情,选择最右端");
		sleepTime(2000);

		deleteAllMMs();
	}

	/**
	 * 精选短信,选择顶部
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_010() {
		startTestCase();
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 点击表情
		clickById("featuremms");

		// 验证
		Myassert("没有进入精选短信页", isExistenceByName("精选短信"));

		// 选择精选短信
		selectFeaturemms(0);

		// 选择第一个
		WebElement we = getWebElementInList("text", "start");
		String subStr = we.getText().toString().substring(0, 4);

		we.click();

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068053");

		// 点击发送
		sendMMS();

		sleepTime(2000);

		back("tab_mms");

		// Assert.assertTrue(getTextViewNameById("subject").contains(subStr));
		Myassert("内容对比不一致", getTextViewNameById("subject").contains(subStr));
		reportLog("精选短信,选择顶部");
		sleepTime(2000);

		deleteAllMMs();
	}

	/**
	 * 精选短信,选择底部
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_011() {
		startTestCase();
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项页", isExistenceById("MMS_option"));

		// 点击表情
		clickById("featuremms");

		// 验证
		Myassert("没有进入精选短信页", isExistenceByName("精选短信"));

		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);

		// 选择精选短信
		selectFeaturemms(9);

		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);
		swipeToUp();
		sleepTime(1000);

		// 选择
		WebElement we = getWebElementInList("text", "end");
		String subStr = we.getText().toString().substring(0, 4);

		we.click();

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068054");

		// 点击发送
		sendMMS();

		sleepTime(2000);

		back("tab_mms");

		// Assert.assertTrue(getTextViewNameById("subject").contains(subStr));
		Myassert("内容对比不一致", getTextViewNameById("subject").contains(subStr));
		reportLog("精选短信,选择底部");
		sleepTime(2000);

		deleteAllMMs();
	}

	/**
	 * 发彩信，选择本地图片
	 */
	@Test(groups = { "debug" })
	public void testCase_mms_012() {
		startTestCase();
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068055");

		intoContentEditTextById("embedded_text_editor", "testCase_055");

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 点击图片
		clickById("picture");

		// 本地相册
		contextMenuTitleSelect(1);
		// 选择相册

		clickByName("相册");

		// 再次选择相册
		clickByName("相册");

		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;

		Point point = new Point(width / 4, height / 2);

		sleepTime(1000);
		this.touchScreen(point);

		Point point2 = new Point(width / 2, height / 5);
		sleepTime(1000);
		this.touchScreen(point2);

		sleepTime(2000);
		// 点击完成
		clickByName("完成");

		sleepTime(3000);

		// 点击发送
		sendMMS();

		sleepTime(2000);

		back("tab_mms");

		Myassert("彩信发送失败1",
				getTextViewNameById("subject").contains("testCase_055"));
		Myassert("彩信发送失败2", isExistenceById("attachment"));
		reportLog("发彩信，选择本地图片");
		sleepTime(2000);

		deleteAllMMs();
	}

	/**
	 * 发送名片，单个
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_013() {
		startTestCase();
		// 清除
		deleteAllMMs();
		deleteAllContacts();

		createContacts("testCase0_056", "13522068056");
		createContacts("testCase1_056", "13522168056");

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068056");

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选项", isExistenceById("MMS_option"));

		// 点击分享名片
		clickById("send_card");

		// 验证
		Myassert("没有进入联系人选择页", isExistenceByName("联系人选择"));

		// 选择联系人数量
		addMMsContactMembers(1);

		// 点击发送
		sendMMS();

		sleepTime(2000);

		back("tab_mms");

		Myassert("发送失败", isExistenceById("from"));

		// 进入第一条短信
		clickById("from");

		String allString = getTextViewNameById("text_view");

		Myassert(
				"短信发送失败",
				(allString.contains("testCase0_056"))
						&& (allString.contains("13522068056")));
		reportLog("发送名片，单个");
		back("tab_mms");

		// 清除
		deleteAllMMs();

		deleteAllContacts();

	}

	/**
	 * 发送名片，多个
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_014() {
		startTestCase();
		// 清除
		deleteAllMMs();
		deleteAllContacts();

		createContacts("testCase0_057", "13522068057");
		createContacts("testCase1_057", "13522168057");
		createContacts("testCase2_057", "13522268057");

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068056");

		// 短信选项中，更多
		clickById("add_mmspart_button");

		// 验证
		Myassert("没有进入更多选择", isExistenceById("MMS_option"));

		// 点击分享名片
		clickById("send_card");

		// 验证
		Myassert("没有进入联系人选择页", isExistenceByName("联系人选择"));

		// 选择联系人数量
		addMMsContactMembers(3);

		// 点击发送
		sendMMS();

		sleepTime(2000);

		back("tab_mms");

		Myassert("短信发送失败", isExistenceById("from"));

		// 进入第一条短信
		clickById("from");

		String allString = getTextViewNameById("text_view");

		Myassert(
				"发送失败，内容不含：testCase0_057",
				(allString.contains("testCase0_057"))
						&& (allString.contains("13522068057")));
		Myassert(
				"发送失败，内容不含：testCase1_057",
				(allString.contains("testCase1_057"))
						&& (allString.contains("13522168057")));
		Myassert(
				"发送失败，内容不含：testCase2_057",
				(allString.contains("testCase2_057"))
						&& (allString.contains("13522268057")));
		reportLog("发送名片，多个");
		back("tab_mms");

		// 清除
		deleteAllMMs();

		deleteAllContacts();

	}

	/**
	 * 输入用户、内容，返回.出现草稿箱
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_015() {
		startTestCase();
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068058");

		intoContentEditTextById("embedded_text_editor", "testCase_058");

		back("tab_mms");

		// Assert.assertTrue("",isExistenceById("from")&&getTextViewNameById("from").contains("草稿"));
		Myassert("没有生产草稿",
				isExistenceById("from")
						&& getTextViewNameById("from").contains("草稿"));

		reportLog("输入用户、内容，返回.出现草稿箱");
		deleteAllMMs();
	}

	/**
	 * 输入用户、内容，清空，返回，不产生草稿
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_016() {
		startTestCase();
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068059");

		intoContentEditTextById("embedded_text_editor", "testCase_059");

		back("tab_mms");

		Myassert("没有生成草稿",
				isExistenceById("from")
						&& getTextViewNameById("from").contains("草稿"));

		// 再次进入
		clickById("from");

		// 清空内容
		intoContentEditTextById("embedded_text_editor", "");

		back("tab_mms");

		// 验证
		Myassert("草稿删除失败", isExistenceById("btResolve"));
		reportLog("输入用户、内容，清空，返回，不产生草稿");
		deleteAllMMs();
	}

	/**
	 * 输入用户、内容，清空，返回，不产生草稿
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_017() {
		startTestCase();
		deleteAllMMs();

		clickById("tab_mms");

		// 点击新建短信
		clickById("iab_ib_action");

		// 验证
		Myassert("没有进入新增信息页", isExistenceByName("新信息"));

		// 向输入框收入内容
		intoContentEditTextByName("收件人:", "13522068060");

		back("tab_mms");

		// 验证
		Myassert("草稿没有被删除", isExistenceById("btResolve"));

		reportLog("输入用户、内容，清空，返回，不产生草稿");

		deleteAllMMs();
	}

	/**
	 * 短信模块，全部标为已读(测试机需要安装短信生成器)
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_018() {
		startTestCase();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信，已经包括了清空短信
		prepareUnreadMMS();

		// 标为已读
		clickMenuAndSelect(1);

		List<WebElement> list = getLisWebElementById("unread");

		Myassert("全部标为已读失败", list.size() == 0);

		reportLog("短信模块，全部标为已读(测试机需要安装短信生成器)");

		deleteAllMMs();

	}

	/**
	 * 短信模块，未读信息(测试机需要安装短信生成器)
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_019() {
		startTestCase();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信，已经包括了清空短信
		int size = prepareUnreadMMS();

		// 标为未读
		clickMenuAndSelect(6);

		// 获取当前短信数量
		Myassert("未读短信数量不一致：准备的未读短信为：" + size, getMMsCount() == size);

		reportLog("短信模块，未读信息(测试机需要安装短信生成器)");

		back("tab_mms");

		deleteAllMMs();
	}

	/**
	 * 短信模块，未读信息读取一条，未读短信变成已读(测试机需要安装短信生成器)
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_020() {
		startTestCase();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		int size = prepareUnreadMMS();

		// 标为未读
		clickMenuAndSelect(6);
		// 获取当前短信数量
		Myassert("未读短信数量不一致：准备的未读短信为：" + size, getMMsCount() == size);

		clickById("tab_mms");
		clickMenuAndSelect(6);
		List<WebElement> listFir = getLisWebElementById("from");

		String PhoneFir = listFir.get(0).getText();

		// 进入短信
		listFir.get(0).click();
		sleepTime(1000);

		// 返回未读短信
		this.backPage(1);
		Myassert("未能返回未读信息列表页", isExistenceByName("未读信息"));

		// 第二次获取
		List<WebElement> listSec = getLisWebElementById("from");

		String PhoneSec = listSec.get(0).getText();

		Myassert(
				"未读信息读取一条，未读短信未能变成已读",
				(listFir.size() == (listSec.size() + 1))
						&& !(PhoneFir.equals(PhoneSec)));

		reportLog("短信模块，未读信息读取一条，未读短信变成已读(测试机需要安装短信生成器)");

		back("tab_mms");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,清空信息
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_021() {
		startTestCase();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS();

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		String firPhone = list.get(0).getText();
		int firSize = list.size();

		list.get(0).click();

		// 选择清空信息
		clickMenuAndSelect(1);

		// 点击删除
		clickById("dialog_btn_positive");

		// 等待页面刷新
		sleepTime(3000);
		list = getLisWebElementById("from");
		String secPhone = list.get(0).getText();
		int secSize = list.size();

		// 自动返回列表
		Myassert("没有自动返回短信列表", getTextViewNameById("iab_title").equals("和通讯录"));
		Myassert("删除短信失败",
				(firSize == secSize + 1) && (!firPhone.equals(secPhone)));

		reportLog("短信对聊页,清空信息");

		back("tab_mms");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,新建联系人
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_023() {
		startTestCase();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS();

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 新建联系人
		clickMenuAndSelect(2);

		Myassert("没有进入新建联系人", getTextViewNameById("iab_title").equals("新建联系人"));

		intoContentEditTextByName("姓名", "testCase_065");

		// 点击保存
		clickById("iab_ib_action");

		Myassert("姓名保存失败",
				getTextViewNameById("iab_title").equals("testCase_065"));

		reportLog("短信对聊页,新建联系人");

		back("tab_mms");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,添加到已有联系人
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_024() {
		startTestCase();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS();

		createContacts("testCase_066", "13522068066");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 新建联系人
		clickMenuAndSelect(3);

		Myassert("没有进入新建联系人", getTextViewNameById("iab_title").equals("新建联系人"));

		Myassert("没有联系人", isExistenceById("contact_name"));

		// 选择联系人
		clickById("contact_name");

		// 点击保存
		clickById("iab_ib_action");

		Myassert("姓名保存失败",
				getTextViewNameById("iab_title").equals("testCase_066"));

		reportLog("短信对聊页,添加到已有联系人");

		back("tab_mms");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,加入黑名单
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_025() {
		startTestCase();
		deleteBlacklist();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS();

		deleteBlacklist();

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 黑名单
		clickMenuAndSelect(4);

		String tmpPhone = getTextViewNameById("iab_sub_title");

		back("tab_mms");

		OpenTabMenu("防打扰", "黑名单");

		Myassert("不存在黑名单联系人", isExistenceByName("管理黑名单"));

		Myassert("加入黑名单的联系人不一致",
				getTextViewNameById("phone_name").equals(tmpPhone));

		back("tab_mms");

		deleteBlacklist();

		reportLog("短信对聊页,加入黑名单");

		back("tab_mms");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,全选
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_026() {
		startTestCase();
		deleteAllMMs();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2", "4");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 更多
		clickMenuAndSelect(5);

		// 点击全选
		clickById("mca_ib_select");

		int num = getNumerByText(getTextViewNameById("mca_title"));

		Myassert("没有全选联系人", num == 4);

		backPage(1);

		reportLog("短信对聊页,全选");

		back("tab_mms");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,多选
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_027() {
		startTestCase();
		deleteAllMMs();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2", "4");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 更多
		clickMenuAndSelect(5);

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		// 点击最后两个
		ll.get(ll.size() - 1).click();

		ll.get(ll.size() - 2).click();

		int num = getNumerByText(getTextViewNameById("mca_title"));

		Myassert("没有联系人数量选择有异常", num == 2);

		backPage(1);

		reportLog("短信对聊页,多选");

		back("tab_mms");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,删除
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_028() {
		startTestCase();
		deleteAllMMs();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2", "4");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 更多
		clickMenuAndSelect(5);

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		int firSize = ll.size();

		// 点击最后两个
		ll.get(firSize - 1).click();

		// 点击删除
		clickByName("删除");

		// 确认删除
		clickById("dialog_btn_positive");

		ll = getLisWebElementById("text_view");
		int secSize = ll.size();

		Myassert("短信删除失败", firSize == secSize + 1);

		reportLog("短信对聊页,删除短信");

		back("tab_mms");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,收藏
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_029() {
		startTestCase();
		deleteAllMMs();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2", "4");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 更多
		clickMenuAndSelect(5);

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		int firSize = ll.size();

		// 点击最后
		ll.get(firSize - 1).click();

		// 点击收藏
		clickByName("收藏");

		String tmpPhone = getTextViewNameById("iab_sub_title");

		backPage(1);

		// 进入信息收藏
		clickMenuAndSelect(4);

		Myassert("未进入信息收藏页", getTextViewNameById("iab_title").equals("信息收藏"));

		Myassert("没有发现收藏的信息号码", getTextViewNameById("from").equals(tmpPhone));

		clickByName(tmpPhone);

		// 清空信息
		clickMenuAndSelect(1);

		back("tab_mms");

		reportLog("短信对聊页,收藏");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,转发（填写手机号码）
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_030() {
		startTestCase();
		deleteAllMMs();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2", "4");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 更多
		clickMenuAndSelect(5);

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		int firSize = ll.size();

		// 点击最后
		ll.get(firSize - 1).click();

		// 点击收藏
		clickByName("转发");

		Myassert("没有进入新信息页", getTextViewNameById("iab_title").equals("新信息"));

		intoContentEditTextByName("收件人:", phone);

		// 点击发送
		sendMMS();

		sleepTime(2000);

		Myassert("没有发送成功", getTextViewNameById("iab_sub_title").equals(phone));

		back("tab_mms");

		reportLog("短信对聊页,转发（填写手机号码）");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,转发（选择分组联系人）
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_031() {
		startTestCase();
		deleteAllMMs();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		createContacts("testCase_073", phone);
		prepareUnreadMMS("2", "4");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 更多
		clickMenuAndSelect(5);

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		int firSize = ll.size();

		// 点击最后
		ll.get(firSize - 1).click();

		// 点击收藏
		clickByName("转发");

		Myassert("没有进入新信息页", getTextViewNameById("iab_title").equals("新信息"));

		// 点击添加按钮
		clickById("add_recipients");

		// 点击联系人
		clickById("contact_name");

		// 点击添加
		clickById("selection_ok");

		// 点击发送
		sendMMS();

		sleepTime(2000);

		Myassert("没有发送成功", getTextViewNameById("iab_sub_title").equals(phone));

		back("tab_mms");

		reportLog("短信对聊页,转发（选择分组联系人）");

		deleteAllContacts();
		deleteAllMMs();
	}

	/**
	 * 短信对聊页,长按转发（填写手机号码）
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_032() {
		startTestCase();
		deleteAllMMs();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		prepareUnreadMMS("2", "4");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		int firSize = ll.size();

		// 判断短信为自己发送的，还是接收
		if (isExistenceById("avatar_right")) {
			// 长按最后一个
			clickLongByElementUseJs(ll.get(firSize - 1));

			// 点击转发
			contextMenuTitleSelect(2);
		} else {
			// 长按最后一个
			clickLongByElementUseJs(ll.get(firSize - 1));

			// 点击转发
			contextMenuTitleSelect(1);
		}

		Myassert("没有进入新信息页", getTextViewNameById("iab_title").equals("新信息"));

		intoContentEditTextByName("收件人:", phone);

		// 点击发送
		sendMMS();

		sleepTime(2000);

		Myassert("没有发送成功", getTextViewNameById("iab_sub_title").equals(phone));

		back("tab_mms");

		reportLog("短信对聊页,长按转发（填写手机号码）");

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,长按转发（选择分组联系人）
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_033() {
		startTestCase();
		deleteAllMMs();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。
		setMessagePop(false);
		// 清空短信
		// 准备未读短信（保证两条以上），已经包括了清空短信
		createContacts("testCase_075", phone);
		prepareUnreadMMS("2", "4");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		int firSize = ll.size();
		
		Myassert("短信内容为0", firSize > 0);

		// 长按最后一个
		clickLongByElementUseJs(ll.get(firSize - 1));

		// 点击转发
		contextMenuTitleSelect(1);

		Myassert("没有进入新信息页", getTextViewNameById("iab_title").equals("新信息"));

		// 点击添加按钮
		clickById("add_recipients");

		// 点击联系人
		clickById("contact_name");

		// 点击添加
		clickById("selection_ok");

		// 点击发送
		sendMMS();

		sleepTime(2000);

		Myassert("没有发送成功", getTextViewNameById("iab_sub_title").equals(phone));

		back("tab_mms");

		reportLog("短信对聊页,转发（选择分组联系人）");

		deleteAllContacts();
		deleteAllMMs();
	}

	/**
	 * 短信对聊页,长按重发（需要点击自己发送的短信）
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_034() {
		startTestCase();

		deleteAllMMs();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_076", sendPhone);

		// 新建短信
		createMMs("testCase_076，重发短信测试");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		// 长按第一个
		clickLongByElementUseJs(ll.get(0));

		Myassert("没有重发按钮", getLisWebElementById("context_menu_title").get(0)
				.getText().equals("重发"));
		// 点击重发
		contextMenuTitleSelect(1);

		sleepTime(2000);

		// Assert.assertTrue("重发失败", isExistenceById("delivered_indicator"));

		reportLog("短信对聊页,长按重发（需要点击自己发送的短信）");

		deleteAllContacts();

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,长按查看信息详情
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_035() {
		startTestCase();

		deleteAllMMs();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。

		prepareUnreadMMS("3", "1");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		String currentPhone = getTextViewNameById("iab_sub_title");

		// 判断短信为自己发送的，还是接收
		if (isExistenceById("avatar_right")) {
			// 长按第一个
			clickLongByElementUseJs(ll.get(0));
			// 点击查看信息详情
			contextMenuTitleSelect(4);
		} else {
			// 长按第一个
			clickLongByElementUseJs(ll.get(0));
			// 点击查看信息详情
			contextMenuTitleSelect(3);
		}

		Myassert("没有弹出信息详情", isExistenceById("title"));

		// 是否需要添加其他验证
		Myassert("没有找到号码", getTextViewNameById("hints").contains(currentPhone));

		clickById("dialog_btn_positive");

		back("tab_mms");

		reportLog("短信对聊页,长按查看信息详情");

		deleteAllContacts();

		deleteAllMMs();
	}

	/**
	 * 短信对聊页,长按收藏短信
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_036() {
		startTestCase();

		deleteAllMMs();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。

		prepareUnreadMMS("3", "1");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		String currentPhone = getTextViewNameById("iab_sub_title");

		// 判断短信为自己发送的，还是接收
		if (isExistenceById("avatar_right")) {
			// 长按第一个
			clickLongByElementUseJs(ll.get(0));
			// 点击查看信息详情
			contextMenuTitleSelect(5);
		} else {
			// 长按第一个
			clickLongByElementUseJs(ll.get(0));
			// 点击查看信息详情
			contextMenuTitleSelect(4);
		}

		backPage(1);

		// 进入信息收藏
		clickMenuAndSelect(4);

		Myassert("未进入信息收藏页", getTextViewNameById("iab_title").equals("信息收藏"));

		Myassert("没有发现收藏的信息号码", getTextViewNameById("from")
				.equals(currentPhone));

		clickByName(currentPhone);

		// 清空信息
		clickMenuAndSelect(1);

		back("tab_mms");

		reportLog("短信对聊页,长按收藏短信");

		deleteAllContacts();

		deleteAllMMs();

	}

	/**
	 * 短信对聊页,长按删除
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_037() {
		startTestCase();

		deleteAllMMs();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。

		prepareUnreadMMS("3", "1");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		int firSize = list.size();

		list.get(0).click();

		// 点击全选
		List<WebElement> ll = getLisWebElementById("text_view");

		// 判断短信为自己发送的，还是接收
		if (isExistenceById("avatar_right")) {
			// 长按第一个
			clickLongByElementUseJs(ll.get(0));
			// 点击删除
			contextMenuTitleSelect(6);
		} else {
			// 长按第一个
			clickLongByElementUseJs(ll.get(0));
			// 点击删除
			contextMenuTitleSelect(5);
		}

		back("tab_mms");

		list = getLisWebElementById("from");

		int secSize = list.size();

		Myassert("删除短信失败", firSize == secSize + 1);

		reportLog("短信对聊页,长按删除");

		deleteAllContacts();

		deleteAllMMs();

	}

	/**
	 * 短信对聊页,拨号
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_038() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。

		prepareUnreadMMS("3", "1");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		String currentPhone = getTextViewNameById("iab_sub_title");

		clickById("iab_ib_action");

		sleepTime(2000);
		// 点击确定
		clickByName("确定");

		sleepTime(2000);

		back("tab_call");

		Myassert("没有找到拨号记录", isExistenceById("line1"));

		Myassert("拨号记录，拨打的号码不一致",
				getTextViewNameById("line1").equals(currentPhone));

		reportLog("短信对聊页,拨号");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();

	}

	/**
	 * 短信对聊页,点击短信的号码(只能是纯号码)-拨号
	 */
	@Test(groups = { "" })
	public void testCase_mms_040() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_081", sendPhone);

		createMMs(phone);

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		String currentPhone = getTextViewNameById("text_view").toString()
				.trim();

		clickById("text_view");

		sleepTime(2000);

		Myassert("没有弹出选择框", isExistenceById("title"));
		Myassert("不是对短信上的号码进行操作",
				getTextViewNameById("title").contains(currentPhone));

		// 打电话
		contextMenuTitleSelect(1);

		clickById("iab_ib_action");

		sleepTime(2000);
		// 点击确定
		clickByName("确定");

		sleepTime(2000);

		back("tab_call");

		Myassert("没有找到拨号记录", isExistenceById("line1"));

		Myassert("拨号记录，拨打的号码不一致",
				getTextViewNameById("line1").equals(currentPhone));

		reportLog("短信对聊页,点击号码选择拨号");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();

	}

	/**
	 * 短信对聊页,点击短信的号码(只能是纯号码)-发短信
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_041() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_082", sendPhone);

		createMMs(phone);

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		String currentPhone = getTextViewNameById("text_view").toString()
				.trim();

		clickById("text_view");

		Myassert("没有弹出选择框", isExistenceById("title"));
		Myassert("不是对短信上的号码进行操作",
				getTextViewNameById("title").contains(currentPhone));

		// 发短信
		contextMenuTitleSelect(2);

		Myassert("没有进入新建短信页",
				getTextViewNameById("iab_sub_title").contains(currentPhone));

		intoContentEditTextById("embedded_text_editor",
				"testCase_082,点击短信的号码(只能是纯号码)-发短信");

		sendMMS();

		back("tab_mms");

		Myassert("没有找到新号码的短信",
				searListContainName(getLisWebElementById("from"), currentPhone));

		reportLog("对聊页， 点击短信的号码(只能是纯号码)-发短信");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();

	}

	/**
	 * 短信对聊页,点击短信的号码(只能是纯号码)-添加为联系人
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_042() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_083", sendPhone);

		createMMs(phone);

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		String currentPhone = getTextViewNameById("text_view").toString()
				.trim();

		clickById("text_view");

		Myassert("没有弹出选择框", isExistenceById("title"));
		Myassert("不是对短信上的号码进行操作",
				getTextViewNameById("title").contains(currentPhone));

		// 添加为联系人
		contextMenuTitleSelect(3);

		Myassert("没有进入新建联系人页", getTextViewNameById("iab_title").equals("新建联系人"));

		intoContentEditTextByName("姓名", "testCase01_083");

		clickById("iab_ib_action");

		sleepTime(2000);

		back("tab_contacts");

		Myassert(
				"没有找到新号码的联系人",
				searListContainName(getLisWebElementById("contact_name"),
						"testCase01_083"));

		reportLog("短信对聊页,点击号码选择添加为联系人");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();

	}

	/**
	 * 短信对聊页,点击短信的号码(只能是纯号码)-复制
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_043() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信。

		createContacts("testCase_084", sendPhone);

		createMMs(phone);

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		list.get(0).click();

		String currentPhone = getTextViewNameById("text_view").toString()
				.trim();

		clickById("text_view");

		Myassert("没有弹出选择框", isExistenceById("title"));
		Myassert("不是对短信上的号码进行操作",
				getTextViewNameById("title").contains(currentPhone));

		// 添加复制
		contextMenuTitleSelect(4);

		// 粘贴到输入框
		pasteString("embedded_text_editor");

		sendMMS();

		// 获取列表
		List<WebElement> ll = getLisWebElementById("text_view");

		Myassert("没有复制成功", ll.size() == 2);

		Myassert(
				"复制的内容不一致",
				ll.get(0).getText().toString().trim()
						.equals(ll.get(1).getText().toString().trim()));

		back("tab_mms");

		reportLog("短信对聊页,点击号码选择添加为联系人");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();

	}

	/**
	 * 短信列表,搜索短信，搜索条件号码或内容
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_044() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("7", "2");

		createMMs(phone, "testCase_085：短信列表,搜索短信内容：是我就是我；搜索条件号码 : " + sendPhone);

		clickById("tab_mms");

		// 搜索框输入搜索内容：内容中的号码
		intoContentEditTextById("contact_search_bar", sendPhone);

		Myassert("没有搜索到相关的结果", isExistenceById("sms_search"));
		Myassert("搜索结果中，不含搜索内容",
				getTextViewNameById("subject").contains(sendPhone));

		// 搜索框输入搜索内容：发送者号码
		intoContentEditTextById("contact_search_bar", phone);

		Myassert("没有搜索到相关的结果", isExistenceById("sms_search"));
		Myassert("搜索结果中，不含搜索内容", getTextViewNameById("from").contains(phone));

		// 搜索框输入搜索内容：内容前部分内容
		intoContentEditTextById("contact_search_bar", "testCase_085");

		Myassert("没有搜索到相关的结果", isExistenceById("sms_search"));
		Myassert("搜索结果中，不含搜索内容",
				getTextViewNameById("subject").contains("testCase_085"));

		// 搜索框输入搜索内容：内容后部分的内容
		intoContentEditTextById("contact_search_bar", "是我就是我");

		Myassert("没有搜索到相关的结果", isExistenceById("sms_search"));
		Myassert("搜索结果中，不含搜索内容",
				getTextViewNameById("subject").contains("是我就是我"));

		back("tab_mms");

		reportLog("短信列表,搜索短信，搜索条件号码或内容");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();

	}

	/**
	 * 短信列表,加入黑名单（长按短信）
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_045() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("3", "1");

		deleteBlacklist();

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		WebElement we = list.get(0);

		String currentPhone = we.getText().toString().trim();

		// 长按第一个
		clickLongByElementUseJs(we);

		clickByName("加入黑名单");

		OpenTabMenu("防打扰", "黑名单");

		Myassert("不存在黑名单联系人", isExistenceByName("管理黑名单"));

		Myassert("加入黑名单的联系人不一致",
				currentPhone.contains(getTextViewNameById("phone_name")));

		back("tab_mms");

		deleteBlacklist();

		reportLog("短信列表,加入黑名单（长按短信）");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();

	}

	/**
	 * 短信列表,标为已读（长按短信）
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_046() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("7", "4");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("unread");

		int size = list.size() - 1;
		// System.out.println("size= " + size);

		Myassert("不存在未读短信", (size >= 0));

		for (int i = size; i >= 0; i--) {
			// 长按
			clickLongByElementUseJs(list.get(i));
			sleepTime(1000);

			clickByName("标记为已读");
			sleepTime(1000);
		}

		Myassert("仍有未读短信 ", !isExistenceById("unread"));

		reportLog("短信列表,标为已读（长按短信）");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();

	}

	/**
	 * 短信列表,批量回复（长按短信）
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_047() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("2", "1");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		String phonestring = new String("");
		// 为了方便，添加一段字符串中
		for (WebElement we : list) {

			phonestring = phonestring + we.getText() + ";";
		}

		System.out.println("查找的字符串：" + phonestring);

		clickLongByElementUseJs(list.get(0));

		// 点击全选
		clickById("mca_ib_select");

		clickByName("批量回复");

		Myassert("没有进入新信息页", isExistenceById("iab_title"));
		Myassert("进入页面错误", getTextViewNameById("iab_title").equals("新信息"));

		intoContentEditTextById("embedded_text_editor", "testCase_088: 批量回复");

		sendMMS();

		List<WebElement> toList = getLisWebElementById("to_contact_name");

		for (int j = 0; j <= toList.size() - 1; j++) {
			Myassert("号码中没有包含：" + toList.get(j).getText(),
					phonestring
							.contains(getNumerByText(toList.get(j).getText())
									+ ""));
		}

		backPage(2);

		reportLog("短信列表,批量回复（长按短信）");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();

	}

	/**
	 * 短信列表,删除（长按短信）
	 */
	@Test(groups = { "mms" })
	public void testCase_mms_048() {
		startTestCase();

		deleteAllMMs();
		deleteAllCall();
		deleteAllContacts();
		// 环境准备
		// 接收短信时需要关闭弹窗提醒。才可生产未读短信
		prepareUnreadMMS("4", "1");

		clickById("tab_mms");

		List<WebElement> list = getLisWebElementById("from");

		clickLongByElementUseJs(list.get(0));

		// 点击全选
		clickById("mca_ib_select");

		clickByName("删除");

		// 确定删除
		clickById("dialog_btn_positive");

		sleepTime(3000);

		Myassert("删除短信失败", isExistenceById("btResolve"));

		reportLog("短信列表,删除（长按短信）");

		deleteAllContacts();
		deleteAllCall();
		deleteAllMMs();
	}

	@Test(groups = { "more" })
	public void testCase_mms_049() {
		startTestCase();
		System.out.println("AppUtil.getCurrentDate()" + AppUtil.getCurrentDate());
		//AppUtil.getCurrentDate();
	}




	// ////////////////////////////////////辅助方法//////////////////////////////////////////


	
	public void sendMMS(){
		AppUtil.sendMMS();
	}
	
	
	/**
	 * 获取分组名称，根据用例名称生成
	 * @return
	 */
	public String getTestGroupName(){
		return AppUtil.getTestGroupName();
	}
	
	
	/**
	 * 截取子字符串
	 * @param str
	 * @param string
	 * @return
	 */
	public String getSubString(String str, String string){
		return AppUtil.getSubString(str, string);
	}
	
	/**
	 * 截取子字符串
	 * @param str
	 * @param string
	 * @return
	 */
	public String getSubString(String str, String string, int index){
		return AppUtil.getSubString(str, string, index);
	}
	
	/**
	 * 将设置值抽象出来
	 * @param bl
	 * @param id
	 */
	public void setValue(boolean bl, String id){
		AppUtil.setValue(bl,id);
	}
	
	
	/**
	 * 设置拨号按键声音和震动
	 * @param bl
	 */
	public void setCallShockAndVoice(boolean bl){
		AppUtil.setCallShockAndVoice(bl);
	}
	
	
	/**
	 * 通话记录生成
	 * @param phone 需要输入号码，生成该号码的记录
	 * @param time 通话时间大于0
	 * @param type 选择记录类型分别为：已接/已拨/未接来电，对应值(1/2/3)
	 * @param isRead type选择3后才有效，选择记录是否为已读/未读(1/2)
	 * <p>实例：prepareUnreadCall("13800138000", 15, 3, 2),生成一条未读的未接来电
	 */
	public String prepareUnreadCall(String phone, int time, int type, int isRead) {
		return AppUtil.prepareUnreadCall(phone, time, type, isRead);
	}

	
	
	/**
	 * 通话记录生成
	 * @param phone 需要输入号码，生成该号码的记录
	 * @param time 通话时间大于0
	 * @param type 选择记录类型分别为：已接/已拨/未接来电，对应值(1/2/3)
	 * @param isRead type选择3后才有效，选择记录是否为已读/未读(1/2)
	 * <p>实例：callHistory("13800138000", 15, 3, 2),生成一条未读的未接来电
	 */
	public String callHistory(String phone, int time, int type, int isRead){
		return AppUtil.callHistory(phone, time, type, isRead);
	}
	
	
	// /////////////webView////////////////////

	public void intoHelp(){
		AppUtil.intoHelp();
		
	}
	
	public void IntoPwd() {
		AppUtil.IntoPwd();
	}

	/**
	 * 切换WEB页面查找元素
	 */
	public static void switchtoWeb() {
		AppUtil.switchtoWeb();
	}
	
	
	// //////////////////短信模块/////////////////////

	public void hasContact(){
		AppUtil.hasContact();
	}
	
	
	/**
	 * 清除所有不相干的分组
	 */
	public void clearGroup() {
		AppUtil.clearGroup();
	}

	/**
	 * 判断字符含有字符
	 * 
	 * @param name
	 * @return
	 */
	public boolean isContains(String name) {
		return AppUtil.isContains(name);
	}

	/**
	 * 准备未读短信,创建含多条的短信
	 */
	public void prepareUnreadMMS(String num1, String num2) {
		AppUtil.prepareUnreadMMS(num1, num2);
	}

	/**
	 * 准备未读短信
	 */
	public int prepareUnreadMMS() {
		return AppUtil.prepareUnreadMMS();
	}

	/**
	 * 取消接收新消息弹窗：true为开启；false为关闭
	 */
	public void setMessagePop(boolean bl) {
		AppUtil.setMessagePop(bl);
	}

	/**
	 * 在精选短信列表
	 * 
	 * @param num
	 */
	public void selectFeaturemms(int num) {
		AppUtil.selectFeaturemms(num);
	}

	/**
	 * 选择选择第几个表情（0-19），20时删除
	 * 
	 * @param id
	 * @param num
	 */
	public void selectEmoticon(int num) {
		AppUtil.selectEmoticon(num);
	}

	/**
	 * 输入时、分，设置时间。注意时间的取值。
	 * 
	 * @param hour
	 * @param minute
	 */
	public void setDate(String year, String month, String date) {
		AppUtil.setDate(year, month, date);
	}

	/**
	 * 输入时、分，设置时间。注意时间的取值。
	 * 
	 * @param hour
	 * @param minute
	 */
	public void setTime(String hour, String minute) {
		AppUtil.setTime(hour, minute);
	}

	/**
	 * 通过控件id，判断是设置什么类型的时间。
	 * <p>
	 * 设置时间（1-24）注意12或24时间。
	 * <p>
	 * 设置分钟（0-59）
	 * <p>
	 * 注意：没有设置异常机制，自行判断。
	 * 
	 * @param time
	 */
	public void setValue(String id, String time) {
		AppUtil.setValue(id, time);
		
	}

	/**
	 * 通过控件ID,输入第几个，获取元素对象。
	 * <p>
	 * （用于定时发送短信，时间控件内选择时间或日期）
	 * 
	 * @param id
	 * @param num
	 * @return
	 */
	public WebElement getWebElementByAndroidUIAutomator(String id, int num) {
		return AppUtil.getWebElementByAndroidUIAutomator(id, num);
	}

	/**
	 * 点击新建，选择联系人列表中，默认是第一个联系人,输入短信内容(短信内容不支持空格)
	 * 
	 * @param num
	 * @param content
	 */
	public void createMMs(String content) {
		AppUtil.createMMs(content);
	}

	/**
	 * 在短信选择联系人页，选择联系人的个数（num<7）
	 * 
	 * @param num
	 */
	public void addMMsContactMembers(int num) {
		AppUtil.addMMsContactMembers(num);
	}

	/**
	 * 在短信页，根据搜索内容获取结果
	 */
	public boolean searchContactInMMs(String contact) {
		return AppUtil.searchContactInMMs(contact);

	}

	/**
	 * 短信新建，输入号码，短信内容，点击发送。（下拉新建短信）
	 * 
	 * @param phone
	 * @param content
	 */
	public void createMMs(String phone, String content) {
		AppUtil.createMMs(phone, content);

	}

	/**
	 * 获取短信列表中，联系人图标坐标列表，空值返回null
	 * 
	 * @return
	 */
	public List<Point> getMMsPoint() {
		return AppUtil.getMMsPoint();
	}

	/**
	 * 获取短信列表中的数量，通过获取头像的控件来获取数量，超于一页的联系人数量无法获取。
	 */
	public int getMMsCount() {
		return AppUtil.getMMsCount();
	}

	/**
	 * 清除联系人列表中所有的短信
	 */
	public void deleteAllMMs() {
		AppUtil.deleteAllMMs();
	}

	/**
	 * 清除特殊类型的短信
	 */
	public void clearSpecialMMs(List<WebElement> list) {
		AppUtil.clearSpecialMMs(list);
	}

	// //////////////////////分组管理////////////////////////

	/**
	 * 选择列表中什么位置的文件(top \ mid \ bottom)
	 * 
	 * @param location
	 */
	public void scrollAndSelect(String GroupName, String location) {
		AppUtil.scrollAndSelect(GroupName, location);
	}

	/**
	 * 选择列表中间的铃声
	 */
	public void scrollToMidAndSelect() {
		AppUtil.scrollToMidAndSelect();
	}

	/**
	 * 移动到列表的底部，并选择铃声
	 */
	public void scrollToBottomAndSelect() {
		AppUtil.scrollToBottomAndSelect();
	}

	/**
	 * 删除分组联系人，输入组名及删除数量
	 * 
	 * @param GroupName
	 * @param num
	 */
	public void deleteGroupMembers(String GroupName, int num) {
		AppUtil.deleteGroupMembers(GroupName, num);
		
	}

	/**
	 * 参数num为添加人数，num必须少于7（控件仅获取当前页的控件） 参数GroupName分组名
	 * 
	 * @param GroupName
	 * @param num
	 */
	public void addGroupMembers(String GroupName, int num) {
		AppUtil.addGroupMembers(GroupName, num);
	}

	/**
	 * 参数分别为用户名、移动方向(down or up)，移动到列表顶部或底部。 注意的是，分组列只能是少于或等于一页
	 * 
	 * @param GroupName
	 * @param direction
	 */
	public void groupMoveto(String GroupName, String direction) {
		AppUtil.groupMoveto(GroupName, direction);
	}

	/**
	 * 输入组名，获取在列表中，排第几
	 * 
	 * @param groupName
	 * @return
	 */
	public int getGroupNum(String groupName) {
		return AppUtil.getGroupNum(groupName);
	}

	/**
	 * 确保进入我的分组页
	 */
	public void intoMyGroupPage() {
		AppUtil.intoMyGroupPage();

	}

	/**
	 * 点击进入已有分组，移除当前列表所有联系人
	 * 
	 * @param name
	 */
	public void deleteAllGroupMembers(String name) {
		AppUtil.deleteAllGroupMembers(name);
	}

	/**
	 * 点击进入已有分组，添加当前列表所有联系人
	 * 
	 * @param name
	 */
	public void addAllGroupMembers(String name) {
		AppUtil.addAllGroupMembers(name);
	}

	/**
	 * 在分组页内，长按分组名-重命名，修改已有的分组，参数分别是原有的分组名，修改后的分组名
	 * 
	 * @param srcName
	 * @param name
	 */
	public void renameGroup(String srcName, String name) {
		AppUtil.renameGroup(srcName, name);

	}

	/**
	 * 在个别的分组内点击菜单-重命名，进入分组，参数分别是原有的分组名，修改后的分组名
	 * 
	 * @param srcName
	 * @param name
	 */
	public void renameOneGroup(String srcName, String name) {
		AppUtil.renameOneGroup(srcName, name);

	}

	/**
	 * 在分组页内调用，长按删除，删除分组（先删除分组，再删除联系人）
	 */
	public void deleteGroup(String name) {
		AppUtil.deleteGroup(name);
	}

	/**
	 * 进入某个分组，点击菜单选择解散分组，删除分组
	 */
	public void deleteOneGroup(String name) {
		AppUtil.deleteOneGroup(name);
	}

	/**
	 * 创建分组，若已存在后，直接放回；不存在，新建分组。
	 * <p>
	 * state
	 * </p>
	 * 0,创建后是否保存在当前页; 1,返回主页
	 */
	public void createGroup(String name, int state) {
		AppUtil.createGroup(name, state);
	}

	// /////////////////////联系人模块//////////////////////

	/**
	 * 去除联系人回收站
	 */
	public void contactsRecycle() {
		AppUtil.contactsRecycle();
	}

	/**
	 * 根据坐标点，获取头像图片
	 * 
	 * @param point
	 * @return
	 */
	public BufferedImage getContactHead(Point point) {	
		return AppUtil.getContactHead(point);
	}

	/**
	 * 选择图片，但不稳定
	 */
	public void selectImage() {
		AppUtil.selectImage();
	}

	/**
	 * 获取联系人列表中，联系图标坐标列表，空值返回null
	 * 
	 * @return
	 */
	public List<Point> getContactsPoint() {
		return AppUtil.getContactsPoint();

	}

	/**
	 * 获取联系人列表中的数量，通过获取头像的控件来获取数量，超于一页的联系人数量无法获取。
	 */
	public int getContactCount() {
		return AppUtil.getContactCount();
	}

	/**
	 * 清空所有的联系人
	 */
	public void deleteAllContacts() {
		AppUtil.deleteAllContacts();

	}

	/**
	 * 保存简单的联系人信息核心部分
	 * 
	 * @param username
	 * @param phone
	 */
	public void saveContact(String username, String phone) {
		AppUtil.saveContact(username, phone);
	}

	/**
	 * 在联系人模块，创建联系人，详细信息
	 * 
	 */
	public void createContacts() {
		AppUtil.createContacts();

	}

	/**
	 * 创建详细联系人，添加头像
	 */
	public void createContactsAddImage() {
		AppUtil.createContactsAddImage();
	}

	/**
	 * 在联系人模块，创建联系人：通过姓名、号码，新建联系人
	 * 
	 * @param username
	 * @param phone
	 */
	public void createContacts(String username, String phone) {
		AppUtil.createContacts(username, phone);
	}

	/**
	 * 联系人模块，删除联系人：通过名称，删除联系人 参数name：手机号码
	 * 
	 * @param name
	 */
	public void deleteContactsByPhone(String name) {
		AppUtil.deleteContactsByPhone(name);
	}

	/**
	 * 联系人模块，删除联系人：通过名称，删除联系人 参数name：手机号码
	 * 
	 * @param name
	 */
	public void deleteContactsByName(String name) {
		AppUtil.deleteContactsByName(name);
	}

	/**
	 * 点击屏幕
	 */
	public void touchWindows() {
		AppUtil.touchWindows();
	}

	// ///////////拨号模块/////////////////////////////////

	
	/**
	 * 获取"标"对应的手机号码
	 * 
	 */
	public String getFirstCallRecorder() {
		return AppUtil.getFirstCallRecorder();
	}
	
	
	
	/**
	 * 隐藏拨号盘的输入盘
	 */
	public void hidekeyboardCall() {
		AppUtil.hidekeyboardCall();
	}

	/**
	 * 显示拨号盘的输入键盘
	 */
	public void displaykeyboardCall() {
		AppUtil.displaykeyboardCall();

	}

	/**
	 * 获取字符串数组 2到9
	 * 
	 * @return
	 */
	public ArrayList<String> getNumberList() {
		return AppUtil.getNumberList();
	}

	/*
	 * 创建测试数据集(num2 - num9),用于一键
	 */
	public void createDate(String num, String phone) {
		AppUtil.createDate(num, phone);

	}

	/**
	 * 创建测试数据，num是联系人数量
	 * 
	 * @param num
	 */
	public void createTestDate(int num) {
		AppUtil.createTestDate(num);

	}

	/**
	 * 点击屏幕，其他坐标添加偏移值，容易触发事件
	 * 
	 * @param point
	 */
	public void touchScreen(Point point) {
		AppUtil.touchScreen(point);
	}

	/**
	 * 这个方法必须是在未设置快捷键时运行。否则获取不全或为空。
	 * 
	 * @return
	 */
	public List<Point> getPointList() {
		return AppUtil.getPointList();
	}

	/**
	 * 清除一键拨号页所有已设置的按键,其中算法有问题，添加一个计数器，加快清理
	 */
	public void deleteAllOneCall() {
		AppUtil.deleteAllOneCall();
	}

	/**
	 * 判断当前元素是否设置为一键拨号
	 * <p>
	 * 返回 1 为未设置快捷;
	 * </p>
	 * <p>
	 * 返回2 为已设置；
	 * </p>
	 * 返回0为都不符合
	 * 
	 * @param WebElement
	 * @return
	 */
	public int getAttributeResourceId(WebElement we) {
		return AppUtil.getAttributeResourceId(we);
	}

	/**
	 * 清空黑名单管理中的内容
	 */
	public void deleteBlacklist() {
		AppUtil.deleteBlacklist();
	}

	/**
	 * 进入防打扰设置
	 */
	public void OpenTabMenu(String tab1, String tab2) {
		AppUtil.OpenTabMenu(tab1, tab2);

	}

	/**
	 * 清空通话记录
	 */
	public void deleteAllCall() {
		AppUtil.deleteAllCall();

	}

	/**
	 * 清理拨号记录
	 */
	public void deleteCall(String phone) {
		AppUtil.deleteCall(phone);

	}

	/**
	 * 拨号盘点击号码
	 * 
	 * @param str
	 */
	public void touchCallNumber(String str) {
		AppUtil.touchCallNumber(str);
		
	}

	/**
	 * 仅用于拨号盘点击数字
	 * 
	 * @param chr
	 */
	public void digitsChangeName(int chr) {
		AppUtil.digitsChangeName(chr);
	}

	/**
	 * 拨号：拨打号码（在断网情况下使用）
	 */
	public void callNumber(String number) {
		AppUtil.callNumber(number);

	}

	// /////////////////登录模块/////////////////////////////

	/**
	 * 是否在登录状态，false为未登录状态，true为已登录
	 * 
	 * @param username
	 * @return
	 */
	public boolean isLoginState(String username) {
		return AppUtil.isLoginState(username);
	}

	/**
	 * 登录：输入用户、密码，登录
	 * 
	 * @param username
	 * @param password
	 */
	public void Logout(String username) {

		AppUtil.Logout(username);

	}

	/**
	 * 输入用户、密码，登录
	 * 
	 * @param username
	 * @param password
	 */
	public void Login(String username, String password) {

		AppUtil.Login(username, password);

	}

	// ///////////////////////////公共模块///////////////////

	/**
	 * 短信生成器，输入两个参数，分别是会话条数，信息数量。
	 * 
	 * @param cscnt
	 * @param msscnt
	 */
	public void setSmsToolDemo(String cscnt, String msscnt) {
		AppUtil.setSmsToolDemo(cscnt, msscnt);

	}

	/**
	 * 该方法内不可使用控件的id（不同包名），只能用name
	 */
	public void openAppByName(String name) {
		AppUtil.openAppByName(name);
	}

	/**
	 * 报告输入用到
	 * 
	 * @param name
	 * @param log
	 */
	public void reportLog(String name, String log) {
		AppUtil.reportLog(name, log);
	}

	/**
	 * 默认添加用例名，自定义输出日志
	 * 
	 * @param log
	 */
	public void reportLog(String log) {
		AppUtil.reportLog(log);
	}

	/**
	 * 用例运行，用于检测用例执行进度
	 */
	public void startTestCase() {
		AppUtil.startTestCase();
	}

	
	
	
	/**
	 * 判断是否通过验证，如果不通过结束并截图
	 * 
	 * @param bl
	 * @param caseName
	 */
	public void Myassert(boolean bl, String caseName) {
		AppUtil.Myassert(bl, caseName);
	}

	/**
	 * 判断是否通过验证，如果不通过结束并截图,并添加具体问题描述
	 * 
	 * @param bl
	 * @param caseName
	 */
	public void Myassert(String message, boolean bl, String caseName) {
		AppUtil.Myassert(message, bl, caseName);
	}

	/**
	 * 判断是否通过验证，如果不通过结束并截图,并添加具体问题描述,自动添加用例名称
	 * 
	 * @param bl
	 * @param caseName
	 */
	public void Myassert(String message, boolean bl) {
		AppUtil.Myassert(message, bl);
	}

	/**
	 * 清除搜索记录和搜索框内容
	 */
	public void clearTextAndNote() {
		AppUtil.clearTextAndNote();
	}

	/**
	 * 点击多少次返回主界面
	 * 
	 * @param num
	 */
	public void backPage(int num) {
		AppUtil.backPage(num);
	}

	// 返回
	public void back(String name) {
		AppUtil.back(name);
	
	}
	
	//不在首层
	public boolean notRootFloor(){
		return AppUtil.notRootFloor();
	}
	
	public boolean rootFloor(){
		return AppUtil.rootFloor();
	}
	
	
	
	//判断是否为弹窗
	public boolean isPopup(){
		return AppUtil.isPopup();
	}
	
	
	// /////////////////////////////通用基础方法////////////////////////////////////////////////////

	public void line() {
		System.out.println("--------------------------");
	}

	/**
	 * 确保联网状态
	 * <p>
	 * 条件：1、有SIM卡，wifi可有可无连接记录。
	 * <p>
	 * 条件：2、无SIM卡，wifi需要连接记录
	 */
	public void openNetworkConn() {
		AppUtil.openNetworkConn();
	}

	public void closeNetworkConn() {
		AppUtil.closeNetworkConn();
	}

	/**
	 * 输入模式，设置联网（1/2/4/6）
	 * <p>
	 * mode = 1, set airplane
	 * <p>
	 * mode = 2, set wifi only
	 * <p>
	 * mode = 4, set data only
	 * <p>
	 * mode = 6, set wifi and data
	 * 
	 * @param mode
	 */
	public void setNetworkConn(int mode) {
		AppUtil.setNetworkConn(mode);
	}

	/***
	 * 检查网络
	 * 
	 * @return 是否正常
	 */
	public static boolean checkNet() {
		return AppUtil.checkNet();

	}

	/**
	 * 点击控件ID，粘贴已有的内容
	 */
	public void pasteString(String id) {
		AppUtil.pasteString(id);
	}

	/**
	 * 输入元素列表，查找的字段
	 * 
	 * @param list
	 * @param search
	 * @return
	 */
	public boolean searListContainName(List<WebElement> list, String search) {
		return AppUtil.searListContainName(list, search);
	}

	/**
	 * 根据控件ID获取 元素列表
	 * 
	 * @param id
	 * @return
	 */
	public List<WebElement> getLisWebElementById(String id) {
		return AppUtil.getLisWebElementById(id);
	}

	/**
	 * 返回列表元素，根据列表的顶部，或底部 location: start\end
	 * 
	 * @param id
	 * @param location
	 * @return
	 */
	public WebElement getWebElementInList(String id, String location) {
		return AppUtil.getWebElementInList(id, location);
	}

	/**
	 * 对列表元素遍历，搜索“含有” content字段的元素。（使用contains方法）
	 * 
	 * @param list
	 * @param content
	 * @return
	 */
	public WebElement getWebElementInList(List<WebElement> list, String content) {
		return AppUtil.getWebElementInList(list, content);
	}

	/**
	 * 根据控件ID获取元素列表，sytle分别是(Image、TextView、CheckBox)类型。
	 * 
	 * @param id
	 * @param sytle
	 * @return
	 */
	public List<WebElement> getWebElementList(String sytle, String id) {
		return AppUtil.getWebElementList(sytle, id);
	}

	/**
	 * 根据文字，获取其中的数字
	 */
	public int getNumerByText(String txt) {
		return AppUtil.getNumerByText(txt);

	}

	/**
	 * 根据控件的ID，获取控件文字中的数字
	 * 
	 * @param id
	 * @return
	 */
	public int getNumById(String id) {
		return AppUtil.getNumById(id);
	}

	/**
	 * 手势向左滑动
	 */
	public void swipeToLeft() {
		AppUtil.swipeToLeft();
	}

	/**
	 * 手势向右滑动
	 */
	public void swipeToRight() {
		AppUtil.swipeToRight();
	}

	/**
	 * 手势向右滑动,添加水平高度变量（即点击屏幕高、中、低，向右滑） location选项为high\mid\bottom
	 */
	public void swipeToLeft(String location) {
		AppUtil.swipeToLeft(location);

	}

	/**
	 * 手势向右滑动,添加水平高度变量（即点击屏幕高、中、低，向右滑） location选项为high\mid\bottom
	 */
	public void swipeToRight(String location) {
		AppUtil.swipeToRight(location);

	}

	/**
	 * 手势向下滑动
	 */
	public void swipeToDown() {
		AppUtil.swipeToDown();
	}

	/**
	 * 
	 * 手势向上滑动
	 */
	public void swipeToUp() {
		AppUtil.swipeToUp();
	}

	/**
	 * 判断某个元素是否存在,true存在；false不存在。
	 */
	public boolean isExistenceById(String id) {
		return AppUtil.isExistenceById(id);
	}

	/**
	 * 在指定时间内，判断元素是否存在，true存在；false不存在。
	 */
	public boolean isExistenceById(String id, int timeout) {
		return AppUtil.isExistenceById(id,timeout);
	}

	/**
	 * 判断某个元素是否存在,true存在；false不存在。
	 */
	public boolean isExistenceByName(String name) {
		return AppUtil.isExistenceByName(name);
	}

	/**
	 * 实现按钮长按，传参为name。（该类方法不建议使用，容易报错）
	 * 
	 * @param name
	 */
	public void clickLongByName(String name) {
		AppUtil.clickLongByName(name);
	}

	/**
	 * 实现按钮长按，传参为WebElement（该类方法不建议使用，容易报错）
	 * 
	 * @param el
	 */
	public void clickLongByWebElement(WebElement el) {
		AppUtil.clickLongByWebElement(el);
	}

	/**
	 * 调用JS脚本实现长按，参数是WebElement元素
	 * 
	 * @param element
	 */
	public void clickLongByElementUseJs(WebElement element) {
		AppUtil.clickLongByElementUseJs(element);
	}

	/**
	 * 调用JS脚本实现长按，参数是txt
	 * 
	 * @param TextViewName
	 */
	public void clickLongByNameUseJs(String TextViewName) {
		AppUtil.clickLongByNameUseJs(TextViewName);

	}

	/**
	 * 调用JS脚本实现长按，参数是txt
	 * 
	 * @param TextView
	 *            id
	 */
	public void clickLongByIdUseJs(String id) {
		AppUtil.clickLongByIdUseJs(id);
		

	}

	/**
	 * 通过控件的id，点击控件
	 * 
	 * @param id
	 */
	public void clickById(String id) {
		AppUtil.clickById(id);
	}

	/**
	 * 通过控件的txt，点击控件
	 * 
	 * @param name
	 */
	public void clickByName(String name) {
		AppUtil.clickByName(name);
	}

	/**
	 * 清空输入框内容
	 * 
	 * @param text
	 */
	public void clearText(String text) {
		AppUtil.clearText(text);
	}

	/**
	 * 获取当前界面上，所有的textView，运行比较慢； 注意：若列表过长不建议使用，容易出现找不到元素，返回null
	 * 
	 * @return
	 */
	public List<WebElement> getAllTextView() {
		return AppUtil.getAllTextView();
	}

	/**
	 * 获取当前界面上，所有的CheckBox，运行比较慢； 注意：若列表过长不建议使用，容易出现找不到元素，返回null
	 * 
	 * @return
	 */
	public List<WebElement> getAllCheckBox() {
		return AppUtil.getAllCheckBox();
	}

	/**
	 * 获取当前界面上，所有的ImageView，运行比较慢； 注意：若列表过长不建议使用，容易出现找不到元素，返回null
	 * 
	 * @return
	 */
	public List<WebElement> getAllImageView() {
		return AppUtil.getAllImageView();
	}

	/**
	 * 对上面的获取所有TextView进行优化，获取返回元素的，第二元素
	 * <p>
	 * 调整index可以获取第一元素：默认是1、其他情况为0
	 * </p>
	 * 返回时元素对象
	 * <p>
	 * name：查找的字符串
	 * </p>
	 * 
	 * @return
	 */
	public WebElement getFirstTextView(String name, int index) {
		return AppUtil.getFirstTextView(name, index);
	}

	/**
	 * 通过使用UiSelector，获取当前列表某个元素，比较高效。返回List<WebElement>
	 * 
	 * @param classname
	 * @param index
	 * @return
	 */
	public static List<WebElement> getElementsByClassAndIndex(String classname,
			int index){
		return AppUtil.getElementsByClassAndIndex(classname, index);
	}

	/**
	 * 获取点击菜单，选择某个选项
	 * 
	 * @param num
	 */
	public void clickMenuAndSelect(int num) {
		AppUtil.clickMenuAndSelect(num);

	}

	/**
	 * 输入控件ID，获取点击菜单，选择某个选项
	 * 
	 * @param num
	 */
	public void clickMenuAndSelect(String id, int num) {
		AppUtil.clickMenuAndSelect(id, num);

	}

	/**
	 * 获取更多选项，点击菜单选项
	 * 
	 * @param num
	 */
	public void contextMenuTitleSelect(int num) {
		AppUtil.contextMenuTitleSelect(num);
	}

	/**
	 * 获取点击菜单选项
	 * 
	 * @param id
	 * @param num
	 */
	public void menuList(String id, int num) {
		AppUtil.menuList(id, num);
	}

	/**
	 * 向输入框收入内容
	 * 
	 * @param id
	 * @param content
	 */
	public void intoContentEditTextById(String id, String content) {
		AppUtil.intoContentEditTextById(id, content);
		
	}

	/**
	 * 向输入框收入内容，若该控件没有ID，只有name，可以使用该方面
	 * 
	 * @param name
	 * @param content
	 */
	public void intoContentEditTextByName(String name, String content) {
		AppUtil.intoContentEditTextByName(name, content);
	}

	/**
	 * 通过控件ID，获取输入框内容
	 * 
	 * @param name
	 * @return
	 */
	public String getEditTextContent(String name) {
		return AppUtil.getEditTextContent(name);
	}

	/**
	 * 这方法适用于查找联系人，不通用。 判断页面是否存在TextView name的元素
	 * mode等于1的时候，使用简单模式；等于0的时候，使用获取所有TextView元素 使用简单模式容易找不到内容，注意使用。
	 * 
	 * @param search
	 * @param mode
	 * @return
	 */
	public boolean searchContact(String search, int mode) {
		return AppUtil.searchContact(search, mode);
	}

	/**
	 * 通过textView name 查找元素，返回找到WebElement对象
	 * 
	 * @param search
	 * @return
	 */
	public WebElement searchWebElement(String search) {
		return AppUtil.searchWebElement(search);
	}

	/**
	 * 根据控件ID获取该控件的text
	 * 
	 * @param id
	 * @return
	 */
	public String getTextViewNameById(String id) {
		return AppUtil.getTextViewNameById(id);
	}

	/**
	 * 休眠 1000 = 1秒
	 * 
	 * @param num
	 */
	public void sleepTime(int num) {
		AppUtil.sleepTime(num);
	}

	/**
	 * 获取调用该方法的名称
	 * 
	 * @return
	 */
	public String getMethodName() {
		return AppUtil.getMethodName();
	}

	public String getTestCaseName(){
		return AppUtil.getTestCaseName();
	}
	
	/**
	 * //此方法为屏幕截图
	 * 
	 * @param drivername
	 * @param filename
	 */
	public static void snapshot(AndroidDriver<WebElement> driver,
			String testCasename) {
		AppUtil.snapshot(driver, testCasename);
	}

	public static String getCurrentDateTime() {
		return AppUtil.getCurrentDateTime();
	}

	// /获取截图，并对比
	public static boolean sameAs(BufferedImage myImage,
			BufferedImage otherImage, double percent) {
		return AppUtil.sameAs(myImage, otherImage, percent);
	}

	public static BufferedImage getSubImage(BufferedImage image, int x, int y,
			int w, int h) {
		return AppUtil.getSubImage(image, x, y, w, h);
	}

	/**
	 * 获取文件
	 * 
	 * @param f
	 * @return
	 */
	public static BufferedImage getImageFromFile(File f) {
		return AppUtil.getImageFromFile(f);
	}
}
