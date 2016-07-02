(ns pico-chat.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[pico-chat started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[pico-chat has shut down successfully]=-"))
   :middleware identity})
