package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

public class ServerNit extends Thread{
	
	LinkedList<ServerNit> klijenti;
	Socket soketZaKomunikacijuKontrolni;
	Socket soketZaKomunikacijuPodaci;
	
	public ServerNit (Socket soketZaKom, LinkedList<ServerNit> klijenti) {
		this.soketZaKomunikacijuKontrolni = soketZaKom;
		this.klijenti = klijenti;
	}

	public boolean isNumeric(String s) {  
	    try {
			return s.matches("[-+]?\\d*\\.?\\d+");
		} catch (Exception e) {
			System.out.println("Greska je sledeca: "+e);
			return false;
		}  
	}
	
	@Override
	public void run() {
		
		try {
			
			BufferedReader ulazniKontrolniTokOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKomunikacijuKontrolni.getInputStream()));
			PrintStream izlazniKontrolniTokKaKlijentu = new PrintStream(soketZaKomunikacijuKontrolni.getOutputStream());
			double zaSlanje = 0;

			izlazniKontrolniTokKaKlijentu.println("Dobrodosli, unesite svoje ime: ");
			String imeKlijenta = ulazniKontrolniTokOdKlijenta.readLine();
			izlazniKontrolniTokKaKlijentu.println("Zdravo "+imeKlijenta+" mozete zapoceti racunanje!");
			
			
			
			while(true){
				
			String nastavi = ulazniKontrolniTokOdKlijenta.readLine();
			
			if (nastavi.equals("ne")) {
				izlazniKontrolniTokKaKlijentu.println("Goodbye, have a nice day "+imeKlijenta+"!");
				break;
			}
				
			
			
			String klijentovIzborOperacije = ulazniKontrolniTokOdKlijenta.readLine();
			
			if (klijentovIzborOperacije.equals("sabiranje") || klijentovIzborOperacije.equals("oduzimanje") || klijentovIzborOperacije.equals("mnozenje") || klijentovIzborOperacije.equals("deljenje") ||
					klijentovIzborOperacije.equals("Sabiranje") || klijentovIzborOperacije.equals("Oduzimanje") || klijentovIzborOperacije.equals("Mnozenje") || klijentovIzborOperacije.equals("Deljenje") ) {
				
				izlazniKontrolniTokKaKlijentu.println("dobro");
			}
			else {
				izlazniKontrolniTokKaKlijentu.println("lose");
				continue;
			}
			
			soketZaKomunikacijuPodaci = Server.serverSoketPodaci.accept();
			BufferedReader ulazniTokPodatakaOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKomunikacijuPodaci.getInputStream()));
			PrintStream izlazniTokPodatakaKaKlijentu = new PrintStream(soketZaKomunikacijuPodaci.getOutputStream());
			
			
			String uneseniBrojevi = ulazniTokPodatakaOdKlijenta.readLine();
			
			String unetiBrojevi[] = uneseniBrojevi.split(" ");
			
			boolean tacno = true;
			for (int i = 0; i < unetiBrojevi.length; i++) {
				if(!isNumeric(unetiBrojevi[i])){
					tacno = false;
					break;
				}
			}

			if(!tacno){
				izlazniKontrolniTokKaKlijentu.println("Nisu uneti odgovarajuci brojevi!");
				continue;
			} else {
				izlazniKontrolniTokKaKlijentu.println("Uneti su odgovarajuci brojevi!");
				if( klijentovIzborOperacije.equals("sabiranje")){
					double sabiranje = 0;
					for (int i = 0; i < unetiBrojevi.length; i++) {
						double br = Double.parseDouble(unetiBrojevi[i]);
						
						sabiranje = sabiranje+br;
					}
					zaSlanje =sabiranje;
				}
				
				if( klijentovIzborOperacije.equals("oduzimanje")){
				
					double oduzimanje = Double.parseDouble(unetiBrojevi[0]);
					for (int i = 1; i < unetiBrojevi.length; i++) {
						double br = Double.parseDouble(unetiBrojevi[i]);
						oduzimanje = oduzimanje-br;
					}
					zaSlanje =oduzimanje;
				}
				
				if( klijentovIzborOperacije.equals("mnozenje")){
					double mnozenje = 1;
					for (int i = 0; i < unetiBrojevi.length; i++) {
						double br = Double.parseDouble(unetiBrojevi[i]);
						mnozenje = mnozenje*br;
					}
					zaSlanje =mnozenje;
				}
				
				if( klijentovIzborOperacije.equals("deljenje")){
					double deljenje = Double.parseDouble(unetiBrojevi[0]);
					for (int i = 1; i < unetiBrojevi.length; i++) {
						double br = Double.parseDouble(unetiBrojevi[i]);
						if(br == 0){
							
						}
						deljenje = deljenje/br;
					}
					zaSlanje =deljenje;
				}
				
				//izlazniKontrolniTokKaKlijentu.println("Broj aktivnih klijenta je: " + klijenti.size());
				izlazniTokPodatakaKaKlijentu.println(zaSlanje);
				
			}
			
			
			}
			
			
			klijenti.remove(this);
			
		} catch (IOException e) {
			
			System.out.println("Doslo je do sledece greske: "+e);
		
		}
		
		
		
	}

}
