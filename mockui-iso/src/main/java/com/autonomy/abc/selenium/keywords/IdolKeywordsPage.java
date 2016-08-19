 package com.autonomy.abc.selenium.keywords;

 import com.autonomy.abc.selenium.application.SOPageBase;
 import com.autonomy.abc.selenium.language.IdolLanguageDropdown;
 import com.autonomy.abc.selenium.language.LanguageDropdown;
 import org.openqa.selenium.By;
 import org.openqa.selenium.WebDriver;

public class IdolKeywordsPage extends KeywordsPage {
    private IdolKeywordsPage(final WebDriver driver) {
        super(driver);
    }

    @Override
    protected LanguageDropdown languageDropdown() {
        return new IdolLanguageDropdown(findElement(By.cssSelector(".languages-select-view-container .dropdown:nth-of-type(2)")), getDriver());
    }

    public static class Factory extends SOPageBase.SOPageFactory<IdolKeywordsPage> {
        public Factory() {
            super(IdolKeywordsPage.class);
        }

        @Override
        public IdolKeywordsPage create(final WebDriver context) {
            return new IdolKeywordsPage(context);
        }
    }
}
