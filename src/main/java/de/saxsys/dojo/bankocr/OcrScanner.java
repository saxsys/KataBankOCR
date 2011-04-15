package de.saxsys.dojo.bankocr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OcrScanner {

	public List<String> read(File accountFile) {
		List<String> accountNumberList = new ArrayList<String>();
		List<ScannedSign> signsOfOneLine = null;
		AccountFileReader reader = new AccountFileReader(accountFile);
		while (!(signsOfOneLine = reader.getDigitsOfOneLine()).isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ScannedSign sign : signsOfOneLine) {
				sb.append(AccountDigit.value(sign).character());
			}
			accountNumberList.add( //
					getEvaluatedAccountNumberResult( //
							sb, signsOfOneLine));
		}
		return accountNumberList;
	}

	private String getEvaluatedAccountNumberResult(StringBuilder sb,
			List<ScannedSign> signsOfOneLine) {
		if (sb.toString().contains("?")) {
			sb.append(" ILL");
		} else if (!AccountNumberValidator.isValid(sb.toString())) {
			if (!findAValidFirstNumber(signsOfOneLine, sb)) {
				sb.append(" ERR");
			}
		}
		return sb.toString();
	}

	private boolean findAValidFirstNumber(List<ScannedSign> signsOfOneLine,
			StringBuilder sb) {
		ScannedSign sign = signsOfOneLine.get(0);
		if (AccountDigit.value(sign) == AccountDigit.ONE) {
			sb.replace(0, 1, "7");
			return true;
		}
		return false;
	}
}
