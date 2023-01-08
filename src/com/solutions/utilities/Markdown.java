package com.solutions.utilities;

public class Markdown {
	public static String ANSIPrefix = "\u001b[";

	public static int NORMAL = 0, BOLD = 1, UNDERLINE = 4, GRAY = 30, RED = 31, GREEN = 32, YELLOW = 33, BLUE = 34,
			PINK = 35, CYAN = 36, WHITE = 37, FIREFLY_DARK_BLUE_BACKGROUND = 40, ORANGE_BACKGROUND = 41,
			MARBLE_BLUE_BACKGROUND = 42, GRAYISH_TURQUOISE_BACKGROUND = 43, GRAY_BACKGROUND = 44,
			INDIGO_BACKGROUND = 45, LIGHT_GRAY_BACKGROUND = 46, WHITE_BACKGROUND = 47;

	public static String ANSI(String string, int... modifiers) {
		String formatting = "";
		for (int i = 0; i < modifiers.length; i++) {
			if (i != modifiers.length - 1) {
				formatting += modifiers[i] + ";";
			} else {
				formatting += modifiers[i] + "m";
			}
		}

		return ANSIPrefix + formatting + string + ANSIPrefix + NORMAL + "m";
	}
}
