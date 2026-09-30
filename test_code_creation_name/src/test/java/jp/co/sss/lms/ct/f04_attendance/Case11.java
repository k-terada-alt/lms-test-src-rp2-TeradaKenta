package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Case11 {

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
		getEvidence(new Object() {
		});

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
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {

		webDriver.findElement(By.xpath("//li[contains(.,'勤怠')]")).click();

		Alert alert = webDriver.switchTo().alert();
		if (alert != null) {
			alert.accept();
		}

		pageLoadTimeout(10);

		//タイトルが正しいか検証
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());

		//エビデンスを取得
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {

		webDriver.findElement(By.xpath("//a[contains(.,'勤怠情報を直接編集する')]")).click();

		pageLoadTimeout(10);

		//タイトルが正しいか検証
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());

		//エビデンスを取得
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {

		for (WebElement regularButton : webDriver.findElement(By.tagName("table")).findElements(By.className("w60"))) {
			regularButton.click();
		}

		scrollBy(String.valueOf(webDriver.findElement(By.tagName("table")).getSize().getHeight()));

		webDriver.findElement(By.name("complete")).click();
		webDriver.switchTo().alert().accept();

		pageLoadTimeout(30);

		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		for (WebElement trTag : webDriver.findElement(By.tagName("tbody")).findElements(By.tagName("tr"))) {
			List<WebElement> regularHours = trTag.findElements(By.className("w80"));
			assertEquals("09:00", regularHours.get(0).getText());
			assertEquals("18:00", regularHours.get(1).getText());
		}

		getEvidence(new Object() {
		});

	}

}
