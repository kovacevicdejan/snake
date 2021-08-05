package igra;

public class Pozicija {

	enum Smer{LEVO, GORE, DESNO, DOLE};
	
	private int vrsta;
	private int kolona;
	
	public Pozicija(int vrsta, int kolona) {
		this.vrsta = vrsta;
		this.kolona = kolona;
	}

	public int getVrsta() {
		return vrsta;
	}

	public int getKolona() {
		return kolona;
	}
	
	public void pomeri(Smer smer) {
		switch(smer) {
		case LEVO:
			kolona--;
		case GORE:
			vrsta--;
		case DESNO:
			kolona++;
		case DOLE:
			vrsta++;
		}
	}
	
	public Pozicija napravi(Smer smer) {
		Pozicija p;
		switch(smer) {
		case LEVO:
			p = new Pozicija(vrsta, kolona - 1);
			break;
		case GORE:
			p = new Pozicija(vrsta - 1, kolona);
			break;
		case DESNO:
			p = new Pozicija(vrsta, kolona + 1);
			break;
		case DOLE:
			p = new Pozicija(vrsta + 1, kolona);
			break;
		default:
			p = this;
			break;
		}
		return p;
	}

	@Override
	public boolean equals(Object obj) {
		return vrsta == ((Pozicija)obj).vrsta && kolona == ((Pozicija)obj).kolona;
	}
	
}
