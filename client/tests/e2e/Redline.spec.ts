// import { expect, test } from "@playwright/test";
// import { TIMEOUT } from "dns";
// import { setTimeout } from "timers/promises";
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

// test("when i search for an exisisting keword, a new layer should show up", async ({
//   page,
// }) => {
//   await page.getByRole("button", { name: "Section 2: Mapbox Demo" }).click();
//   await page.getByPlaceholder("Enter area description search").click();
//   await page
//     .getByPlaceholder("Enter area description search")
//     .fill("lower class");
//   await page.getByRole("button", { name: "Submit" }).click();
//   await expect(page.getByLabel("Map", { exact: true })).toBeVisible();
//   await expect(page.getByLabel("filter-overlay")).toBeVisible();

//   await page.getByRole("button", { name: "Clear Search" }).click();
//   await expect(page.getByLabel("filter-overlay")).toBeHidden();

//   // search for something that isn't there -- layer should still be there
//   await page.getByPlaceholder("Enter area description search").fill("CS32");
//   await page.getByRole("button", { name: "Submit" }).click();
//   await expect(page.getByLabel("filter-overlay")).toBeVisible();

//   await page.getByPlaceholder("Enter area description search").fill("");
//   await page.getByRole("button", { name: "Submit" }).click();
//   await expect(page.getByLabel("filter-overlay")).toBeVisible();

//   await page.getByRole("button", { name: "Clear Search" }).click();
//   await expect(page.getByLabel("filter-overlay")).toBeHidden();

//   await page.getByRole("button", { name: "Clear Search" }).click();
//   await expect(page.getByLabel("filter-overlay")).toBeHidden();

//   await page
//     .getByPlaceholder("Enter area description search")
//     .fill("lower class");
//   await page.getByRole("button", { name: "Submit" }).click();
//   await expect(page.getByLabel("filter-overlay")).toBeVisible();

//   // on page reload, the overlay shoud dissapear
//   await page.reload();
//   await page.getByRole("button", { name: "Section 2: Mapbox Demo" }).click();
//   await expect(page.getByLabel("filter-overlay")).toBeHidden();

//   await page
//     .getByPlaceholder("Enter area description search")
//     .fill("lower class");
//   await page.getByRole("button", { name: "Submit" }).click();
//   await expect(page.getByLabel("filter-overlay")).toBeVisible();

//   await page.getByPlaceholder("Enter area description search").fill("CS32");
//   await page.getByRole("button", { name: "Submit" }).click();
//   await expect(page.getByLabel("filter-overlay")).toBeVisible();
// });
