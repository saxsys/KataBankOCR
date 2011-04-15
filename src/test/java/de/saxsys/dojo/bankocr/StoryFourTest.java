package de.saxsys.dojo.bankocr;

import static de.saxsys.dojo.bankocr.TestUtils.createDummyFileFor;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import org.junit.Test;

/**
 * User Story 4
 * <p>
 * It turns out that often when a number comes back as ERR or ILL it is because
 * the scanner has failed to pick up on one pipe or underscore for one of the
 * figures. For example
 * </p>
 * 
 * <pre>
 *  _  _  _  _  _  _     _ 
 * |_||_|| || ||_   |  |  ||_ 
 *   | _||_||_||_|  |  |  | _|
 * </pre>
 * <p>
 * The 9 could be an 8 if the scanner had missed one |. Or the 0 could be an 8.
 * Or the 1 could be a 7. The 5 could be a 9 or 6. So your next task is to look
 * at numbers that have come back as ERR or ILL, and try to guess what they
 * should be, by adding or removing just one pipe or underscore. If there is
 * only one possible number with a valid checksum, then use that. If there are
 * several options, the status should be AMB. If you still can't work out what
 * it should be, the status should be reported ILL.
 * </p>
 * 
 * @author Sebastian Schmeck
 */
public class StoryFourTest {

	@Test
	public void ifNumberIsInvalidButOnlyTheFirstSignMissesACharacterToAValidNumberTakeTheValidOne()
			throws Exception {
		String str = "" + //
				"                           \n" + //
				"  |  |  |  |  |  |  |  |  |\n" + //
				"  |  |  |  |  |  |  |  |  |\n";
		OcrScanner scanner = new OcrScanner();
		assertThat(scanner.read(createDummyFileFor(str)), //
				contains("711111111"));
	}

	@Test
	public void ifNumberIsInvalidButOnlyTheSeventhSignMissesACharacterToAValidNumberTakeTheValidOne()
			throws Exception {
		String str = "" + //
				" _  _  _  _  _  _  _  _  _ \n" + //
				"  |  |  |  |  |  |  |  |  |\n" + //
				"  |  |  |  |  |  |  |  |  |\n";
		OcrScanner scanner = new OcrScanner();
		assertThat(scanner.read(createDummyFileFor(str)), //
				contains("777777177"));
	}

	@Test
	public void ifNumberIsInvalidButOnlyTheFourthSignMissesACharacterToAValidNumberTakeTheValidOne()
			throws Exception {
		String str = "" + //
				" _  _  _  _  _  _  _  _  _ \n" + //
				" _|| || || || || || || || |\n" + //
				"|_ |_||_||_||_||_||_||_||_|\n";
		OcrScanner scanner = new OcrScanner();
		assertThat(scanner.read(createDummyFileFor(str)),
				hasItem(startsWith("200800000")));
	}

}
