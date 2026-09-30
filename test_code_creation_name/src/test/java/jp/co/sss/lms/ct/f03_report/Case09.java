package jp.co.sss.lms.ct.f03_report;

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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Case09 {

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

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {

		webDriver.findElement(By.xpath("//small[text()='ようこそ受講生ＡＡ１さん']")).click();

		pageLoadTimeout(10);

		assertEquals("ユーザー詳細", webDriver.getTitle());

		getEvidence(new Object() {});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		
		visibilityTimeout(By.className("table-hover"), 10);

		List<WebElement> tables = webDriver.findElements(By.className("table-hover"));
		scrollBy("500");

		List<WebElement> reports = tables.get(2).findElements(By.tagName("tr"));
		isFinished: for (WebElement report : reports) {
			List<WebElement> tdTags = report.findElements(By.tagName("td"));
			if (tdTags.size() > 0 && tdTags.get(0).getText().equals("2022年10月2日(日)")
					&& tdTags.get(1).getText().equals("週報【デモ】")) {
				for (WebElement imputTag : tdTags.get(4).findElements(By.className("btn-default"))) {
					if (imputTag.getAttribute("value").equals("修正する")) {
						imputTag.click();
						break isFinished;
					}
				}
			}
		}

		pageLoadTimeout(30);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		
		getEvidence(new Object() {});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {

		webDriver.findElement(By.id("intFieldName_0")).clear();

		List<WebElement> tables = webDriver.findElements(By.className("bs-component"));
		scrollBy(String.valueOf(tables.get(1).getSize().getHeight()));

		webDriver.findElement(By.className("btn-primary")).click();

		pageLoadTimeout(30);

		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		
		assertEquals("form-control errorInput", webDriver.findElement(By.id("intFieldName_0")).getAttribute("class"));

		getEvidence(new Object() {});

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {

		webDriver.findElement(By.id("intFieldName_0")).sendKeys("ジャマイカの極意");
		WebElement element = webDriver.findElement(By.id("intFieldValue_0"));

		Select select = new Select(element);

		select.selectByValue("");

		List<WebElement> tables = webDriver.findElements(By.className("bs-component"));
		scrollBy(String.valueOf(tables.get(1).getSize().getHeight()));

		webDriver.findElement(By.className("btn-primary")).click();

		pageLoadTimeout(30);

		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		
		assertEquals("form-control errorInput", webDriver.findElement(By.id("intFieldValue_0")).getAttribute("class"));

		getEvidence(new Object() {});

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {

		WebElement element = webDriver.findElement(By.id("intFieldValue_0"));
		Select select = new Select(element);
		select.selectByValue("2");

		webDriver.findElement(By.id("content_0")).clear();
		webDriver.findElement(By.id("content_0")).sendKeys("寒いですね");

		List<WebElement> tables = webDriver.findElements(By.className("bs-component"));
		scrollBy(String.valueOf(tables.get(1).getSize().getHeight()));

		webDriver.findElement(By.className("btn-primary")).click();

		pageLoadTimeout(30);

		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		
		assertEquals("form-control errorInput", webDriver.findElement(By.id("content_0")).getAttribute("class"));

		getEvidence(new Object() {});

	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {

		webDriver.findElement(By.id("content_0")).clear();
		webDriver.findElement(By.id("content_0")).sendKeys("11");

		List<WebElement> tables = webDriver.findElements(By.className("bs-component"));
		scrollBy(String.valueOf(tables.get(1).getSize().getHeight()));

		webDriver.findElement(By.className("btn-primary")).click();

		pageLoadTimeout(30);

		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		
		assertEquals("form-control errorInput", webDriver.findElement(By.id("content_0")).getAttribute("class"));

		getEvidence(new Object() {});

	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {

		webDriver.findElement(By.id("content_0")).clear();
		webDriver.findElement(By.id("content_1")).clear();

		List<WebElement> tables = webDriver.findElements(By.className("bs-component"));
		scrollBy(String.valueOf(tables.get(1).getSize().getHeight()));

		webDriver.findElement(By.className("btn-primary")).click();

		pageLoadTimeout(30);

		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		
		assertEquals("form-control errorInput", webDriver.findElement(By.id("content_0")).getAttribute("class"));
		
		assertEquals("form-control errorInput", webDriver.findElement(By.id("content_1")).getAttribute("class"));

		getEvidence(new Object() {});

	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		
		String overText = "あ".repeat(2001);

		webDriver.findElement(By.id("content_0")).sendKeys("5");

		List<WebElement> tables = webDriver.findElements(By.className("bs-component"));
		scrollBy(String.valueOf(tables.get(1).getSize().getHeight()));
		
		webDriver.findElement(By.id("content_1")).clear();

		webDriver.findElement(By.id("content_1")).sendKeys(overText);
		
		webDriver.findElement(By.id("content_2")).clear();
		
		webDriver.findElement(By.id("content_2")).sendKeys(overText);

		webDriver.findElement(By.className("btn-primary")).click();

		pageLoadTimeout(30);

		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		
		assertEquals("form-control errorInput", webDriver.findElement(By.id("content_1")).getAttribute("class"));
		
		assertEquals("form-control errorInput", webDriver.findElement(By.id("content_2")).getAttribute("class"));
		
		scrollBy("200");

		getEvidence(new Object() {});

	}

}
