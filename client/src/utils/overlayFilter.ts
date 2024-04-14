import { FeatureCollection, GeoJsonProperties, Geometry } from "geojson";
import { FillLayer } from "react-map-gl";

let rl_data: FeatureCollection<Geometry, GeoJsonProperties> | string = "";
/**
 * Function that gets the users area description and fetches locations with the description
 * @param area_description the area description to search for
 * @returns the fetched filtered data
 */
async function fetchLoad(area_description: string) {
  try {
    const data = await fetch(
      "http://localhost:3232/search-area?area-desc=" + area_description
    );
    const json = await data.json();
    if (json["success"]) {
      console.log(json["success"]);
      rl_data = await json["success"];
      return rl_data;
    } else {
      return json["error"];
    }
  } catch (err) {
    return "Failed to fetch string promise for red line data loading";
  }
}
/**
 * Fills the area given by the fetch call
 */
export const geoLayerFilter: FillLayer = {
  id: "geo_overlay_filter",
  type: "fill",
  paint: {
    "fill-color": "#6A0DAD",
    "fill-opacity": 1.0,
  },
};

/**
 * To check if the correct data format was fetched
 * @param json the json object to check
 * @returns true or false
 */
function isFeatureCollection(json: any): json is FeatureCollection {
  return json.type === "FeatureCollection";
}
/**
 * Adds the overlay of the received locations
 * @param area_description
 * @returns the fetched data or undefined if it's not a FeatureCollection type
 */
export async function overlayDataFilter(
  area_description: string
): Promise<GeoJSON.FeatureCollection | undefined> {
  const data = await fetchLoad(area_description);
  if (data !== undefined) {
    return isFeatureCollection(data) ? data : undefined;
  } else return undefined;
}
