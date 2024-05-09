import { useState } from "react";
import { getLoginCookie } from "../../utils/cookie";
import LoginLogout from "./LoginLogout";

/**
 * interface to manage authentication content
 */
interface AuthRouteProps {
  gatedContent: React.ReactNode;
}
/**
 * Component to render authentication component for firebase
 *
 * @param props Component for handling authentication logic.
 * @return component for loggin in and loggin out
 */
function AuthRoute(props: AuthRouteProps) {
  const [loggedIn, setLogin] = useState(false);

  if (!loggedIn && import.meta.env.VITE_APP_NODE_ENV === "test") {
      setLogin(true);
   }
   
  // SKIP THE LOGIN BUTTON IF YOU HAVE ALREADY LOGGED IN.
  if (!loggedIn && getLoginCookie() !== undefined) {
    setLogin(true);
  }

  return (
    <>
      <LoginLogout loggedIn={loggedIn} setLogin={setLogin} />

      {loggedIn ? props.gatedContent : null}
    </>
  );
}

export default AuthRoute;
