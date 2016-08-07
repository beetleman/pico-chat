(ns pico-chat.db.users
  (:require
   [rethinkdb.query :as r]
   [pico-chat.db.core :refer [conn]]
   [pico-chat.db.utils :as utils]
   [mount.core :as mount]
   [clj-time.core :as time]
   [clj-time.coerce :as tc]))


(utils/def-table users conn)


(defn save
  ([user] (save user conn))
  ([user conn]
   (utils/save-item user conn users)))

(defn save-or-update
  ([user] (save user conn))
  ([user conn]
   (utils/save-or-update-item user conn users)))

(defn get-by-id
  ([id] (get-by-id id conn))
  ([id conn]
   (-> (r/table users)
       (r/get (str id))
       (r/run conn))))

(defn get-all
  ([] (get-all conn))
  ([conn]
   (utils/get-all-items conn users)))


(defn filter-by-ids
  ([ids] (filter-by-ids ids conn))
  ([ids conn]
   (-> (r/table users)
       (r/get-all (map str ids) {})
       (r/run conn))))


(defn changesfeed
  ([] (changesfeed conn))
  ([conn] (utils/changesfeed conn users)))
