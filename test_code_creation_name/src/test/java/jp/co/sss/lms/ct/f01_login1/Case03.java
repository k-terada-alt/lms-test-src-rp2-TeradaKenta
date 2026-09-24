package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * 結合テスト ログイン機能①
 * ケース03
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Case03 {

	@LocalServerPort
	private int port;

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {

		//ログイン画面のURLに遷移
		goTo("http://localhost:" + port + "/lms/");

		//タイトルが正しいか検証
		String title = webDriver.getTitle();
		assertEquals("ログイン | LMS", title);

		//ボタンが正しいか検証
		WebElement loginButtonElement = webDriver.findElement(By.cssSelector(".btn-primary"));
		assertEquals("ログイン", loginButtonElement.getAttribute("value"));

		//エビデンスを取得
		getEvidence(new Object() {});

	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		//1度ログインしたことのあるユーザーでログイン
		WebElement id = webDriver.findElement(By.id("loginId"));
		id.clear();
		id.sendKeys("StudentAA01");
		
		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("Student01");
		
		webDriver.findElement(By.cssSelector(".btn-primary")).click();

		//タイトルを確実に取得するためコース詳細画面の見出しが表示されるまで最大10秒待機
		visibilityTimeout(By.tagName("h2"), 10);

		//タイトルが正しいか検証
		assertEquals("コース詳細 | LMS", webDriver.getTitle());

		//エビデンスを取得
		getEvidence(new Object() {});

	}

}
