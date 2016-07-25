(ns pico-chat.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [pico-chat.layout :refer [error-page]]
            [pico-chat.routes.home :refer [home-routes]]
            [pico-chat.routes.chat :refer [chat-routes]]
            [pico-chat.routes.auth :refer [auth-routes]]
            [compojure.route :as route]
            [pico-chat.env :refer [defaults]]
            [mount.core :as mount]
            [pico-chat.middleware :as middleware]))

(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
   (->
    #'chat-routes
    (wrap-routes middleware/wrap-restricted))
   #'auth-routes
   (->
    #'home-routes
    (wrap-routes middleware/wrap-csrf)
    (wrap-routes middleware/wrap-formats)
    (wrap-routes middleware/wrap-restricted))
   (route/not-found
    (:body
     (error-page {:status 404
                  :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
