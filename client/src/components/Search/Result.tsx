import { useState } from "react";
import "../../styles/result.css";
import {
  locationTopDescriptions,
  locationCoords,
} from "../data/mock";

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
      <h1 className="result">Here are study spots based on your filters:</h1>

      <Map
        mapboxAccessToken={MAPBOX_API_KEY}
        {...viewState}
        mapStyle={"mapbox://styles/mapbox/streets-v12"}
        onMove={(ev: ViewStateChangeEvent) => setViewState(ev.viewState)}
      >
        {Array.from(locationCoords.entries()).map(([key, coord], index) => (
          <Marker
            key={index}
            latitude={coord[0]}
            longitude={coord[1]}
            anchor="bottom"
          >
            <div
              onMouseEnter={() => handleMouseEnter(key)}
              onMouseLeave={handleMouseLeave}
              style={{ fontSize: 20 }}
            >
              📍
            </div>
          </Marker>
        ))}

        {hoveredLocation && (
          <Popup
            latitude={locationCoords.get(hoveredLocation)![0]}
            longitude={locationCoords.get(hoveredLocation)![1]}
            closeButton={false}
            anchor="bottom"
          >
            <div>
              <h3>{hoveredLocation}</h3>
              <ul>
                {locationTopDescriptions
                  .get(hoveredLocation)
                  ?.map((desc, index) => (
                    <li key={index}>{desc}</li>
                  ))}
              </ul>
            </div>
          </Popup>
        )}
      </Map>

      <button className="new-search-btn" onClick={setToFilterPage}>
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
