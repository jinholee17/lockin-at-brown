/**
 * This class serves as a way to test the front end without backend logic
 * through modeling different kinds of csv files that are stored and accessed
 * from data structures
 */
export const locationTopDescriptions = new Map<string, string[]>();
export const locationCoords = new Map<string, number[]>();


/**
 *  Descriptions of study spots
 */
const rockDes = [
    "Quiet", 
    "Large Space",
    "Aesthetic"
];
const sciLiDes = [
    "Loud", 
    "Conversational",
    "Large Space"
];
const blueRoomDes = [
    "Loud", 
    "Conversational",
    "Small Space"
];


/**
 * Coordinates of study spots
 */
const sciliCoord = [41.827, -71.4002];

const rockCoord = [41.825725,-71.405089]

const blueRoomCoord = [41.8268392794992, -71.40324137065315];


//Creating file dictionary
locationTopDescriptions.set("Science Library", sciLiDes);
locationTopDescriptions.set("John D. Rockefeller, Jr. Library", rockDes);
locationTopDescriptions.set("Blue Room", blueRoomDes);



locationCoords.set("Science Library", sciliCoord);
locationCoords.set("John D. Rockefeller, Jr. Library", rockCoord);
locationCoords.set("Blue Room", blueRoomCoord);

