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

// function fetchLocData(){
//   const fetchData

// }

export default function Loading(props: pageProps) {
  setTimeout(function () {
    props.setCurrPage("result");
  }, 2500);

  let data_response;
  const coords = new Map<string, number[]>();
  const descriptions = new Map<string, string[]>();

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

  const data_json = async () => {
    const response = await fetch(url);
    return response.json();
  };

  const parsedResponse = data_json;

  // Extract the lists for each name
  data_json()
    .then((parsedResponse) => {
      for (const name in parsedResponse.Result) {
        if (Object.prototype.hasOwnProperty.call(parsedResponse.Result, name)) {
          const [desc_list, coord_list] = parsedResponse.Result[name];
          coords.set(name, coord_list);
          descriptions.set(name, desc_list);
        }
      }
    })
    .catch((error) => {
      console.error("Error fetching or processing data:", error);
    });

  props.setLocationCoords(coords);
  props.setDescriptions(descriptions);

  return (
    <div className="load">
      <h1> Searching ... </h1>
      <img src={img} />
    </div>
  );
}
