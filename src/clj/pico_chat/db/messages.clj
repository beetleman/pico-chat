(ns pico-chat.db.messages
  (:require
   [rethinkdb.query :as r]
   [pico-chat.db.core :refer [conn]]
   [pico-chat.db.utils :as utils]
   [pico-chat.db.users :as users]
   [mount.core :as mount]
   [clj-time.core :as time]
   [clj-time.coerce :as tc]))


(utils/def-table messages conn
  (r/index-create "user_id" (r/fn [row]
                              (r/get-field row :user_id))))


(defn save
  ([msg] (save msg conn))
  ([msg conn]
   (utils/save-item msg conn messages)))


(defn join-with-user
  ([message] (join-with-user message conn))
  ([message conn]
   (let [user (users/get-by-id (:user_id message))]
     (assoc message :user user))))


(defn get-all
  ([] (get-all conn))
  ([conn]
   (utils/get-all-items conn messages)))


(defn changesfeed
  ([] (changesfeed conn))
  ([conn] (utils/changesfeed conn messages)))
