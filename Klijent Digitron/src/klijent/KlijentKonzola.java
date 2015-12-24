package klijent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class KlijentKonzola {
	
	public static void main(String[] args) {
		
		BufferedReader ulazSaTastature = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			
			Socket klijentskiSoketKontrolni = new Socket("localhost", 1908);
			BufferedReader tokKaKlijentu = new BufferedReader(new InputStreamReader(klijentskiSoketKontrolni.getInputStream()));
			PrintStream tokOdKlijenta = new PrintStream(klijentskiSoketKontrolni.getOutputStream());
			
			System.out.println(tokKaKlijentu.readLine());
			String imeKlijenta = ulazSaTastature.readLine();
			tokOdKlijenta.println(imeKlijenta);
			System.out.println(tokKaKlijentu.readLine());
			
			String nastavi= "da";
			tokOdKlijenta.println(nastavi);
			
			
			while (nastavi.equals("da")) {
				
				System.out.println("Unesite racunsku operaciju: "+"\n\t"+
						"sabiranje, "+"\n\t"+
						"oduzimanje, "+"\n\t"+
						"mnozenje ili "+"\n\t"+
						"deljenje. ");
				
				String racunskaOperacija = ulazSaTastature.readLine();
				
				tokOdKlijenta.println(racunskaOperacija);
				
				String odgovorOdServeraNaRacunskuOperaciju = tokKaKlijentu.readLine();
				
				if(odgovorOdServeraNaRacunskuOperaciju.equals("lose")){
					
					System.out.println("Ponovite unos racunske operacije! Niste uneli odgovarajucu racunsku operaciju");
					tokOdKlijenta.println(nastavi);
					continue;
					
				} else{
			
					System.out.println("Nakon operacije unesite brojeve tako da izmedju svaka dva bude mesto razmaka.");
					String unosBrojeva = ulazSaTastature.readLine();
					
					Socket klijentskiSoketPodaci = new Socket("localhost", 2113);
					BufferedReader tokKaKlijentuPodaci = new BufferedReader(new InputStreamReader(klijentskiSoketPodaci.getInputStream()));
					PrintStream tokOdKlijentaPodaci = new PrintStream(klijentskiSoketPodaci.getOutputStream());
					
					
					tokOdKlijentaPodaci.println(unosBrojeva); 
					
					String unetiOdgovarajuciBrojevi = tokKaKlijentu.readLine();
					
					if(unetiOdgovarajuciBrojevi.equals("Nisu uneti odgovarajuci brojevi!")){
						System.out.println("Nisu uneti brojevi u odgovarajucoj formi, pokusajte ispocetka, unesite ispravno operaciju i zatim zeljene brojeve!");
						tokOdKlijenta.println(nastavi);
						klijentskiSoketPodaci.close();
						continue;
					} else {
						
						//System.out.println(tokKaKlijentu.readLine());
						
						String odgovorServera = tokKaKlijentuPodaci.readLine();
					
						System.out.println("Rezultat: " + odgovorServera);
						
						System.out.println("Ukucajte 'ne' ako ne zelite vise da racunate"+"\n"+
								"U slucaju da zelite da nastavite racunanje napisite 'da'!");
						
	
						klijentskiSoketPodaci.close();
						
						
						boolean promenljiva = true;
						while(promenljiva){
							nastavi = ulazSaTastature.readLine();
							if (nastavi.equals("da") || nastavi.equals("ne")) {
								tokOdKlijenta.println(nastavi);
								promenljiva = false;
							} else {
								System.out.println("Kako bismo znali sta zelite dalje, morate napisati eksplicitno"+"\n\t"+" da " +"\n"+"ili "+"\n\t"+" ne ");
							}
						
						}
					}
				
				}
			}
			
			String odgovorServeraNaKraj = tokKaKlijentu.readLine();
			System.out.println(odgovorServeraNaKraj);
			
			klijentskiSoketKontrolni.close(); 
			
		} catch (UnknownHostException e) {
			System.out.println("Doslo je do greske: "+e);
		} catch (IOException e) {
			System.out.println("Doslo je do greske: "+e);
		}
	
	}
	
}
