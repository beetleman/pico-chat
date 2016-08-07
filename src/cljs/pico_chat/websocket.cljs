(ns pico-chat.websocket
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
   [cljs.core.async :as async :refer (<! >! put! chan)]
   [pico-chat.logger :as logger]
   [taoensso.sente  :as sente :refer (cb-success?)]
   [taoensso.encore :as encore :refer-macros (have have?)]
   [taoensso.timbre :as timbre :refer-macros (tracef debugf infof warnf errorf)]
   [re-frame.core :refer [dispatch]]
   [mount.core :as mount]))


(mount/defstate ^{:on-reload :noop} sch
  :start (sente/make-channel-socket-client! "/chsk" {:type :auto}))

;;;; Sente event handlers

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler
  :default
  [{:as ev-msg :keys [event]}]
  (logger/info :default-handler event))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (let [[old-state-map new-state-map] (have vector? ?data)]
    (if (:first-open? new-state-map)
      (logger/info :new-conncection new-state-map)
      (logger/info :new-state new-state-map))))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (logger/info :handshake ?data)))

(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (let [[id data] ?data]
    (condp = id
      :chat/message (dispatch [:recv-message data])
      :chat/users (dispatch [:recv-users data])
      (logger/info :recv ?data))))

(mount/defstate ^{:on-reload :noop} router
  :start (sente/start-client-chsk-router!
          (:ch-recv @sch) event-msg-handler))
