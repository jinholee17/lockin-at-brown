import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";
import React from "react";
import { addLoginCookie, removeLoginCookie } from "../../utils/cookie";

/**
 * interface to manage login element
 */
export interface ILoginPageProps {
  loggedIn: boolean;
  setLogin: React.Dispatch<React.SetStateAction<boolean>>;
}
/**
 * Manage log-in
 *
 * @param props Function that handles loggin in. It uses firebase to ensure only brown emaisl can login
 * @returns JSX component for login prompt
 */
const Login: React.FunctionComponent<ILoginPageProps> = (props) => {
  const auth = getAuth();

  const signInWithGoogle = async () => {
    try {
      const response = await signInWithPopup(auth, new GoogleAuthProvider());
      const userEmail = response.user.email || "";

      // Check if the email ends with the allowed domain
      if (userEmail.endsWith("@brown.edu")) {
        console.log(response.user.uid);
        // add unique user id as a cookie to the browser.
        addLoginCookie(response.user.uid);
        props.setLogin(true);
      } else {
        // User is not allowed, sign them out and show a message
        await auth.signOut();
        console.log("User not allowed. Signed out.");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="home-page">
      <header>
        <img src="/src/images/bearLogo.png" id="bearLogo" />
        <h1 className="title"> LockIn@Brown</h1>
      </header>
      <div className="login-box">
        <div className="text-btn">
          <h1 className="welcomeText">Where can I study at?</h1>
          <button
            className="Login-button"
            id="loginButton"
            onClick={() => signInWithGoogle()}
          >
            Sign in
          </button>
        </div>
        <img src="/src/images/peerBear.png" className="peer-bear" />
      </div>
    </div>
  );
};
/**
 * Manage logout
 *
 * @param props Logout component that deals with the user loggin out so they can not have access to the maps main page
 * @returns JSX for log-out
 */
const Logout: React.FunctionComponent<ILoginPageProps> = (props) => {
  const signOut = () => {
    removeLoginCookie();
    props.setLogin(false);
  };

  return (
    <header>
      <img src="/src/images/bearLogo.png" id="bearLogo" />
      <h1 className="title"> LockIn@Brown</h1>
      <div className="logout-box">
        <button className="SignOut" onClick={() => signOut()}>
          Sign Out
        </button>
      </div>
    </header>
  );
};

/**
 * To Render and manage entire log-in and log-out system
 *
 * @param props
 * @returns JSX for entire login and log-out management
 */
const LoginLogout: React.FunctionComponent<ILoginPageProps> = (props) => {
  return <>{!props.loggedIn ? <Login {...props} /> : <Logout {...props} />}</>;
};

export default LoginLogout;
