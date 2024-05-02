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
  userLoc: Number[];
}

export default function Loading(props: pageProps) {
  setTimeout(function () {
    props.setCurrPage("result");
  }, 1800);

  useEffect(() => {
    const coords = new Map<string, number[]>();
    const descriptions = new Map<string, string[]>();

    const url = new URL("http://localhost:3232/search-study");
    if (props.filters.size == 0) {
      // console.log("over here");
      // url.searchParams.append("volume", "Conversational");
      // url.searchParams.append("traffic", "Moderate traffic");
      // url.searchParams.append("capacity", "4-8 people");
    }
    props.filters.forEach((filter) => {
      console.log(filter);
      if (
        filter == "Total Silence" ||
        filter == "Quiet" ||
        filter == "Conversational" ||
        filter == "Loud"
      ) {
        url.searchParams.append("volume", filter);
      }
      if (
        filter == "Barely any traffic" ||
        filter == "Light traffic" ||
        filter == "Moderate traffic" ||
        filter == "Heavy traffic"
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
      if (filter == "Aesthetic" || filter == "Not Aesthetic") {
        url.searchParams.append("aesthetics", "yes");
      }
      if (props.userLoc[0] != null && props.userLoc[1] != null) {
        url.searchParams.append("lat", props.userLoc[0].toString());
        url.searchParams.append("lon", props.userLoc[1].toString());
      }
    });
    console.log(url.pathname);

    const data_json = async () => {
      const response = await fetch(url);
      return response.json();
    };

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
    console.log(coords);
    props.setLocationCoords(coords);
    props.setDescriptions(descriptions);
  }, []);

  return (
    <div className="load">
      <h1 tabIndex={0}> Searching ... </h1>
      <img src={img} alt="bear loading moving image" />
    </div>
  );
}
