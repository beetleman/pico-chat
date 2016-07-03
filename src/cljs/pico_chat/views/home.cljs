(ns pico-chat.views.home
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require [reagent.core :as r]
            [pico-chat.logger :as logger]
            [pico-chat.events :as events]
            [cljs.core.async :refer [put! chan <! >! timeout]]
            [re-frame.core :refer [subscribe dispatch]]))


(defn page []
  (let []
    (fn []
      [:div.container
       [:div.row
        [:div.col.m12
         [:h1 "Hello!"]]]])))
