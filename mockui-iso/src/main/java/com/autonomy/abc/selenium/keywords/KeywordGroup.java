package com.autonomy.abc.selenium.keywords;

import com.autonomy.abc.selenium.actions.wizard.OptionWizardStep;
import com.autonomy.abc.selenium.actions.wizard.Wizard;
import com.autonomy.abc.selenium.actions.wizard.WizardStep;
import com.autonomy.abc.selenium.language.Language;
import com.hp.autonomy.frontend.selenium.util.Waits;
import org.apache.commons.lang3.StringUtils;

public class KeywordGroup {
    private final String keywordString;
    private final KeywordWizardType type;
    private final Language language;

    public KeywordGroup(final KeywordWizardType type, final Language language, final Iterable<String> keywords) {
        this.keywordString = StringUtils.join(keywords, " ");
        this.type = type;
        this.language = language;
    }

    public Wizard makeWizard(final CreateNewKeywordsPage newKeywordsPage) {
        return new KeywordWizard(newKeywordsPage);
    }

    private class KeywordWizard extends Wizard {
        private final CreateNewKeywordsPage page;

        private KeywordWizard(final CreateNewKeywordsPage newKeywordsPage) {
            page = newKeywordsPage;
            buildSteps();
        }

        private void buildSteps() {
            this.add(new TypeStep(page));
            this.add(new InputStep(page));
        }

        @Override
        public void next() {
            if (onFinalStep()) {
                page.finishWizardButton().click();
            } else {
                page.continueWizardButton().click();
                incrementStep();
            }
            Waits.loadOrFadeWait();
        }

        @Override
        public void cancel() {
            page.cancelWizardButton().click();
        }
    }

    private class TypeStep extends OptionWizardStep {
        private final CreateNewKeywordsPage page;

        public TypeStep(final CreateNewKeywordsPage container) {
            super(container, "Select Type of Keywords", type.getOption());
            this.page = container;
        }

        @Override
        public Object apply() {
            super.apply();
            page.selectLanguage(language);
            return null;
        }
    }

    private class InputStep implements WizardStep {
        private final CreateNewKeywordsPage page;

        public InputStep(final CreateNewKeywordsPage container) {
            this.page = container;
        }

        @Override
        public String getTitle() {
            return type.getInputTitle();
        }

        @Override
        public Object apply() {
            page.getTriggerForm().addTrigger(keywordString);
            return null;
        }
    }

}
