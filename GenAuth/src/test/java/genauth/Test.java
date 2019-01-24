package genauth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
//		String test = "<tr ng-repeat=\"page in pages track by $index\" class=\"ng-scope\">";
//		String res = test.replaceAll("ng-repeat=\"[^\"]*\"", "");
//		System.out.println(res);
		
		//Class<Date> c = Date.class;
		//System.out.println(c.cast("2017-11-12"));
		
		String test = "to_char(tttt,'yyyy-MM-dd HH:mm:ss')";
		String pattern = "[\\'\\\"](?<dateformat>([yMdhHmsS]|[^a-z])*)[\\'\\\"]";
		Pattern p = Pattern.compile(pattern);
		System.out.println(Pattern.matches(pattern, test));
		Matcher matcher = p.matcher(test);
		System.out.println(matcher.find());
		System.out.println(matcher.group("dateformat"));
		System.out.println("1  "+matcher.group(1));
		System.out.println("2  "+matcher.group(2));
		System.out.println("3  "+matcher.group(3));
	}

}
