(ns pico-chat.auth
  (:require [oauth.google :as google]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [pico-chat.config :refer [env]]
            [pico-chat.db.users :as users]
            [mount.core :refer [defstate]]
            [ring.util.response :as resp]))

(def google-callback-path "/oauth2callback")

(defn google-auth-uri [{:keys [google-client-id google-client-secret
                               domain-name]}]
  (google/oauth-authorization-url google-client-id
                                  (str domain-name
                                       google-callback-path)))


(defn get-token [code {:keys [google-client-id google-client-secret
                              domain-name]}]
  (google/oauth-access-token google-client-id
                             google-client-secret
                             code
                             (str domain-name
                                  google-callback-path)))

(defn get-user [token]
  (google/user-info (google/oauth-client (:access-token token))))

(defn req->token [req]
  (when-let [code (get-in req [:query-params "code"])]
    (when-let [token (get-token code env)]
      token)))

(defn oauth-callback [req]
  (if-let [token (req->token req)]
    (let [user-data (get-user token)]
      (users/save-or-update user-data)
      (-> (resp/redirect "/")
          (assoc-in [:session :token] token)
          (assoc-in [:session :identity] user-data)))
    (throw-unauthorized {:message "Wrong code"})))

(defn logout [req]
  (-> (resp/redirect "/")
      (update-in [:session] dissoc :token)
      (update-in [:session] dissoc :identity)))

(defn redirect-to-provider []
  (resp/redirect (google-auth-uri env)))
