import { useState, useEffect } from "react";
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

  useEffect(() => {
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
      if (
        filter == "Low Traffic" ||
        filter == "Moderate Traffic" ||
        filter == "High Traffic"
      ) {
        url.searchParams.append("traffic", filter);
      }
      if (
        filter == "1 person (Studying alone)" ||
        filter == "2-4 people" ||
        filter == "4-8 people" ||
        filter == "8+ people"
      ) {
        url.searchParams.append("capacity", filter);
      }
      if (filter == "Accessible") {
        url.searchParams.append("accessible", "yes");
      }
      if (filter == "Has Whiteboard/TV") {
        url.searchParams.append("whiteboard", "yes");
      }
      // add aesthetics and lon and lat
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
          if (
            Object.prototype.hasOwnProperty.call(parsedResponse.Result, name)
          ) {
            const [desc_list, coord_list] = parsedResponse.Result[name];
            coords.set(name, JSON.parse(coord_list));
            descriptions.set(
              name,
              desc_list.substring(1, desc_list.length - 1).split(", ")
            );
          }
        }
      })
      .catch((error) => {
        console.error("Error fetching or processing data:", error);
      });

    props.setLocationCoords(coords);
    props.setDescriptions(descriptions);
  }, []);

  return (
    <div className="load">
      <h1> Searching ... </h1>
      <img src={img} />
    </div>
  );
}
