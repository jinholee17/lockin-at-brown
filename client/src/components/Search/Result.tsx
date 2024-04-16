import { useState } from "react";
import "../../styles/loading.css";
import Page from "../Search/Search";

interface pageProps {
  setCurrPage: React.Dispatch<React.SetStateAction<String>>;
}

export default function Result(props: pageProps) {
  return (
    <div className="Result">
      <p>Hi</p>
    </div>
  );
}
