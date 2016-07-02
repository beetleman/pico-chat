(ns pico-chat.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [pico-chat.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[pico-chat started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[pico-chat has shut down successfully]=-"))
   :middleware wrap-dev})
