import { useState } from "react";
import FirestoreDemo from "../FirestoreDemo";
import Mapbox from "../Mapbox";
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

  return (
    <div>
      {currPage == Page.FILTER && (
        <Filter options={props.options} setCurrPage={setCurrPage} />
      )}
      {currPage == Page.LOAD && <Loading setCurrPage={setCurrPage} />}
      {currPage == Page.RESULT && <Result setCurrPage={setCurrPage} />}
    </div>
  );
}
