package com.autonomy.abc.find;

import com.autonomy.abc.base.FindTestBase;
import com.autonomy.abc.base.Role;
import com.autonomy.abc.selenium.find.CSVExportModal;
import com.autonomy.abc.selenium.find.FindPage;
import com.autonomy.abc.selenium.find.FindService;
import com.autonomy.abc.selenium.find.IdolFindPage;
import com.autonomy.abc.selenium.find.application.BIIdolFindElementFactory;
import com.autonomy.abc.selenium.find.application.UserRole;
import com.autonomy.abc.selenium.find.concepts.ConceptsPanel;
import com.autonomy.abc.selenium.find.filters.FilterPanel;
import com.autonomy.abc.selenium.find.filters.ParametricFieldContainer;
import com.autonomy.abc.selenium.find.results.FindResult;
import com.autonomy.abc.selenium.find.results.ResultsView;
import com.autonomy.abc.selenium.query.Query;
import com.hp.autonomy.frontend.selenium.config.TestConfig;
import com.hp.autonomy.frontend.selenium.framework.logging.ResolvedBug;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

import static com.hp.autonomy.frontend.selenium.framework.state.TestStateAssert.assertThat;
import static com.hp.autonomy.frontend.selenium.framework.state.TestStateAssert.verifyThat;
import static com.hp.autonomy.frontend.selenium.matchers.ElementMatchers.containsText;
import static com.hp.autonomy.frontend.selenium.matchers.ElementMatchers.hasTagName;
import static com.hp.autonomy.frontend.selenium.matchers.StringMatchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.openqa.selenium.lift.Matchers.displayed;

public class ResultsITCase extends FindTestBase {
    private FindPage findPage;
    private FindService findService;

    public ResultsITCase(final TestConfig config) {
        super(config);
    }

    @Before
    public void setUp() {
        findPage = getElementFactory().getFindPage();
        findService = getApplication().findService();
        if (!findPage.footerLogo().isDisplayed()) {
            ((IdolFindPage) findPage).goToListView();
        }
    }

    @Test
    @ResolvedBug("CSA-1665")
    public void testSearchTermInResults() {
        final String searchTerm = "tiger";

        findService.search(searchTerm);

        for (final WebElement searchElement : findPage.resultsContainingString(searchTerm)) {
            if (searchElement.isDisplayed()) {        //They can become hidden if they're too far in the summary
                verifyThat(searchElement.getText().toLowerCase(), containsString(searchTerm));
            }
            verifyThat(searchElement, not(hasTagName("a")));
        }
    }

    @Test
    @ResolvedBug("CSA-2082")
    public void testAutoScroll() {
        final ResultsView results = findService.search("nightmare");

        verifyThat(results.getResults().size(), lessThanOrEqualTo(30));

        findPage.scrollToBottom();
        verifyThat(results.getResults(), hasSize(allOf(greaterThanOrEqualTo(30), lessThanOrEqualTo(60))));

        findPage.scrollToBottom();
        verifyThat(results.getResults(), hasSize(allOf(greaterThanOrEqualTo(60), lessThanOrEqualTo(90))));

        final List<String> references = new ArrayList<>();

        for (final FindResult result : results.getResults()) {
            references.add(result.getReference());
        }

        final Set<String> referencesSet = new HashSet<>(references);

        /* References apparently may not be unique, but they're definitely ~more unique
                than titles within our data set  */
        verifyThat("No duplicate references", references, hasSize(referencesSet.size()));
    }

    @Test
    @ResolvedBug("CCUK-3647")
    public void testNoMoreResultsFoundAtEnd() {
        final ResultsView results = findService.search(new Query("Cheese AND Onion AND Carrot"));
        results.waitForResultsToLoad();

        verifyThat(findPage.totalResultsNum(), lessThanOrEqualTo(30));

        findPage.scrollToBottom();
        verifyThat(results.resultsDiv(), containsText("No more results found"));
    }

    @Test
    @ResolvedBug("FIND-93")
    public void testNoResults() {
        final ResultsView results = findService.search("thissearchwillalmostcertainlyreturnnoresults");

        new WebDriverWait(getDriver(), 60L).withMessage("No results message should appear").until(ExpectedConditions.textToBePresentInElement(results.resultsDiv(), "No results found"));

        findPage.scrollToBottom();

        final int occurrences = StringUtils.countMatches(results.resultsDiv().getText(), "results found");
        verifyThat("Only one message showing at the bottom of search results", occurrences, is(1));
    }

