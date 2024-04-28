import { getLoginCookie } from "./cookie";

const HOST = "http://localhost:3232";
/**
 * Function to query the API endpoint.
 * @param endpoint The API endpoint.
 * @param query_params Query parameters to be added to the URL.
 * @returns JSON response from the API.
 */
async function queryAPI(
  endpoint: string,
  query_params: Record<string, string>
) {
  // query_params is a dictionary of key-value pairs that gets added to the URL as query parameters
  // e.g. { foo: "bar", hell: "o" } becomes "?foo=bar&hell=o"
  const paramsString = new URLSearchParams(query_params).toString();
  const url = `${HOST}/${endpoint}?${paramsString}`;
  const response = await fetch(url);
  if (!response.ok) {
    console.error(response.status, response.statusText);
  }
  return response.json();
}
/**
 * Adds a word to the favorite word database
 * @param word
 * @returns Promise for result
 */
export async function addWord(word: string) {
  return await queryAPI("add-word", {
    uid: getLoginCookie() || "",
    word: word,
  });
}

/**
 * Adds a word to the favorite word database
 * @param word
 * @returns Promise for result
 */
export async function deleteWord(word: string) {
  return await queryAPI("delete-word", {
    uid: getLoginCookie() || "",
    word: word,
  });
}

/**
 * retrieves the list of favorite words in the database
 * @returns Promise for result of words
 */
export async function getWords() {
  return await queryAPI("list-words", {
    uid: getLoginCookie() || "",
  });
}
/**
 *
 * @param uid Clears the users logged in
 * @returns Promise for result
 */
export async function clearUser(uid: string = getLoginCookie() || "") {
  return await queryAPI("clear-user", {
    uid: uid,
  });
}
/**
 * Adds pins to the the databse
 * @param pin
 * @returns Promise for result
 */
export async function addPin(pin: string) {
  return await queryAPI("add-pin", {
    uid: getLoginCookie() || "",
    pin: pin,
  });
}
/**
 * Gets the list of pins from the database
 * @returns Promise for result of pins
 */
export async function getPins() {
  return await queryAPI("list-pins", {
    uid: getLoginCookie() || "",
  });
}
/**
 * Clears the list of pins with in the database
 * @returns Promise for result
 */
export async function clearPins() {
  return await queryAPI("clear-pins", {
    uid: getLoginCookie() || "",
  });
}
