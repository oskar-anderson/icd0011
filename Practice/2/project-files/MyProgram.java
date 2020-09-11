package demo;

import java.util.Optional;

public class MyProgram {

	public static void main(String[] args) throws Exception {

		String fileName = "conf.txt";

		Optional<String> optional = Util.getContent(fileName);

		if (optional.isPresent()) {
			System.out.println(optional.get());
		} else {
			System.err.println("Can't find file: " + fileName);
		}
		
	}

}
