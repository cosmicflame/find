/*
 * Copyright 2015 Hewlett-Packard Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */
package com.autonomy.abc.selenium.find.application;

import com.autonomy.abc.selenium.find.IdolFindPage;
import com.autonomy.abc.selenium.find.bi.MapView;
import com.autonomy.abc.selenium.find.bi.SunburstView;
import com.autonomy.abc.selenium.find.bi.TableView;
import com.autonomy.abc.selenium.find.bi.TopicMapView;
import com.autonomy.abc.selenium.find.comparison.ResultsComparisonView;
import com.autonomy.abc.selenium.find.save.SearchOptionsBar;
import com.autonomy.abc.selenium.find.save.SearchTabBar;
import com.hp.autonomy.frontend.selenium.element.FormInput;
import org.openqa.selenium.WebDriver;

public class BIIdolFindElementFactory extends IdolFindElementFactory {

    BIIdolFindElementFactory(final WebDriver driver) {
        super(driver);
    }

    @Override
    public IdolFindPage getFindPage() {
        return new IdolFindPage.Factory().create(getDriver());
    }

    @Override
    public FormInput getSearchBox() {
        return getConceptsPanel().getConceptBoxInput();
    }

    public SearchTabBar getSearchTabBar() {
        return new SearchTabBar(getDriver());
    }

    public SearchOptionsBar getSearchOptionsBar() {
        return new SearchOptionsBar(getDriver());
    }

    public SunburstView getSunburst() {
        return new SunburstView(getDriver());
    }

    public TopicMapView getTopicMap() {
        return new TopicMapView(getDriver());
    }

    public TableView getTableView() {
        return new TableView(getDriver());
    }

    public ResultsComparisonView getResultsComparison() {
        return new ResultsComparisonView(getDriver());
    }

    public MapView getMap() {
        return new MapView(getDriver());
    }
}
