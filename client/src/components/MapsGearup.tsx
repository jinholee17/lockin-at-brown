import { useState } from "react";
import FirestoreDemo from "./FirestoreDemo";
import Mapbox from "./Mapbox";

enum Section {
  FIRESTORE_DEMO = "FIRESTORE_DEMO",
  MAP_DEMO = "MAP_DEMO",
}
/**
 * Sets up the map layout in the front end. Has the favorite word stoarge and maps options
 *
 * @returns
 */
export default function MapsGearup() {
  const [section, setSection] = useState<Section>(Section.FIRESTORE_DEMO);

  return (
    <div>
      <div className="filter-input">
        <input type="text" list="filter-list" placeholder="Quiet" />
        <datalist id="filter-list">
          <option>Total Silence</option>
          <option>Quiet</option>
          <option>Conversational</option>
          <option>Loud</option>
        </datalist>
        <button>+</button>
      </div>
      {/* <button onClick={() => setSection(Section.FIRESTORE_DEMO)}>
        Section 1: Firestore Demo
      </button>
      <button onClick={() => setSection(Section.MAP_DEMO)}>
        Section 2: Mapbox Demo
      </button>
      {section === Section.FIRESTORE_DEMO ? <FirestoreDemo /> : null}
      {section === Section.MAP_DEMO ? <Mapbox /> : null} */}
    </div>
  );
}
