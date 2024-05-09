import { initializeApp } from "firebase/app";
import "../styles/App.css";
import Search from "./Search/Search";
import AuthRoute from "./auth/AuthRoute";

// REMEMBER TO PUT YOUR API KEY IN A FOLDER THAT IS GITIGNORED!!
// (for instance, /src/private/api_key.tsx)
//import {API_KEY} from "./private/api_key"

/**
 * Main program that starts the program front end
 */

/**
 * configuration for firebase
 */
const firebaseConfig = {
  apiKey: process.env.API_KEY,
  authDomain: process.env.AUTH_DOMAIN,
  projectId: process.env.PROJECT_ID,
  storageBucket: process.env.STORAGE_BUCKET,
  messagingSenderId: process.env.MESSAGING_SENDER_ID,
  appId: process.env.APP_ID,
};

/**
 * initializes firebase app
 */
initializeApp(firebaseConfig);

/**
 * A function that reders the entire APP in the top level.
 *
 * @returns The entire Map app and its relative components
 */
function App() {
  let options: string[] = [
    "Total Silence",
    "Quiet",
    "Conversational",
    "Loud",
    "Barely any traffic",
    "Light traffic",
    "Moderate traffic",
    "Heavy Traffic",
    "Aesthetic",
    "Not Aesthetic",
    "Accessible",
    "Has Whiteboard or TV",
    "1 person (Studying alone)",
    "2-4 people",
    "4-8 people",
    "8+ people",
  ];

  return (
    <div className="App">
      <AuthRoute gatedContent={<Search options={options} />} />
    </div>
  );
}

export default App;
