package intrnlp;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.simple.*;

public class IE {
	private CSVReader reader;
	private ArrayList<String> toWrite = new ArrayList<String>();

	public void Run() throws IOException {
		reader = new CSVReader(new FileReader("iso.csv"));
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			toWrite.clear();
			nextLine[5] = nextLine[5].replaceAll(";", ",");
			nextLine[5] = nextLine[5].replaceAll("(s)", "s");
			toWrite.add("Standard Control: "+nextLine[1]+" - "+nextLine[4]); //header is 5th column
			extractAction(nextLine[5]); //content is in 6th column
			print();
		}
	}

	private void extractAction(String string) {
		Document doc = new Document(string);
		String[] list = null;
		for (Sentence sent : doc.sentences()) {
			String newAction = "", newAsset = "", newOrg = "";
			if (sent.toString().contains("a)") || sent.toString().contains("1)")) {
				list = sent.toString().split("[a-z][)]");
				for(int i=1; i<list.length;i++) {
					newAction = ""; 
					newAsset = ""; 
					newOrg = "";
					String newS = (list[0]+list[i]).replaceAll("\\n", " ");
					newS = newS.replaceAll(":[ ]+ ", " ");
					newS = newS.replaceAll(":[ ]+.", " ");
					newS = newS.replaceAll(",$", ".");
					newS = newS.replaceAll(",[ ]+$", ".");
					newS = newS.replaceAll(", [ ]+", " ");
					newS = newS.replaceAll("NOTE [1-9]+ ", "");
					newS = newS.replaceAll("NOTE ", "");
					Sentence newSent = new Document(newS).sentence(0);
					for (RelationTriple triple : newSent.openieTriples()) {
						if (!new Sentence(triple.relationGloss()).posTag(0).contains("VBG")){
							newAction = triple.relationGloss();
							newAsset = triple.objectGloss();
							newAction = newAction.replace("shall ", "");
							newOrg = triple.subjectGloss();
						}
					}
					if (!newAction.isEmpty() && !newOrg.isEmpty() && !newAction.equalsIgnoreCase("Shall") && !newAsset.contains(newAction)) {
						addtoWrite("\tAction: "+newAction+"\n\t\tAsset: "+newAsset+"\n\t\t\tOrganization: "+newOrg);
					}
				}
			}
			// Iterate over the triples in the sentence
			else {
				String newS = sent.toString();
				newS = newS.replaceAll("NOTE [1-9]+ ", "");
				newS = newS.replaceAll("NOTE ", "");
				Sentence newSent = new Document(newS).sentence(0);
				for (RelationTriple triple : newSent.openieTriples()) {
					if (!new Sentence(triple.relationGloss()).posTag(0).contains("VBG")){
						newAction = triple.relationGloss();
						newAsset = triple.objectGloss();
						newAction = newAction.replace("shall ", "");
						newOrg = triple.subjectGloss();
					}
				}
				if (!newAction.isEmpty() && !newOrg.isEmpty() && !newAction.equalsIgnoreCase("Shall") && !newAsset.contains(newAction)) {
					addtoWrite("\tAction: "+newAction+"\n\t\tAsset: "+newAsset+"\n\t\t\tOrganization: "+newOrg);
				}
			}
		}
	}
	
	public void addtoWrite(String s) {
		if (!toWrite.contains(s)) {
			toWrite.add(s);
		}
	}

	private void print() {
		PrintWriter out = null;
		try {
		    out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)));
		    for(String s : toWrite) {
		    	out.println(s);
		    }
		}catch (IOException e) {
		    System.err.println(e);
		}finally{
		    if(out != null){
		    	out.println("");
		        out.close();
		    }
		} 
	}
}
