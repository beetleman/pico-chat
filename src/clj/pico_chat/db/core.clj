(ns pico-chat.db.core
  (:require
   [rethinkdb.core :refer [close]]
   [rethinkdb.query :as r]
   [pico-chat.config :refer [env]]
   [mount.core :refer [defstate]]))



(defn db-exist? [conn db-name]
  (some #{db-name} (-> (r/db-list)
                       (r/run conn))))


(defn table-exist? [conn table-name]
  (some #{table-name} (-> (r/table-list)
                          (r/run conn))))


(defstate conn
  :start (r/connect
          :host (env :rethink-host)
          :port (env :rethink-port)
          :db (env :rethink-db))
  :stop (close conn))


(defstate db
  :start (when-not (db-exist? conn (env :rethink-db))
           (-> (r/db-create (env :rethink-db))
               (r/run conn))))
