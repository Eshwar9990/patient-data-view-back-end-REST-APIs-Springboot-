package net.guides.springboot2.springboot2jpacrudexample;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.expressionlanguage.core.VariableDeclarations;
import weka.filters.unsupervised.attribute.Remove;
import weka.knowledgeflow.Data;
import weka.core.AttributeStats;
import weka.core.Capabilities;
import weka.core.Attribute;

public class DataMine {
	String[] defaultHeadings = { "contactType", "personContacted", "placeOfService", "appointmentType", "billingType",
			"intesityType", "addOnModifier", "labType", "outsideFacility", "ebp_ss1", "category" };
	String[] contactType = { "Documentation", "Telephone", "Tele-Medicine", "Indirect", "Correspondence", "Other",
			"Face to Face", "" };
	String[] personContacted = { "Patient with Family", "Patient", "Family", "Guardian", "Patient with Guardian",
			"Other", "Collateral", "" };
	String[] placeOfService = { "Clinic/Office", "Home", "Community Setting", "Jail/Juvenile Detention",
			"BH State Facility", "Nursing Facility", "School", "Residential Program", "Day Program",
			"Family Child Care", "Hospital", "State Supported Living Center", "Court", "Vocational/Habilitation",
			"Psych Hospital", "" };
	String[] appointmentType = { "Scheduled", "Walk-in\\Unscheduled", "No show", "Cancelled by Patient", "Crisis",
			"Telephone", "Cancelled by Provider", "Cancelled by Family", "" };
	String[] billingType = { "Billable", "Non-billable", "Authorized on Plan", "No Authorization Required",
			"PAP - Service Funded", "Unauthorized - Not on Plan", "No Current Plan", "Expanded Problem-Focused",
			"Crisis", "" };
	String[] intesityType = { "Crisis", "Routine", "High", "Moderate", "Low", "" };
	String[] addOnModifier = { "Total Time Test", "Initial Evaluation", "Enrichment Service", "" };
	String[] labType = { "Urine Drug Screen", "Urine Dipstick", "Blood Glucose", "CBC", "" };
	String[] outsideFacility = { "Outside Facility A", "Outside Facility B", "Outside Facility C", "" };
	String[] ebp_ss1 = { "Assertive Community Treatment", "Supportive Employment", "Supportive Housing",
			"Family Psychoeducation", "Integrated Dual Diagnosis Tx", "" };
	String[] category = { "Indicated", "Selected", "Universal", "" };

	public static HashMap<String, String> hmap = new HashMap<String, String>();

	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;

		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}

		return inputReader;
	}

	public static Evaluation classify(Classifier model, Instances trainingSet, Instances testingSet) throws Exception {
		Evaluation evaluation = new Evaluation(trainingSet);

		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);

		return evaluation;
	}

	public static double calculateAccuracy(FastVector predictions) {
		double correct = 0;

		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}

		return 100 * correct / predictions.size();
	}

	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];

		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}

		return split;
	}

	private void updateMax(int headingIndex, int maxIndex, ArrayList<Integer> statCounts) {
		System.out.println("updateMax Index " + maxIndex);
		System.out.println("updateMax Value " + statCounts.get(maxIndex));
		Integer totalWeight = statCounts.stream().mapToInt(Integer::intValue).sum();
		System.out.println("Total: " + totalWeight);
		System.out.println("Div: " + (double) statCounts.get(maxIndex) * 100 / (double) totalWeight);
		if ((double) statCounts.get(maxIndex) * 100 / (double) totalWeight > 75) {
			System.out.println("done" + defaultHeadings[headingIndex]);
			switch (defaultHeadings[headingIndex]) {
			case "contactType":
				hmap.put(defaultHeadings[headingIndex], contactType[maxIndex]);
				break;
			case "personContacted":
				hmap.put(defaultHeadings[headingIndex], personContacted[maxIndex]);
				break;
			case "placeOfService":
				hmap.put(defaultHeadings[headingIndex], placeOfService[maxIndex]);
				break;
			case "appointmentType":
				hmap.put(defaultHeadings[headingIndex], appointmentType[maxIndex]);
				break;
			case "billingType":
				hmap.put(defaultHeadings[headingIndex], billingType[maxIndex]);
				break;
			case "intesityType":
				hmap.put(defaultHeadings[headingIndex], intesityType[maxIndex]);
				break;
			case "addOnModifier":
				hmap.put(defaultHeadings[headingIndex], addOnModifier[maxIndex]);
				break;
			case "labType":
				hmap.put(defaultHeadings[headingIndex], labType[maxIndex]);
				break;
			case "outsideFacility":
				hmap.put(defaultHeadings[headingIndex], outsideFacility[maxIndex]);
				break;
			case "ebp_ss1":
				hmap.put(defaultHeadings[headingIndex], ebp_ss1[maxIndex]);
				break;
			case "category":
				hmap.put(defaultHeadings[headingIndex], category[maxIndex]);
				break;
			default:
				break;
			}
		} else {
			hmap.put(defaultHeadings[headingIndex], "");
		}
	}

	/**
	 * @param args
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public HashMap<String, String> mostLikelyOutcome() throws Exception {
		BufferedReader datafile = readDataFile("patient.arff");
		Instances data = new Instances(datafile);
		data.setClassIndex(data.numAttributes() - 1);

		// Length to be modified
		for (int i = 0; i < defaultHeadings.length; i++) {
			ArrayList<Integer> statCounts = new ArrayList<>();
			AttributeStats stats = data.attributeStats(i);
			int[] nominalCounts = stats.nominalCounts;
			for (int j = 0; j < nominalCounts.length; j++) {
				statCounts.add(nominalCounts[j]);
			}

			Integer presentIndex = i;
			IntStream.range(0, statCounts.size()).boxed().max(Comparator.comparing(statCounts::get))
					.ifPresent(maxIndex -> new DataMine().updateMax(presentIndex, maxIndex, statCounts));

		}
		System.out.println("Hmap: " + hmap);

		// Do 10-split cross validation
		Instances[][] split = crossValidationSplit(data, 10);

		// Separate split into training and testing arrays
		Instances[] trainingSplits = split[0];
		Instances[] testingSplits = split[1];

		// Use a set of classifiers
		Classifier[] models = { new J48(), // a decision tree
				new NaiveBayes(), new PART(), new DecisionTable(), // decision table majority classifier
				new DecisionStump() // one-level decision tree
		};

		// Run for each model
		for (int j = 0; j < models.length; j++) {
			FastVector predictions = new FastVector();

			// For each training-testing split pair, train and test the classifier
			for (int i = 0; i < trainingSplits.length; i++) {

				Evaluation validation = classify(models[j], trainingSplits[i], testingSplits[i]);

				predictions.appendElements(validation.predictions());

				// Uncomment to see the summary for each training-testing pair.
				// System.out.println(models[j]);
			}

			// Calculate overall accuracy of current classifier on all splits
			double accuracy = calculateAccuracy(predictions);

			System.out.println("Final output " + models[j]);

			System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
					+ String.format("%.2f%%", accuracy) + "\n---------------------------------");

		}
		return hmap;
	}
}