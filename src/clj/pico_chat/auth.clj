(ns pico-chat.auth
  (:require [oauth.google :as google]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [ring.util.response :as resp]))

(def client-id "144528828490-gv7on6ttvd6e2c56v5v5oachqpmg3u4t.apps.googleusercontent.com")
(def client-secret "EqOALFpbfJA79WvtGnFbPpWU")

(def oauth-callback-uri {:uri "http://localhost:3000/oauth2callback"
                         :path "/oauth2callback"})

(defn google-auth-uri
  ([]
   (google-auth-uri oauth-callback-uri))
  ([oauth-callback-uri]
   (google/oauth-authorization-url client-id
                                   (:uri oauth-callback-uri))))


(defn get-token [code oauth-callback-uri]
  (google/oauth-access-token client-id client-secret code (:uri oauth-callback-uri)))

(defn get-user [token]
  (google/user-info (google/oauth-client (:access-token token))))

(defn req->token [req]
  (when-let [code (get-in req [:query-params "code"])]
    (when-let [token (get-token code oauth-callback-uri)]
      token)))

(defn oauth-callback [req]
  (if-let [token (req->token req)]
    (-> (resp/redirect "/")
        (assoc-in [:session :identity] token))
    (throw-unauthorized {:message "Wrong code"})))

(defn redirect-to-provider []
  (resp/redirect (google-auth-uri)))
