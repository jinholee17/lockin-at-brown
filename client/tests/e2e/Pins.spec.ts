// import { expect, test } from "@playwright/test";
// import { TIMEOUT } from "dns";
// import { clearUser } from "../../src/utils/api";

// const SPOOF_UID = "mock-user-id";

// test.beforeEach(
//   "add spoof uid cookie to browser",
//   async ({ context, page }) => {
//     // - Add "uid" cookie to the browser context
//     await context.addCookies([
//       {
//         name: "uid",
//         value: SPOOF_UID,
//         url: "http://localhost:8000",
//       },
//     ]);

//     // wipe everything for this spoofed UID in the database.
//     await clearUser(SPOOF_UID);
//     await page.goto("http://localhost:8000/");
//   }
// );

// test("when i click on the map, a pin should show up", async ({ page }) => {
//   await page.getByRole("button", { name: "Section 2: Mapbox Demo" }).click();
//   await page.getByLabel("Map", { exact: true }).click();
//   await expect(page.getByText("📍").nth(0)).toBeVisible();
//   await expect(page.locator("#root")).toContainText("📍");

//   // when i click another time it should have a second pin
//   await page.getByLabel("Map marker").click();
//   await expect(page.getByText("📍").nth(1)).toBeVisible();

//   // when i click clear pins, everything should clear
//   await page.getByRole("button", { name: "Clear Pins" }).click();
//   await expect(page.getByText("📍").nth(0)).toBeHidden();
//   await expect(page.getByText("📍").nth(1)).toBeHidden();
//   await expect(page.getByText("📍").nth(2)).toBeHidden();

//   // if i click again, only one pin will appear
//   await page.getByLabel("Map", { exact: true }).click();
//   await expect(page.getByText("📍").nth(0)).toBeVisible();

//   await page.reload();
//   await page.getByRole("button", { name: "Section 2: Mapbox Demo" }).click();
//   await expect(page.getByText("📍").nth(0)).toBeVisible();
//   await expect(page.locator("#root")).toContainText("📍");

//   // when i click another time it should have a second pin
//   await page.getByLabel("Map marker").click();
//   await expect(page.getByText("📍").nth(1)).toBeVisible();

//   // await page.getByPlaceholder("Enter area description search").fill("CS32");
//   // await page.getByRole("button", { name: "Submit" }).click();
//   // await expect(page.getByLabel("filter-overlay")).toBeVisible();

//   // await page.getByPlaceholder("Enter area description search").fill("");
//   // await page.getByRole("button", { name: "Submit" }).click();
//   // await expect(page.getByLabel("filter-overlay")).toBeVisible();

//   // when i click clear pins twice, everything should clear
//   await page.getByRole("button", { name: "Clear Pins" }).click();
//   await expect(page.getByText("📍").nth(0)).toBeHidden();
//   await expect(page.getByText("📍").nth(1)).toBeHidden();

//   await page.getByRole("button", { name: "Clear Pins" }).click();
//   await expect(page.getByText("📍").nth(0)).toBeHidden();
//   await expect(page.getByText("📍").nth(1)).toBeHidden();

//   await page.reload();
//   await page.getByRole("button", { name: "Section 2: Mapbox Demo" }).click();
//   await expect(page.getByText("📍").nth(0)).toBeHidden();
//   await expect(page.getByText("📍").nth(1)).toBeHidden();
//   await expect(page.getByLabel("filter-overlay")).toBeHidden();
//   // when i click another time it should have a second pin
//   await page.getByLabel("Map", { exact: true }).click();
//   await expect(page.getByText("📍").nth(0)).toBeVisible();
//   await page.getByLabel("Map marker").click();
//   await expect(page.getByText("📍").nth(1)).toBeVisible();
//   // await page.getByLabel("Map marker").click();
//   // await expect(page.getByText("📍").nth(2)).toBeVisible();
//   // await page.getByPlaceholder("Enter area description search").fill("family");
//   // await page.getByRole("button", { name: "Submit" }).click();
//   // await expect(page.getByLabel("filter-overlay")).toBeVisible();

//   await page.reload();
//   await page.reload();
//   await page.getByRole("button", { name: "Section 2: Mapbox Demo" }).click();
//   await expect(page.getByText("📍").nth(0)).toBeVisible();
//   await expect(page.getByText("📍").nth(1)).toBeVisible();
//   //await expect(page.getByText("📍").nth(2)).toBeVisible();
// });
