# 🗺️ Project Details

- **Project Name:** Maps
- **Contributors:** Alyssa Sun (asun59) & Megan Zheng (mzheng37)
- **Estimated time:** 18 hours
- **Project Description:** This project is for the 5th sprint for CS32 at Brown. In this project, we incorporated a zoomable and movable map that supports:
  - Google login for user specific information storage with firestore database management
  - Pin with click on any location / clear pin function (gets stored in database for consistent access according to user)
  - Overlay on map displaying red-lining data in the US
  - Putting another overlay highlight to filter according to area description keywords / clearing those searches
  - API for developers to access red-lining dataset, filtering red-lining dataset according to bounding-box coordinates, and filtering dataset based on provided keyword for area-description
- **[Repo Link](https://github.com/cs0320-s24/maps-mzheng37-asun59)**

# 💡 Design Choices

- **The Front-End Client** :
  - Displays map that is scrollable and zoomable using Mapbox
  - When user clicks somewhere on the map, the lat. and long. information are obtained to create a pin and stored in the database through sending this API request and interaction with a backend handler that actually manages this action 
  - When user clicks the clear pin, all previously store pin information should be removed from database through sending this API request and interaction with a backend handler that actually manages this action  
  - Fetch data from red-lining API calls then display these data using overlay feature in Mapbox to render an overlay with color
  - When user puts in an area-description keyword, another fetch request to backend API to obtain filtered dataset based on that keyword is initiated; this data is then taken and generated another overlay with dark purple highlight displaying those regions matching the keyword
  - When user clicks clear search button, all highlight should be removed
- **Red-Lining Dataset API (Backend)**:
  - A Server class initiates end-point with different handler classes that takes care of various functions
  - Pin handler classes each takes care of storing one pin, clearing pins, and getting a list of pins (when the user re-logins) interacting with the database user-specifically
  - Red-lining handler classes take care of obtaining entire set of red-lining data, filtering red-lining data based on bounding box parameter queries, and filtering red-lining data based on
  - Red-lining dataset is parsed through a provided parser that takes the GeoJson and parse it into a specific formatted object with according fields that is compatible with Moshi to return to frontend (See the parsed object in `server/src/main/java/edu/brown/cs/student/main/server/geoJsonParser/GeoJsonObject.java`)
  - Moshi adapters are used to deliver formatted results to JSON usable by frontend
  - We used a shared interface where normal datasource and mocked datasource can be passsed into the handler to alter the data source 
- **Firebase Authentication / Firestore (Database)**:
  - Login / Logout with Goolge accounts
  - Stores pin data based on user

# 🐛 Errors/Bugs

No known bugs in program 
Front-end total integration tests with overlay are sample tests for if we can grab the overlay; however, they do not work as they are since overlay are outside components (some tests are commented out because of this)

# 🧪 Tests

## e2e Tests: Front-end / Front-end + Back-end Interction

### Integrated Real Query to Server: To test everything is expected as how users would actually interact with the web-app
- e2e/RedLine.spec.ts: redline search (sample) 
- e2e/Pins.spec.ts: pins + onload pins 
- e2e/Integration.spec.ts: pins + redline search (sample) 
- e2e/App.spec.ts: general login and logout

- unit/RedLine.spec.ts: redline search actual data, test if the correct data returns

## Backend Tests: Handler Test / Unit Test

### Handler Test:

- `server/src/test/java/edu/brown/cs/student/backendIntegrationTesting/TestAPIHandlers.java` : includes all tests on the relative APIHandlers used (pin + red-lining data interaction), error cases, and interaction between the two handlers

### Unit Tests: Backend Functions

- `server/src/test/java/edu/brown/cs/student/backendUnitTesting/unitTests.java` : includes testing for red-lining data parsing unit functions

### Backend Mocked: 
- `server/src/test/java/edu/brown/cs/student/MockTest/MockRedlineData.java`: mocks geojson and tests various filtering with this mocked data using a mocked datasource being passed into the handlers 

# 💻 How to

- Run `make dependencies` to install all dependencies for running the frontend particularly
  - Or install the following dependencies
  ```
  cd client
  npm install
  npm install firebase
  npx playwright install
  ```
- Run `make frontend` to make all front-end related components (everything in the client folder) and follow the clickable link to open the front-end in your local browser
  - Or run
  ```
  cd client
  npm run start
  ```
- Run `make backend` to make all back-end related components (everything in the server foler) to start a backend sever

  - Or run

  ```
  cd server
  mvn package
  ./run
  ```

- For Developers working with Firebase / Firestore:
  - Ensure you have your API key from Firebase and Mapbox API key inside a `.env` file inside the top-level of the `client` directory
  - Ensure you have a `firebase_config.json` from Firebase inside `server/src/main` directory inside a `resources` directory

## Developers:

- If you would like to use the red-lining dataset APIs and pin setting APIs, access them through the below urls after starting the server:
  - `/redline/*`: access all red-lining data
  - `/redline?max-lat=[value]&max-long=[value]&min-lat=[value]&min-long=[value]`: provided a bounding box of minimum and maximum latitude and longitude, filter the red-lining data
  - `/search-area?area-desc=[key_words]`: provided keywords, filter the red-lining data based on area description
  - `/add-pin?uid=[user]&pin=[coordinate]` : to add a pin data to the database for user
  - `/list-pins?uid=[user]`: list all current pins for user
  - `clear-pins?uid=[user]`: clear all pins for user

# 🤝 Collaboration

No other resources or collaboration in this project other than provided code through Maps GearUp and Parser from given Parser code.

Red-lining data set from: https://dsl.richmond.edu/panorama/redlining/data
