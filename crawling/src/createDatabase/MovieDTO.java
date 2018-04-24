package createDatabase;

public class MovieDTO {

	private int wno;
	private String morpheme;
	private int freq;
	private String word;
	private String meaning;
	
	
	public MovieDTO() {
		super();
	}


	public MovieDTO(int wno, String morpheme, int freq, String word, String meaning) {
		super();
		this.wno = wno;
		this.morpheme = morpheme;
		this.freq = freq;
		this.word = word;
		this.meaning = meaning;
	}
	
	


	public MovieDTO(String morpheme, int freq) {
		super();
		this.morpheme = morpheme;
		this.freq = freq;
	}


	public MovieDTO(String word, String meaning) {
		super();
		this.word = word;
		this.meaning = meaning;
	}


	public int getWno() {
		return wno;
	}


	public void setWno(int wno) {
		this.wno = wno;
	}


	public String getMorpheme() {
		return morpheme;
	}


	public void setMorpheme(String morpheme) {
		this.morpheme = morpheme;
	}


	public int getFreq() {
		return freq;
	}


	public void setFreq(int freq) {
		this.freq = freq;
	}


	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}


	public String getMeaning() {
		return meaning;
	}


	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	
	
	
	
	
}
