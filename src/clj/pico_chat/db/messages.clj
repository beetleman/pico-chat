(ns pico-chat.db.messages
  (:require
   [rethinkdb.query :as r]
   [pico-chat.db.core :refer [conn]]
   [pico-chat.db.utils :as utils]
   [mount.core :as mount]
   [clj-time.core :as time]
   [clj-time.coerce :as tc]))


(utils/def-table messages conn)


(defn save
  ([msg] (save msg conn))
  ([msg conn]
   (utils/save-item msg conn messages)))


(defn get-all
  ([] (get-all conn))
  ([conn]
   (utils/get-all-items conn messages)))


(defn changesfeed
  ([] (changesfeed conn))
  ([conn] (utils/changesfeed conn messages)))
