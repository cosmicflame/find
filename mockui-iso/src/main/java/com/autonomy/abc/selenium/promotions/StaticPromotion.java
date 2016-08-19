package com.autonomy.abc.selenium.promotions;


import com.autonomy.abc.selenium.actions.wizard.BlankWizardStep;
import com.autonomy.abc.selenium.actions.wizard.Wizard;


public class StaticPromotion extends Promotion {
    private final String title;
    private final String content;

    public StaticPromotion(final String title, final String content, final String trigger) {
        super(trigger);
        this.title = title;
        this.content = content;
    }

    @Override
    public String getName() {
        return "static";
    }

    @Override
    public Wizard makeWizard(final CreateNewPromotionsBase newPromotionsBase) {
        return new StaticPromotionWizard((HsodCreateNewPromotionsPage) newPromotionsBase);
    }

    private class StaticPromotionWizard extends Promotion.PromotionWizard {
        public StaticPromotionWizard(final HsodCreateNewPromotionsPage page) {
            super(page);
            add(new NewDocumentDetailsStep(page, title, content));
            add(new BlankWizardStep("Language"));
            add(new SearchTriggerStep(page, getTrigger()));
        }
    }
}
