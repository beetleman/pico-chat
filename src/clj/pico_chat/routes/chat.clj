(ns pico-chat.routes.chat
  (:require [pico-chat.db.core :as db]
            [pico-chat.websocket :refer [sch]]
            [compojure.core :refer [defroutes GET POST]]
            [taoensso.sente :as sente]
            [mount.core :as mount]))

(defmulti event :id)

(defmethod event :default [{:as ev-msg :keys [event]}]
  (println "Unhandled event: " event))

(defroutes chat-routes
  (GET  "/chsk" req ((:ajax-get-or-ws-handshake-fn sch) req))
  (POST "/chsk" req ((:ajax-post-fn sch) req)))

(mount/defstate ^{:on-reload :noop} router
  :start (sente/start-chsk-router! (:ch-recv sch) event))
