package igra;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import igra.Pozicija.Smer;

public class Zmija extends Figura {

	private ArrayList<Pozicija> clanci = new ArrayList<Pozicija>();
	
	public Zmija(Pozicija pozicijaGlave, Color boja, int sirina, int visina) {
		super(pozicijaGlave, boja, sirina, visina);
		clanci.add(pozicijaGlave);
	}

	@Override
	public void iscrtajFiguru(Graphics g) {
		int x, y;
		for(Pozicija p: clanci) {
			x = p.getKolona() * sirina;
			y = p.getVrsta() * visina;
			g.setColor(boja);
			g.fillOval(x, y, sirina, visina);
			if(p == pozicijaGlave) {
				g.setColor(Color.YELLOW);
				g.fillOval(x + sirina / 4, y + visina / 4, sirina / 2, visina / 2);
			}
		}
	}

	@Override
	public boolean prostire(Pozicija p) {
		if(p.equals(pozicijaGlave))
			return true;
		for(Pozicija poz: clanci) {
			if(poz.equals(p))
				return true;
		}
		return false;
	}

	@Override
	public void pomeriFiguru(Smer smer, Tabla t) throws GLosaPozicija {
		Pozicija p = pozicijaGlave.napravi(smer);
		if (p.getVrsta() == t.getBrojVrsta() || p.getKolona() == t.getBrojKolona() ||
			    p.getVrsta() < 0 || p.getKolona() < 0)
			throw new GLosaPozicija("Greska: glava zmije izvan table!");
		if(t.zauzeta(p))
			throw new GLosaPozicija("Greska: glava zmije udarila u telo zmije!");
		clanci.remove(clanci.size() - 1);
		pozicijaGlave = p;
		clanci.add(0, pozicijaGlave);
	}
	
	public int duzina() {
		return clanci.size();
	}
	
	public void uvecaj() {
		clanci.add(clanci.get(clanci.size() - 1));
	}

}
