(ns pico-chat.views.about
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]))


(defn page []
  (let [version (subscribe [:version])]
    (fn []
      [:div.container
       [:div.row
        [:div.col-md-12
         "This is the story of pico-chat... work in progress"]]
       [:div.row
        [:div.col-md-12
         [:p "Version: " (apply str (interpose "." @version))]]]])))
