import { useState } from "react";
import "../../styles/loading.css";
import Page from "../Search/Search";
import img from "../../images/loading_bear.gif";

interface pageProps {
  setCurrPage: React.Dispatch<React.SetStateAction<String>>;
  filters: Set<String>;
}

export default function Loading(props: pageProps) {
  setTimeout(function () {
    props.setCurrPage("result");
  }, 2500);
  return (
    <div className="load">
      <h1> Searching ... </h1>
      <img src={img} />
    </div>
  );
}
