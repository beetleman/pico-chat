(ns pico-chat.websocket
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.immutant :refer (get-sch-adapter)]
            [pico-chat.db.messages :as messages]
            [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go go-loop chan buffer close! thread
                     alts! alts!! timeout]]
            [mount.core :as mount]))

(mount/defstate ^{:on-reload :noop} sch
  :start (sente/make-channel-socket! (get-sch-adapter)))


(defmulti event :id)

(defmethod event :default [{:as ev-msg :keys [event]}]
  (println "Unhandled event: " event))

(defmethod event :chat/new-message [{:as ev-msg :keys [event uid ?data]}]
  (when-not (-> ?data :text clojure.string/trim empty?)
    (messages/save ?data)))


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
