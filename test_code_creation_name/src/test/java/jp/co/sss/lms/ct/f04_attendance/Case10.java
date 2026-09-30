package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

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
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Case10 {

	@LocalServerPort
	private int port;

	//日付
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日(E)");

	//時間
	private final SimpleDateFormat stf = new SimpleDateFormat("HH:mm");

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
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() {

		webDriver.findElement(By.xpath("//input[@value='出勤']")).click();

		Alert alert = webDriver.switchTo().alert();
		if (alert != null) {
			alert.accept();
		}

		String now = stf.format(new Date());

		for (WebElement time : webDriver.findElement(By.tagName("tbody")).findElements(By.tagName("tr"))) {

			if (time.findElement(By.className("w160")).getText().equals(now)) {

				assertEquals(now, time.findElements(By.className("w80")).get(0).getText());

				break;

			}

		}
		
		scrollBy("150");

		getEvidence(new Object() {
		});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() {

		webDriver.findElement(By.xpath("//input[@value='退勤']")).click();

		Alert alert = webDriver.switchTo().alert();
		if (alert != null) {
			alert.accept();
		}

		String now = stf.format(new Date());

		for (WebElement time : webDriver.findElement(By.tagName("tbody")).findElements(By.tagName("tr"))) {

			if (time.findElement(By.className("w160")).getText().equals(now)) {

				assertEquals(now, time.findElements(By.className("w80")).get(1).getText());

				break;

			}

		}
		
		scrollBy("150");

		getEvidence(new Object() {
		});

	}

}
