import "mapbox-gl/dist/mapbox-gl.css";
import { useEffect, useState } from "react";
import Map, {
  Layer,
  MapLayerMouseEvent,
  Source,
  ViewStateChangeEvent,
  Marker,
} from "react-map-gl";
import { geoLayer, overlayData } from "../utils/overlay";
import { geoLayerFilter, overlayDataFilter } from "../utils/overlayFilter";
import { addPin, getPins, clearPins } from "../utils/api";

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

const ProvidenceLatLong: LatLong = {
  lat: 41.824,
  long: -71.4128,
};
const initialZoom = 10;

/**
 * Main function that handles all the map box logic
 * It deals with storing pins and highlighting various data
 *
 * @returns the mapbox render
 */
export default function Mapbox() {
  // Adding pin to firestore
  const [pins, setPins] = useState<string[]>([]);
  useEffect(() => {
    getPins().then((data) => {
      console.log("Fetched pins:", data);
      setPins(data.pins);
    });
    console.log(pins);
  }, []);

  const addingPin = async (newPin: string) => {
    // - update the client words state to include the new word
    setPins([...pins, newPin]);
    // - query the backend to add the new word to the database
    await addPin(newPin);
  };

  /**
   * To ensure that whenever a click happens, a pin will be added
   *
   * @param e the clicking event
   */
  function onMapClick(e: MapLayerMouseEvent) {
    addingPin(
      "lat:" + e.lngLat.lat.toString() + ", long:" + e.lngLat.lng.toString()
    );
  }

  // zoom and move around for map
  const [viewState, setViewState] = useState({
    latitude: ProvidenceLatLong.lat,
    longitude: ProvidenceLatLong.long,
    zoom: initialZoom,
  });

  // red-line data default overlay
  const [overlay, setOverlay] = useState<GeoJSON.FeatureCollection | undefined>(
    undefined
  );
  // Adds the overlay to the map (redlining)
  useEffect(() => {
    overlayData().then((data) => {
      setOverlay(data);
    });
  }, []);

  // search with area-description filter, adds another overlay
  const [commandString, setCommandString] = useState<string>("");
  const [useFilter, setUseFilter] = useState<boolean>(false);
  // Adds the overlay to the map (search description)
  const [overlayFilter, setOverlayFilter] = useState<
    GeoJSON.FeatureCollection | undefined
  >(undefined);

  /**
   * Deals with taking ina user descriptiont that will be utilzied to
   * highlight areas with the provided description
   * Clears search input area after clicking submit
   *
   * @param commandString
   */
  function handleSubmit(commandString: string) {
    setUseFilter(true);
    overlayDataFilter(commandString).then((data) => {
      setOverlayFilter(data);
    });
    const inputField = document.getElementById(
      "input-field"
    ) as HTMLInputElement;
    if (inputField) {
      inputField.value = "";
    }
    setCommandString("");
  }

  return (
    <div className="map">
      <input
        id="input-field"
        aria-label="area description search textbox"
        className="area-input"
        placeholder="Enter area description search here"
        onChange={(ev) => setCommandString(ev.target.value)}
      ></input>
      <button
        aria-lable="Submit"
        aria-description="Press enter key or click submit button to process command"
        className="submit-btn"
        onClick={() => handleSubmit(commandString)}
      >
        Submit
      </button>
      <button
        aria-lable="Clear Search"
        aria-description="Press enter key or click submit button to clear current search"
        className="submit-btn"
        onClick={() => {
          setOverlayFilter(undefined);
          setUseFilter(false);
        }}
      >
        Clear Search
      </button>
      <Map
        mapboxAccessToken={MAPBOX_API_KEY}
        {...viewState}
        style={{ width: window.innerWidth, height: window.innerHeight }}
        mapStyle={"mapbox://styles/mapbox/streets-v12"}
        onMove={(ev: ViewStateChangeEvent) => setViewState(ev.viewState)}
        onClick={(ev: MapLayerMouseEvent) => onMapClick(ev)}
      >
        <Source id="geo_data" type="geojson" data={overlay}>
          <Layer id={geoLayer.id} type={geoLayer.type} paint={geoLayer.paint} />
        </Source>

        {useFilter && (
          <Source
            id="geo_data_filter"
            type="geojson"
            data={overlayFilter}
            arai-label="filter-overlay"
          >
            <Layer {...geoLayerFilter} />
          </Source>
        )}

        {pins.map((pin, index) => {
          // Split the pin string into latitude and longitude
          const [lat, long] = pin.split(" ");
          const latitude = parseFloat(lat.split(":")[1]);
          const longitude = parseFloat(long.split(":")[1]);
          return (
            <Marker key={index} latitude={latitude} longitude={longitude}>
              {/* Marker content */}
              <div style={{ fontSize: 20 }}>📍</div>
            </Marker>
          );
        })}
      </Map>

      <button
        onClick={async () => {
          // - query the backend to clear the user's words
          setPins([]); // Assuming setPins is a function to update the pins state
          // - clear the user's words in the database
          await clearPins(); // Assuming clearPins is a function to clear the pins in the database
        }}
        // Handles clearing the users stored pins
      >
        Clear Pins
      </button>
    </div>
  );
}
