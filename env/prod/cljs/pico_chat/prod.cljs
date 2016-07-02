(ns pico-chat.app
  (:require [pico-chat.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
