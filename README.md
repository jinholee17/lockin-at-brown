# 🔓 Project Details

- **Project Name:** LockIn@Brown
- **Contributors:** Alyssa Sun (asun59), Jinho Lee (jlee812), Julie Chung (hchung33), Megan Zheng (mzheng37)
- **Estimated time:** 35 hours
- **Project Description:** This project is for the final project of CS32. LockIn@Brown is a web-app that suggests study spaces on Brown's campus to students with Brown login. The web-app has the following main features:
  - Google login for @brown.edu email addresses to store user login information
  - User may include they're prefered features
    - These features are stored in the firestore database (save on reload) when they are added or deleted
    - Features calculate which locations to return as pins on a map, along with star rating from Yelp
    - User can enter new searches
    - Defualt search result will be used if user do not enter anything
  - Accessibility feature including screen-reader adaptibility and navigation without trackback / adaptibility to changing screen size
- **Planning:** As a team, we discussed comprehensively stakeholders / end-user needs. After dividing work between the four of us, we drafted in Figma our UI meanwhile collecting study space data. We added new features such as API call to Yelp and accessibilty as we had more time and considered which features may be beneficial to our end-users.
- **[Repo Link](https://github.com/cs0320-s24/term-project-jlee812-asun59-mzheng37-hchung33)**

# 💡 Design Choices

- **Front-End (React/TypeScript/CSS Styling/MapBox API)** :

  - `Main.tsx` > `App.tsx` : Entry point to the frontend program, add new avaiable filters here by appending new filters to the `options` array
  - **_Authentication_**

    - `AuthRoute.tsx`: Maintain state of logged-in or not
    - `LoginLogout.tsx`: Return appropriate JSX depending on if the user is logged-in or logged-out

  - **_Main Web-App_**

    - `Search.tsx`: Maintain state about which of the following component should be rendered
      - `Filter.tsx`: Maintain interactive filter lists, adding and deleting filters; change current page status to `load` when user press `lock-in` button
      - `Loading.tsx`: Return the loading page, wait for a couple of seconds for filters to be send to and used by the server and return prefered locations as a promise before setting the page to the result page
      - `Result.tsx`: Use MapBox to display locations as pins where user can hover over to see exact filters and Yelp ratings based on the server's response to filters inputted by user through tht frontend. Also contains button to query change filters for a new search

  - **_Utils_**
    - `api.ts`: Establish endpoints to interact with server and firestroe
    - `cookie.ts`: Get information on user's cookie

- **Backend (Java/Yelp API)**:

  - After starting the server, accessing the folowing API endpoints will trigger different functionaility as described below. _Developers may also use integrate the following APIs for their use _ :
    - _Returning Prefered Location + Yelp Rating:_
      - `/search-study?volume=[value]&traffic=[value]&capacity=[value]&accessible=[value]&whiteboard=[value]&aesthetics=[value]`: Take in a user's desired filters for a room, and returns top 3 matching study spots
      - `/study-review?businessID=[YelpID for location]`: Obtain and process rating of target location from Yelp using the YelpID of that location
    - _Interacting with firestore:_
      - _uid are obtained by unqiue identifier using the user's cookie information_
      - `/delete-word?uid=[user]&word=[filter]`: access firestore and delete the target `filter` stored in user's firestore database
      - `/add-word?uid=[user]&word=[filter]`: access firestore and add the target `filter` stored into user's firestore database
      - `/list-words?uid=[user]`: access firestore and lists all filters stored in user's firestore databse
  - **_Data_**:
    - `data.csv`: Data regarding study space and their features are collected from students at Brown through a Google form survey. This data is then transferred into a CSV file to be used for returning preference-based locations using our algorithm described below.
    - `locationcoords.json`: Hard-coded json object with all the existing study locations coordinate information and their YelpIDs for obtaining rating
  - **_Algorithm_**:
    - `SearchStudyHandler.java` in /server/handlers
    - `StudySpotDataSource.java` in /server/DataSource
    - Uses a point ranking system where it goes through a list of study spots, and based on
      matching filters from the user and that study spot, adds points. Also removes points
      based on differing volumes and location. Also gives a point if the user's current time matches with the studyspot data's time.

- **Firebase Authentication / Firestore (Database)**:
  - Login / Logout with Google accounts (Brown address only)
  - Stores filter data based on user searches

# 🐛 Errors/Bugs

No known bugs in program

# 🧪 Tests

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
  - Ensure you have a Yelp API key if you decide to use ratings
  - Ensure you have a `firebase_config.json` from Firebase inside `server/src/main` directory inside a `resources` directory

# 🤝 Collaboration / Citations

- Home page text typing effect starter code from article: https://www.sitepoint.com/css-typewriter-effect/
