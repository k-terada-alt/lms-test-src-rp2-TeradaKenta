package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

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
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Case06 {

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
		getEvidence(new Object() {});

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		//コース詳細画面からヘルプへ遷移
		webDriver.findElement(By.cssSelector(".dropdown-toggle")).click();
		webDriver.findElement(By.linkText("ヘルプ")).click();

		//タイトルが正しいか検証
		assertEquals("ヘルプ | LMS", webDriver.getTitle());

		//エビデンスを取得
		getEvidence(new Object() {});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {

		//元のタブ(ヘルプ画面)の識別子を保持
		String originalWindow = webDriver.getWindowHandle();

		//「よくある質問」リンクを取得してクリック
		webDriver.findElement(By.linkText("よくある質問")).click();

		//全てのタブの識別子を取得
		Set<String> allWindows = webDriver.getWindowHandles();

		//新しく開いたタブへ操作対象を切り替える
		for (String windowHandle : allWindows) {
			if (!windowHandle.equals(originalWindow)) {
				webDriver.switchTo().window(windowHandle);
				break;
			}
		}

		//タイトルが正しいか検証
		assertEquals("よくある質問 | LMS", webDriver.getTitle());

		//エビデンス取得
		getEvidence(new Object() {});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		
		//「【研修関係】」カテゴリのリンクをクリック
		webDriver.findElement(By.linkText("【研修関係】")).click();

		//検索結果が表示されるまで待機
		visibilityTimeout(By.cssSelector("[id^='question-h']"), 5);

		//検索結果として表示されている質問の一覧を取得
		//idが「question-h」から始まる要素をすべて取得
		final List<WebElement> resultList = webDriver.findElements(By.cssSelector("[id^='question-h']"));
		
		//期待値と同じであるかを検証
		assertEquals(2, resultList.size());
		assertTrue(resultList.get(0).getText().contains("キャンセル料・途中退校について"));
		assertTrue(resultList.get(1).getText().contains("研修の申し込みはどのようにすれば良いですか？"));
		
		//スクリーンショットに検索結果が移るようにスクロール
		scrollBy("300");
		
		//エビデンス取得
		getEvidence(new Object() {});

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {

		//検索結果1件目の質問をクリックする
		webDriver.findElement(By.cssSelector("[id^='question-h']")).click();

		//対応する回答を取得
		final WebElement answerElement = webDriver.findElement(By.cssSelector("[id^='answer-h']"));

		//期待値と同じであるかを検証
		assertTrue(answerElement.getText().contains("受講者の退職や解雇等"));
		
		//エビデンス取得
		getEvidence(new Object() {});
				
	}

}
