package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	private int X;
	private int Y;
	private List<PowerOutages> poListParziale;
	private int customersAffected;
	private List<PowerOutages> solution;
	private List<PowerOutages> poList;
	
	public Model() {
		podao = new PowerOutageDAO();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public List<PowerOutages> getPOList(Nerc nerc){
		return this.podao.getPOList(nerc);
	}

	public List<PowerOutages> getEventList(Nerc nerc, int X, int Y){
		//inizializzo la soluzione parziale e la migliore
		this.poListParziale = new ArrayList<PowerOutages>();
		this.solution = new ArrayList<PowerOutages>();
		this.X = X;
		this.Y = Y;
		//inizializzo numero clienti affetti da blackout
		this.customersAffected = 0;
		//carico la lista di poweroutages
		this.poList = this.getPOList(nerc);
		/*for(PowerOutages po : this.getPOList(nerc)) {
			this.poList.add(po);
		}*/
		//prima chiamata al metodo ricorsivo
		this.sequenzaWorstCase(X, Y, poListParziale);
		return this.solution;
	}
	
	private void sequenzaWorstCase(int x, int y, List<PowerOutages> partial){
		// caso terminale
		if(sumClient(partial) > this.customersAffected) {
			// qui la soluzione è valida: ricorda che devo massimizzare il numero di clienti
			this.solution = new ArrayList<PowerOutages>(partial);
			// sostituisco con attuale valore migliore del numero di clienti
			this.customersAffected = sumClient(partial);
		}
		//caso normale
		for(PowerOutages event : this.poList) {
			if(controlloSommaOre(event, partial) && this.controlloAnni(event, partial) && !partial.contains(event)) {
				//aggiungo l'evento attuale
				partial.add(event);
				//mando avanti la ricorsione
				this.sequenzaWorstCase(x, y, partial);
				//backtracking
				partial.remove(partial.size()-1);
			}
		}
	}
	
	private int sumClient(List<PowerOutages> partial) {
		int partialSum = 0;
		for(PowerOutages po : partial) {
			partialSum += po.getCustomersAffected();
		}
		return partialSum;
	}
	
	private boolean controlloSommaOre(PowerOutages po, List<PowerOutages> parziale) {
		//caso livello 0
		if(parziale.size() == 0 && po.getDuration() <= this.Y) {
			return true;
		}
		//devo verificare che la somma delle ore degli eventi non superi Y
		long sumParziale = 0;
		for(PowerOutages pp : parziale) {
			sumParziale += pp.getDuration();
		}
		long oreAttuali = po.getDuration();
		if((sumParziale+oreAttuali) <= this.Y) {
			return true; //posso aggiungere quell'evento alla lista di parziali
		}
		return false;
	}
	
	private boolean controlloAnni(PowerOutages po, List<PowerOutages> parziale) {
		//caso livello 0
		if(parziale.size() == 0) {
			return true;
		}
		//devo controllare che la differenza fra l'anno più recente e l'anno più vecchio non superi X anni.
		int annoVecchio = 0;
		for(PowerOutages pp : parziale) {
			if(annoVecchio == 0) {
				annoVecchio = pp.getYear();
			}
			else if(pp.getYear() < annoVecchio) {
				annoVecchio = pp.getYear();
			}
		}
		int annoRecente = 0;
		for(PowerOutages pp : parziale) {
			if(annoRecente == 0) {
				annoRecente = pp.getYear();
			}
			else if(pp.getYear() > annoRecente) {
				annoRecente = pp.getYear();
			}
		}
		int annoNuovo = po.getYear();
		if(annoNuovo > annoVecchio) {
			if((annoNuovo-annoVecchio) <= this.X) {
				return true;
			}
		}
		if(annoNuovo < annoVecchio) {
			if((annoRecente-annoNuovo) <= this.X) {
				return true;
			}
		}
		
		return false;
	}
}
