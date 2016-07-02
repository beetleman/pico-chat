(ns pico-chat.figwheel
  (:require [figwheel-sidecar.repl-api :as ra]))

(defn start-fw []
  (ra/start-figwheel!))

(defn stop-fw []
  (ra/stop-figwheel!))

(defn restart-fw []
  (stop-fw)
  (start-fw))

(defn cljs []
  (ra/cljs-repl))
