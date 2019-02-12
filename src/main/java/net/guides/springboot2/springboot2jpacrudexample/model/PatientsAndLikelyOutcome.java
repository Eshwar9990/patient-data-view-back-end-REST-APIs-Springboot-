package net.guides.springboot2.springboot2jpacrudexample.model;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

public class PatientsAndLikelyOutcome {
    
    private List<Patient> fetchData;
    private Patient mostLikelyOutcome;
    
	
    
	public List<Patient> getFetchData() {
		return fetchData;
	}



	public void setFetchData(List<Patient> fetchData) {
		this.fetchData = fetchData;
	}



	public Patient getMostLikelyOutcome() {
		return mostLikelyOutcome;
	}



	public void setMostLikelyOutcome(Patient mostLikelyOutcome) {
		this.mostLikelyOutcome = mostLikelyOutcome;
	}



	public PatientsAndLikelyOutcome(List<Patient> patientList, Patient outcome) {
		fetchData = patientList;
		mostLikelyOutcome = outcome;
    }
    
}