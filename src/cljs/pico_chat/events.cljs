(ns pico-chat.events
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require
   [cljs.core.async :refer [put! chan <! >! timeout]]
   [goog.events :as events]))


(defn listen
  ([event-type]
   (listen event-type identity))
  ([event-type parse-function]
   (let [ev-chan (chan)]
     (events/listen (.-body js/document)
                    event-type
                    #(put! ev-chan (parse-function %)))
     ev-chan)))


(def listen-keypress
  (partial listen (.-KEYPRESS events/EventType)))


(def listen-keydown
  (partial listen (.-KEYDOWN events/EventType)))


(def listen-keyup
  (partial listen (.-KEYUP events/EventType)))
