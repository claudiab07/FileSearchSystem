package searchLayer;

import spellcheck.SpellingCorrector;

public class CorrectingSearchService implements SearchService {

    private final SearchService delegate;
    private final SpellingCorrector corrector;

    public CorrectingSearchService(SearchService delegate, SpellingCorrector corrector) {
        this.delegate = delegate;
        this.corrector = corrector;
    }

    @Override
    public String getSearchResults(String query) {
        String corrected = corrector.correct(query);
        if (!corrected.equalsIgnoreCase(query)) {
            return "Did you mean \"" + corrected + "\"?\n\n" + delegate.getSearchResults(corrected);
        }
        return delegate.getSearchResults(query);
    }

    public SearchService getDelegate() {
        return delegate;
    }
}
