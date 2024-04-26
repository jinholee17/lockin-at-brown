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
  const [filters, setFilters] = useState<Set<String>>(new Set());

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
        <Loading filters={filters} setCurrPage={setCurrPage} />
      )}
      {currPage == Page.RESULT && (
        <Result filters={filters} setCurrPage={setCurrPage} />
      )}
    </div>
  );
}
