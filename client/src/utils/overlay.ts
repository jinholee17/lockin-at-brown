import { FeatureCollection, GeoJsonProperties, Geometry } from "geojson";
import { FillLayer } from "react-map-gl";

let rl_data: FeatureCollection<Geometry, GeoJsonProperties> | string = "";

/**
 * Fetch the entire red-line dataset and use this data for overlay
 * @returns Adds red line data
 */
async function fetchLoad() {
  try {
    const data = await fetch("http://localhost:3232/red-line/*");
    const json = await data.json();
    if (json["success"]) {
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
 * The overlay style
 */
const propertyName = "holc_grade";
export const geoLayer: FillLayer = {
  id: "geo_data",
  type: "fill",
  paint: {
    "fill-color": [
      "match",
      ["get", propertyName],
      "A",
      "#5bcc04",
      "B",
      "#04b8cc",
      "C",
      "#e9ed0e",
      "D",
      "#d11d1d",
      "#ccc",
    ],
    "fill-opacity": 0.3,
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
 *
 * @returns Adds overlay of red line data
 * @returns the fetched data or undefined if it's not a FeatureCollection type
 */
export async function overlayData(): Promise<
  GeoJSON.FeatureCollection | undefined
> {
  const data = await fetchLoad();
  console.log(data);
  return isFeatureCollection(data) ? data : undefined;
}
