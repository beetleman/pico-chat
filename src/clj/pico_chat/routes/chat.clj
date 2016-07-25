(ns pico-chat.routes.chat
  (:require [pico-chat.websocket :refer [sch]]
            [compojure.core :refer [defroutes GET POST]]
            [taoensso.sente :as sente]
            [mount.core :as mount]))


(defroutes chat-routes
  (GET  "/chsk" req ((:ajax-get-or-ws-handshake-fn sch) req))
  (POST "/chsk" req ((:ajax-post-fn sch) req)))
