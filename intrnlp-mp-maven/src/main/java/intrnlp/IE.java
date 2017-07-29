package intrnlp;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.simple.*;

public class IE {
	private CSVReader reader;

	public void Run() throws IOException {
		reader = new CSVReader(new FileReader("iso.csv"));
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			nextLine[5] = nextLine[5].replaceAll("; ", ", ");
			System.out.println("Standard Control: "+nextLine[4]); //header is 5th column
			extractAction(nextLine[5]); //content is in 6th column
		}
	}

	private void extractAction(String string) {
		Document doc = new Document(string);
		for (Sentence sent : doc.sentences()) {
			String longestRel = "", newAction = "", newAsset = "", longestObj = "", newOrg = "";
			// Iterate over the triples in the sentence
			for (RelationTriple triple : sent.openieTriples()) {
				if (!new Sentence(triple.relationGloss()).posTag(0).contains("VBG")){
					newAction = triple.relationGloss();
					newAsset = triple.objectGloss();
					newAction = newAction.replace("shall ", "");
					if (newAction.length()>=longestRel.length()){
						longestRel = newAction;
					}
					else newAction = "";

					if (newAsset.length()>=longestObj.length()){
						longestObj = newAsset;
					}
					else newAsset = "";
					newOrg = triple.subjectGloss();
				}
			}
			if (!longestRel.isEmpty())
				System.out.println("\tAction: "+longestRel+" "+longestObj);
			if (!newOrg.isEmpty())
				System.out.println("\t\tOrganization: "+newOrg);
		}
	}

	public static String xWord(String input, int n) {
		return input.split(" ")[n-1]; // Create array of words and return the 0th word
	}
}
