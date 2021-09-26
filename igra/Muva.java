package igra;

import java.awt.Color;
import java.awt.Graphics;
import igra.Pozicija.Smer;

public class Muva extends Figura {
	
	public Muva(Pozicija pozicijaGlave, Color boja, int sirina, int visina) {
		super(pozicijaGlave, boja, sirina, visina);
	}
	
	public void promeniBoju(Color novaBoja) {
		boja = novaBoja;
	}

	@Override
	public void iscrtajFiguru(Graphics g) {
		Color prevColor = g.getColor();
		g.setColor(boja);
		int x = pozicijaGlave.getKolona() * sirina;
		int y = pozicijaGlave.getVrsta() * visina;
		g.drawLine(x + sirina / 2, y, x + sirina / 2, y + visina);
		g.drawLine(x, y + visina / 2, x + sirina, y + visina / 2);
		g.drawLine(x, y, x + sirina, y + visina);
		g.drawLine(x + sirina, y, x, y + visina);
		g.setColor(prevColor);
	}

	@Override
	public boolean prostire(Pozicija p) {
		return pozicijaGlave.equals(p);
	}

	@Override
	public void pomeriFiguru(Smer smer, Tabla t) {}

}

