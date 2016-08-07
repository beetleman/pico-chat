(ns pico-chat.websocket
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.immutant :refer (get-sch-adapter)]
            [pico-chat.db.messages :as messages]
            [pico-chat.db.users :as users]
            [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go go-loop chan buffer close! thread
                     alts! alts!! timeout]]
            [mount.core :as mount]))


(declare user-id-fn)

(mount/defstate ^{:on-reload :noop} sch
  :start (sente/make-channel-socket! (get-sch-adapter)
                                     {:user-id-fn user-id-fn}))

(defn user-id-fn [ring-req]
  (get-in ring-req [:session :identity :id]))

(defmulti event :id)

(defmethod event :default [{:as ev-msg :keys [event id uid]}]
  (println "Unhandled event: " uid " " (get-in ev-msg [:ring-req :session :identity])))

(defmethod event :chat/new-message [{:as ev-msg :keys [event uid ?data]}]
  (when-not (-> ?data :text clojure.string/trim empty?)
    (messages/save (assoc ?data :user_id uid))))


(defn send-fn-all
  [sch args]
  (let [{:keys [send-fn connected-uids]} sch]
    (doseq [uid (:any @connected-uids)]
      (send-fn uid args))))


(mount/defstate users-broadcast
  :start (add-watch (:connected-uids sch) :users-broadcast
                    (fn [_ _ _ {:keys [any]}]
                      (send-fn-all sch [:chat/users (users/filter-by-ids any)])))
  :stop (remove-watch (:connected-uids sch) :users-broadcast))


(mount/defstate messages-broadcast
  :start (let [run (atom true)
               changes-chan (messages/changesfeed)]
           (go-loop []
             (when @run
               (send-fn-all sch [:chat/message (-> (<! changes-chan)
                                                   :new_val
                                                   (messages/join-with-user))])
               (recur)))
           run)
  :stop (reset! messages-broadcast false))


(mount/defstate ^{:on-reload :noop} router
  :start (sente/start-chsk-router! (:ch-recv sch) event))
