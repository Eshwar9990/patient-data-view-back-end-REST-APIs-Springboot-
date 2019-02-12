package net.guides.springboot2.springboot2jpacrudexample.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.aspectj.weaver.ast.Var;
import org.hibernate.boot.model.source.spi.FetchCharacteristics;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.springboot2jpacrudexample.DataMine;
import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.Patient;
import net.guides.springboot2.springboot2jpacrudexample.model.PatientsAndLikelyOutcome;
import net.guides.springboot2.springboot2jpacrudexample.repository.PatientRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
@RequestMapping("/api/v1")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;
    @GetMapping("/patients")
    public PatientsAndLikelyOutcome getAllPatients() {
    	List<Patient> patientsList = patientRepository.findAll();
    	String fileData ="";
    	Patient likelyOutcome = new Patient();
    	PatientsAndLikelyOutcome fetchResponse = null;
    	try {
    		for(int i=0;i<patientsList.size();i++) {
    			fileData += "\"" + patientsList.get(i).getContactType() + "\"," + "\"" + patientsList.get(i).getPersonContacted() + "\"," + "\"" + patientsList.get(i).getPlaceOfService() + "\"," + "\"" + patientsList.get(i).getAppointmentType() + "\"," + "\"" + patientsList.get(i).getBillingType() + "\"," + "\"" + patientsList.get(i).getIntesityType() + "\"," + "\"" + patientsList.get(i).getAddOnModifier() + "\"," + "\"" + patientsList.get(i).getLabType() + "\"," + "\"" + patientsList.get(i).getOutsideFacility() + "\"," + "\"" + patientsList.get(i).getEbp_ss1() + "\"," + "\"" + patientsList.get(i).getCategory() + "\"" + "\n" ;
    		}
			FileOutputStream patientFileWriter = new FileOutputStream("patient.arff");
			patientFileWriter.write(("@relation patient\n" + 
					"\n" + 
					"@attribute contactType { \"Documentation\", \"Telephone\", \"Tele-Medicine\", \"Indirect\", \"Correspondence\", \"Other\", \"Face to Face\", \"\"}\n" + 
					"@attribute personContacted { \"Patient with Family\", \"Patient\", \"Family\", \"Guardian\", \"Patient with Guardian\", \"Other\", \"Collateral\", \"\"}\n" + 
					"@attribute placeOfService { \"Clinic/Office\", \"Home\", \"Community Setting\", \"Jail/Juvenile Detention\", \"BH State Facility\", \"Nursing Facility\", \"School\", \"Residential Program\", \"Day Program\", \"Family Child Care\", \"Hospital\", \"State Supported Living Center\", \"Court\", \"Vocational/Habilitation\", \"Psych Hospital\", \"\"}\n" + 
					"@attribute appointmentType { \"Scheduled\", \"Walk-in\\\\Unscheduled\", \"No show\", \"Cancelled by Patient\", \"Crisis\", \"Telephone\", \"Cancelled by Provider\", \"Cancelled by Family\", \"\" }\n" + 
					"@attribute billingType { \"Billable\", \"Non-billable\", \"Authorized on Plan\", \"No Authorization Required\", \"PAP - Service Funded\", \"Unauthorized - Not on Plan\", \"No Current Plan\", \"Expanded Problem-Focused\", \"Crisis\", \"\" }\n" + 
					"@attribute intesityType { \"Crisis\", \"Routine\", \"High\", \"Moderate\", \"Low\", \"\" }\n" + 
					"@attribute addOnModifier { \"Total Time Test\", \"Initial Evaluation\", \"Enrichment Service\", \"\" }\n" + 
					"@attribute labType { \"Urine Drug Screen\", \"Urine Dipstick\", \"Blood Glucose\", \"CBC\", \"\" }\n" + 
					"@attribute outsideFacility { \"Outside Facility A\", \"Outside Facility B\", \"Outside Facility C\", \"\" }\n" + 
					"@attribute ebp_ss1 { \"Assertive Community Treatment\", \"Supportive Employment\", \"Supportive Housing\", \"Family Psychoeducation\", \"Integrated Dual Diagnosis Tx\", \"\" }\n" + 
					"@attribute category { \"Indicated\", \"Selected\", \"Universal\", \"\" }\n" + 
					"\n" + 
					"@data" + "\n" +fileData).getBytes());
			patientFileWriter.close();
			DataMine dataMine = new DataMine();
			try {
				HashMap<String, String> dataMineLikelyOutcome = dataMine.mostLikelyOutcome();
				likelyOutcome.setContactType(dataMineLikelyOutcome.get("contactType"));
				likelyOutcome.setPersonContacted(dataMineLikelyOutcome.get("personContacted"));
				likelyOutcome.setPlaceOfService(dataMineLikelyOutcome.get("placeOfService"));
				likelyOutcome.setAppointmentType(dataMineLikelyOutcome.get("appointmentType"));
				likelyOutcome.setBillingType(dataMineLikelyOutcome.get("billingType"));
				likelyOutcome.setIntesityType(dataMineLikelyOutcome.get("intesityType"));
				likelyOutcome.setAddOnModifier(dataMineLikelyOutcome.get("addOnModifier"));
				likelyOutcome.setLabType(dataMineLikelyOutcome.get("labType"));
				likelyOutcome.setOutsideFacility(dataMineLikelyOutcome.get("outsideFacility"));
				likelyOutcome.setEbp_ss1(dataMineLikelyOutcome.get("ebp_ss1"));
				likelyOutcome.setCategory(dataMineLikelyOutcome.get("category"));
				fetchResponse = new PatientsAndLikelyOutcome(patientsList, likelyOutcome);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return fetchResponse;
    }
    
    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable(value = "id") Long patientId)
        throws ResourceNotFoundException {
    	Patient patient = patientRepository.findById(patientId)
          .orElseThrow(() -> new ResourceNotFoundException("Patient not found for this id :: " + patientId));
        return ResponseEntity.ok().body(patient);
    }
    @PostMapping("/patients")
    public Patient createPatient(@Valid @RequestBody Patient patient) {   	
        return patientRepository.save(patient);
    }
    @PutMapping("/patients/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable(value = "id") Long patientId,
         @Valid @RequestBody Patient patientDetails) throws ResourceNotFoundException {
    	Patient patient = patientRepository.findById(patientId)
        .orElseThrow(() -> new ResourceNotFoundException("Patient not found for this id :: " + patientId));
    	patient.setName(patientDetails.getName());
    	patient.setPersonContacted(patientDetails.getPersonContacted());
    	patient.setPlaceOfService(patientDetails.getPlaceOfService());
    	patient.setContactType(patientDetails.getContactType());
    	patient.setAppointmentType(patientDetails.getAppointmentType());
    	patient.setBillingType(patientDetails.getBillingType());
    	patient.setIntesityType(patientDetails.getIntesityType());
    	patient.setAddOnModifier(patientDetails.getAddOnModifier());
    	patient.setLabType(patientDetails.getLabType());
    	patient.setOutsideFacility(patientDetails.getOutsideFacility());
    	patient.setEbp_ss1(patientDetails.getEbp_ss1());
    	patient.setCategory(patientDetails.getCategory());
    	
        final Patient updatedPatient = patientRepository.save(patient);
        return ResponseEntity.ok(updatedPatient);
    }
    @DeleteMapping("/patients/{id}")
    public Map<String, Boolean> deletePatient(@PathVariable(value = "id") Long patientId)
         throws ResourceNotFoundException {
        Patient patient = patientRepository.findById(patientId)
       .orElseThrow(() -> new ResourceNotFoundException("Patient not found for this id :: " + patientId));
        patientRepository.delete(patient);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}