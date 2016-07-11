(ns pico-chat.messages
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]
            [pico-chat.logger :as logger]))


(defn new []
  (let [text (r/atom "")]
    (fn []
      [:form {:onSubmit (fn [e]
                          (.preventDefault e)
                          (dispatch [:send-message @text])
                          (reset! text ""))}
       [:div.input-field
        [:input {:value @text
                 :type "text"
                 :placeholder "Message"
                 :onChange #(reset! text (.. % -target -value))}]]
       [:button.btn "Send"]])))


(defn one [{:keys [username text id]}]
  [:div.msg
   [:div.username username]
   [:div.text text]])


(defn all [messages]
  [:div "messages"
   (for [m @messages]
     ^{:key (:id m)} [one m])])
