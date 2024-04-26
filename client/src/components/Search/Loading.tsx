import { useState } from "react";
import "../../styles/loading.css";
import Page from "../Search/Search";
import img from "../../images/loading_bear.gif";

interface pageProps {
  setCurrPage: React.Dispatch<React.SetStateAction<String>>;
  filters: Set<string>;
  setLocationCoords: React.Dispatch<
    React.SetStateAction<Map<string, number[]>>
  >;
  setDescriptions: React.Dispatch<React.SetStateAction<Map<string, string[]>>>;
}

export default async function Loading(props: pageProps) {
  setTimeout(function () {
    props.setCurrPage("result");
  }, 2500);

  let data_response;
  const url = new URL("http://localhost:3232/search-study");

  props.filters.forEach((filter) => {
    if (
      filter == "Total Silence" ||
      filter == "Quiet" ||
      filter == "Conversational" ||
      filter == "Loud"
    ) {
      url.searchParams.append("volume", filter);
    }
  });

  data_response = await fetch(url);
  const data_json = await data_response?.json();
  props.setLocationCoords(data_json);

  return (
    <div className="load">
      <h1> Searching ... </h1>
      <img src={img} />
    </div>
  );
}
