package de.saxsys.dojo.bankocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountFileReader {

	private final static Pattern THREE_CHARS = Pattern.compile("[ |_]{3}");;

	private BufferedReader bufferedReader;

	public AccountFileReader(File accountFile) {
		try {
			bufferedReader = new BufferedReader(new FileReader(accountFile));
		} catch (FileNotFoundException e) {
			bufferedReader = null;
		}
	}

	public List<String> getDigitsOfOneLine() {

		if (null == bufferedReader) {
			return cleanUpAndReturnEmptyList();
		}

		String firstLine = getNextLine();
		if (null == firstLine || firstLine.isEmpty()) {
			return cleanUpAndReturnEmptyList();
		}

		List<String> threeCharactersPartsOfLineOne = getThreeCharacterParts(firstLine);
		List<String> threeCharactersPartsOfLineTwo = getThreeCharacterParts(getNextLine());
		List<String> threeCharactersPartsOfLineThree = getThreeCharacterParts(getNextLine());
		getNextLine();

		List<String> digitList = new ArrayList<String>();
		for (int i = 0; i < 9; i++) {
			digitList.add(threeCharactersPartsOfLineOne.get(i)
					+ threeCharactersPartsOfLineTwo.get(i)
					+ threeCharactersPartsOfLineThree.get(i));
		}
		return digitList;
	}

	private List<String> cleanUpAndReturnEmptyList() {
		if (null != bufferedReader)
			try {
				bufferedReader.close();
			} catch (IOException e) {
			}
		return Collections.emptyList();
	}

	private List<String> getThreeCharacterParts(String nextLine) {
		final List<String> threeCharParts = new ArrayList<String>();
		final Matcher m = THREE_CHARS.matcher(nextLine);
		while (m.find()) {
			threeCharParts.add(m.group());
		}
		return threeCharParts;
	}

	private String getNextLine() {
		try {
			return (null != bufferedReader) ? bufferedReader.readLine() : "";
		} catch (IOException e) {
			return "";
		}
	}
}
