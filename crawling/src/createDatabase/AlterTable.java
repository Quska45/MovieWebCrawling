package createDatabase;

//텍스트 마이닝을 통해 만들어진 n개의 movie 테이블에 word(dont와 같은 단어들을 원형으로 치환하기 위한 컬럼)와 meaning(단어 뜻) 컬럼을 추가 한다. 
public class AlterTable {
	public static void main(String[] args) {
		
		for(int i = 1; i <= 17; i++) {
			System.out.println(i);
			ScriptDAO sDao = new ScriptDAO();
			sDao.alterTable(i);
		}
		
	}
}
