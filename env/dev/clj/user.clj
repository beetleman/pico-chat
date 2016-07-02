(ns user
  (:require [mount.core :as mount]
            [pico-chat.figwheel :refer [start-fw stop-fw restart-fw cljs]]
            pico-chat.core))

(defn start []
  (mount/start-without #'pico-chat.core/repl-server))

(defn stop []
  (mount/stop-except #'pico-chat.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-all []
  (restart)
  (restart-fw))
