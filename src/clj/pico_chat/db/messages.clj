(ns pico-chat.db.messages
  (:require
   [rethinkdb.query :as r]
   [pico-chat.db.core :refer [conn table-exist?]]
   [mount.core :as mount]
   [clj-time.core :as time]
   [clj-time.coerce :as tc]))


(def table-name "message")
(mount/defstate setup-table
  :start  (when-not (table-exist? conn table-name)
              (-> (r/table-create table-name)
                  (r/run conn))))


(defn save
  ([msg] (save msg conn))
  ([msg conn]
   (-> (r/table table-name)
       (r/insert (assoc msg :created (tc/to-long (time/now))))
       (r/run conn))))


(defn get-all
  ([])
  ([conn]
   (-> (r/table table-name)
       (r/get-all)
       (r/run conn))))


(defn changesfeed
  ([] (changesfeed conn))
  ([conn] (-> (r/table table-name)
              (r/changes {:include-initial true})
              (r/run conn {:async? true}))))
