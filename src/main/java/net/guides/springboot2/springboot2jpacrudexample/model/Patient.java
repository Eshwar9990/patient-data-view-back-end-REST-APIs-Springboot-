package net.guides.springboot2.springboot2jpacrudexample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient {
    private long id;
    private String name;
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonContacted() {
		return personContacted;
	}

	public void setPersonContacted(String personContacted) {
		this.personContacted = personContacted;
	}

	public String getPlaceOfService() {
		return placeOfService;
	}

	public void setPlaceOfService(String placeOfService) {
		this.placeOfService = placeOfService;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getIntesityType() {
		return intesityType;
	}

	public void setIntesityType(String intesityType) {
		this.intesityType = intesityType;
	}

	public String getAddOnModifier() {
		return addOnModifier;
	}

	public void setAddOnModifier(String addOnModifier) {
		this.addOnModifier = addOnModifier;
	}

	public String getLabType() {
		return labType;
	}

	public void setLabType(String labType) {
		this.labType = labType;
	}

	public String getOutsideFacility() {
		return outsideFacility;
	}

	public void setOutsideFacility(String outsideFacility) {
		this.outsideFacility = outsideFacility;
	}

	public String getEbp_ss1() {
		return ebp_ss1;
	}

	public void setEbp_ss1(String ebp_ss1) {
		this.ebp_ss1 = ebp_ss1;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	private String personContacted;
    private String placeOfService;
    private String contactType;
    private String appointmentType;
    private String billingType;
    private String intesityType;
    private String addOnModifier;
    private String labType;
    private String outsideFacility;
    private String ebp_ss1;
    private String category;
    public Patient() {
    }

    public Patient(String name, String personContacted, String placeOfService, String contactType, String appointmentType, String billingType, String intesityType, String addOnModifier, String labType, String outsideFacility, String ebp_ss1, String category) {
        this.name = name;
        this.personContacted = personContacted;
        this.placeOfService = placeOfService;
        this.contactType = contactType;
        this.appointmentType = appointmentType;
        this.billingType = billingType;
        this.intesityType = intesityType;
        this.addOnModifier = addOnModifier;
        this.labType = labType;
        this.outsideFacility = outsideFacility;
        this.ebp_ss1 = ebp_ss1;
        this.category = category;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
        public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}