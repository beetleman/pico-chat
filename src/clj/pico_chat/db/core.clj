(ns pico-chat.db.core
  (:require
   [rethinkdb.core :refer [close]]
   [rethinkdb.query :as r]
   [pico-chat.config :refer [env]]
   [mount.core :refer [defstate]]))


(defstate ^:dynamic *db*
  :start (r/connect
          :host (env :rethink-host)
          :port (env :rethink-port)
          :db (env :rethink-db))
  :stop (close *db*))
