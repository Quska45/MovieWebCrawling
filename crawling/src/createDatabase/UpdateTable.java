package createDatabase;

public class UpdateTable {
	public static void main(String[] args) {
		
		for(int i = 1; i <= 17; i++) {
			ScriptDAO sDao = new ScriptDAO();
			sDao.selectWord(i);
			
		}
		
	}
}
