package spellcheck;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class NorvigSpellingCorrector implements SpellingCorrector {

    private final Map<String, Integer> WORDS;

    public NorvigSpellingCorrector() throws IOException {
        String text = Files.readString(Paths.get("src/main/resources/big.txt")).toLowerCase();
        this.WORDS = Counter(words(text));
    }

    private List<String> words(String text) {
        List<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\w+").matcher(text);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    private Map<String, Integer> Counter(List<String> words) {
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        return map;
    }

    private double P(String word) {
        int N = WORDS.values().stream().mapToInt(i -> i).sum();
        return WORDS.getOrDefault(word, 0) / (double) N;
    }

    @Override
    public String correct(String word) {
        return Collections.max(candidates(word), Comparator.comparingDouble(this::P));
    }

    private Set<String> candidates(String word) {
        Set<String> known0 = known(Set.of(word));
        if (!known0.isEmpty()) return known0;

        Set<String> known1 = known(edits1(word));
        if (!known1.isEmpty()) return known1;

        Set<String> known2 = known(edits2(word));
        if (!known2.isEmpty()) return known2;

        return Set.of(word);
    }

    private Set<String> known(Iterable<String> words) {
        Set<String> result = new HashSet<>();
        for (String w : words) {
            if (WORDS.containsKey(w)) {
                result.add(w);
            }
        }
        return result;
    }

    private Set<String> edits1(String word) {
        Set<String> result = new HashSet<>();
        String letters = "abcdefghijklmnopqrstuvwxyz";
        List<String[]> splits = new ArrayList<>();

        for (int i = 0; i <= word.length(); i++) {
            splits.add(new String[]{word.substring(0, i), word.substring(i)});
        }

        for (String[] split : splits) {
            String L = split[0];
            String R = split[1];

            if (!R.isEmpty()) result.add(L + R.substring(1));
            if (R.length() > 1) result.add(L + R.charAt(1) + R.charAt(0) + R.substring(2));
            for (char c : letters.toCharArray()) {
                if (!R.isEmpty()) result.add(L + c + R.substring(1));
                result.add(L + c + R);
            }
        }

        return result;
    }

    private Set<String> edits2(String word) {
        Set<String> result = new HashSet<>();
        for (String e1 : edits1(word)) {
            result.addAll(edits1(e1));
        }
        return result;
    }
}
