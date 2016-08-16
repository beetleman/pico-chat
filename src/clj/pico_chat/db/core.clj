(ns pico-chat.db.core
  (:require
   [rethinkdb.core :refer [close]]
   [rethinkdb.query :as r]
   [pico-chat.db.utils :as utils]
   [pico-chat.config :refer [env]]
   [mount.core :refer [defstate]]))


(defstate conn
  :start (r/connect
          :host (env :rethink-host)
          :port (env :rethink-port)
          :db (env :rethink-db))
  :stop (close conn))


(defstate db
  :start (when-not (utils/db-exist? conn (env :rethink-db))
           (-> (r/db-create (env :rethink-db))
               (r/run conn))))


(defn drop-db []
  (-> (r/db-drop (env :rethink-db))
      (r/run conn)))
