package at.backend.MarketingCompany.shared.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResult {
		private final List<String> errors = new ArrayList<>();
		private final List<String> warnings = new ArrayList<>();

		public void addError(String error) {
			errors.add(error);
		}

		public void addWarning(String warning) {
			warnings.add(warning);
		}

		public boolean isValid() {
			return errors.isEmpty();
		}

		public boolean hasWarnings() {
			return !warnings.isEmpty();
		}

		public List<String> getErrors() {
			return Collections.unmodifiableList(errors);
		}

		public List<String> getWarnings() {
			return Collections.unmodifiableList(warnings);
		}

		public String getErrorsAsString() {
			return String.join("; ", errors);
		}

		public String getWarningsAsString() {
			return String.join("; ", warnings);
		}

	}