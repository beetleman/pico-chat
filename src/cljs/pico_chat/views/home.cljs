(ns pico-chat.views.home
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require [reagent.core :as r]
            [pico-chat.logger :as logger]
            [pico-chat.events :as events]
            [pico-chat.messages :as messages]
            [pico-chat.users :as users]
            [cljs.core.async :refer [put! chan <! >! timeout]]
            [re-frame.core :refer [subscribe dispatch]]))


(defn scroll-down-fn [el]
  (fn [& _]
    (when-let [el @el]
      (aset el "scrollTop" (aget el "scrollHeight")))))


(defn page []
  (let [messages (subscribe [:messages])
        message-container (r/atom nil)
        users (subscribe [:users])]
    (fn []
      [:div.container
       [:div.row.expand
        [:div.col.m4.users-container
         [users/all users]]
        [:div.col.m8.messages-container
         {:ref #(reset! message-container %)}
         [messages/all messages (scroll-down-fn message-container)]]]
       [:div.row.fixed
        [:div.col.m12.new-message-container
         [messages/new]]]])))
