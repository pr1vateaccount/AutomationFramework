package org.example.utils;

import org.apache.commons.io.FileUtils;
import org.example.testbase.PageInitializer;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SeleniumUtility extends PageInitializer {
    /**
     * clears textbox and enter text into textbox
     * @param element textbox webelement
     * @param text String to pass into textbox
     */
    public static void sendText(WebElement element, String text){
        element.clear();
        element.sendKeys(text);
    }

    /**
     * checks if radio/checkbox is enabled & then clicks on the element that has value we want
     * @param listElement a list of webelements
     * @param value String
     */
    public static void clickRadioOrCheckBox(List<WebElement> listElement, String value){
        String actualValue;
        for (WebElement element: listElement){
            actualValue = element.getAttribute("value").trim();
            if (element.isEnabled() && actualValue.equals(value)){
                element.click();
                break;
            }
        }
    }

    /**
     * checks if the text is found in the dropdown element and only then it selects it
     * @param element
     * @param textToSelect
     */
    public static void selectDropdown(WebElement element, String textToSelect){
        try {
            Select select = new Select(element);
            List<WebElement> allOptions = select.getOptions();

            for (WebElement option : allOptions) {
                if (option.getText().equals(textToSelect)) {
                    select.selectByVisibleText(textToSelect);
                    break;
                }
            }
        } catch (UnexpectedTagNameException e){
            e.printStackTrace();
        }
    }

    public static void selectDropdown(WebElement element, int index){
        try {
            Select select = new Select(element);
            List<WebElement> allOptions = select.getOptions();
            int numOfOptions = allOptions.size();
            if (index < numOfOptions) {
                select.selectByIndex(index);
            }
        } catch (UnexpectedTagNameException e){
            e.printStackTrace();
        }
    }

    /**
     * accepts alert and catches exception if alert is not present
     */
    public static void acceptAlert(){
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException e){
            e.printStackTrace();
        }
    }

    public static void dismissAlert(){
        try{
            driver.switchTo().alert().dismiss();
        } catch (NoAlertPresentException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return alert text. If no alert is present, exception is handled and null is returned
     */
    public static String getAlertText(){
        try{
            return driver.switchTo().alert().getText();
        } catch (NoAlertPresentException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * send text to alert. NoAlertPresentException is handled
     * @param text String to send to Alert
     */
    public static void sendTextToAlert(String text){
        try{
            driver.switchTo().alert().sendKeys(text);
        } catch (NoAlertPresentException e){
            e.printStackTrace();
        }
    }

    public static void switchToFrame(String nameOrID){
        try{
           driver.switchTo().frame(nameOrID);
        } catch (NoSuchFrameException e){
            e.printStackTrace();
        }
    }

    public static void switchToFrame(int index){
        try{
            driver.switchTo().frame(index);
        } catch(NoSuchFrameException e){
            e.printStackTrace();
        }
    }

    public static void switchToFrame(WebElement element){
        try{
            driver.switchTo().frame(element);
        } catch(NoSuchFrameException e){
            e.printStackTrace();
        }
    }

    public static void switchToChildWindow(){
        String mainWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for(String window: allWindows){
            if (!window.equals(mainWindow)){
                driver.switchTo().window(window);
            }
        }
    }

    public static WebDriverWait getWaitObject(){
        WebDriverWait wait = new WebDriverWait(driver,Constants.EXPLICIT_WAIT_TIME);
        return wait;
    }

    public static WebElement waitForClickability(WebElement element){
        return getWaitObject().until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForVisibility(WebElement element){
        return getWaitObject().until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * click on element and wait implemented on it
     * @param element WebElement to be clicked
     */
    public static void click(WebElement element){
        waitForClickability(element);
        element.click();
    }

    public static void wait(int seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * casts drievr to JavaScriptExecutor and returns it
     * @return driver of type JavaScriptExecutor
     */
    public static JavascriptExecutor getJSObject(){
        return (JavascriptExecutor)driver;
    }

    public static void jsClick(WebElement element){
        getJSObject().executeScript("arguments[0].click()", element);
    }

    /**
     * scroll the page until element is visible on page
     * @param element WebElement
     */
    public static void scrollToElement(WebElement element){
        getJSObject().executeScript("arguments[0].scrollIntoView(true)", element);
    }

    public static void scrollDown(int pixel){
        getJSObject().executeScript("window.scrollBy(0, "+ pixel +" )");
    }

    public static void scrollUp(int pixel){
        getJSObject().executeScript("window.scrollBy(0, - "+ pixel+")");
    }

    public static void selectCalendarDate(List<WebElement> elements, String text){
        for (WebElement day: elements){
            if (day.isEnabled()){
                if(day.getText().equals(text)){
                    day.click();
                    break;
                }
            }
        }
    }

    public static byte[] takeScreenshot(String fileName){
        TakesScreenshot ts = (TakesScreenshot) driver;
        byte[] picBytes =ts.getScreenshotAs(OutputType.BYTES);

        File file = ts.getScreenshotAs(OutputType.FILE);
        String destination = Constants.SCREENSHOT_FILEPATH + fileName + getTimeStamp() + ".png";
        try{
            FileUtils.copyFile(file, new File(destination));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return picBytes;
    }

    public static String getTimeStamp(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(date.getTime());
    }
}
