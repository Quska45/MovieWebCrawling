package createDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class ScriptDAO {

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int result = 0;

	// 크롤링 한 테이블에 삽입하는 메서드
	public void insertScript(String index, String tit_str, int year, String body_str) {

		try {
			conn = DBManager.getConnection();
			
			// index / indexChar == "A"
			// tit_str / title == Script_Crawling에서 제목을 저장한 변수 tit_str2이다.
			// year / createyear == Script_Crawling에서 년도를 저장한 변수 year이다.
			// body_str / body == Script_Crawling에서 본문을 저장한 body_str이다.
			String sql = "INSERT INTO moviescript(mno, indexChar, title, createyear, body) " 
					   + "VALUES(seq_moviescript.nextval, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, index); //a로 시작하는 영화를 나타낸다.
			pstmt.setString(2, tit_str); //제목에 해당한다.
			pstmt.setInt(3, year); //영화의 제작 년도에 해당한다.
			pstmt.setString(4, body_str); //영화의 본문 스크립트에 해당한다.

			result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("등록 성공");
			} else {
				System.out.println("등록 실패");
			}

		} catch (Exception e) {
			System.out.println("DB 연결에 문제가 있습니다.");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}

	}

	
	//형태소를 매개변수로 받아와 단어 원형과 단어 뜻을 크롤링 해오는 메서드
	public MovieDTO crawling(String morpheme, int i, int k) throws IOException {
		
		MovieDTO mDto = new MovieDTO();
		
		//다음 단어 사전이다. 입력받은 인자인 morpheme을 사전 url뒤에 넣어주면 된다.
		String base_url = "http://alldic.daum.net/search.do?q=";
		String complete_url = base_url + morpheme;
		
		//완성된 url을 이용해서 객체를 하나 생성했다.
		Document doc = Jsoup.connect(complete_url).get();
		//다음 사전 페이지는 영어사전, 영영사전 등으로 구성되어 있는데 각 사전은 감싸고 있는 것이 card_word div이다.
		Elements card_word = doc.select("div.card_word");
		
		//word == dont과 같은 단어를 원형으로 치환하기 위한 변수
		//meaning == 단어의 뜻을 저장하는 변수이다.
		String word = null;
		String meaning = null;
		
		//card_word의 사이즈 만큼, 즉 사전의 갯수 만큼 반복문이 실행된다.
		for(int j = 0; j < card_word.size(); j++) {
			//tit_word == 사전의 타이들에 해당한다. 영어사전, 영영사전 같은거
			//txt_emph1 == 내가 검색하고자 하는 단어를 영어사전에서 찾아온 것이다.
			Elements tit_word = card_word.get(j).select("h4.tit_word");
			Elements txt_emph1 = card_word.get(j).select("a.txt_cleansch span.txt_emph1");
			if (txt_emph1.text().equals("")) {
				txt_emph1 = card_word.get(j).select("a.txt_searchword");
			}
			//단어 뜻을 감싸고 있는 ul태그 자체를 가져온 것이다.
			Elements list_search = card_word.get(j).select("div.cleanword_type ul.list_search");
			
			if(tit_word.text().equals("영어사전")) {
				word = txt_emph1.text();
				meaning = list_search.toString();
				//System.out.println(word);
				//System.out.println(meaning);
				break;
			} else if(tit_word.text().equals("영영사전")){
				word = txt_emph1.text();
				//System.out.println(word);
			} else {
				continue;
			}
			
		}
		
		
		System.out.println("테이블 번호 : " + i);
		System.out.println("테이블 인덱스 : " + k);
		System.out.println("크롤링 단어 : " + word);
		System.out.println("크롤링 뜻 : " + meaning);
		
		ScriptDAO sDao = new ScriptDAO();
		//셋팅된 값들을 넣어서 테이블을 업데이트 시키는 메소드에 넣어준다.
		sDao.completeTable(word, meaning, i, k);
		
		return mDto;
		
	}
	
	
	//DB에 저장된 원시 테이블(형태소, 빈도)를 업데이트(단어, 번호, 뜻)시키는 메서드
	public void selectWord(int i) {
		
		try {
			conn = DBManager.getConnection();
			
			String sql = "SELECT * FROM movie" + i;
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			ArrayList<MovieDTO> list = new ArrayList<>();
			while(rs.next()) {
				//형태소와 빈도 수를 가져온다.
				String morpheme = rs.getString("morpheme");
				int freq = rs.getInt("freq");
				//가져온 형태소와 빈도 수를 DTO생성자에 넣고 list에 담아준다.
				MovieDTO mDto = new MovieDTO(morpheme, freq);
				list.add(mDto);
				
			}
			
			
			int k = 1;
			for (MovieDTO movieDTO : list) {
				//list(movieDTO)에 있는 형태소 값을 가져와서 변수에 저장해줬다.  
				String morpheme = movieDTO.getMorpheme();
				
				//크롤링을 해서 값을 저장한다.
				ScriptDAO sDao = new ScriptDAO();
				sDao.crawling(morpheme, i, k);
			
				k++;
			}
					
					
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		
		
	}
	

	
	//원시 테이블 수정(morpheme, meaning, rno 추가), r을 통해 텍스트마이닝된 결과가 들어있는 테이블에 내가 원하는 값을 추가 시켜 줄 것이다. 
	public void alterTable(int num) {
		
		try {
			conn = DBManager.getConnection();
			
			String sql = "ALTER TABLE movie" + num + " "
						+ "ADD (word VARCHAR2(255)) "
						+ "ADD (meaning VARCHAR2(4000))";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
		
		
	}
	
	// 크롤링 해온 내용으로 각 영화 테이블 업데이트 시키는 메서드
	public void completeTable(String word, String meaning, int i, int k) {
		
		try {
			
			conn = DBManager.getConnection();
			
			String sql = "UPDATE movie" + i + " SET "
					   + "word = '" + word + "', "
					   + "meaning = '" + meaning + "' "
					   + "WHERE wno = " + k;
			
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
			
			if(result > 0 ) {
				System.out.println("단어, 뜻 수정 성공");
			} else {
				System.out.println("단어, 뜻 수정 실패");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
		
	}
	
	//영화포스터를 크롤링하기 위한 메서드
	public String imgCrawling(int i){
		String tit_result = null;
		try {
			conn = DBManager.getConnection();
			
			String sql = "SELECT title FROM moviescript WHERE mno = " + i;
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				tit_result = rs.getString("title");
			}
			System.out.println(tit_result);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
			
		}
		return tit_result;
	}
	
	//시퀀스 전체 삭제하는 sql문 찾다가 빡쳐서 만든 메서드
	public void deleteSequence(int i) {
		
		try {
			conn = DBManager.getConnection();
			
			String sql = "DROP SEQUENCE seq_movie" + i;
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
	}
	
	
	
	
}
