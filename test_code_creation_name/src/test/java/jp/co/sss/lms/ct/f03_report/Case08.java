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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		//セクション一覧の行要素を取得
		List<WebElement> sectionRows = webDriver.findElements(By.cssSelector("table.sctionList tr"));

		//提出済みの行を探索し「詳細」ボタンを押下
		for (WebElement row : sectionRows) {
			scrollBy("50");
			List<WebElement> cellsElements = row.findElements(By.tagName("td"));
			if ("提出済み".equals(cellsElements.get(2).getText())) {
				row.findElement(By.cssSelector("input[value='詳細']")).click();
				pageLoadTimeout(10);
				break;
			}
		}

		//タイトルが正しいか検証
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		//エビデンスを取得
		getEvidence(new Object() {});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		//「提出済み日報【デモ】を確認する」ボタンを押下
		webDriver.findElement(By.cssSelector("input[value='提出済み日報【デモ】を確認する']")).click();

		pageLoadTimeout(10);

		//タイトルが正しいか検証
		assertEquals("レポート登録 | LMS", webDriver.getTitle());

		//エビデンスを取得
		getEvidence(new Object() {});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {

		//報告内容を修正
		WebElement reportElement = webDriver.findElement(By.className("form-control"));
		reportElement.clear();
		reportElement.sendKeys("test");

		pageLoadTimeout(10);

		//「提出する」ボタンを押下
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();

		pageLoadTimeout(10);

		//タイトルが正しいか検証
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		//エビデンスを取得
		getEvidence(new Object() {});

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {

		//ユーザー詳細画面へ遷移するリンクを押下
		webDriver.findElement(By.xpath("//small[text()='ようこそ受講生ＡＡ１さん']")).click();

		pageLoadTimeout(10);

		//タイトルが正しいか検証
		assertEquals("ユーザー詳細", webDriver.getTitle());

		//エビデンスを取得
		getEvidence(new Object() {});

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {

		scrollBy("100");

		//レポート一覧の行要素を取得
		List<WebElement> sectionRows = webDriver.findElements(By.cssSelector("table.table-hover tr"));

		//該当レポートの「詳細」ボタンを押下
		for (WebElement row : sectionRows) {
			scrollBy("50");
			List<WebElement> cellsElements = row.findElements(By.tagName("td"));
			if (cellsElements.size() < 5) {
				continue;
			}

			row.findElement(By.cssSelector("input[value='詳細']")).click();
			pageLoadTimeout(10);
			break;
		}

		//修正内容が画面上に反映されているか検証
		assertTrue(!webDriver.findElements(By.xpath("//*[contains(text(),'テスト')]")).isEmpty());

		//エビデンスを取得
		getEvidence(new Object() {});

	}

}
