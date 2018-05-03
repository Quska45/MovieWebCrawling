package createDatabase;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImgCrawling {
	static String base_url1 = "https://movie.naver.com/movie/search/result.nhn?query=";
	static String base_url2 = "&section=all";
	static String page = null;
	static String complete_url = base_url1 + page + base_url2;
	
	public static void main(String[] args) throws IOException {
		ScriptDAO sDao = new ScriptDAO();
		String result;
		String tit_result;
		int i =1;
		while(i<3){
			//영화의 제목을 가져와 변수에 담아줬다.
			result= sDao.imgCrawling(i);
			//네이버 영화에서 제목을 검색하면 공백이 +로 치환되어 들어가기 떄문에 공백을 +로 치환했다.
			tit_result = result.replace(" ", "+");
			
			complete_url = base_url1 + page + base_url2;
			
			//내가 검색하고 싶은 제목으로 url을 구성하고 이것을 document객체에 넣어줬다. 
			Document doc = Jsoup.connect(complete_url).get();
			//내가 원하는 진짜 img가 있는 곳으로 이동하기 위해 a태그를 가져왔고 이제 이것을 통해 다음 경로를 찾아 갈 것이다.
			Elements firstPath = doc.select("p.result_thumb a");
			System.out.println("포스터 이미지를 보기 위한 1차경로" + firstPath);
			
			
			//firstPath에서 큰 사진이 있는 경로로 들어가 img태그를 가져오는 과정을 진행할 것이다.
			String url = firstPath.attr("href");
			String real_url = "https://movie.naver.com"+url;
			real_url = real_url.replace("basic.nhn?code", "photoViewPopup.nhn?movieCode");
			System.out.println("진짜 경로" + real_url);
			
			Document doc2 = Jsoup.connect(real_url).get();
			Elements img = doc2.select("div#page_content a img");
			System.out.println("img : " + img);
			
			i++;
		}
		
	}
}
