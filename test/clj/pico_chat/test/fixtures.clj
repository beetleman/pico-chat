(ns pico-chat.test.fixtures
  (:require [pico-chat.db.core :as db]
            [pico-chat.config :refer [env]]
            [rethinkdb.query :as r]
            [mount.core :as mount]))


(defn db-fixture [f]
  (mount/stop  #'db/conn #'db/db #'env)
  (mount/start  #'db/conn  #'db/db #'env)
  (f)
  (db/drop-db))
