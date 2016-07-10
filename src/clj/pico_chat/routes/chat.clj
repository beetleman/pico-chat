(ns pico-chat.routes.chat
  (:require [pico-chat.db.messages :as messages]
            [pico-chat.websocket :refer [sch]]
            [compojure.core :refer [defroutes GET POST]]
            [taoensso.sente :as sente]
            [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go go-loop chan buffer close! thread
                     alts! alts!! timeout]]
            [mount.core :as mount]))


(defroutes chat-routes
  (GET  "/chsk" req ((:ajax-get-or-ws-handshake-fn sch) req))
  (POST "/chsk" req ((:ajax-post-fn sch) req)))


(defmulti event :id)

(defmethod event :default [{:as ev-msg :keys [event]}]
  (println "Unhandled event: " event))

(defmethod event :chat/new-message [{:as ev-msg :keys [event uid ?data]}]
  (messages/save ?data))


(defn send-fn-all
  [sch args]
  (let [{:keys [send-fn connected-uids]} sch]
    (doseq [uid (:any @connected-uids)]
      (send-fn uid args))))


(mount/defstate broadcast
  :start (let [run (atom true)
               changes-chan (messages/changesfeed)]
           (go-loop []
             (when @run
               (send-fn-all sch [:chat/message (-> (<! changes-chan)
                                                   :new_val)])
               (recur)))
           run)
  :stop (reset! broadcast false))

(mount/defstate ^{:on-reload :noop} router
  :start (sente/start-chsk-router! (:ch-recv sch) event))
