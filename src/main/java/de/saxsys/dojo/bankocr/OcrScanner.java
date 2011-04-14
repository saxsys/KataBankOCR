package de.saxsys.dojo.bankocr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OcrScanner {

	public List<String> read(File accountFile) {
		List<String> accountNumberList = new ArrayList<String>();
		List<String> digitsOfOneLine = null;
		AccountFileReader reader = new AccountFileReader(accountFile);
		while (!(digitsOfOneLine = reader.getDigitsOfOneLine()).isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String digit : digitsOfOneLine) {
				sb.append(Digit.value(digit).character());
			}
			evaluateAccountNumber(sb, accountNumberList);
			accountNumberList.add(sb.toString());
		}
		return accountNumberList;
	}

	private void evaluateAccountNumber(StringBuilder sb,
			List<String> accountNumberList) {
		if (sb.toString().contains("?")) {
			sb.append(" ILL");
		} else if (!AccountNumberValidator.isValid(sb.toString())) {
			sb.append(" ERR");
		}
	}
}
