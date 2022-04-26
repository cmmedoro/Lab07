package it.polito.tdp.poweroutages.model;

//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PowerOutages {
	
	private int id;
	private Nerc nerc;
	private int customersAffected;
	private LocalDateTime dateEventBegan;
	private LocalDateTime dateEventFinished;
	private long duration;
	private int year;
	
	public PowerOutages(int id, Nerc nerc, int customersAffected, LocalDateTime dateEventBegan, LocalDateTime dateEventFinished) {
		super();
		this.id = id;
		this.nerc = nerc;
		this.customersAffected = customersAffected;
		this.dateEventBegan = dateEventBegan;
		this.dateEventFinished = dateEventFinished;
		
		this.year = this.dateEventBegan.getYear();
		LocalDateTime beginning = LocalDateTime.from(dateEventBegan);
		this.duration = beginning.until(dateEventFinished, ChronoUnit.HOURS);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Nerc getNerc() {
		return nerc;
	}

	public void setNerc(Nerc nerc) {
		this.nerc = nerc;
	}

	public int getCustomersAffected() {
		return customersAffected;
	}

	public void setCustomersAffected(int customersAffected) {
		this.customersAffected = customersAffected;
	}

	public LocalDateTime getDateEventBegan() {
		return dateEventBegan;
	}

	public void setDateEventBegan(LocalDateTime dateEventBegan) {
		this.dateEventBegan = dateEventBegan;
	}

	public LocalDateTime getDateEventFinished() {
		return dateEventFinished;
	}

	public void setDateEventFinished(LocalDateTime dateEventFinished) {
		this.dateEventFinished = dateEventFinished;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PowerOutages other = (PowerOutages) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return year + " " + dateEventBegan + " " + dateEventFinished + " " + duration + " " + this.customersAffected;
	}
	
	
	
	
}
