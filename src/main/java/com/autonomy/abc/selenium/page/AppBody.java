package com.autonomy.abc.selenium.page;

import com.autonomy.abc.selenium.AppElement;
import com.autonomy.abc.selenium.menubar.NotificationsDropDown;
import com.autonomy.abc.selenium.menubar.SideNavBar;
import com.autonomy.abc.selenium.menubar.TopNavBar;
import com.autonomy.abc.selenium.util.AbstractWebElementPlaceholder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AppBody extends AppElement {

	private final AbstractWebElementPlaceholder<OverviewPage> overviewPage;
	private final AbstractWebElementPlaceholder<PromotionsPage> promotionsPage;
	private final AbstractWebElementPlaceholder<KeywordsPage> keywordsPage;
	private final AbstractWebElementPlaceholder<AboutPage> aboutPage;
	private final AbstractWebElementPlaceholder<UsersPage> usersPage;
	private final AbstractWebElementPlaceholder<SettingsPage> settingsPage;
	private final SearchPage.Placeholder searchPage;
	private final LoginPage.Placeholder loginPage;
	private final CreateNewPromotionsPage.Placeholder createPromotionsPage;
	private final CreateNewDynamicPromotionsPage.Placeholder dynamicPromotionsPage;
	private final SchedulePage.Placeholder schedulePage;
	private final CreateNewKeywordsPage.Placeholder createKeywordsPage;
	private final TopNavBar topNavBar;
	private final SideNavBar sideNavBar;
	private final NotificationsDropDown.Placeholder notifications;
	private final EditDocumentReferencesPage.Placeholder editReferences;

	public AppBody(final WebDriver driver) {
		this(driver, new SideNavBar(driver), new TopNavBar(driver));
	}

	public AppBody(final WebDriver driver, final SideNavBar navBar, final TopNavBar topNavBar) {
		super(driver.findElement(By.cssSelector("body")), driver);

		this.overviewPage = new OverviewPage.Placeholder(this, navBar, topNavBar);
		this.promotionsPage = new PromotionsPage.Placeholder(this, navBar, topNavBar);
		this.keywordsPage = new KeywordsPage.Placeholder(this, navBar, topNavBar);
		this.aboutPage = new AboutPage.Placeholder(this, navBar, topNavBar);
		this.usersPage = new UsersPage.Placeholder(this, navBar, topNavBar);
		this.settingsPage = new SettingsPage.Placeholder(this, navBar, topNavBar);

		this.searchPage = new SearchPage.Placeholder(topNavBar);
		this.loginPage = new LoginPage.Placeholder(topNavBar);
		this.createPromotionsPage = new CreateNewPromotionsPage.Placeholder(topNavBar);
		this.dynamicPromotionsPage = new CreateNewDynamicPromotionsPage.Placeholder(topNavBar);
		this.schedulePage = new SchedulePage.Placeholder(topNavBar);
		this.createKeywordsPage = new CreateNewKeywordsPage.Placeholder(topNavBar);
		this.topNavBar = new TopNavBar(driver);
		this.sideNavBar = new SideNavBar(driver);
		this.notifications = new NotificationsDropDown.Placeholder(topNavBar);
		this.editReferences = new EditDocumentReferencesPage.Placeholder(topNavBar);
	}

	public OverviewPage getOverviewPage() {
		return overviewPage.$page();
	}

	public PromotionsPage getPromotionsPage() {
		return promotionsPage.$page();
	}

	public KeywordsPage getKeywordsPage() {
		return keywordsPage.$page();
	}

	public AboutPage getAboutPage() {
		return aboutPage.$topNavBarDropDownPage();
	}

	public UsersPage getUsersPage() {
		return usersPage.$topNavBarDropDownPage();
	}

	public SettingsPage getSettingsPage() {
		return settingsPage.$topNavBarDropDownPage();
	}

	public SearchPage getSearchPage() {
		return searchPage.$searchPage(getParent(getParent(getDriver().findElement(By.cssSelector(".search-page-controls")))));
	}

	public CreateNewPromotionsPage getCreateNewPromotionsPage() {
		return createPromotionsPage.$createNewPromotionsPage(getDriver().findElement(By.cssSelector(".page-content-pages")));
	}

	public CreateNewDynamicPromotionsPage getCreateNewDynamicPromotionsPage() {
		return dynamicPromotionsPage.$createNewDynamicPromotionsPage(getDriver().findElement(By.cssSelector(".page-content-pages")));
	}

	public SchedulePage getSchedulePage() {
		return schedulePage.$schedulePage(getDriver().findElement(By.xpath(".//h2[contains(text(), 'Schedule')]/../../..")).findElement(By.cssSelector(".pd-wizard")));
	}

	public CreateNewKeywordsPage getCreateKeywordsPage() {
		return createKeywordsPage.$createNewKeywordsPage(getDriver().findElement(By.cssSelector(".page-content-pages")));
	}

	public LoginPage getLoginPage() {
		return loginPage.$loginPage(getDriver().findElement(By.cssSelector(".loginscreen")));
	}

	public TopNavBar getTopNavBar() {
		return topNavBar;
	}

	public SideNavBar getSideNavBar() {
		return sideNavBar;
	}

	public NotificationsDropDown getNotifications() {
		return notifications.$notificationsDropDown(findElement(By.cssSelector(".notification-list")));
	}

	public EditDocumentReferencesPage getEditDocumentReferencesPage() {
		return editReferences.$editReferences(findElement(By.xpath(".//*[contains(@class, 'edit-document-references')]/../..")));
	}

	public void logout() {
		topNavBar.findElement(By.cssSelector(".fa-cog")).click();
		topNavBar.findElement(By.xpath(".//span[text()='Logout']")).click();
	}
}
