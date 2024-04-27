import { useState } from "react";
import "../../styles/filters.css";
import Page from "../Search/Search";

interface searchProps {
  options: string[];
  filters: Set<string>;
  setFilters: React.Dispatch<React.SetStateAction<Set<string>>>;
  setCurrPage: React.Dispatch<React.SetStateAction<String>>;
  userLoc: Number[];
  setUserLocation: React.Dispatch<React.SetStateAction<Number[]>>;
}

export default function Filter(props: searchProps) {
  let options: string[] = props.options;
  // const [filters, setFilters] = useState<Set<String>>(new Set());
  const [filtersNA, setFiltersNA] = useState<string>("");

  function addFilter() {
    setFiltersNA("");
    let input = document.getElementById("input-filter") as HTMLInputElement;
    let text = input.value;
    if (options.includes(text)) {
      const newFilters = new Set(props.filters);
      newFilters.add(text);
      props.setFilters(newFilters);
    } else {
      setFiltersNA("Filter " + text + " Not Found!");
    }
    input.value = "";
  }

  function deleteFilter(filter: string) {
    if (props.filters.has(filter)) {
      const newFilters = new Set(props.filters);
      newFilters.delete(filter);
      props.setFilters(newFilters);
    }
  }

  function setToLoadPage() {
    props.setCurrPage("load");
  }

  if ("geolocation" in navigator) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;
        // console.log("Latitude:", latitude);
        // console.log("Longitude:", longitude);
        props.setUserLocation([latitude, longitude]);
      },
      (error) => {
        console.error("Error getting current position:", error);
      }
    );
  } else {
    console.error("Geolocation is not supported by this browser.");
  }

  return (
    <div className="input">
      <p className="Not-Found">{filtersNA}</p>
      <h1 className="input-text" id="direction-text" tabIndex={0}>
        Enter filters in the input space below and use the add button to add the
        filter:
      </h1>
      <div className="filter-input">
        <input
          type="text"
          list="filter-list"
          placeholder="Quiet"
          id="input-filter"
          aria-label="input field"
          aria-labelledby="direction-text"
        />
        <datalist id="filter-list">
          {options.map((option, index) => (
            <option key={index}> {option} </option>
          ))}
        </datalist>
        <button
          aria-label="add button"
          aria-description="click or use enter to add the filter"
          onClick={addFilter}
        >
          +
        </button>
      </div>

      <div className="added-filters">
        {Array.from(props.filters).map((filter, index) => (
          <button
            aria-label="delete filter"
            aria-description={
              "click or use enter to delete this filter" + filter
            }
            onClick={() => deleteFilter(filter)}
            key={index}
          >
            {filter} ❌
          </button>
        ))}
      </div>

      <button
        className="lock-btn"
        onClick={setToLoadPage}
        aria-label="Lock in search button"
        aria-description={"click or use enter to search locations with filters"}
      >
        Lock In! 🔓
      </button>
      <img src="/src/images/peerBear.png" className="corner-bear" />
    </div>
  );
}
