import { useState } from "react";
import FirestoreDemo from "../FirestoreDemo";
import Filter from "../Search/Filter";
import Loading from "../Search/Loading";
import Result from "./Result";

export enum Page {
  FILTER = "filter",
  LOAD = "load",
  RESULT = "result",
}

interface searchProps {
  options: string[];
}

export default function Search(props: searchProps) {
  const [currPage, setCurrPage] = useState<String>(Page.FILTER);
  // const [query, setQuery] = useState<string>("");
  const [filters, setFilters] = useState<Set<string>>(new Set());

  const initialCoords = new Map<string, number[]>();
  const initialDesc = new Map<string, string[]>();
  const [locationCoords, setLocationCoords] =
    useState<Map<string, number[]>>(initialCoords);
  const [locationTopDescriptions, setDescriptions] =
    useState<Map<string, string[]>>(initialDesc);

  return (
    <div>
      {currPage == Page.FILTER && (
        <Filter
          options={props.options}
          filters={filters}
          setFilters={setFilters}
          setCurrPage={setCurrPage}
        />
      )}
      {currPage == Page.LOAD && (
        <Loading
          filters={filters}
          setCurrPage={setCurrPage}
          setDescriptions={setDescriptions}
          setLocationCoords={setLocationCoords}
        />
      )}
      {currPage == Page.RESULT && (
        <Result
          locationTopDescriptions={locationTopDescriptions}
          locationCoords={locationCoords}
          setCurrPage={setCurrPage}
        />
      )}
    </div>
  );
}
