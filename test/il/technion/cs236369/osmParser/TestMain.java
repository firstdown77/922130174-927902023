package il.technion.cs236369.osmParser;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestMain {

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		JUnitCore junit = new JUnitCore();
		Result result = junit.run(OSMParserSuit.class);
		System.out.println("Run total " + result.getRunCount() + " test");
	}
}
