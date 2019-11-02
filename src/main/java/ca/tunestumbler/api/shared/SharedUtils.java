package ca.tunestumbler.api.shared;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class SharedUtils {
	private final Random RANDOM = new SecureRandom();
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public String getCurrentTime() {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter dateFormatObject = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		return date.format(dateFormatObject);
	}

	public Long setStartId(Long userMaxId, Long maxId) {
		Long startId = 1L;
		if (userMaxId != null) {
			startId = userMaxId + 1;
		} else {
			if (maxId != null) {
				startId = maxId + 1;
			}
		}

		return startId;
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

	public String generateAggregateId(int length) {
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
