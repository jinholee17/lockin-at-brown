import { useState } from "react";
import "../../styles/result.css";
import star from "../../images/star.png";
import nostar from "../../images/nostar.png";
import halfstar from "../../images/halfstar.png";
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
import mapboxgl, { FillExtrusionLayer } from "mapbox-gl";

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
const initialZoom = 16;

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
    pitch: 50,
    bearing: 0,
    zoom: initialZoom,
  });

  function setToFilterPage() {
    props.setCurrPage("filter");
  }

  const [hoveredLocation, setHoveredLocation] = useState<string | null>(null);
  const [hoveredLocationRating, setLocationRating] = useState<number>(0);

  const handleMouseEnter = (location: string) => {
    setHoveredLocation(location);
    const url = new URL("http://localhost:3232/study-review?name=" + location);
    const fetchData = async () => {
      try {
        const response = await fetch(url);
        const data = await response.json();

        if ("Success" in data) {
          const successValue: number = data.Success;
          setLocationRating(successValue);
          console.log("hi " + successValue);
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  };

  const handleMouseLeave = () => {
    setHoveredLocation(null);
    setLocationRating(0);
  };

  const buildingLayer: FillExtrusionLayer = {
    id: "3d-buildings",
    source: "composite",
    "source-layer": "building",
    filter: ["==", "extrude", "true"],
    paint: {
      "fill-extrusion-color": "#FFDB47",
      "fill-extrusion-opacity": 0.4,
      "fill-extrusion-height": ["get", "height"],
      "fill-extrusion-base": ["get", "min_height"],
    },
    type: "fill-extrusion",
  };

  // generates the star images based off rating
  const generateStarComponents = () => {
    console.log("omg " + hoveredLocationRating);
    const starComponents: JSX.Element[] = [];
    const maxRating = 5;
    const roundedRating = Math.round(hoveredLocationRating * 2) / 2; // Round rating to nearest 0.5

    if (roundedRating != 0) {
      starComponents.push(<text className="rating-text">Yelp Rating:</text>);
    }
    for (let i = 1; i <= maxRating; i++) {
      if (i <= roundedRating) {
        // Display a full star
        starComponents.push(
          <img src={star} className="star" aria-label="star" key={i}></img>
        );
      } else if (i - 0.5 === roundedRating) {
        // Display a half star
        starComponents.push(
          <img
            src={halfstar}
            className="star"
            aria-label="half-star"
            key={i}
          ></img>
        );
      }
      // } else {
      //   // Display a blank star
      //   starComponents.push(
      //     <img src={nostar} className="star" aria-label="no-star" key={i}></img>
      //   );
      // }
    }

    return starComponents;
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
        <Layer {...buildingLayer} />
        {Array.from(props.locationCoords.entries()).map(
          ([key, coord], index) => {
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
              <div>{generateStarComponents()}</div>
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
