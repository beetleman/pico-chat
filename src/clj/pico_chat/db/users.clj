(ns pico-chat.db.users
  (:require
   [rethinkdb.query :as r]
   [pico-chat.db.core :refer [conn]]
   [pico-chat.db.utils :as utils]
   [mount.core :as mount]
   [clj-time.core :as time]
   [clj-time.coerce :as tc]))


(utils/def-table users conn
  (r/index-create "id" (r/fn [row]
                            (r/get-field row :id))))


(defn save
  ([user] (save user conn))
  ([user conn]
   (utils/save-item user conn users)))


(defn get-all
  ([] (get-all conn))
  ([conn]
   (utils/get-all-items conn users)))


(defn changesfeed
  ([] (changesfeed conn))
  ([conn] (utils/changesfeed conn users)))
