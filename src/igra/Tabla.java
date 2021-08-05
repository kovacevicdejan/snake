package igra;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import igra.Pozicija.Smer;

public class Tabla extends Canvas implements Runnable {

	private int brojVrsta;
	private int brojKolona;
	private Muva muva = null;
	private Zmija zmija = null;
	private Thread nit = new Thread(this);
	private int interval;
	private Smer tekuci = Smer.DESNO;
	private Igra vlasnik;
	private int radi = 0;
	private int tema = 0;
	private Color bojaLinija = Color.DARK_GRAY;
	private Color bojaMuve = Color.BLACK;
	
	public Tabla(int brojVrsta, int brojKolona) {
		super();
		this.brojVrsta = brojVrsta;
		this.brojKolona = brojKolona;
		this.interval = 500;
		nit.start();
		pokreni(brojVrsta, brojKolona);
		
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				switch(key) {
				case KeyEvent.VK_LEFT:
					postaviTekuci(Smer.LEVO);
				    break;
				case KeyEvent.VK_RIGHT:
					postaviTekuci(Smer.DESNO);
				    break;
				case KeyEvent.VK_UP:
					postaviTekuci(Smer.GORE);
				    break;
				case KeyEvent.VK_DOWN:
					postaviTekuci(Smer.DOLE);
				    break;
				}
			}
			
		});
	}

	public int getBrojVrsta() {
		return brojVrsta;
	}
	
	public void setBrojVrsta(int brojVrsta) {
		this.brojVrsta = brojVrsta;
	}
	
	public int getBrojKolona() {
		return brojKolona;
	}
	
	public void setBrojKolona(int brojKolona) {
		this.brojKolona = brojKolona;
	}
	
	@Override
	public void paint(Graphics g) {
		iscrtajLinije();
		if(zmija == null && muva == null)
			namestiFigure();
		iscrtajFigure();
	}
	
	private void iscrtajLinije() {
		int dimX = getWidth();
		int dimY = getHeight();
		int korak = dimX / brojKolona;
		Graphics g = getGraphics();
		g.setColor(bojaLinija);
		for(int i = 0; i < dimX; i += korak) {
			g.drawLine(i, 0, i, dimY - 1);
		}
		
		korak = dimY / brojVrsta;
		for(int i = 0; i < dimY; i += korak) {
			g.drawLine(0, i, dimX - 1, i);
		}
		
		g.setColor(Color.BLACK);
		
		g.drawLine(0, 0, dimX, 0);
		g.drawLine(0, 0, 0, dimY);
		g.drawLine(0, dimY, dimX, dimY);
		g.drawLine(dimX, 0, dimX, dimY);
	}
	
	public void iscrtajFigure() {
		zmija.iscrtajFiguru(getGraphics());
		muva.iscrtajFiguru(getGraphics());
		muva.iscrtajFiguru(getGraphics());
	}
	
	public void namestiFigure() {
		int sirina = getWidth() / brojKolona;
		int visina = getHeight() / brojVrsta;
		int x = (int)(brojVrsta / 2);
		int y = (int)(brojKolona / 2);
		Pozicija pz = new Pozicija(x, y);
		zmija = new Zmija(pz, Color.GREEN, sirina, visina);
		generisiMuvu();
	}
	
	public void generisiMuvu() {
		int x, y;
		int sirina = getWidth() / brojKolona;
		int visina = getHeight() / brojVrsta;
		Pozicija pm;
		do {
			x = (int)(Math.random() * brojVrsta);
			y = (int)(Math.random() * brojKolona);
			pm = new Pozicija(x, y);
		} while(zauzeta(pm));
		muva = new Muva(pm, bojaMuve, sirina, visina);
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				synchronized (this) {
					while(radi == 0)
						wait();
				}
				Thread.sleep(interval);
                azuriraj();
				repaint();
			}
		} catch(InterruptedException e) {}
	}
	
	public synchronized void pokreni(int x, int y) {
		radi = 1;
		brojVrsta = x;
		brojKolona = y;
		tekuci = Smer.DESNO;
		zmija = null;
		muva = null;
		repaint();
		notify();
	}
	
	public synchronized void stani() {
		radi = 0;
	}
	
	public void zaustavi() {
		nit.interrupt();
	}
	
	public synchronized void postaviTekuci(Smer novi) {
		tekuci = novi;
	}
	
	public boolean zauzeta(Pozicija p) {
		return zmija.prostire(p);
	}
	
	public synchronized void azuriraj() {
		try {
			if(zmija != null && muva != null) {
				if(zmija.pozicijaGlave.equals(muva.pozicijaGlave)) {
					zmija.uvecaj();
					generisiMuvu();
					vlasnik.duzina.setText(zmija.duzina() + "");
				}
				zmija.pomeriFiguru(tekuci, this);
			}
		} catch (GLosaPozicija g) {
			g.printStackTrace();
			zmija.boja = Color.RED;
			stani();
		}
	}
	
	public void postaviVlasnika(Igra i) {
		vlasnik = i;
	}
	
	public synchronized void postaviInterval(int i) {
		if(radi == 0)
		    interval = i;
	}
	
	public void promeniTemu() {
		if(tema == 0) {
			tema = 1;
			bojaLinija = Color.LIGHT_GRAY;
			muva.promeniBoju(Color.WHITE);
			bojaMuve = Color.WHITE;
		}
		else {
			tema = 0;
			bojaLinija = Color.DARK_GRAY;
			muva.promeniBoju(Color.BLACK);
			bojaMuve = Color.BLACK;
		}
		repaint();
	}
	
	public synchronized void nastavi() {
		radi = 1;
		notify();
	}
}
