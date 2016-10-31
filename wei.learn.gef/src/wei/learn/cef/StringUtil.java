package wei.learn.cef;



import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class StringUtil {
	public static final String EMPTY = "";
	public static final String EMAIL_REGEX = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	public static final String LINE_SPLITTER_REGEX = "\r?\n|\r";
	public static final String REGEX_NEWLINE_GROUP = format("({0})", "\r?\n|\r");
	public static final Pattern LINE_SPLITTER = Pattern.compile("\r?\n|\r");
	private static final Pattern HTML_TAG_PATTERN = Pattern
			.compile("\\<.*?\\>");

	public static boolean areEqualIgnoreCase(String s1, String s2) {
		return s1 == null ? s2 == null : (s2 != null ? s1.equalsIgnoreCase(s2)
				: false);
	}

	public static int characterInstanceCount(String source, char c) {
		int result = -1;
		if (source != null) {
			int length = source.length();
			result = 0;

			for (int i = 0; i < length; ++i) {
				if (source.charAt(i) == c) {
					++result;
				}
			}
		}

		return result;
	}

	public static int compare(String s1, String s2) {
		s1 = getStringValue(s1);
		s2 = getStringValue(s2);
		return s1.compareTo(s2);
	}

	public static int compareCaseInsensitive(String s1, String s2) {
		s1 = getStringValue(s1);
		s2 = getStringValue(s2);
		return s1.compareToIgnoreCase(s2);
	}

	public static String concat(Collection<String> items) {
		return items != null ? join((String) null,
				(String[]) ((String[]) items.toArray(new String[items.size()])))
				: null;
	}

	public static String concat(String... items) {
		return join((String) null, (String[]) items);
	}

	public static boolean contains(String[] set, String toFind) {
		if (set != null && toFind != null) {
			String[] arg4 = set;
			int arg3 = set.length;

			for (int arg2 = 0; arg2 < arg3; ++arg2) {
				String value = arg4[arg2];
				if (value.equals(toFind)) {
					return true;
				}
			}
		}

		return false;
	}

	public static String ellipsify(String message) {
		return message == null ? null : message + "...";
	}

	public static int findNextWhitespaceOffset(String string, int offset) {
		int i = -1;
		if (string != null && !"".equals(string) && offset <= string.length()) {
			for (int j = offset; j < string.length(); ++j) {
				char ch = string.charAt(j);
				if (Character.isWhitespace(ch)) {
					i = j;
					break;
				}
			}

			return i;
		} else {
			return i;
		}
	}

	public static int findPreviousWhitespaceOffset(String string, int offset) {
		int i = -1;
		if (string != null && !"".equals(string) && offset <= string.length()) {
			for (int j = offset; j > 0; --j) {
				char ch = string.charAt(j - 1);
				if (Character.isWhitespace(ch)) {
					i = j - 1;
					break;
				}
			}

			return i;
		} else {
			return i;
		}
	}

	public static String format(String str, int replacement) {
		return MessageFormat.format(str,
				new Object[] { Integer.toString(replacement) });
	}

	public static String format(String str, long replacement) {
		return MessageFormat.format(str,
				new Object[] { Long.toString(replacement) });
	}

	public static String format(String str, Object replacement) {
		return MessageFormat.format(str,
				new Object[] { replacement.toString() });
	}

	public static String format(String str, Object[] replacements) {
		return MessageFormat.format(str, replacements);
	}

	public static String format(String str, String replacement) {
		return MessageFormat.format(str, new Object[] { replacement });
	}

	public static String getStringValue(Object object) {
		return object != null ? object.toString() : "";
	}

	public static boolean isEmpty(String text) {
		return text == null || text.trim().length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str != null && (strLen = str.length()) != 0) {
			for (int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(str.charAt(i))) {
					return false;
				}
			}

			return true;
		} else {
			return true;
		}
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static String join(String delimiter, Collection<String> items) {
		return items != null ? join(delimiter,
				(String[]) items.toArray(new String[items.size()])) : null;
	}

	public static String join(String delimiter, Object... items) {
		String[] s = new String[items.length];

		for (int i = 0; i < items.length; ++i) {
			Object item = items[i];
			if (item == null) {
				s[i] = "null";
			} else {
				s[i] = item.toString();
			}
		}

		return join(delimiter, s);
	}

	public static String join(String delimiter, char... items) {
		String[] strings = new String[items.length];

		for (int i = 0; i < items.length; ++i) {
			strings[i] = new String(items, i, 1);
		}

		return join(delimiter, strings);
	}

	public static String join(String delimiter, String... items) {
		String result = null;
		if (items != null) {
			switch (items.length) {
			case 0:
				result = "";
				break;
			case 1:
				result = items[0];
				break;
			default:
				int lastIndex = items.length - 1;
				int delimiterLength = delimiter != null ? delimiter.length()
						: 0;
				int targetLength = lastIndex * delimiterLength;

				int offset;
				for (offset = 0; offset <= lastIndex; ++offset) {
					targetLength += items[offset].length();
				}

				offset = 0;
				char[] accumulator = new char[targetLength];
				int i;
				String item;
				int length;
				if (delimiterLength == 0) {
					for (i = 0; i <= lastIndex; ++i) {
						item = items[i];
						length = item.length();
						item.getChars(0, length, accumulator, offset);
						offset += length;
					}
				} else {
					for (i = 0; i < lastIndex; ++i) {
						item = items[i];
						length = item.length();
						item.getChars(0, length, accumulator, offset);
						offset += length;
						delimiter.getChars(0, delimiterLength, accumulator,
								offset);
						offset += delimiterLength;
					}

					String arg10 = items[lastIndex];
					arg10.getChars(0, arg10.length(), accumulator, offset);
				}

				result = new String(accumulator);
			}
		}

		return result;
	}

	public static String makeFormLabel(String message) {
		return message == null ? null : message + ":";
	}

	public static String pad(String string, int desiredLength, char padChar) {
		if (string == null) {
			string = "";
		}

		int diff = desiredLength - string.length();
		if (diff > 0) {
			string = repeat(padChar, diff) + string;
		}

		return string;
	}

	public static String quote(String string) {
		return string == null ? null : '\'' + string + '\'';
	}

	public static String replace(String str, String pattern, String replace) {
		if (str == null) {
			return null;
		} else {
			int s = 0;
			boolean e = false;

			StringBuilder result;
			int e1;
			for (result = new StringBuilder(); (e1 = str.indexOf(pattern, s)) >= 0; s = e1
					+ pattern.length()) {
				result.append(str.substring(s, e1));
				result.append(replace);
			}

			result.append(str.substring(s));
			return result.toString();
		}
	}

	public static String replaceAll(String template,
			Map<String, String> variables) {
		if (template != null && variables != null && !variables.isEmpty()) {
			Entry entry;
			String value;
			for (Iterator arg2 = variables.entrySet().iterator(); arg2
					.hasNext(); template = template.replaceAll(
					(String) entry.getKey(), value).replace('', '$')) {
				entry = (Entry) arg2.next();
				value = (String) entry.getValue();
				if (value == null) {
					value = "";
				} else {
					value = value.replace('$', '');
				}
			}

			return template;
		} else {
			return template;
		}
	}

	public static boolean startsWith(String string, char c) {
		return !isEmpty(string) && string.charAt(0) == c;
	}

	public static boolean startsWith(String string, String prefix) {
		return !isEmpty(string) && string.startsWith(prefix);
	}

	public static String stripUTF8BOM(String input)
			throws UnsupportedEncodingException {
		if (!isEmpty(input) && input.length() >= 2) {
			byte[] b = input.substring(0, 2).getBytes("UTF-8");
			if (Integer.toHexString(b[0] & 255).equals("ef")
					&& Integer.toHexString(b[1] & 255).equals("bb")
					&& Integer.toHexString(b[2] & 255).equals("bf")) {
				input = input.substring(1, input.length());
			}

			return input;
		} else {
			return input;
		}
	}

	public static String stripHTMLTags(String textWithHTML) {
		return !isEmpty(textWithHTML) ? HTML_TAG_PATTERN.matcher(textWithHTML)
				.replaceAll("") : textWithHTML;
	}

	public static List<String> tokenize(String inputString, String delim) {
		ArrayList tokens = new ArrayList();
		if (inputString == null) {
			return tokens;
		} else {
			StringTokenizer tokenizer = new StringTokenizer(inputString, delim);

			while (tokenizer.hasMoreTokens()) {
				tokens.add(tokenizer.nextToken());
			}

			return tokens;
		}
	}

	public static String truncate(String text, int length) {
		return text != null && text.length() > length ? new String(
				ellipsify(text.substring(0, length))) : text;
	}

	public static String repeat(char c, int times) {
		char[] buf = new char[times];

		for (int i = 0; i < times; ++i) {
			buf[i] = c;
		}

		return new String(buf);
	}

	public static List<String> split(String string, char toSplit) {
		ArrayList ret = new ArrayList();
		int len = string.length();
		int last = 0;
		char c = 0;

		for (int i = 0; i < len; ++i) {
			c = string.charAt(i);
			if (c == toSplit) {
				if (last != i) {
					ret.add(string.substring(last, i));
				}

				while (c == toSplit && i < len - 1) {
					++i;
					c = string.charAt(i);
				}

				last = i;
			}
		}

		if (c != toSplit) {
			if (last == 0 && len > 0) {
				ret.add(string);
			} else if (last < len) {
				ret.add(string.substring(last, len));
			}
		}

		ret.trimToSize();
		return ret;
	}

	public static List<String> dotSplit(String string) {
		return split(string, '.');
	}

	public static String avoidNull(String str) {
		return isEmpty(str) ? "" : str;
	}

	public static String dotFirst(String string) {
		int i = string.indexOf(46);
		return i != -1 ? string.substring(0, i) : string;
	}

	public static String stripQuotes(String text) {
		return text.length() < 2 || text.charAt(0) != 39
				&& text.charAt(0) != 34 ? text : text.substring(1,
				text.length() - 1);
	}

	public static String parseValue(byte[] buf) {
		if (buf == null) {
			return null;
		} else {
			String ret = new String(buf);

			try {
				ret = new String(buf, "gbk");
			} catch (UnsupportedEncodingException arg1) {
				;
			}

			return ret.charAt(ret.length() - 1) == 0 ? ret.substring(0,
					ret.length() - 1) : ret;
		}
	}

	public static boolean isEmail(String s) {
		return s == null ? false
				: s.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	}

	public static boolean isInt(String s) {
		if (s == null) {
			return false;
		} else {
			try {
				Integer.parseInt(s);
				return true;
			} catch (Exception arg0) {
				return false;
			}
		}
	}

	public static boolean isBoolean(String s) {
		return s == null ? false : "true".equals(s.toLowerCase())
				|| "false".equals(s.toLowerCase());
	}

	public static boolean isUrl(String s) {
		if (s == null) {
			return false;
		} else {
			try {
				new URL(s);
				return true;
			} catch (Exception arg0) {
				return false;
			}
		}
	}

	public static boolean isIdentifier(String name) {
		if (isEmpty(name)) {
			return false;
		} else if (!Character.isJavaIdentifierStart(name.charAt(0))) {
			return false;
		} else {
			for (int i = 1; i < name.length(); ++i) {
				if (!Character.isJavaIdentifierPart(name.charAt(i))) {
					return false;
				}
			}

			return true;
		}
	}

	public static String toBolder(String content) {
		return "<span style=\'font-size:14px;\'>" + content + "</span>";
	}

	public static String abbreviate(String str, int maxWidth) {
		return abbreviate(str, 0, maxWidth);
	}

	public static String abbreviate(String str, int offset, int maxWidth) {
		if (str == null) {
			return null;
		} else if (maxWidth < 4) {
			throw new IllegalArgumentException(
					"Minimum abbreviation width is 4");
		} else if (str.length() <= maxWidth) {
			return str;
		} else {
			if (offset > str.length()) {
				offset = str.length();
			}

			if (str.length() - offset < maxWidth - 3) {
				offset = str.length() - (maxWidth - 3);
			}

			if (offset <= 4) {
				return str.substring(0, maxWidth - 3) + "...";
			} else if (maxWidth < 7) {
				throw new IllegalArgumentException(
						"Minimum abbreviation width with offset is 7");
			} else {
				return offset + (maxWidth - 3) < str.length() ? "..."
						+ abbreviate(str.substring(offset), maxWidth - 3)
						: "..." + str.substring(str.length() - (maxWidth - 3));
			}
		}
	}

	public static String trimToEmpty(String str) {
		return str == null ? "" : str.trim();
	}
}