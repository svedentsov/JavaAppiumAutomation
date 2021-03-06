package lib.ui;

import lib.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

abstract public class ArticlePageObject extends MainPageObject {
    protected static String
            TITLE,
            FOOTER_ELEMENT,
            OPTION_BUTTON,
            OPTIONS_ADD_TO_MY_LIST_BUTTON,
            OPTIONS_REMOVE_FROM_MY_LIST_BUTTON,
            ADD_TO_MY_LIST_OVERlAY,
            MY_LIST_NAME_INPUT,
            MY_LIST_OK_BUTTON,
            CLOSE_ARTICLE_BUTTON;

    public ArticlePageObject(RemoteWebDriver driver) {
        super(driver);
    }

    public WebElement waitForTitleElement() {
        return this.waitForElementPresent(TITLE, "Can't find article title on page", 15);
    }

    public String getArticleTitle() {
        WebElement title_element = waitForTitleElement();
        if (Platform.getInstance().isAndroid()) {
            return title_element.getAttribute("text");
        } else if (Platform.getInstance().isIOS()) {
            return title_element.getAttribute("name");
        } else {
            return title_element.getText();
        }
    }

    /**
     * Свайп от начала статья до конца, пока не появится заданный элемент.
     */
    public void swipeToFooter() {
        if (Platform.getInstance().isAndroid()) {
            this.swipeUpFindElement(FOOTER_ELEMENT, "Cannot find the end of the article", 40);
        } else if (Platform.getInstance().isIOS()) {
            this.swipeUpTillElementAppear(FOOTER_ELEMENT, "Cannot find the end of the article", 40);
        } else {
            this.scrollWebPageTillElementNotVisible(FOOTER_ELEMENT, "Cannot find the end of the article", 40);
        }
    }

    /**
     * Добавление статьи для Android.
     *
     * @param name_of_folder название папки
     */
    public void addArticleToMyList(String name_of_folder) {
        this.waitForElementAndClick(OPTION_BUTTON, "Cannot find button to open article options", 5);
        this.waitForElementAndClick(OPTIONS_ADD_TO_MY_LIST_BUTTON, "Cannot find option to add article to reading list", 5);
        this.waitForElementAndClick(ADD_TO_MY_LIST_OVERlAY, "Cannot find 'Got it' tip overlay", 5);
        this.waitForElementAndClear(MY_LIST_NAME_INPUT, "Cannot find input to set name of article folder", 5);
        this.waitForElementAndSendKeys(MY_LIST_NAME_INPUT, name_of_folder, "Cannot put text into article folder input", 5);
        this.waitForElementAndClick(MY_LIST_OK_BUTTON, "Cannot press 'OK' button", 5);
    }

    /**
     * Добавление статьи для Веб.
     */
    public void addArticleToMySaved() {
        if (Platform.getInstance().isMW()) {
            this.removeArticleFromSavedIfItAdded();
        }
        this.waitForElementAndClear(OPTIONS_ADD_TO_MY_LIST_BUTTON, "Cannot find option to add article to reading list", 5);
    }

    /**
     * Удаление статьи, если она уже была добавлена ранее.
     */
    public void removeArticleFromSavedIfItAdded() {
        if (this.isElementPresent(OPTIONS_REMOVE_FROM_MY_LIST_BUTTON)) {
            this.waitForElementAndClick(OPTIONS_REMOVE_FROM_MY_LIST_BUTTON, "Cannot click button to remove an article from saved", 2);
            this.waitForElementPresent(OPTIONS_REMOVE_FROM_MY_LIST_BUTTON, "Cannot find button to add an article to saved list after removing it from this list before", 2);
        }
    }

    /**
     * Закрыть статью.
     */
    public void closeArticle() {
        if (Platform.getInstance().isIOS() || Platform.getInstance().isAndroid()) {
            this.waitForElementAndClick(CLOSE_ARTICLE_BUTTON, "Cannot close article, cannot find X link", 5);
        } else {
            System.out.println("Method closeArticle() do nothing for platform:" + Platform.getInstance().getPlatformVar());
        }
    }
}
