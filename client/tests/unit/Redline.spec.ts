import { expect, test } from "vitest";
import { clearUser } from "../../src/utils/api";
import { overlayDataFilter } from "../../src/utils/overlayFilter";
import { overlayData } from "../../src/utils/overlay";

test("when i search for an exisisting keword, a new layer data should return", async () => {
  await overlayDataFilter("lower class").then((data) => {
    expect(data?.features.length).toBe(104);
  });

  await overlayDataFilter("school").then((data) => {
    expect(data?.features.length).toBe(3151);
  });
});

test("when i search for a non-existing keword, a new layer data with nothing should return", async () => {
  await overlayDataFilter("CS32").then((data) => {
    expect(data?.features.length).toBe(0);
  });

  await overlayDataFilter("").then((data) => {
    expect(data).toBe(undefined);
  });

  await overlayDataFilter("32 CS").then((data) => {
    expect(data?.features.length).toBe(0);
  });
});

test("when i fetch for the entire data, we should have all the points", async () => {
  await overlayData().then((data) => {
    expect(data?.features.length).toBe(8878);
  });
});

test("alternate fetching between redline data and filter data", async () => {
  await overlayData().then((data) => {
    expect(data?.features.length).toBe(8878);
  });

  await overlayData().then((data) => {
    expect(data?.features.length).toBe(8878);
  });

  await overlayDataFilter("CS320").then((data) => {
    expect(data?.features.length).toBe(0);
  });

  await overlayDataFilter("school").then((data) => {
    expect(data?.features.length).toBe(3151);
  });

  await overlayDataFilter("CS32000").then((data) => {
    expect(data?.features.length).toBe(0);
  });

  await overlayDataFilter("lower class").then((data) => {
    expect(data?.features.length).toBe(104);
  });

  await overlayDataFilter("School").then((data) => {
    expect(data?.features.length).toBe(944);
  });

  await overlayDataFilter("").then((data) => {
    expect(data?.features.length).toBe(undefined);
  });

  await overlayData().then((data) => {
    expect(data?.features.length).toBe(8878);
  });
});
