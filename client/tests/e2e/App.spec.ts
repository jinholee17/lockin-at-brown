import { expect, test } from "@playwright/test";
import { clearUser } from "../../src/utils/api";

/**
  The general shapes of tests in Playwright Test are:
    1. Navigate to a URL
    2. Interact with the page
    3. Assert something about the page against your expectations
  Look for this pattern in the tests below!
 */

const SPOOF_UID = "mock-user-id";


test.beforeEach(async ({ page }) => {
  await page.goto("http://localhost:8000/");

});

/**
 * Don't worry about the "async" yet. We'll cover it in more detail
 * for the next sprint. For now, just think about "await" as something
 * you put before parts of your test that might take time to run,
 * like any interaction with the page.
 */
test("on page load, I see the filter screen and skip auth.", async ({
  page,
}) => {
  // Notice: http, not https! Our front-end is not set up for HTTPs.
  // await page.goto("http://localhost:8000/");
  
  await page.getByLabel("input-field").click();
  await page.getByLabel("input field").fill("Quiet");
  await page.keyboard.press("Enter");
  await page.getByLabel("input field").click();
  await page.getByLabel("input field").fill("Aesthetic");
  await page.keyboard.press("Enter");
  await page.getByLabel("Lock in search button").click();
  await expect(page.getByLabel("map with search results")).toBeVisible();
});

// test("I can add a word to my favorites list", async ({ page }) => {
//   await page.goto("http://localhost:8000/");
//   // - get the <p> elements inside the <ul> with aria-label="favorite-words"
//   const favoriteWords = await page.getByLabel("favorite-words");
//   await expect(favoriteWords).not.toContainText("hello");

//   await page.getByLabel("word-input").fill("hello");
//   await page.getByLabel("add-word-button").click();

//   const favoriteWordsAfter = await page.getByLabel("favorite-words");
//   await expect(favoriteWordsAfter).toContainText("hello");

//   // .. and this works on refresh
//   await page.reload();
//   const favoriteWordsAfterReload = await page.getByLabel("favorite-words");
//   await expect(favoriteWordsAfterReload).toContainText("hello");
// });
