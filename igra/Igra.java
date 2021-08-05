package igra;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import igra.Pozicija.Smer;

public class Igra extends Frame {
	
	private Tabla tabla = new Tabla(20, 20);
	private Panel donjiPanel = new Panel(new GridLayout(0, 1, 0, 0));
	Label duzina = new Label("1");
	Panel centralniPanel = new Panel();
	private TextField x = new TextField("20");
	private TextField y = new TextField("20");
	Label d = new Label("Length: ");
	Panel drugiPanel = new Panel(new GridLayout(1, 2, 0, 0));
	Panel donjiLevi = new Panel();
	Panel donjiDesni = new Panel();
	Label xy = new Label("x, y: ");
	Panel prviPanel = new Panel(new GridLayout(1, 2, 5, 0));
	Button stani = new Button("Stop");
	Choice izbor = new Choice();
	private int t = 0;
	private int radnja = 0;

	public Igra() {
		setBounds(400, 100, 500, 300);
		setResizable(false);
		
		setTitle("Snake");
		
		populateWindow();
		pack();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				tabla.zaustavi();
			}
		});
		
		setVisible(true);
	}
	
	public void populateWindow() {
		donjiLevi.setBackground(Color.LIGHT_GRAY);
		donjiDesni.setBackground(Color.LIGHT_GRAY);
		d.setBackground(Color.LIGHT_GRAY);
		duzina.setBackground(Color.LIGHT_GRAY);
		xy.setBackground(Color.LIGHT_GRAY);
		prviPanel.setBackground(Color.LIGHT_GRAY);
		stani.setBackground(Color.LIGHT_GRAY);
		tabla.setFocusable(true);
		izbor.setFocusable(false);
		stani.setFocusable(false);
		x.setFocusable(false);
		y.setFocusable(false);
		
		MenuItem novaIgra = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N));
		MenuItem zatvori = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_Z));
		
		Menu file = new Menu("Action");
		novaIgra.addActionListener((ae) -> {
			tabla.pokreni(Integer.parseInt(x.getText()), Integer.parseInt(y.getText()));
			tabla.setFocusable(true);
			stani.setLabel("Stop");
			radnja = 0;
			duzina.setText("1");
		});
		file.add(novaIgra);
		file.addSeparator();
		zatvori.addActionListener((ae) -> {
			tabla.zaustavi();
			dispose();
		});
		file.add(zatvori);
		
		Menu tema = new Menu("Theme");
		MenuItem tamnaTema = new MenuItem("Dark theme", new MenuShortcut(KeyEvent.VK_D));
		MenuItem svetlaTema = new MenuItem("Light theme", new MenuShortcut(KeyEvent.VK_L));
		
		tamnaTema.addActionListener((ae) -> {
			donjiLevi.setBackground(Color.DARK_GRAY);
			donjiDesni.setBackground(Color.DARK_GRAY);
			d.setBackground(Color.DARK_GRAY);
			duzina.setBackground(Color.DARK_GRAY);
			xy.setBackground(Color.DARK_GRAY);
			prviPanel.setBackground(Color.DARK_GRAY);
			stani.setBackground(Color.DARK_GRAY);
			d.setForeground(Color.WHITE);
			duzina.setForeground(Color.WHITE);
			xy.setForeground(Color.WHITE);
			stani.setForeground(Color.WHITE);
			izbor.setBackground(Color.DARK_GRAY);
			izbor.setForeground(Color.WHITE);
			x.setBackground(Color.DARK_GRAY);
			y.setBackground(Color.DARK_GRAY);
			x.setForeground(Color.WHITE);
			y.setForeground(Color.WHITE);
			tabla.setBackground(Color.DARK_GRAY);
			if(t == 0) {
				t = 1;
			    tabla.promeniTemu();
			}
		});
		
        svetlaTema.addActionListener((ae) -> {
        	donjiLevi.setBackground(Color.LIGHT_GRAY);
			donjiDesni.setBackground(Color.LIGHT_GRAY);
			d.setBackground(Color.LIGHT_GRAY);
			duzina.setBackground(Color.LIGHT_GRAY);
			xy.setBackground(Color.LIGHT_GRAY);
			prviPanel.setBackground(Color.LIGHT_GRAY);
			stani.setBackground(Color.LIGHT_GRAY);
			d.setForeground(Color.BLACK);
			duzina.setForeground(Color.BLACK);
			xy.setForeground(Color.BLACK);
			stani.setForeground(Color.BLACK);
			izbor.setBackground(Color.WHITE);
			izbor.setForeground(Color.BLACK);
			x.setBackground(Color.WHITE);
			y.setBackground(Color.WHITE);
			x.setForeground(Color.BLACK);
			y.setForeground(Color.BLACK);
			tabla.setBackground(Color.LIGHT_GRAY);
			if(t == 1) {
				t = 0;
			    tabla.promeniTemu();
			}
		});
        
        tema.add(tamnaTema);
        tema.addSeparator();
        tema.add(svetlaTema);
		
		MenuBar menuBar = new MenuBar();
		menuBar.add(file);
		menuBar.add(tema);
		setMenuBar(menuBar);
		
		int dim1 = getWidth() / tabla.getBrojKolona() * tabla.getBrojKolona();
		int dim2 = getHeight() / tabla.getBrojVrsta() * tabla.getBrojVrsta();
		tabla.setPreferredSize(new Dimension(dim1, dim2));
		tabla.setBackground(Color.LIGHT_GRAY);
		tabla.postaviVlasnika(this);
		tabla.postaviInterval(500);
		centralniPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		centralniPanel.add(tabla);
		add(centralniPanel, BorderLayout.CENTER);
		
		izbor.add("Easy");
		izbor.add("Middle");
		izbor.add("Hard");
		
		izbor.addItemListener((ie) -> {
			String mod = izbor.getSelectedItem();
			if(mod.equals("Easy"))
				tabla.postaviInterval(500);
			else if(mod.equals("Middle"))
				tabla.postaviInterval(300);
			else if(mod.equals("Hard"))
				tabla.postaviInterval(100);
		});
		prviPanel.add(izbor);
		
		stani.addActionListener((ae) -> {
			if(radnja == 0) {
				radnja = 1;
				tabla.stani();
				stani.setLabel("Continue");
			}
			else {
				radnja = 0;
				tabla.nastavi();
				stani.setLabel("Stop");
			}
		});
		
		stani.setBackground(Color.LIGHT_GRAY);
		prviPanel.add(stani);
		donjiPanel.add(prviPanel);
		
		d.setFont(new Font("Arial", Font.BOLD, 16));
		donjiLevi.add(d);
		duzina.setFont(new Font("Arial", Font.BOLD, 16));
		donjiLevi.add(duzina);
		drugiPanel.add(donjiLevi);
		
		donjiDesni.add(xy);
		
		donjiDesni.add(x);
		donjiDesni.add(y);
		
		drugiPanel.add(donjiDesni);
		
		donjiPanel.add(drugiPanel);
		add(donjiPanel, BorderLayout.SOUTH);
	}
	
	
	public static void main(String[] args) {
		new Igra();
	}
	
}
