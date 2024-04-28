import { useState } from "react";
import "../../styles/result.css";
// import { locationTopDescriptions, locationCoords } from "../data/mock";

import "mapbox-gl/dist/mapbox-gl.css";
import Map, {
  Layer,
  MapLayerMouseEvent,
  Source,
  ViewStateChangeEvent,
  Marker,
  Popup,
} from "react-map-gl";

/**
 * Ensure that Mapbox API key is there
 */
const MAPBOX_API_KEY = process.env.MAPBOX_TOKEN;
if (!MAPBOX_API_KEY) {
  console.error("Mapbox API key not found. Please add it to your .env file.");
}

export interface LatLong {
  lat: number;
  long: number;
}

interface pageProps {
  setCurrPage: React.Dispatch<React.SetStateAction<String>>;
  locationTopDescriptions: Map<string, string[]>;
  locationCoords: Map<string, number[]>;
}

const ProvidenceLatLong: LatLong = {
  lat: 41.8268,
  long: -71.4025,
};
const initialZoom = 15;

/**
 * Main function that handles all the map box logic
 * It deals with storing pins and highlighting various data
 *
 * @returns the mapbox render
 */
export default function Result(props: pageProps) {
  // zoom and move around for map
  const [viewState, setViewState] = useState({
    latitude: ProvidenceLatLong.lat,
    longitude: ProvidenceLatLong.long,
    zoom: initialZoom,
  });

  console.log(props.locationCoords);
  console.log(props.locationTopDescriptions);
  function setToFilterPage() {
    props.setCurrPage("filter");
  }

  const [hoveredLocation, setHoveredLocation] = useState<string | null>(null);

  const handleMouseEnter = (location: string) => {
    setHoveredLocation(location);
  };

  const handleMouseLeave = () => {
    setHoveredLocation(null);
  };

  return (
    <div className="map">
      <h1 className="result" tabIndex={0}>
        Here are study spots based on your filters:
      </h1>

      <Map
        mapboxAccessToken={MAPBOX_API_KEY}
        {...viewState}
        mapStyle={"mapbox://styles/mapbox/streets-v12"}
        onMove={(ev: ViewStateChangeEvent) => setViewState(ev.viewState)}
        aria-lable="map with search results"
        aria-description="a map with search results as pins"
      >
        {Array.from(props.locationCoords.entries()).map(
          ([key, coord], index) => {
            console.log(props.locationTopDescriptions);
            console.log(props.locationCoords);
            console.log("omg");
            return (
              <Marker
                key={index}
                latitude={coord[1]}
                longitude={coord[0]}
                anchor="bottom"
              >
                <div
                  tabIndex={0}
                  onMouseEnter={() => handleMouseEnter(key)}
                  onMouseLeave={handleMouseLeave}
                  style={{ fontSize: 24 }}
                  aria-lable="a pin"
                  aria-description="hover over the pin for the pop up on the description of this location"
                >
                  📍
                </div>
              </Marker>
            );
          }
        )}

        {hoveredLocation && (
          <Popup
            latitude={props.locationCoords.get(hoveredLocation)![1]}
            longitude={props.locationCoords.get(hoveredLocation)![0]}
            closeButton={false}
            anchor="bottom"
          >
            <div className="Popup">
              <h3>{hoveredLocation}</h3>
              <ul>
                {props.locationTopDescriptions
                  .get(hoveredLocation)
                  ?.map((desc, index) => (
                    <li aria-label={desc} tabIndex={0} key={index}>
                      {desc}
                    </li>
                  ))}
              </ul>
            </div>
          </Popup>
        )}
      </Map>

      <button
        aria-label="new search button"
        aria-description="click or use enter to begin a new search"
        className="new-search-btn"
        onClick={setToFilterPage}
      >
        New Search 🔍
      </button>
    </div>
  );
}

// {
//   pins.map((pin, index) => {
//     // Split the pin string into latitude and longitude
//     const [lat, long] = pin.split(" ");
//     const latitude = parseFloat(lat.split(":")[1]);
//     const longitude = parseFloat(long.split(":")[1]);
//     return (
//       <Marker key={index} latitude={latitude} longitude={longitude}>
//         {/* Marker content */}
//         <div style={{ fontSize: 20 }}>📍</div>
//       </Marker>
//     );
//   });
// }
