package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

	public static ServerSocket serverSoketPodaci;
	
	public static void main(String[] args){
		
		LinkedList <ServerNit> klijenti = new LinkedList<ServerNit>();
		
		try {
			
			ServerSocket serverSoketKontrolni = new ServerSocket(1908);
			serverSoketPodaci = new ServerSocket(2113);
			
			while(true){
				
				Socket klijentSoketZaKomunikaciju = serverSoketKontrolni.accept();
				
				ServerNit noviKlijent = new ServerNit(klijentSoketZaKomunikaciju,klijenti);
				
				klijenti.add(noviKlijent);
				
				noviKlijent.start();
				
			}
			
		} catch (IOException e) {
			System.out.println("Doslo je do greske: "+e);
		}
		

	}

}
