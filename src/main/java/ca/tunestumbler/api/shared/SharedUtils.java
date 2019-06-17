package ca.tunestumbler.api.shared;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class SharedUtils {
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	public String generateStateId(int length) {
		return generateRandomString(length);
	}

	public String generateUserId(int length) {
		return generateRandomString(length);
	}

	public String generateSubredditId(int length) {
		return generateRandomString(length);
	}

	public String generateMultiredditId(int length) {
		return generateRandomString(length);
	}

	public String generateSubredditAggregateId(int length) {
		return generateRandomString(length);
	}

	public String generateFiltersId(int length) {
		return generateRandomString(length);
	}

	public String generateResultsId(int length) {
		return generateRandomString(length);
	}

	public String generateResultsPaginationId(int length) {
		return generateRandomString(length);
	}

	private String generateRandomString(int length) {
		StringBuilder randomString = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			randomString.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return new String(randomString);
	}

}
