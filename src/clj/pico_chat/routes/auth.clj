(ns pico-chat.routes.auth
  (:require [pico-chat.layout :as layout]
            [pico-chat.db.core :as db]
            [pico-chat.auth :as auth]
            [compojure.core :refer [defroutes GET make-route]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))


(defroutes auth-routes
  (GET (:path auth/oauth-callback-uri) req (auth/oauth-callback req)))
