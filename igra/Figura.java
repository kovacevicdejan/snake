package igra;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Figura {

	protected Pozicija pozicijaGlave;
	protected int sirina;
	protected int visina;
	protected Color boja;
	
	public Figura(Pozicija pozicijaGlave, Color boja, int sirina, int visina) {
		this.pozicijaGlave = pozicijaGlave;
		this.boja = boja;
		this.sirina = sirina;
		this.visina = visina;
	}
	
	public abstract void iscrtajFiguru(Graphics g);
	
	public abstract boolean prostire(Pozicija p);
	
	public abstract void pomeriFiguru(Pozicija.Smer smer, Tabla t)throws GLosaPozicija;
	
	public Pozicija dohvatiGlavu() {
		return pozicijaGlave;
	}

	public void setBoja(Color boja) {
		this.boja = boja;
	}

}
