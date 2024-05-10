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

test.beforeEach(
  "add spoof uid cookie to browser",
  async ({ context, page }) => {
    // - Add "uid" cookie to the browser context
    await context.addCookies([
      {
        name: "uid",
        value: SPOOF_UID,
        url: "http://localhost:8000",
      },
    ]);

    // wipe everything for this spoofed UID in the database.
    await clearUser(SPOOF_UID);
  }
);

/**
 * Don't worry about the "async" yet. We'll cover it in more detail
 * for the next sprint. For now, just think about "await" as something
 * you put before parts of your test that might take time to run,
 * like any interaction with the page.
 */
// test("on page load, I see the filter screen and skip auth.", async ({
//   page,
// }) => {
//   // Notice: http, not https! Our front-end is not set up for HTTPs.
//   await page.goto("http://localhost:8000/");
//   await expect(page.getByLabel("lockin @ brown title")).toBeVisible();
//   // <i> with aria-label favorite-words-header should include the SPOOF_UID
//   await expect(page.getByLabel("Lock in search button")).toContainText(
//     "Enter filters in the input space below and use the add button to add the filter:"
//   );
// });

test("Click around & delete filters on frontend test", async ({ page }) => {
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("Light traffic");
  await page.getByLabel("add button", { exact: true }).click();
  await expect(page.locator("#root")).toContainText("Light traffic ❌");
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("Heavy Traffic");
  await page.getByLabel("add button", { exact: true }).click();
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("Has Whiteboard or TV");
  await page.getByLabel("add button", { exact: true }).click();
  await expect(page.getByText("Light traffic ❌Heavy Traffic")).toBeVisible();
  await expect(page.locator("#root")).toContainText(
    "Light traffic ❌Heavy Traffic ❌Has Whiteboard or TV ❌"
  );
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("4-8 people");
  await page.getByLabel("add button", { exact: true }).click();
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("8+ people");
  await page.getByLabel("add button", { exact: true }).click();
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("4-8 people");
  await page.getByLabel("add button", { exact: true }).click();
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("4-8 people");
  await page.getByLabel("add button", { exact: true }).click();
  await expect(page.getByText("Light traffic ❌Heavy Traffic")).toBeVisible();
  await expect(page.locator("#root")).toContainText(
    "Light traffic ❌Heavy Traffic ❌Has Whiteboard or TV ❌4-8 people ❌8+ people ❌"
  );
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("Accessible");
  await page.getByLabel("add button", { exact: true }).click();
  await expect(page.locator("#root")).toContainText(
    "Light traffic ❌Heavy Traffic ❌Has Whiteboard or TV ❌4-8 people ❌8+ people ❌Accessible ❌"
  );
  await page.getByRole("button", { name: "Light traffic ❌" }).click();
  await page.getByRole("button", { name: "+ people ❌" }).click();
  await expect(page.locator("#root")).toContainText(
    "Heavy Traffic ❌Has Whiteboard or TV ❌4-8 people ❌Accessible ❌"
  );
  await page.getByRole("button", { name: "Heavy Traffic ❌" }).click();
  await expect(page.locator("#root")).toContainText(
    "Has Whiteboard or TV ❌4-8 people ❌Accessible ❌"
  );
  await page.getByRole("button", { name: "Accessible ❌" }).click();
  await page.getByRole("button", { name: "Has Whiteboard or TV ❌" }).click();
  await page.getByRole("button", { name: "-8 people ❌" }).click();
});

test("test when i enter a filter that's not there a error message will show up", async ({
  page,
}) => {
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("ghrihgi");
  await page.getByLabel("add button", { exact: true }).click();
  await expect(page.getByText("Filter ghrihgi Not Found!")).toBeVisible();
});

test("When I click refresh, the filters will still be there", async ({
  page,
}) => {
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("Heavy Traffic");
  await page.getByLabel("add button", { exact: true }).click();
  await page.getByLabel("add button", { exact: true }).press("Meta+r");
  await page.goto("http://localhost:8000/");
  await expect(
    page.getByRole("button", { name: "Heavy Traffic ❌" })
  ).toBeVisible();
});

test("When I click new search, the filters will still be there", async ({
  page,
}) => {
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("2-4 people");
  await page.getByLabel("add button", { exact: true }).click();
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("8+ people");
  await page.getByLabel("add button", { exact: true }).click();
  await page.getByLabel("Lock in search button").click();
  await page.getByLabel("new search button").click();
  await expect(
    page.getByRole("button", { name: "-4 people ❌" })
  ).toBeVisible();
  await page.getByRole("button", { name: "+ people ❌" }).click();
  await expect(
    page.getByRole("button", { name: "-4 people ❌" })
  ).toBeVisible();
  await page.getByRole("button", { name: "Heavy Traffic ❌" }).click();
});

test("When I click search, it will take me to the loading page, and then it would take me to the result page", async ({
  page,
}) => {
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("Aesthetic");
  await page.getByLabel("add button", { exact: true }).click();
  await expect(page.locator("#root")).toContainText("Aesthetic ❌");
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("Aesthetic");
  await page.getByLabel("add button", { exact: true }).click();
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("4-8 people");
  await page.getByLabel("add button", { exact: true }).click();
  await page.getByPlaceholder("Quiet").click();
  await page.getByPlaceholder("Quiet").fill("Conversational");
  await page.getByLabel("add button", { exact: true }).click();
  await expect(
    page.getByRole("button", { name: "-8 people ❌" })
  ).toBeVisible();
  await page.getByRole("button", { name: "Conversational ❌" }).click();
  await expect(page.locator("#root")).toContainText(
    "Aesthetic ❌4-8 people ❌"
  );
  await page.getByLabel("Lock in search button").click();
  await expect(page.getByRole("heading", { name: "Searching" })).toBeVisible();
  await expect(page.getByLabel("Map", { exact: true })).toBeVisible();
  await expect(page.getByLabel("new search button")).toBeVisible();
});

test("When I search with no filters, it will get the default outputs", async ({
  page,
}) => {
  await page.getByLabel("Lock in search button").click();
  await expect(page.getByLabel("Map", { exact: true })).toBeVisible();
  await page.getByLabel("new search button").click();
});
