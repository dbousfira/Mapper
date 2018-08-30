import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		String file = "./log.txt";
		FileInputStream in = null, regex = null;
        BufferedReader readerRegex = null, readerLog = null;

		try {
			in = new FileInputStream(file);
			regex = new FileInputStream("./regex.txt");
            readerRegex = new BufferedReader(new InputStreamReader(regex));
            readerLog = new BufferedReader(new InputStreamReader(in));
			
	        String regexStr = readerRegex.readLine();
			Set<String> namedGroups = getNamedGroup(regexStr);
			
			Pattern pattern = Pattern.compile(regexStr);
			
			String log;
	        BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt")),
	        		writerUnmatch = new BufferedWriter(new FileWriter("./unmatch.txt"));
			while ((log = readerLog.readLine()) != null) {
				Matcher matcher = pattern.matcher(log);
				if (matcher.find())
					printMatches(matcher, namedGroups, writer);	
				else {
					writerUnmatch.write(log);
				}							
			}
			
	        writer.close();
	        writerUnmatch.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();					
				}
			}
		}

	}
	
	private static Set<String> getNamedGroup(String regex) throws Exception {
        Set<String> namedGroups = new TreeSet<String>();
        Matcher m = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>").matcher(regex);

        while (m.find()) {
        	namedGroups.add(m.group(1));
        }
        
        if (!namedGroups.contains("LABEL")) {
        	throw new Exception("L'expression régulière doit contenir une étiquette LABEL !");
        }
        	
        return namedGroups;
	}
	
	private static void printMatches(Matcher matcher, Set<String> namedGroups, BufferedWriter writer) throws IOException {
        StringBuilder out = new StringBuilder(), matches = new StringBuilder();
        
        out.append(matcher.group("LABEL"));
        out.append('(');
        
        for (String name: namedGroups) {
        	if (name.equals("LABEL")) continue;
        	
            String matchedString = matcher.group(name);
            if (matchedString != null) {
            	matches.append(name + "=" + matchedString + ", ");
            }
        }
        
        out.append(matches.toString().replaceAll(", $", ""));
        out.append(')');
        
        writer.write(out.toString());
        writer.write('\n');
    }
}

