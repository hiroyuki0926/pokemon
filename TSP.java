package pokemon;

import java.util.ArrayList;

public class TSP {

	//初期位置を含めたリスト
	ArrayList<Regex> addDistanceArray = new ArrayList<Regex>();
	float firstTotalDistance;
	float totalDistance;
	//出力用のリスト
	ArrayList<String> moldListStrng = new ArrayList<String>();

	public TSP (ArrayList<Regex> list) { 
		
		for (int i = 0; i < list.size(); i++) {

			Regex coordinate01 = list.get(i);
			Regex coordinate02;

			try {

				coordinate02 = list.get(i + 1);

				addDistanceArray.add(distance(coordinate01, coordinate02));
				this.firstTotalDistance += addDistanceArray.get(i).distance;
				
			} catch (Exception e) {

				Regex coordinate00 = list.get(0);

				addDistanceArray.add(distance(coordinate01, coordinate00));
				addDistanceArray.add(coordinate00);
				this.firstTotalDistance += addDistanceArray.get(i).distance;

				
				System.out.println("初めの総距離" + firstTotalDistance);
				System.out.println("取得した座標の数" + (i + 2));
				
				
			}

		}

		for (int i = 0; i < addDistanceArray.size(); i++) {
			
			for (int j = 1; j < addDistanceArray.size(); j++) {
				
				for (int q = j + 1; q < addDistanceArray.size(); q++) {

					//取得した座標の並び替え
					try {
						//対象の座標の取得	
						Regex r01 = addDistanceArray.get(j - 1);
						Regex r02 = addDistanceArray.get(j);
						Regex r03 = addDistanceArray.get(q);
						Regex r04 = addDistanceArray.get(q + 1);

						float td;
						float td1;

						//元の座標同士の距離
						Distance distance01 = new Distance(r01, r02);
						Distance distance02 = new Distance(r02, r03);
						Distance distance03 = new Distance(r03, r04);
						td = distance01.distance + distance02.distance + distance03.distance;

						//並び替え変えた後の座標同士の距離
						Distance distance1 = new Distance(r01, r03);
						Distance distance2 = new Distance(r03, r02);
						Distance distance3 = new Distance(r02, r04);
						td1 = distance1.distance + distance2.distance + distance3.distance;

						Regex newObject02 = distance2.rg;
						Regex newObject03 = distance3.rg;

						if (td1 < td) {

							//入れ替え
							addDistanceArray.remove(j);
							addDistanceArray.remove(q - 1);

							addDistanceArray.add(j, newObject02);
							addDistanceArray.add(j + 1, newObject03);

						} 

					} catch (Exception e) {
						
					}
				}
				
			}
		}

		//出力用の配列
		float t = 0;		
		for (Regex regex : addDistanceArray) {

			String lat = Float.toString(regex.lat);
			String lon = Float.toString(regex.lon);
			t += regex.distance;
			//取得した値の成形
			String moldCoordinate = "<rtept lat=\"" + lat + "\" lon=\"" + lon + "\"/>\n";
			
			moldListStrng.add(moldCoordinate);
		}	
		System.out.println("入れ替え後の総距離" + t);
	}

	//座標同士の距離の取得
	private Regex distance(Regex coordinate01 ,Regex coordinate02) {

		float distance;

		float x01 = coordinate01.lat;
		float y01 = coordinate01.lon;
		float x02 = coordinate02.lat;
		float y02 = coordinate02.lon;
		
		float dx = x01 - x02;
		float dy = y01 - y02;
		
		distance = (float) Math.sqrt( dx * dx + dy * dy ) * 10000;
		
		Regex rg = new Regex(distance, x01, y01);
		return rg;

	} 

	 
}

class Distance {
	
	float distance;
	Regex rg;
	
	public Distance (Regex coordinate01 ,Regex coordinate02) {
		
		float x01 = coordinate01.lat;
		float y01 = coordinate01.lon;
		float x02 = coordinate02.lat;
		float y02 = coordinate02.lon;
		
		float dx = x01 - x02;
		float dy = y01 - y02;
		
		if (dx < 0) dx *= -1;
		if (dy < 0) dy *= -1;
		
		float distance = (float) Math.sqrt( dx * dx + dy * dy ) * 10000;

		this.rg = new Regex(distance, x01, y01);
		this.distance = distance;
	} 
	
}

