(ns pico-chat.views.home
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require [reagent.core :as r]
            [pico-chat.logger :as logger]
            [pico-chat.events :as events]
            [pico-chat.messages :as messages]
            [pico-chat.users :as users]
            [cljs.core.async :refer [put! chan <! >! timeout]]
            [re-frame.core :refer [subscribe dispatch]]))


(defn page []
  (let [messages (subscribe [:messages])
        users (subscribe [:users])]
    (fn []
      [:div.container
       [:div.row
        [:div.users.col.m4
         [users/all users]]
        [:div.messages.col.m8
         [messages/all messages]]]
       [:div.row
        [:div.col.m12
         [messages/new]]]])))
