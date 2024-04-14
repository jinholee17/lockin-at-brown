import Cookies from "js-cookie";

/**
 * To set the current uid
 * @param uid Functions made during gear up to handle cookies and logged in accounts
 */
export function addLoginCookie(uid: string): void {
  Cookies.set("uid", uid);
}

/**
 * To remove the uid
 */
export function removeLoginCookie(): void {
  Cookies.remove("uid");
}

/**
 * To get the current uid
 * @returns the current uid
 */
export function getLoginCookie(): string | undefined {
  return Cookies.get("uid");
}
