(ns pico-chat.views.home
  (:require [reagent.core :as r]
            [pico-chat.logger :as logger]
            [re-frame.core :refer [subscribe dispatch]]))


(defn page []
  [:h1 "Hello!"])
