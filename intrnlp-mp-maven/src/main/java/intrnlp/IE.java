package intrnlp;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.simple.*;

public class IE {
	private CSVReader reader;

	public void Run() throws IOException {
		reader = new CSVReader(new FileReader("iso.csv"));
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			System.out.println("Standard Control: "+nextLine[4]); //header is 5th column
			extract(nextLine[5]); //content is in 6th column
		}
	}

	private void extract(String string) {
		Document doc = new Document(string);

		for (Sentence sent : doc.sentences()) {
			// Iterate over the triples in the sentence
			for (RelationTriple triple : sent.openieTriples()) {
				// Print the triple
				System.out.println("\t Action: " + triple.relationGloss() + triple.objectGloss());
			}
		}
	}
}