    @Test
    @ResolvedBug("FIND-350")
    @Role(UserRole.FIND)
    public void testDecliningAutoCorrectNotPermanent() {
        search("blarf");

        findPage.originalQuery().click();
        findPage.waitForParametricValuesToLoad();

        search("maney");
        verifyThat("Says it corrected query", findPage.originalQuery(), displayed());

        //TODO: Soon FindPage will have goToListView and this will be redundant
        if (findPage.listTabExists()) {
            ((IdolFindPage) findPage).goToListView();
        }

        verifyThat("There are results in list view", findPage.totalResultsNum(), greaterThan(0));
    }

    @Test
    @ResolvedBug("FIND-694")
    @Role(UserRole.FIND)
    public void testAutoCorrectedQueriesHaveRelatedConceptsAndParametrics() {
        final String term = "blarf";
        final String termAutoCorrected = "Blair";
        search(termAutoCorrected);

        LOGGER.info("Need to verify that " + termAutoCorrected + " has results, related concepts and parametrics");

        assertThat(termAutoCorrected + " has some results", findPage.totalResultsNum(), greaterThan(0));

        final int indexOfCategoryWFilters = getElementFactory().getFilterPanel().nonZeroParamFieldContainer(0);
        assertThat(termAutoCorrected + " has some parametric fields", indexOfCategoryWFilters, not(-1));
        assertThat(termAutoCorrected + " has related concepts", !getElementFactory().getRelatedConceptsPanel().noConceptsPresent());

        search(term);
        assertThat("Has autocorrected", findPage.hasAutoCorrected());
        assertThat("Has autocorrected" + term + " to " + termAutoCorrected, findPage.correctedQuery(), is("( " + termAutoCorrected + " )"));

        findPage.waitForParametricValuesToLoad();
        verifyThat("Still has parametric fields", getElementFactory().getFilterPanel().parametricField(indexOfCategoryWFilters).getFilterNumber(), not("0"));
        verifyThat("Still has related concepts", !getElementFactory().getRelatedConceptsPanel().noConceptsPresent());
    }

    @Test
    @Role(UserRole.FIND)
    public void testRefreshWithSlash() {
        final String query = "foo/bar";
        search(query);

        getDriver().navigate().refresh();

        // This could fail because %2F can be blocked by Tomcat
        assertThat(getElementFactory().getTopNavBar().getSearchBoxTerm(), is(query));
    }

    @Test
    @ResolvedBug("FIND-508")
    @Role(UserRole.BIFHI)
    public void testCanSelectParametricsThenExport() {
        final FilterPanel filters = getElementFactory().getFilterPanel();
        findPage.waitForParametricValuesToLoad();

        final int goodFilter = filters.nonZeroParamFieldContainer(0);
        final ParametricFieldContainer container = filters.parametricField(goodFilter);
        container.expand();
        container.getFilters().get(0).check();
        findPage.waitForParametricValuesToLoad();

        //TODO: part of the bad structure -> will be fixed w/ refactor of Roles vs App.
        ((BIIdolFindElementFactory)getElementFactory()).getSearchOptionsBar().exportResultsToCSV();

        final CSVExportModal modal = CSVExportModal.make(getDriver());
        assertThat("Modal has some contents", modal.fieldsToExport(), hasSize(greaterThan(0)));

        modal.close();
    }

    @Test
    @ResolvedBug("FIND-563")
    public void testQueryHighlightingForNonLatin() {
        search("*");

        final ConceptsPanel conceptsPanel = getElementFactory().getConceptsPanel();

        //Japanese: Human; Hebrew: Home; Thai: make; Russian: Russia; Arabic: white; Chinese: China
        final List<String> nonLatinQueries = Arrays.asList("人", "אדום", "ทำ", "Россия", "بيض", "中国");
        final String weightOfHighlightedTerm = "900";

        boolean foundResults = false;

        for (String query : nonLatinQueries) {
            if(!foundResults) {
                search(query);

                if (findPage.totalResultsNum() > 0) {
                    foundResults = true;
                    final WebElement incidenceOfTerm = findPage.resultsContainingString(query).get(0);
                    assertThat("Term \"" + query + "\" is highlighted (bold).",
                            incidenceOfTerm.getCssValue("font-weight"),
                            is(weightOfHighlightedTerm));
                }

                conceptsPanel.removeAllConcepts();
            }
        }
        assertThat("Found some results for the non-Latin queries", foundResults);
    }

    private void search(final String term) {
        findService.search(term);
        findPage.waitForParametricValuesToLoad();
    }

}
