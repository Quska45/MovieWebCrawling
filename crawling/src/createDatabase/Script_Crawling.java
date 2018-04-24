package createDatabase;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Script_Crawling {
	//영화대본 사이트 A로 시작하는 페이지인데 마지막 페이지 넘버는 공란이다.
	static String base_url = "https://www.springfieldspringfield.co.uk/movie_scripts.php?order=A&page=";
	static int page = 1;
	static String complete_url = base_url + page;
	
	public static void main(String[] args) throws IOException {
		
		while(page < 2) {
			//A로 시작하는 영화 1페이지에 해당하는 Jsoup 객체를 하나 생성해줬다.
			Document doc = Jsoup.connect(complete_url).get();
			//영화의 스크립트를 보러 들어가는 a태그 이다.
			Elements titleList_url = doc.select("a.script-list-item");
			
			
			for (Element element : titleList_url) {
				//a.script-list-item 이 태그에 걸려있는 url을 가지고 온다.
				String url = element.attr("href");
				//스크립트를 볼 수 있는 url이 완성되었다.
				String url_click = "https://www.springfieldspringfield.co.uk" + url;
				System.out.println("클릭 URL 경로" + url_click);
				
				//contains 함수는 두개의 String값을 비교해 같으면 true 다르면 false를 반환한다.
				if(url_click.contains("movie_script.php")) {
					//스크립트에 해당하는 url의 객체를 하나 생성했다.
					Document scripts = Jsoup.connect(url_click).get();
					Elements title = scripts.select("div.main-content-left > h1");
					Elements body = scripts.select("div.scrolling-script-container");
					
					System.out.println("Web page 번호" + page);
					System.out.println("제목 : " + title.text());
					//System.out.println("스크립트 : " + body.text());
					
					
					String tit_str = title.text();
					//substring은 문자열을 잘라주는 함수이다.
					//제목 부분만을 추출 해준다.
					String tit_str2 = tit_str.substring(0, tit_str.length()-20);
					//년도를 추출하기 위해 제목에서 년도가 아닌 부분을 다 없애 준다.
					String year2 = tit_str.substring(tit_str.length()-18, tit_str.length()-14);
					int year = Integer.parseInt(year2);
					
					String body_str = body.text();
					String index = "A";
					
					System.out.println("제목 수정 : " + tit_str2);
					System.out.println("년도 : " + year);
					
					
					ScriptDAO sDao = new ScriptDAO();
					sDao.insertScript(index, tit_str2, year, body_str);
					
				} 
				
			}
		
			++page;
			complete_url = base_url + page;
			}
			
		
	}
	
	
	

}
