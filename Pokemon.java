package pokemon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



public class Pokemon {
	
	static ArrayList<Regex> coordinateArray = new ArrayList<>();
	
	public static void main(String[] args) {

		//初期位置
		//緯度
		float latitude = 34.67749f;
		//経度
		float longitude = 135.49402f;

		//方向の切り替え回数
		int count = 1;

		float coordinateX = 0;
		float coordinateY = 0;
		//一方角へのループ回数
		int coordinateCount = 1;

		//東西南北の指示
		int controller = 4;

		for (int i = 0; i < count; i++) {

			//URLへの入力座標
			String coordinate = null;

			//東西移動量
			float scrollX = 0.02f;
			//南北移動量
			float scrollY = 0.012f;

			switch (controller) {

			//東移動
			case 0: {

				for (int j = 0; j < coordinateCount; j++) {

					coordinateX += scrollX; 
					String coordinateXString = String.valueOf(coordinateX);
					String coordinateYString = String.valueOf(coordinateY);
					//URL座標
					coordinate = coordinateYString + "," + coordinateXString;

					getPokemon(coordinate);

				}

				controller = 1;
				break;
			}
			//北移動
			case 1: {

				for (int j = 0; j < coordinateCount; j++) {

					coordinateY += scrollY; 
					String coordinateXString = String.valueOf(coordinateX);
					String coordinateYString = String.valueOf(coordinateY);
					coordinate = coordinateYString + "," + coordinateXString;

					getPokemon(coordinate);

				}
				
				//coordinateCount++;

				controller = 2;
				
				break;

			}
			//西移動
			case 2: {

				for (int j = 0; j < coordinateCount; j++) {

					coordinateX -= scrollX; 
					String coordinateXString = String.valueOf(coordinateX);
					String coordinateYString = String.valueOf(coordinateY);
					coordinate = coordinateYString + "," + coordinateXString;

					getPokemon(coordinate);

				}

				controller = 3;

				break;
			}
			//南移動
			case 3: {

				for (int j = 0; j < coordinateCount; j++) {

					coordinateY -= scrollY; 
					String coordinateXString = String.valueOf(coordinateX);
					String coordinateYString = String.valueOf(coordinateY);
					coordinate = coordinateYString + "," + coordinateXString;

					getPokemon(coordinate);

				}
				
				coordinateCount++;

				controller = 0;

				break;
			}
			//初期位置
			case 4: {
				coordinateY = latitude;
				coordinateX = longitude;
				String coordinateXString = String.valueOf(coordinateX);
				String coordinateYString = String.valueOf(coordinateY);
				coordinate = coordinateYString + "," + coordinateXString;

				getPokemon(coordinate);

				controller = 0;

				break;
			}
			}

		}
		
		TSP tsp = new TSP(coordinateArray);
		
		ArrayList<String> list = tsp.moldListStrng;

		//現在時間の取得
		Date now = new Date();
		
		String name = new SimpleDateFormat("yyyyMMddHHmm").format(now);
		//テキスト作成準備
		FileWriter fileWriter = null;

		File createfile = null;
		try {
			createfile = new File("c:\\pokemon\\" + name + ".gpx"); 

			//テキストの重複確認
			if (createfile.createNewFile()) {
				fileWriter = new FileWriter(createfile);

				//書き込み
				fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<gpx version=\"1.1\" creator=\"GPS JoyStick - gpsjoystick@gmail.com - https://www.facebook.com/gpsjoystick\">\n"
						+ "<rte><name>" + name + "</name><number>0</number>\n");

				for (String a : list) {

					fileWriter.write(a);
					
				}
				
				fileWriter.write("</rte></gpx>");
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				System.out.println("書き込み終了");
				//テキストクローズ処理
				fileWriter.close();
				
				String file = createfile.toString();
				
				new SendMail(file);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}



	//
	private static void getPokemon(String coordinate) {
		//WebDriverの参照
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver_win32/chromedriver.exe");

//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--headless");
//		WebDriver driver = new ChromeDriver(options);

		//google driverの呼び出し 
		WebDriver driver = new ChromeDriver();


		//URLの指定
		driver.get("https://9db.jp/pokemongo/map#"+coordinate+",16");

		try {
			//次の処理への待ち時間
			Thread.sleep(2000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		//ポップアップの閉じるボタン押下
		driver.findElement(By.xpath("//*[@id=\"pokemap_info\"]/div[1]/div/a")).click();

		try {
			//次の処理への待ち時間
			Thread.sleep(2000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		//ポップアップの押下
		List<WebElement> elementsRed = driver.findElements(By.xpath("//img[starts-with(@id, \"toggle_check_gym_\")]"));
		//
		for (WebElement e : elementsRed) {

			
			try {
				e.click();

				//次の処理への待ち時間
				Thread.sleep(1000);
				//座標取得
				String red = driver.findElement(By.xpath("//*[@id=\"pokemap\"]/div[1]/div[6]/div/div[1]/div/div/div[1]")).getText();
				//成形
				Regex regex = new Regex(red);
				
				//配列へ格納
				coordinateArray.add(regex);

			} catch (Exception er) {

			}		

		}
		//ポップアップの押下
		List<WebElement> elementsBule = driver.findElements(By.xpath("//img[starts-with(@id, \"toggle_check_pokestop_\")]"));
		//
		for (WebElement e : elementsBule) {

			try {
				e.click();

				//次の処理への待ち時間
				Thread.sleep(1000);
				//座標取得
				String bule = driver.findElement(By.xpath("//*[@id=\"pokemap\"]/div[1]/div[6]/div/div[1]/div/div/div[1]")).getText();
				
				Regex regex = new Regex(bule);
				
				//配列へ格納
				coordinateArray.add(regex);

			} catch (Exception er) {

			}		

		}

		//ブラウザを閉じる
		driver.close();
	}

}

 class Regex {
	
	//緯度経度距離情報
	float lat;
	float lon;
	float distance;
	
	public Regex (float distance, float x01, float y01) {
		this.lat = x01;
		this.lon = y01;
		this.distance = distance;
	}

	public Regex (String coordinate) {
		
		String lat = null;
		String lon = null;

		//正規表現のパターン
		Pattern p = Pattern.compile("(\\D{3})(\\d{2,3}.\\d{3,})(\\D{2})(\\d{2,3}.\\d{3,})");
		//パターンに取得した値を代入
		Matcher m = p.matcher(coordinate);

		if (m.matches()){

			//緯度の取得
			lat = m.group(2);
			//経度の取得
			lon = m.group(4);
			
		}
		
		this.lat = Float.valueOf(lat);
		this.lon = Float.valueOf(lon);
		
		
	}

}
 
 