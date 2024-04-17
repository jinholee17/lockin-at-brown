import { useState } from "react";
import "../../styles/filters.css";
import Page from "../Search/Search";

interface searchProps {
  options: string[];
  setCurrPage: React.Dispatch<React.SetStateAction<String>>;
}

export default function Filter(props: searchProps) {
  let options: string[] = props.options;
  const [filters, setFilters] = useState<Set<String>>(new Set());
  const [filtersNA, setFiltersNA] = useState<string>("");

  function addFilter() {
    setFiltersNA("");
    let input = document.getElementById("input-filter") as HTMLInputElement;
    let text = input.value;
    if (options.includes(text)) {
      const newFilters = new Set(filters);
      newFilters.add(text);
      setFilters(newFilters);
    } else {
      setFiltersNA("Filter " + text + " Not Found!");
    }
    input.value = "";
  }

  function deleteFilter(filter: String) {
    if (filters.has(filter)) {
      const newFilters = new Set(filters);
      newFilters.delete(filter);
      setFilters(newFilters);
    }
  }

  function setToLoadPage() {
    props.setCurrPage("load");
  }

  return (
    <div className="input">
      <p className="Not-Found">{filtersNA}</p>
      <div className="filter-input">
        <input
          type="text"
          list="filter-list"
          placeholder="Quiet"
          id="input-filter"
        />
        <datalist id="filter-list">
          {options.map((option, index) => (
            <option key={index}> {option} </option>
          ))}
        </datalist>
        <button onClick={addFilter}>+</button>
      </div>

      <div className="added-filters">
        {Array.from(filters).map((filter, index) => (
          <button onClick={() => deleteFilter(filter)} key={index}>
            {filter} ❌
          </button>
        ))}
      </div>

      <button className="lock-btn" onClick={setToLoadPage}>
        Lock In! 🔓
      </button>
      <img src="/src/images/peerBear.png" className="corner-bear" />
    </div>
  );
}
