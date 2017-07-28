package intrnlp;
import java.io.IOException;

public class Driver {
	public static void main(String[] args) {
		IE ie = new IE();
		try {
			ie.Run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
