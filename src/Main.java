import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		String file = "./log.txt",
				regex = "<ip> [<dtime>] \"GET HTTP/1.1 /<url>";
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			in = new FileInputStream(file);
			out = new FileOutputStream("./output.txt");

			int c, i = 0;
			c = in.read();
			while (c != -1) {
				if (c == '\n') {
					i = 0;
					out.write('\n');
				}
				
				// Détection du label
				if (regex.charAt(i) == '<') {
					if (i != 0) {
						out.write(',');
						out.write(' ');
					}
					i++;
					
					// Tant que fin de label non détecté
					while (regex.charAt(i) != '>') {
						out.write(regex.charAt(i++));
					}
					
					out.write('=');
				}

				// Elimination
				if (regex.length() != i + 1 && (char)c == regex.charAt(i+1)) {
					while ((char)c == regex.charAt(i+1)) {
						c = in.read();
						i++;
					}
					i++;
				}
				// Récupération de la donnée associée au label
				else {
					if (c == '\n')
						c = in.read();

					out.write(c);
					c = in.read();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
